import { Component, DestroyRef, inject } from '@angular/core';
import { AbstractControl, FormControl, FormGroup, ValidationErrors, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Location } from '@angular/common';
import { W9Formservice } from '../w9-service/w9-form.service';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { WsCurrentUserService } from '../../../ws-services/state-services/currentuser.service';
@Component({
  selector: 'ws-w9referralcodeform',
  templateUrl: './w9referralcodeform.component.html',
  styleUrl: './w9referralcodeform.component.scss'
})
export class W9referralcodeformComponent {

  private router = inject(Router);
  private location = inject(Location);
  private destroyRef = inject(DestroyRef);
  private service = inject(W9Formservice);
  private current = inject(WsCurrentUserService);

  discountArray = this.service.selectDiscount;

  formContainsError = false;

  w9ReferralForm = new FormGroup({
    newReferralCode: new FormControl('', [Validators.required, this.alphanumericValidator]),
    newReferralLastName: new FormControl('', [Validators.required, this.alphanumericValidator]),
    discount: new FormControl('0')
  });

  ngOnInit() {
    this.w9ReferralForm.valueChanges.subscribe((value) => {
      if (this.w9ReferralForm.valid) {
        this.formContainsError = false;
      } else {
        this.formContainsError = true;
      }
    });
  }

  alphanumericValidator(control: AbstractControl): ValidationErrors | null {
    const value = control.value;
    const alphanumericRegex = /^[a-zA-Z0-9]*$/;
  
    if (value && !alphanumericRegex.test(value)) {
      return { alphanumeric: true };
    }
  
    return null;
  }

  submit() {
    if (this.w9ReferralForm.valid) {
      this.formContainsError = false;
      this.callPostApi();
    } else {
      this.formContainsError = true;
      this.w9ReferralForm.markAllAsTouched();
    }
  }

  callPostApi() {
    this.service.postReferralCodeForm(this.createPayLoad())
      .pipe(takeUntilDestroyed(this.destroyRef)).subscribe(
        { 
          next: () =>{
            this.current.callCurrentApiToUpdateState();
            this.router.navigate(['practitioner/patient-direct/success']) ;
          } 

        });
  }

  createPayLoad() {
    return {
      "referralCode": this.w9ReferralForm.controls.newReferralCode.value,
      "discount": this.w9ReferralForm.controls.discount.value,
      "lastName": this.w9ReferralForm.controls.newReferralLastName.value,
    }
  }

  back() {
    this.location.back();
  }
}
