import { AfterViewInit, ChangeDetectorRef, Component, DestroyRef, ElementRef, EventEmitter, inject, Input, Output, ViewChild } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { AccountSettingsService } from '../../../ws-services/ws-account-settings.service';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { WsCheckoutDeliveryAddressService } from '../../../ws-services/ws-checkout-delivery-address.service';

@Component({
  selector: 'ws-checkout-delivery-new-address',
  templateUrl: './ws-checkout-delivery-new-address.component.html',
  styleUrl: './ws-checkout-delivery-new-address.component.scss'
})
export class WsCheckoutDeliveryNewAddressComponent implements AfterViewInit {
  @Input() regions: any[] = [];
  @Output() hideNewAddressModal = new EventEmitter();
  @Output() showValidateAddressModal = new EventEmitter<any>();
  @ViewChild('firstElement') firstElement?: ElementRef;
  @ViewChild('lastElement') lastElement?: ElementRef;

  newDeliveryAddress = new FormGroup({
    companyName: new FormControl("", [Validators.required]),
    firstName: new FormControl("", [Validators.required]),
    lastName: new FormControl("", [Validators.required]),
    email: new FormControl("", [Validators.pattern(/^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/)]),
    line1: new FormControl("", [Validators.required]),
    line2: new FormControl("", []),
    town: new FormControl("", [Validators.required]),
    country: new FormControl("US", [Validators.required]),
    region: new FormControl("", [Validators.required]),
    postalCode: new FormControl("", [Validators.required]),
    phone: new FormControl("", [])
  });
  submitted: boolean = false;
  addNewAddressError: boolean = false;

  private destroyRef = inject(DestroyRef);

  constructor(private cd: ChangeDetectorRef,
    private wsCheckoutDeliveryAddressService: WsCheckoutDeliveryAddressService
  ) {}

  closeModal() {
    this.hideNewAddressModal.emit();
  }

  ngAfterViewInit(): void {
    setTimeout(() => {
      this.firstElement.nativeElement.focus();
    }, 0);
  }

  validateAddress() {
    this.submitted = true;
    const payload = {
        firstName: this.newDeliveryAddress.value.firstName,
        lastName: this.newDeliveryAddress.value.lastName,
        line1: this.newDeliveryAddress.value.line1,
        line2: this.newDeliveryAddress.value.line2,
        town: this.newDeliveryAddress.value.town,
        region: {
          isocode : this.newDeliveryAddress.value.region
        },
        postalCode: this.newDeliveryAddress.value.postalCode,
        country: {
          isocode: this.newDeliveryAddress.value.country,
          name: "United States"
        }
    }
    this.addNewAddressError = false;
    if (this.newDeliveryAddress.valid) {
      this.wsCheckoutDeliveryAddressService.validateAddress(payload)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe((data: any) => {
        if(data?.decision === 'ACCEPT') {
          this.showValidateAddressModal.emit({newDeliveryAddress: this.newDeliveryAddress.value, validatedAddressResponse: data});
        } else {
          this.addNewAddressError = true;
        }
        this.cd.detectChanges();
      }, () => {
        this.addNewAddressError = true;
        this.cd.detectChanges();
      })
      
    } else {
      this.newDeliveryAddress.markAllAsTouched();
    }
  }

  validateZipCode(event: KeyboardEvent): void {
    const input = event.target as HTMLInputElement;
    const value = input.value;

    const forbiddenKeys = ['e', 'E', '+', '-'];
    if (value.length > 10 || forbiddenKeys.includes(event.key)) {
      event.preventDefault();
    }
  }


  handleTab(event: KeyboardEvent): void {
    if (event.key === 'Tab' && this.firstElement && this.lastElement) {
      const { nativeElement: first } = this.firstElement;
      const { nativeElement: last } = this.lastElement;
      if (event.shiftKey && document.activeElement === first) {
        event.preventDefault();
        (last as HTMLElement).focus();
        return;
      }
      if (!event.shiftKey && document.activeElement === last) {
        event.preventDefault();
        (first as HTMLElement).focus();
        return;
      }
    }
    if (event.key === 'Escape' || event.code === 'Escape') {
      event.stopPropagation(); 
      this.closeModal();
    }
  }

}
