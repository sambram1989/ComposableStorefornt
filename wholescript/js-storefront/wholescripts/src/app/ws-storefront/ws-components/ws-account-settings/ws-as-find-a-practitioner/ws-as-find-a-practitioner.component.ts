import { TemplatePortal } from '@angular/cdk/portal';
import { ChangeDetectorRef, Component, DestroyRef, inject, Input, TemplateRef, ViewChild, ViewContainerRef } from '@angular/core';
import { FormGroup, FormControl, Validators, AbstractControl, ValidationErrors } from '@angular/forms';
import { WsOverlay } from '../../../ws-services/ws-overlay.service';
import { AccountSettingsService } from '../../../ws-services/ws-account-settings.service';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { CmsService, Config } from '@spartacus/core';
import { Observable } from 'rxjs';

@Component({
  selector: 'ws-as-find-a-practitioner',
  templateUrl: './ws-as-find-a-practitioner.component.html',
  styleUrl: './ws-as-find-a-practitioner.component.scss'
})
export class WsAsFindAPractitionerComponent {

  private _viewContainerRef = inject(ViewContainerRef);
  private overLayService = inject(WsOverlay);
  accountInfoForm = new FormGroup({
    name: new FormControl("", [
      Validators.required, this.noWhitespaceValidator
    ]),
    practiceName: new FormControl("", [
      Validators.required, this.noWhitespaceValidator
    ]),
    address: new FormControl("", [Validators.required, this.noWhitespaceValidator]),
    aptOrSuite: new FormControl("", []),
    city: new FormControl("", [Validators.required, this.noWhitespaceValidator, Validators.pattern(/^[a-zA-Z\s]+$/)]),
    state: new FormControl("", [Validators.required]),
    zipCode: new FormControl("", [
      Validators.required,
      Validators.pattern(/^\d{5}(-\d{4})?\s*$/)
    ]),
    phone: new FormControl("", [
      Validators.required,
      Validators.pattern(
        /^\+?1?\s*(\(\d{3}\)|\d{3})[\s.-]?\d{3}[\s.-]?\d{4}(?:\s*(?:ext|x|extension)\s*\d{1,5})?\s*$/
      )
    ]),
    email: new FormControl("", [Validators.required, Validators.pattern(
      /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/)]),
    webSite: new FormControl("", [])
  })
  submitted: boolean = false;
  formSuccess: boolean = false;
  @ViewChild('removeFromDirectoryPopUp') removeFromDirectoryPopUp: TemplateRef<unknown>;
  templatePortal: TemplatePortal<any>;
  practionerDetails: any;
  selectedFile!: File;
  isDragOver = false;
  private destroyRef = inject(DestroyRef);
  toastMessage: string = '';
  regions: any[] = [];
  imageUrl: string | ArrayBuffer | null = null;
  pageUrl = window.location.pathname;
  isPatientDirect: boolean = false;
  @Input() referralDetails: any;
  findPractitionerHeading$: Observable<any> = this.cmsService.getComponentData('FindAPractitionerContent');
  findPractitionerPara$: Observable<any> = this.cmsService.getComponentData('PatientDirectoryParagraph');
  loading: boolean = false;

  constructor(
    private accountSettingsService: AccountSettingsService, 
    private cd: ChangeDetectorRef,
    protected cmsService: CmsService,
    private config: Config
  ) {
    this.getPractionerDetails();
    this.getRegions();
    this.isPatientDirect = this.pageUrl.includes('patient-direct');
  }

  ngAfterViewInit() {
    this.templatePortal = new TemplatePortal(
      this.removeFromDirectoryPopUp,
      this._viewContainerRef,
    );
    if(document.getElementsByTagName('title') && !document.getElementsByTagName('title')[0].innerText) document.getElementsByTagName('title')[0].innerText = 'Find Practitioner';
  }

