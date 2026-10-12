import { Component, DestroyRef, ElementRef, HostListener, inject, QueryList, signal, TemplateRef, ViewChild, ViewChildren, ViewContainerRef } from '@angular/core';
import { FormControl, Validators } from '@angular/forms';
import { WsAddCreditCardService } from '../ws-add-credit-card.service';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { Location } from '@angular/common';
import { WsOverlay } from '../../../ws-services/ws-overlay.service';
import { TemplatePortal } from '@angular/cdk/portal';
import { ActiveCartFacade } from '@spartacus/cart/base/root';

@Component({
  selector: 'ws-add-new-creditcard',
  templateUrl: './add-new-creditcard.component.html',
  styleUrl: './add-new-creditcard.component.scss',
})
export class AddNewCreditcardComponent {
  private service = inject(WsAddCreditCardService);
  private overLayService = inject(WsOverlay);
  public activeCartService = inject(ActiveCartFacade);
  private vcr = inject(ViewContainerRef);
  private destroyRef = inject(DestroyRef);
  public location = inject(Location);

  @ViewChild('closeButton') closeButton: ElementRef;
  @ViewChildren('addressRadio') radioInputs!: QueryList<ElementRef>;


  radioButtons = [
    { key: 'existing', value: 'Choose Existing Address' },
    { key: 'new', value: 'Add New Address' },
  ];
  radioButtonForm = new FormControl('existing', Validators.required);

  newAndValidateAddressDetails = signal<any>({});
  invokeGetAllAddresses = signal<any>({});

  modalWidth = window.innerWidth * 0.30 + 'px';
  @ViewChild('validateAddress') validateAddress: TemplateRef<any>;
  validateAddressPortal: any;
  newAddressAdded: any = {};

  ngOnInit() {
    this.customEventsHandler();
  }

  ngAfterViewInit() {
    this.validateAddressPortal = new TemplatePortal(this.validateAddress, this.vcr);
    setTimeout(() => {
      const selectedIndex = this.radioButtons.findIndex(rb => rb.key === this.radioButtonForm.value);
      this.radioInputs.get(selectedIndex)?.nativeElement.focus();

      // if (this.closeButtonRef?.nativeElement) {
      //   this.closeButtonRef.nativeElement.setAttribute('tabindex', '-1');
      //   this.closeButtonRef.nativeElement.focus();
      // }
      this.closeButton?.nativeElement?.focus();
    }, 0);
  
  }

  customEventsHandler() {
    this.service.addAddressButton.pipe(takeUntilDestroyed(this.destroyRef)).subscribe({
      next: (res: any) => {
        this.newAndValidateAddressDetails.set(res);
        this.overLayService.openModal(this.validateAddressPortal,'0');
      }
    })
  }

  closePopup() {
    this.overLayService.closeAllModals();
  }

  closeTopModal() {
    this.overLayService.closeTopModal();
    setTimeout(() => {
      this.closeButton?.nativeElement?.focus();
    }, 0);
  }

  getAllAddresses(addedAddress: any) {
    this.newAddressAdded = addedAddress;
    this.service.userSelectedAddress.set({});
    this.radioButtonForm.setValue('existing');
  }

  onRadioKeydown(event: KeyboardEvent, index: number) {
    const items = this.radioInputs.toArray();
    const total = items.length;

    if (event.key === 'ArrowRight' || event.key === 'ArrowDown') {
      event.preventDefault();
      const nextIndex = (index + 1) % total;
      const nextValue = this.radioButtons[nextIndex].key;
      this.radioButtonForm.setValue(nextValue);
      items[nextIndex].nativeElement.focus();
    }

    if (event.key === 'ArrowLeft' || event.key === 'ArrowUp') {
      event.preventDefault();
      const prevIndex = (index - 1 + total) % total;
      const prevValue = this.radioButtons[prevIndex].key;
      this.radioButtonForm.setValue(prevValue);
      items[prevIndex].nativeElement.focus();
    }

    if (event.key === 'Enter' || event.key === ' ') {
      event.preventDefault();
      this.radioButtonForm.setValue(this.radioButtons[index].key);
    }
  }
  
}
