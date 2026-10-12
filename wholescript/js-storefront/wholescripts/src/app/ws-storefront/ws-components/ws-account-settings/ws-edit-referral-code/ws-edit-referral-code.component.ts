import { PatientDirectService } from '../../../ws-services/ws-patient-direct.service';
import { TemplatePortal } from '@angular/cdk/portal';
import { AfterViewInit, ChangeDetectorRef, Component, DestroyRef, inject, Input, TemplateRef, ViewChild, ViewContainerRef , Output, EventEmitter} from '@angular/core';
import { FormGroup, FormControl, Validators , AbstractControl, ValidationErrors, ValidatorFn  } from '@angular/forms';
import { WsOverlay } from '../../../ws-services/ws-overlay.service';
import { AccountSettingsService } from '../../../ws-services/ws-account-settings.service';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { BsModalRef, BsModalService, ModalOptions } from 'ngx-bootstrap/modal';
import { WsConfirmReferralPopupComponent } from '../ws-confirm-referral-popup/ws-confirm-referral-popup.component';
import { Router } from '@angular/router';
import { GlobalMessageService, GlobalMessageType } from '@spartacus/core';

interface NavigationState {
  data: any; // Replace 'any' with the actual type of 'code' (e.g., string, number, or custom interface)
}
@Component({
  selector: 'ws-edit-referral-code',
  templateUrl: './ws-edit-referral-code.component.html',
  styleUrls: ['./ws-edit-referral-code.component.scss']
})
export class WsEditReferralComponent implements AfterViewInit {
  @Input() referralDetails: any;
  @Output() saveChanges = new EventEmitter();
  baseUrl = window.location.origin;
  loader: boolean = false;
 private _viewContainerRef = inject(ViewContainerRef);
  private overLayService = inject(WsOverlay);
  disallowedPOBoxTextInAddress: any[] = ['PO BOX','P.O BOX','P.O. BOX','P.O. BOX.'];
  accountInfoForm = new FormGroup({
    name: new FormControl("", []),
    practiceName: new FormControl("", [
      Validators.required, this.noWhitespaceValidator
    ]),
    description: new FormControl("", []),
    address: new FormControl("", [Validators.required, this.noWhitespaceValidator, this?.forbiddenTextValidator(this?.disallowedPOBoxTextInAddress)]),
    aptOrSuite: new FormControl("", [this?.forbiddenTextValidator(this?.disallowedPOBoxTextInAddress)]),
    city: new FormControl("", [Validators.required, this.noWhitespaceValidator, Validators.pattern(/^[a-zA-Z\s]+$/)]),
    state: new FormControl("", [Validators.required]),
    zipCode: new FormControl("", [Validators.required,, this.noWhitespaceValidator, Validators.pattern(/^\d{5}(-\d{4})?\s*$/)]),
    phone: new FormControl("", [Validators.required, this.noWhitespaceValidator,
      Validators.pattern(
        /^\+?1?\s*(\(\d{3}\)|\d{3})[\s.-]?\d{3}[\s.-]?\d{4}(?:\s*(?:ext|x|extension)\s*\d{1,5})?\s*$/
      )]),
    email: new FormControl("", [Validators.required,, this.noWhitespaceValidator, Validators.pattern(
      /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/)]),
    webSite: new FormControl("", [])
  })
  submitted: boolean = false;
  formSuccess: boolean = false;
  templatePortal: TemplatePortal<any>;
  referralCodeDetails: any;
  selectedFile!: File;
  isDragOver = false;
  private destroyRef = inject(DestroyRef);
  toastMessage: string = '';
  regions: any[] = [];
  imageUrl: string | ArrayBuffer | null = null;
  pageUrl = window.location.pathname;
  isPatientDirect: boolean = false;
  bsModalRef?: BsModalRef;
  referralLink: string = '';
  discount: any;

  constructor(private accountSettingsService: AccountSettingsService, private cd: ChangeDetectorRef, private referralService: PatientDirectService,
    private dialog: BsModalService, private router: Router, private globalMessageService: GlobalMessageService
  ) {
    this.getRegions();
    this.isPatientDirect = this.pageUrl.includes('patient-direct');
  }

  noWhitespaceValidator(control: AbstractControl): ValidationErrors | null {
    const isWhitespace = (control.value || '').trim().length === 0;
    const isValid = !isWhitespace;
    return isValid ? null : { whitespace: true };
  }

  goBack() {
    this.router.navigateByUrl('patient-direct-hub/Referral-code');
  }

  ngAfterViewInit() {
    const state = history.state as NavigationState;
    this.referralDetails = state?.data ?? null;
    if(!this.referralDetails) this.referralDetails = sessionStorage.getItem('editReferral');
    this.referralLink = this.referralDetails?.referralLink;
    this.discount = this.referralDetails?.discount;
    this.getAddressForReferralCode();
  }

  ngOnDestroy(){
    sessionStorage.removeItem('editReferral');
  }

  openRemoveFromDirectoryModal() {
    this.overLayService.openModal(this.templatePortal, 'auto');
  }

  closeRemoveFromDirectoryModal() {
    this.overLayService.closeAllModals();
  }

  getRegions() {
    this.accountSettingsService.getRegions('US').pipe(takeUntilDestroyed(this.destroyRef)).subscribe(data => {
      this.regions = data?.regions || [];
      this.cd.detectChanges();
    })
  }

  confirmReferral() {
    const initialState: ModalOptions = {
      class: 'ws-right-modal',
    };
    this.bsModalRef = this.dialog.show(
      WsConfirmReferralPopupComponent,
      initialState
    );
    this.bsModalRef.content.updateChanges.subscribe((res: any) => {
      if(res) {
        this.updateChanges();
        this.cd.detectChanges();
      }
    })
  }

