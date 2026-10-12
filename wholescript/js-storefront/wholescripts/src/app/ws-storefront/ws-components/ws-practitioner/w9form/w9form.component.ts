import { Component, DestroyRef, inject } from '@angular/core';
import { AbstractControl, FormControl, FormGroup, ValidatorFn, Validators, ValidationErrors } from '@angular/forms';
import { Router } from '@angular/router';
import { Location } from '@angular/common';
import { Observable, map, tap } from 'rxjs';
import { AccountSettingsService } from '../../../ws-services/ws-account-settings.service';
import { W9Formservice } from '../w9-service/w9-form.service';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';

@Component({
  selector: 'ws-w9form',
  templateUrl: './w9form.component.html',
  styleUrl: './w9form.component.scss'
})
export class W9formComponent {
  private accountSettingsService = inject(AccountSettingsService);
  private router = inject(Router);
  private location = inject(Location);
  private service = inject(W9Formservice);
  private destroyRef = inject(DestroyRef);
  
  tinNumberValidator: ValidatorFn = (control: AbstractControl): { [key: string]: boolean } | null => {
    const formGroup = control as FormGroup;
    const tinNumber = formGroup.get('tinNumber');
    const tinNumberOp = formGroup.get('tinNumberOp');
  
    if (!tinNumber || !tinNumberOp) return null;   
    if (tinNumberOp.value && !tinNumber.value) {
      tinNumber.setErrors(null);
      return null;
    }  
    if (!tinNumber.value) {
      tinNumber.setErrors({ required: true });
      return { tinNumberRequired: true };
    }  
    tinNumber.setErrors(null);
    return null;
  };

  radioGroupValidator: ValidatorFn = (control: AbstractControl): { [key: string]: boolean } | null => {
    if (!(control instanceof FormGroup)) return null;
  
    const radioGroup = control.get('radioGroup');
    const radioOne = control.get('radioOne');
    const radioTwo = control.get('radioTwo');
  
    if (!radioGroup || !radioOne || !radioTwo) return null;
  
    radioOne.setErrors(null);
    radioTwo.setErrors(null);
  
    switch (radioGroup.value) {
      case 'TaxClassLimited':
        if (!radioOne.value) {
          radioOne.setErrors({ required: true });
          return { radioOneRequired: true };
        }
        break;
      case 'TaxClassOther':
        if (!radioTwo.value) {
          radioTwo.setErrors({ required: true });
          return { radioTwoRequired: true };
        }
        break;
      default:
        break;
    }
  
    return null;
  };
  

  radioGrouptax = this.service.fedTaxClass;
  limitedLiabilityfedTax = this.service.limitedLiabilityFedTaxClass;
  otherFedTax = this.service.otherFedtaxClass;

  toDay: any = new Date()
  date = this.toDay.getDate();
  month = this.toDay.getMonth() + 1; 
  year = this.toDay.getFullYear();
  formContainsError:boolean = null;

  w9Form = new FormGroup({
    name: new FormControl('',[Validators.required, this.noWhitespaceValidator]),
    businessName: new FormControl(''),
    radioGroup: new FormControl('',Validators.required),
    radioOne: new FormControl(''),
    radioTwo: new FormControl(''),
    exemptionPayeeCode: new FormControl(''),
    exemptionFATCAcode: new FormControl(''),
    address: new FormControl('', [Validators.required, this.noWhitespaceValidator]),
    city: new FormControl('', [Validators.required, this.noWhitespaceValidator]),
    state: new FormControl('', Validators.required),
    zipCode: new FormControl('', [Validators.required, this.noWhitespaceValidator, Validators.pattern(/^\d{5}(-\d{4})?$/)]),
    listAccountNumber: new FormControl(''),
    RequesterName: new FormControl(''),
    tinNumber: new FormControl('', Validators.required),
    tinNumberOp: new FormControl(''),
    signatureName: new FormControl('', [Validators.required, this.noWhitespaceValidator]),
    date: new FormControl(`${this.date}/${this.month}/${this.year}`)
  },{validators:[this.tinNumberValidator,this.radioGroupValidator]});
  regions$: Observable<any> = this.accountSettingsService.getRegions('US').pipe(
    map((data: any) =>
      data?.regions?.map((item: any) => ({
        code: item.isocode,
        value: item.name,
      }))
    ),
    tap(regions => {if (regions?.length)  this.w9Form.controls.state.setValue(regions[0].code)})
  );

  ngOnInit() {
    this.w9Form.valueChanges.pipe(takeUntilDestroyed(this.destroyRef)).subscribe((res) => {
      if(this.w9Form.valid) this.formContainsError = false;
    });
  }

  onZipCodeInput(event: Event) {
    const input = event.target as HTMLInputElement;
    input.value = input.value.replace(/[^0-9\-]/g, '').slice(0, 10);
    this.w9Form.get('zipCode')?.setValue(input.value, { emitEvent: false });
  }
  

  noWhitespaceValidator(control: AbstractControl): ValidationErrors | null {
    const isWhitespace = (control.value || '').trim().length === 0;
    const isValid = !isWhitespace;
    return isValid ? null : { whitespace: true };
  }

  canNavigate(){

    if(this.w9Form.valid){
      this.formContainsError = false;
      this.callPostAPI();

    }else{
       this.formContainsError = true;
      this.w9Form.markAllAsTouched();
    }
  }

  createPayLoad() {
    const controls = this.w9Form.controls;
    const commonPayload = {
      name: controls.name.value,
      businessName: controls.businessName.value,
      fedTaxClass: controls.radioGroup.value,
      exemptPayee: controls.exemptionPayeeCode.value,
      exemptFatca: controls.exemptionFATCAcode.value,
      address: {
        line1: controls.address.value,
        town: controls.city.value,
        region: { isocode: controls.state.value },
        postalCode: controls.zipCode.value,
      },
      accountNumbers: controls.listAccountNumber.value,
      requestersInfo: controls.RequesterName.value,
      ssn: controls.tinNumber.value,
      ein: controls.tinNumberOp.value,
      signature: controls.signatureName.value,
      signatureDate: this.toDay.getFullYear() + '-' +
      String(this.toDay.getMonth() + 1).padStart(2, '0') + '-' +
      String(this.toDay.getDate()).padStart(2, '0') + ' ' +
      String(this.toDay.getHours()).padStart(2, '0') + ':' +
      String(this.toDay.getMinutes()).padStart(2, '0') + ':' +
      String(this.toDay.getSeconds()).padStart(2, '0'),
      browserString: navigator.userAgent,
      ip: "127.0.0.1",
    };
  
    switch (controls.radioGroup.value) {
      case 'TaxClassLimited':
        return { ...commonPayload, taxClassLimitedText: controls.radioOne.value };
      case 'TaxClassOther':
        return { ...commonPayload, taxClassOtherText: controls.radioTwo.value };
      default:
        return commonPayload;
    }
  }
  

  callPostAPI(){
    this.service.postW9form(this.createPayLoad()).pipe(takeUntilDestroyed(this.destroyRef)).subscribe({
      next:()=>{this.router.navigate(['practitioner/patient-direct/referral-code']);}
    })
  }

  back(){
   this.location.back();
  }
 
}
