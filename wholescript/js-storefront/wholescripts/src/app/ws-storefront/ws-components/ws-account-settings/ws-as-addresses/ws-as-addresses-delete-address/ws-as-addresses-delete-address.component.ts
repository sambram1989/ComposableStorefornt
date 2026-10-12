import { ChangeDetectorRef, Component, DestroyRef, ElementRef, EventEmitter, inject, Input, Output, ViewChild } from '@angular/core';
import { WsAddressesService } from '../../../../ws-services/ws-addresses.service';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { WsAccertifyService } from '../../../../ws-services/ws-checkout/ws-accertify.service';

@Component({
  selector: 'ws-as-addresses-delete-address',
  templateUrl: './ws-as-addresses-delete-address.component.html',
  styleUrl: './ws-as-addresses-delete-address.component.scss'
})
export class WsAsAddressesDeleteAddressComponent {

  private destroyRef = inject(DestroyRef);
  loading: boolean = false;
  
  @Input() addressToDelete: any;
  @Output() hideDeleteAddressModal = new EventEmitter<boolean>();
  @ViewChild('firstElement') firstElement?: ElementRef;
  @ViewChild('lastElement') lastElement?: ElementRef;

  constructor(private addressService: WsAddressesService, private wsAccertifyService: WsAccertifyService,
    private cd: ChangeDetectorRef
  ) {
    this.wsAccertifyService.loadAccertifyCollectorScript().catch(() => {});
  }

  closeModal() {
    this.hideDeleteAddressModal.emit(false);
  }
  ngAfterViewInit(): void {
    setTimeout(() => {
      this.firstElement?.nativeElement?.focus();
    }, 0);
  }

  deleteAddress() {
    let creditCardIds = '';
    if(this.addressToDelete?.isCardAvailable) {
      creditCardIds = this.addressToDelete?.creditCardIds?.join(',') || '';
    }
    this.loading = true;
    let window = this.wsAccertifyService.getCollectorJsData();
    const payload = {
      addressNumber: this.addressToDelete?.addressNumber,
      country: {
        isocode: this.addressToDelete?.country?.isocode
      },
      firstName: this.addressToDelete.firstName,
      lastName: this.addressToDelete.lastName,
      line1: this.addressToDelete?.line1,
      line2: this.addressToDelete?.line2,
      town:  this.addressToDelete?.town,
      region: {
        isocode: this.addressToDelete?.region?.isocode
      },
      postalCode: this.addressToDelete?.postalCode,
      phone: this.addressToDelete.phone,
      cellphone: "",
      defaultAddress: false,
      email: this.addressToDelete.email,
      remarks: this.addressToDelete.remarks,
      accertifyEventSource: 'web',
      accertifyDeviceTransactionId: window?.deviceTransactionId,
      accertifyUbaID: window?.ubaId,
      accertifyPageId: window?.pageId,
      accertifyUbaEvents: window?.ubaEvents,
      accertifySessionID: window?.sessionId
    }
    this.addressService.deleteAddress(this.addressToDelete?.addressNumber, creditCardIds, payload)
    .pipe(takeUntilDestroyed(this.destroyRef))
    .subscribe(data => {
      this.loading = false;
      this.hideDeleteAddressModal.emit(true);
      this.cd.markForCheck();
    }, () => {
      this.loading = false;
      this.cd.markForCheck();
    })
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