  forbiddenTextValidator(disallowed: string | string[]): ValidatorFn {
    const disallowedList = Array.isArray(disallowed) ? disallowed : [disallowed];

    return (control: AbstractControl): ValidationErrors | null => {
      const value = control.value?.toLowerCase();
      if (!value) return null;

      const hasDisallowedText = disallowedList.some(text => value.includes(text.toLowerCase()));
      return hasDisallowedText ? { forbiddenText: { value: control.value } } : null;
    };
  }

  onZipcodeInput(event? : Event) {
    const input = event.target as HTMLInputElement;
    input.value = input.value.replace(/[^0-9\-]/g, '').slice(0, 10);
    this.accountInfoForm.get('zipCode')?.setValue(input.value, { emitEvent: false });
  }

  onSubmit() {
    this.submitted = true;
    if (this.accountInfoForm.valid) {
      this.confirmReferral();
    }
  }
  
  updateChanges() {
    this.loader = true;
    const payload = {
      id: this.accountInfoForm.value.email,
      title: "",
      titleCode: this.accountInfoForm.value.name,
      firstName: this.accountInfoForm.value.practiceName,
      lastName: this.referralDetails.lastName,
      companyName: "",
      line1: this.accountInfoForm.value.address,
      line2: this.accountInfoForm.value.aptOrSuite,
      town: this.accountInfoForm.value.city,
      region: {
        isocode: this.accountInfoForm.value.state
      },
      district: "",
      postalCode: this.accountInfoForm.value.zipCode,
      phone: this.accountInfoForm.value.phone,
      cellphone: "",
      email: this.accountInfoForm.value.email,
      country: {
        isocode: "US",
        name: "United States"
      },
      shippingAddress: true,
      defaultAddress: true,
      visibleInAddressBook: true,
      formattedAddress: "",
      website: this.accountInfoForm.value.webSite,
      description: this.accountInfoForm.value.description
    }
    const referral = {
      code: `${this.referralDetails.referralCode}|${this.referralDetails.lastName}`,
      link: this.referralLink || '',
      discount: this.discount
    }
    if (this.accountInfoForm.valid) {
      this.referralService.updateReferralCode(referral).subscribe((res) => {
        this.toastMessage = 'Referral code updated successfully';
        this.referralService.addAddressReferralCode(payload, `${this.referralDetails.referralCode}|${this.referralDetails.lastName}`).pipe(takeUntilDestroyed(this.destroyRef)).subscribe(data => {
          this.showToastMessage();
          this.uploadFile();
           this.cd.detectChanges();
        }, () => {
          this.loader = false;
        })
      }, () => {
        this.loader = false;
      })
    }
  }

  showToastMessage() {
      this.globalMessageService.add(
        this.toastMessage,
        GlobalMessageType.MSG_TYPE_INFO,
        3000
      );
      this.toastMessage = '';
    }

  updateReferralDetails() {
    console.log(this.referralDetails)
  }

  resetToastMessage() {
    setTimeout(() => {
      this.toastMessage = '';
      this.cd.detectChanges();
    }, 3000)
  }

  getAddressForReferralCode() {
    this.referralService.getAddressReferralCode(`${this.referralDetails.referralCode}|${this.referralDetails.lastName}`).pipe(takeUntilDestroyed(this.destroyRef)).subscribe(data => {
      this.populateAddressForReferralCode(data);
      this.cd.detectChanges();
    })
  }

  populateAddressForReferralCode(response: any) {
    this.referralCodeDetails = response;
    this.accountInfoForm.patchValue({
      name: response?.address?.titleCode || '',
      practiceName: response?.address?.firstName || '',
      address: response?.address?.line1 || '',
      aptOrSuite: response?.address?.line2 || '',
      city: response?.address?.town || '',
      state: response?.address?.region?.isocode || '',
      zipCode: response?.address?.postalCode || '',
      phone: response?.phone || response?.address?.phone || '',
      email: response?.email || response?.address?.email || '',
      webSite: response?.website || '',
      description: response?.description || ''
    })
  }

  onFileSelected(event: Event) {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      this.selectedFile = input.files[0];
      if (this.selectedFile) {
        const reader = new FileReader();
        reader.onload = () => {
          this.imageUrl = reader.result;
          this.cd.detectChanges();
        };
        reader.readAsDataURL(this.selectedFile);
      }
    }
  }

  onDragOver(event: DragEvent) {
    event.preventDefault();
    this.isDragOver = true;
  }

  onDragLeave() {
    this.isDragOver = false;
  }

  onDrop(event: DragEvent) {
    event.preventDefault();
    this.isDragOver = false;

    if (event.dataTransfer?.files && event.dataTransfer.files.length > 0) {
      this.selectedFile = event.dataTransfer.files[0];
      if (this.selectedFile) {
        const reader = new FileReader();
        reader.onload = () => {
          this.imageUrl = reader.result;
          this.cd.detectChanges();
        };
        reader.readAsDataURL(this.selectedFile);
      }
    }
  }

  uploadFile() {
    if (this.selectedFile) {
      this.referralService.uploadProfileImage(this.selectedFile, this.referralDetails.referralCode, this.referralDetails.lastName).pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe(
        (response) => {
          console.log('File uploaded successfully!', response);
          this.loader = false;
          this.goBack();
        },
        (error) => {
          console.error('Error uploading file', error);
          this.loader = false;
          this.goBack();
        }
      );
    } else  {
      this.loader = false;
      this.goBack();
    }
  }

  removeImage() {
    this.selectedFile = null;
    this.imageUrl = null;
  }

}
