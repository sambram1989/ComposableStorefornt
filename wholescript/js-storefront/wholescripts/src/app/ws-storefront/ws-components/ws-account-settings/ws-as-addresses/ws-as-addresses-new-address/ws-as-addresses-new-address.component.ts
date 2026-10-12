import { ChangeDetectorRef, Component, DestroyRef, ElementRef, EventEmitter, inject, Input, OnChanges, Output, SimpleChanges, ViewChild } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { FormGroup, FormControl, Validators, ValidatorFn, AbstractControl, ValidationErrors } from '@angular/forms';
import { AccountSettingsService } from '../../../../ws-services/ws-account-settings.service';
import { WsAddressesService } from '../../../../ws-services/ws-addresses.service';
import { WsAccertifyService } from '../../../../ws-services/ws-checkout/ws-accertify.service';
@Component({
  selector: 'ws-as-addresses-new-address',
  templateUrl: './ws-as-addresses-new-address.component.html',
  styleUrl: './ws-as-addresses-new-address.component.scss'
})
export class WsAsAddressesNewAddressComponent implements OnChanges {

  @Input() addressToEdit: any;
  @Input() isEdit: boolean;
  @Input() disableEmail: boolean = false;
  @Output() hideNewAddressModal = new EventEmitter();
  @Output() showValidateAddressModal = new EventEmitter<any>();
  @Output() refreshAddresses = new EventEmitter<any>();
  @ViewChild('firstElement') firstElement?: ElementRef;
  @ViewChild('lastElement') lastElement?: ElementRef;
  @ViewChild('closeBtn') closeBtn?: ElementRef;

  newAddress = new FormGroup({
    companyName: new FormControl("", [Validators.required]),
    firstName: new FormControl("", [Validators.required]),
    lastName: new FormControl("", [Validators.required]),
    email: new FormControl("", []),
    line1: new FormControl("", [Validators.required]),
    line2: new FormControl("", [this.poBoxValidator()]),
    town: new FormControl("", [Validators.required]),
    country: new FormControl("US", [Validators.required]),
    region: new FormControl("", [Validators.required]),
    postalCode: new FormControl("", [Validators.required]),
    phone: new FormControl("", [])
  });
  submitted: boolean = false;
  addNewAddressError: boolean = false;
  regions: any[] = [];
  updateAddressError: string = '';
  loader: boolean = false;

  private destroyRef = inject(DestroyRef);

  constructor(private accountSettingsService: AccountSettingsService,
    private cd: ChangeDetectorRef,
    private addressService: WsAddressesService,
    private wsAccertifyService: WsAccertifyService
  ) {
    this.getRegions();
  }

  poBoxValidator(): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
      if (!control.value) return null;
  
      const poBoxPattern = /\bPO Box\b/i;
      return poBoxPattern.test(control.value) ? { 'poBoxError': true } : null;
    };
  }

  ngOnInit(){
    this.wsAccertifyService.loadAccertifyCollectorScript().then((res) => {
      console.log('Accertify script loaded successfully ');
    }).catch(error => {
      console.error('Error loading Accertify script:', error);
    });;
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['isEdit'].currentValue) {
      this.newAddress.setValue({
        companyName: this.addressToEdit.addressDescription || '',
        firstName: this.addressToEdit.firstName || '',
        lastName: this.addressToEdit.lastName || '',
        email: this.addressToEdit.email || '',
        line1: this.addressToEdit.line1 || '',
        line2: this.addressToEdit.line2 || '',
        town: this.addressToEdit.town || '',
        country: this.addressToEdit.country?.isocode || '',
        region: this.addressToEdit.region?.isocode || '',
        postalCode: this.addressToEdit.postalCode || '',
        phone: this.addressToEdit.phone || '',
      })
      if (this.disableEmail) {
        this.newAddress.get('email')?.disable({ emitEvent: false });
      } else {
        this.newAddress.get('email')?.enable({ emitEvent: false });
      }
    }
  }

  closeModal() {
    this.hideNewAddressModal.emit();
  }

  getRegions() {
    this.accountSettingsService.getRegions('US').pipe(takeUntilDestroyed(this.destroyRef)).subscribe(data => {
      this.regions = data?.regions || [];
      this.cd.detectChanges();
    })
  }

  ngAfterViewInit(): void {
    setTimeout(() => {
      this.firstElement?.nativeElement?.focus();
    }, 0);
  }

  validateAddress(isEdit: boolean) {
    this.submitted = true;
    const payload = {
      firstName: this.newAddress.value.firstName,
      lastName: this.newAddress.value.lastName,
      line1: this.newAddress.value.line1,
      line2: this.newAddress.value.line2,
      town: this.newAddress.value.town,
      region: {
        isocode: this.newAddress.value.region
      },
      postalCode: this.newAddress.value.postalCode,
      country: {
        isocode: this.newAddress.value.country,
        name: "United States"
      }
    }
    this.addNewAddressError = false;
    if (this.newAddress.valid) {
      this.loader = true;
      this.addressService.validateAddress(payload)
        .pipe(takeUntilDestroyed(this.destroyRef))
        .subscribe((data: any) => {
          if (data?.decision === 'ACCEPT') {
            this.showValidateAddressModal.emit({ newAddress: this.newAddress.value, validatedAddressResponse: data, isEdit: isEdit, addressToEdit: isEdit ? this.addressToEdit : null });
          } else {
            this.addNewAddressError = true;
          }
          this.loader = false;
          this.cd.detectChanges();
        }, () => {
          this.loader = false;
          this.addNewAddressError = true;
          this.cd.detectChanges();
        })

    }
  }




  validateZipCode(event: KeyboardEvent): void {
    const input = event.target as HTMLInputElement;
    const value = input.value;

    const forbiddenKeys = ['e', 'E', '+'];
    if (value.length > 10 || forbiddenKeys.includes(event.key)) {
      event.preventDefault();
    }
  }

  handleTab(event: KeyboardEvent): void {
    if (event.key === 'Tab' && this.closeBtn && this.lastElement) {
      const { nativeElement: first } = this.closeBtn;
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