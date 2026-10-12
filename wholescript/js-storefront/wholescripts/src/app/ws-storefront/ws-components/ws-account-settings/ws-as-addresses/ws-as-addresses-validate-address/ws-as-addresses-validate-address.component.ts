import { ChangeDetectorRef, Component, DestroyRef, ElementRef, EventEmitter, inject, Input, OnDestroy, Output, ViewChild } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { WsAddressesService } from '../../../../ws-services/ws-addresses.service';
import { WsAccertifyService } from '../../../../ws-services/ws-checkout/ws-accertify.service';
import { GlobalMessageService, GlobalMessageType } from '@spartacus/core';

@Component({
  selector: 'ws-as-addresses-validate-address',
  templateUrl: './ws-as-addresses-validate-address.component.html',
  styleUrl: './ws-as-addresses-validate-address.component.scss'
})
export class WsAsAddressesValidateAddressComponent {
  @Output() hideValidateAddressModal = new EventEmitter();
  @Output() refreshAddresses = new EventEmitter();
  @Input() newAndValidateAddressDetails: any;
  @Input() isEdit: boolean;
  @Input() addressToEdit: any;
  @Input() newAddress: any = {}
  @Input() validatedAddressResponse: any = {}
  @Input() activeCartId: string = '';
  @ViewChild('firstElement') firstElement?: ElementRef;
  @ViewChild('lastElement') lastElement?: ElementRef;

  loader: boolean = false;

  private destroyRef = inject(DestroyRef);

  constructor(private cd: ChangeDetectorRef,
    private addressService: WsAddressesService,
    private wsAccertifyService: WsAccertifyService,
    private globalMessageService: GlobalMessageService
  ) { }

  ngOnInit() {
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
      this.firstElement?.nativeElement?.focus();
    }, 0);
  }

  useValidated(address: any) {
    if (this.isEdit) {
      this.updateAddress(address);
    } else {
      this.loader = true;
      let window = this.wsAccertifyService.getCollectorJsData();
      const payload = {
        country: {
          isocode: address?.country?.isocode
        },
        firstName: this.newAddress?.firstName,
        lastName: this.newAddress?.lastName,
        line1: address?.line1,
        line2: address?.line2 || this.newAddress?.line2,
        town: address?.town,
        region: {
          isocode: address?.region?.isocode
        },
        postalCode: address?.postalCode,
        phone: this.newAddress?.phone,
        defaultAddress: false,
        email: this.newAddress?.email,
        remarks: this.newAddress?.companyName,
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
          this.loader = false;
          this.refreshAddresses.emit();
          this.closeModal();
          this.cd.detectChanges();
        }, () => {
          this.loader = false;
          this.cd.detectChanges();
        })
    }
  }

  updateAddress(address: any) {
    this.loader = true;
    let window = this.wsAccertifyService.getCollectorJsData();
    const payload = {
      addressNumber: this.addressToEdit?.addressNumber,
      country: {
        isocode: address?.country?.isocode || this.newAddress.country,
      },
      firstName: this.newAddress.firstName,
      lastName: this.newAddress.lastName,
      line1: address?.line1 || this.newAddress.line1,
      line2: address?.line2 || this.newAddress.line2,
      town:  address?.town || this.newAddress.town,
      region: {
        isocode: address?.region?.isocode || this.newAddress.region
      },
      postalCode: address?.postalCode || this.newAddress.postalCode,
      phone: this.newAddress.phone,
      cellphone: "",
      defaultAddress: false,
      email: this.newAddress.email,
      remarks: this.newAddress.companyName,
      accertifyEventSource: 'web',
      accertifyDeviceTransactionId: window?.deviceTransactionId,
      accertifyUbaID: window?.ubaId,
      accertifyPageId: window?.pageId,
      accertifyUbaEvents: window?.ubaEvents,
      accertifySessionID: window?.sessionId
    }
    this.addressService.updateAddress(payload)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe(data => {
        this.loader = false;
        this.refreshAddresses.emit({
          updatedAddress: { ...this.newAddress.value, addressNumber: this.addressToEdit.addressNumber },
          originalIndex: this.addressToEdit.index
        });
        this.closeModal();
        this.cd.detectChanges();
      }, (error) => {
        this.loader = false;
        if (error?.error?.errors?.length > 0) {
          error?.error?.errors?.map((e: any) => {
            if (e.type === 'AgeRestrictionError' || e.type === 'POBoxAgeRestrictionError') {
              this.closeModal();
              this.globalMessageService.add(
                e?.message || '',
                GlobalMessageType.MSG_TYPE_ERROR,
                10000
              );
            }
          })
        }
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