  noWhitespaceValidator(control: AbstractControl): ValidationErrors | null {
    const isWhitespace = (control.value || '').trim().length === 0;
    const isValid = !isWhitespace;
    return isValid ? null : { whitespace: true };
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

  onSubmit() {
    this.submitted = true;
    const payload = {
      practitionerDetails: {
        name: this.accountInfoForm.value.practiceName,
        address: {
          line1: this.accountInfoForm.value.address,
          line2: this.accountInfoForm.value.aptOrSuite,
          town: this.accountInfoForm.value.city,
          postalCode: this.accountInfoForm.value.zipCode,
          region: {
            isocode: this.accountInfoForm.value.state
          }
        },
        phoneNumber: this.accountInfoForm.value.phone,
        webSite: this.accountInfoForm.value.webSite,
        email: this.accountInfoForm.value.email,
        practitionerName: this.accountInfoForm.value.name
      }
    }
    if (this.accountInfoForm.valid) {
      this.loading = true;
      if(this.practionerDetails && this.practionerDetails['practitionerDirectoryOptIn']) {
        this.accountSettingsService.updatePractionerDetails(payload, this.practionerDetails?.name || this.accountInfoForm.value.practiceName).pipe(takeUntilDestroyed(this.destroyRef)).subscribe(data => {
          this.toastMessage = 'Profile information saved';
          this.resetToastMessage();
          this.uploadFile();
          this.selectedFile = null;
          this.loading = false;
          this.cd.detectChanges();
        }, () => {
          this.loading = false;
          this.cd.detectChanges();
        })
      } else {
        this.accountSettingsService.addPractionerDetails(payload).pipe(takeUntilDestroyed(this.destroyRef)).subscribe(data => {
          this.toastMessage = 'Profile information saved';
          this.resetToastMessage();
          this.uploadFile();
          this.selectedFile = null;
          this.loading = false;
          this.cd.detectChanges();
        }, () => {
          this.loading = false;
          this.cd.detectChanges();
        })
      }
      
    }
  }

  resetToastMessage() {
    setTimeout(() => {
      this.toastMessage = '';
      this.cd.detectChanges();
    }, 2000)
  }

  getPractionerDetails() {
    this.loading = true;
    this.accountSettingsService.getPractionerDetails().pipe(takeUntilDestroyed(this.destroyRef)).subscribe(data => {
      if(!!data['practitionerDirectoryOptIn']) {
        this.populatePractionerDetails(data);
      } else {
        this.practionerDetails = {};
        this.accountInfoForm.reset();
      }
      this.loading = false;
      this.cd.detectChanges();
    }, error => {
      this.loading = false;
      this.practionerDetails = {};
      this.accountInfoForm.reset();
      this.cd.detectChanges();
    });
  }

  populatePractionerDetails(response: any) {
    this.practionerDetails = response;
    if(response?.mapIcon?.url) this.imageUrl = response?.mapIcon?.url;
    this.accountInfoForm.patchValue({
      name: response?.practitionerName || '',
      practiceName: response?.displayName || '',
      address: response?.address?.line1 || '',
      aptOrSuite: response?.address?.line2 || '',
      city: response?.address?.town || '',
      state: response?.address?.region?.isocode || '',
      zipCode: response?.address?.postalCode || '',
      phone: response?.phoneNumber || '',
      email: response?.email || '',
      webSite: response?.webSite || ''
    })
  }

  onFileSelected(event: Event) {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      this.selectedFile = input.files[0];
      if (this.selectedFile) {
        const img = new Image();
        const objectUrl = URL.createObjectURL(this.selectedFile);
        
        img.onload = () => {
          URL.revokeObjectURL(objectUrl);
          if (img.width <= 300 && img.height <= 300) {
            const reader = new FileReader();
            reader.onload = () => {
              this.imageUrl = reader.result as string;
              this.cd.detectChanges();
            };
            reader.readAsDataURL(this.selectedFile!);
          } else {
            this.selectedFile = null;
            this.imageUrl = null;
            input.value = '';
            alert('Image dimensions must be 300x300 pixels or smaller.');
            this.cd.detectChanges();
          }
        };
        
        img.onerror = () => {
          URL.revokeObjectURL(objectUrl);
          this.selectedFile = null;
          this.imageUrl = null;
          input.value = '';
          alert('Failed to load image.');
          this.cd.detectChanges();
        };
        
        img.src = objectUrl;
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
        const validTypes = ['image/png', 'image/jpeg', 'image/gif'];
        if (!validTypes.includes(this.selectedFile.type)) {
          alert('Invalid file type. Only PNG, JPG, and GIF are allowed.');
          this.selectedFile = null;
          this.imageUrl = null;
          return;
        }
  
        const img = new Image();
        const objectUrl = URL.createObjectURL(this.selectedFile);
  
        img.onload = () => {
          URL.revokeObjectURL(objectUrl);
          if (img.width <= 300 && img.height <= 300) {
            const reader = new FileReader();
            reader.onload = () => {
              this.imageUrl = reader.result as string;
              this.cd.detectChanges();
            };
            reader.readAsDataURL(this.selectedFile);
          } else {
            this.selectedFile = null;
            this.imageUrl = null;
            alert('Image dimensions must be 300x300 pixels or smaller.');
            this.cd.detectChanges();
          }
        };
  
        img.onerror = () => {
          URL.revokeObjectURL(objectUrl);
          this.selectedFile = null;
          this.imageUrl = null;
          alert('Failed to load image.');
          this.cd.detectChanges();
        };

        img.src = objectUrl;
      }
    }
  }

  uploadFile() {
    if (this.selectedFile) {
      this.accountSettingsService.uploadProfileImage(this.selectedFile, this.practionerDetails?.name || this.accountInfoForm.value.practiceName).pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe(
        (response) => {
          console.log('File uploaded successfully!', response);
          this.getPractionerDetails();
        },
        (error) => {
          console.error('Error uploading file', error);
        }
      );
    }
  }

  removeImage() {
    this.selectedFile = null;
    this.imageUrl = null;
    if(this.practionerDetails?.mapIcon) this.practionerDetails.mapIcon = null;
  }

  removeFromDirectory() {
    this.loading = true;
    this.closeRemoveFromDirectoryModal();
    this.accountSettingsService.removePractionerFromDirectory(this.practionerDetails?.name || this.accountInfoForm.value.practiceName).pipe(takeUntilDestroyed(this.destroyRef)).subscribe(data => {
      this.toastMessage = 'Removed from directory';
      this.submitted = false;
      this.resetToastMessage();
      this.practionerDetails = {};
      this.accountInfoForm.reset();
      this.loading = false;
      this.removeImage();
      this.cd.detectChanges();
    }, () => {
      this.loading = false;
      this.cd.detectChanges();
    })
  }

  getUrl() {
    const baseUrl = this.config.backend?.occ?.baseUrl ? this.config.backend?.occ?.baseUrl : '';
    return `${baseUrl}${this.practionerDetails?.mapIcon?.url}` || '';
  }

}
