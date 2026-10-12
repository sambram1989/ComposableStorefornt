import { ChangeDetectorRef, Component, OnDestroy } from '@angular/core';
import { CmsService } from '@spartacus/core';
import { ContactUsPageService } from '../../../ws-services/ws-contact-us-page.service';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Subject, takeUntil } from 'rxjs';

@Component({
  selector: 'ws-contact-us-page',
  templateUrl: './ws-contact-us-page.component.html',
  styleUrl: './ws-contact-us-page.component.scss'
})
export class WsContactUsPageComponent implements OnDestroy {
  contactUsForm = new FormGroup({
    firstName: new FormControl("", [
      Validators.required
    ]),
    lastName: new FormControl("", [
      Validators.required
    ]),
    phone: new FormControl("", []),
    email: new FormControl("", [Validators.required, Validators.email]),
    accountNumber: new FormControl("", []),
    message: new FormControl("", [Validators.required]),
    category: new FormControl("", []),
  })
  submitted: boolean = false;
  formSuccess: boolean = false;
  categories: any[] = [];
  cmsComponentsData: any = {};
  private ngUnsubscribe = new Subject<void>();

  constructor(protected cmsservice: CmsService, private contactUsPageService: ContactUsPageService,
    private cd: ChangeDetectorRef
  ) {
    this.getAllDetails();
  }

  getAllDetails() {
    this.contactUsPageService.fetchCategories().pipe(takeUntil(this.ngUnsubscribe)).subscribe(data => {
      this.categories = data || [];
      this.contactUsForm.patchValue({
        category: this.categories?.[0]?.Key || ''
      })
      this.cd.detectChanges();
    })
  }

  onSubmit() {
    this.submitted = true;
    const payload = {
      "firstName": this.contactUsForm.value.firstName,
      "lastName": this.contactUsForm.value.lastName,
      "phone": this.contactUsForm.value.phone,
      "email": this.contactUsForm.value.email,
      "accountNumber": this.contactUsForm.value.accountNumber,
      "message": this.contactUsForm.value.message,
      "category": this.contactUsForm.value.category,
    }
    if (this.contactUsForm.valid) {
      this.contactUsPageService.submitContactUsForm(payload).pipe(takeUntil(this.ngUnsubscribe)).subscribe(data => {
        this.formSuccess = true;
        this.submitted = false;
        this.contactUsForm.reset();
        this.cd.detectChanges();
      })
    }
  }

  ngOnDestroy() {
    this.ngUnsubscribe.next();
    this.ngUnsubscribe.complete();
  }


}
