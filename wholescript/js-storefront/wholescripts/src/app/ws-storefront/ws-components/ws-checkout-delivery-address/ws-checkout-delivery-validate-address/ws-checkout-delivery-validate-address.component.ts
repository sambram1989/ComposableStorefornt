import { ChangeDetectorRef, Component, DestroyRef, ElementRef, EventEmitter, HostListener, inject, Input, Output, ViewChild } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { WsAddressesService } from '../../../ws-services/ws-addresses.service';
import { WsAccertifyService } from '../../../ws-services/ws-checkout/ws-accertify.service';

@Component({
  selector: 'ws-checkout-delivery-validate-address',
  templateUrl: './ws-checkout-delivery-validate-address.component.html',
  styleUrl: './ws-checkout-delivery-validate-address.component.scss'
})
export class WsCheckoutDeliveryValidateAddressComponent {
  @Output() hideValidateAddressModal = new EventEmitter();
  @Output() refreshDeliveryAddresses = new EventEmitter<any>();
  @Input() newAndValidateAddressDetails: any;
  @Input() newDeliveryAddress: any = {}
  @Input() validatedAddressResponse: any = {}
  @Input() activeCartId: string = '';
  @ViewChild('firstElement') firstElement?: ElementRef;
  @ViewChild('lastElement') lastElement?: ElementRef;

  private destroyRef = inject(DestroyRef);

  constructor(private cd: ChangeDetectorRef,
    private addressService: WsAddressesService,
    private wsAccertifyService: WsAccertifyService
  ) { }

  ngOnInit(){
    this.wsAccertifyService.loadAccertifyCollectorScript().then((res) => {
      console.log('Accertify script loaded successfully ');
    }).catch(error => {
      console.error('Error loading Accertify script:', error);
    });;
  }

  closeModal() {
    this.hideValidateAddressModal.emit();
  }

  ngAfterViewInit(): void {
    setTimeout(() => {
      this.firstElement.nativeElement.focus();
    }, 0);
  }

  useValidated(address: any) {
    let window = this.wsAccertifyService.getCollectorJsData(); 
    const payload = {
      country: {
        isocode: address?.country?.isocode
      },
      firstName: this.newDeliveryAddress?.firstName,
      lastName: this.newDeliveryAddress?.lastName,
      line1: address?.line1 ,
      line2: address?.line2 || this.newDeliveryAddress?.line2 || this.newDeliveryAddress?.apartment,
      town: address?.town,
      region: {
        isocode: address?.region?.isocode
      },
      postalCode: address?.postalCode,
      phone: this.newDeliveryAddress?.phone,
      defaultAddress: false,
      email: this.newDeliveryAddress?.email,
      remarks: this.newDeliveryAddress?.companyName || this.newDeliveryAddress?.address,
      accertifyEventSource: 'web',
      accertifyDeviceTransactionId: window?.deviceTransactionId,
      accertifyUbaID: window?.ubaId,
      accertifyPageId: window?.pageId,
      accertifyUbaEvents: window?.ubaEvents,
      accertifySessionID: window?.sessionId
    }
    this.addressService.addAddress(payload)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe(data => {
        this.refreshDeliveryAddresses.emit(data);
        this.closeModal();
        this.cd.detectChanges();
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
