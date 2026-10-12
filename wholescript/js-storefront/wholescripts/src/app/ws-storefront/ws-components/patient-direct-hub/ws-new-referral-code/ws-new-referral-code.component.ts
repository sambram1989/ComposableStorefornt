import { ChangeDetectorRef, Component } from '@angular/core';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { PatientDirectService } from '../../../ws-services/ws-patient-direct.service';

@Component({
  selector: 'ws-new-referral-code',
  templateUrl: './ws-new-referral-code.component.html',
  styleUrls: ['./ws-new-referral-code.component.scss']
})
export class WsNewReferralCodeComponent {
  isModalOpen = true;
  name: string = '';
  code: string = '';
  select: string = 'Select discount';
  hasError: boolean = false;
  alerts :any[] = [];
  createCodeError: boolean = false;

  referralCodeValue: string = '';
  lastNameValue: string = '';

  isReferralCodeValid: boolean = true;
  isLastNameValid: boolean = true;

  constructor(public bsModalRef: BsModalRef, private referralsService: PatientDirectService,
    private cd: ChangeDetectorRef
  ) {}

  close() {
    this.bsModalRef.hide();
  }

  onKeyDown(event: KeyboardEvent) {
    if (event.key === 'Enter' || event.key === ' ') {
      event.preventDefault();
      this.close()
    }
  }

  createReferralCode() {
    this.isReferralCodeValid = this.isAlphanumericOnly(this.referralCodeValue.trim());    // check if referralcode in only Alphanumeric or not
    this.isLastNameValid = this.isAlphanumericOnly(this.lastNameValue.trim());    // check if lastname in only Alphanumeric or not

    if(!this.isReferralCodeValid || !this.isLastNameValid) return; // if any one value is not alphanumeric then return

    this.createCodeError = false;
    if(!this.name || !this.code || this.select === 'Select discount') {
      this.hasError = true;
      return;
    }
    this.hasError = false;
    let payload = {
      "referralCode": this.code,
      "discount": this.select.replace('%', ''),
      "lastName": this.name,
    }
    this.referralsService.createNewReferralCode(payload).subscribe(() => {
      setTimeout(() => {
        this.close();
      }, 3000)
        this.add('Referral code created', 'success')
    },
    (err) => {
      const errors = err?.error?.errors
      if(errors && errors.length > 0 && errors?.[0]['reason'] === "invalid" && errors?.[0]['type'] === "ValidationError") {
        this.createCodeError = true;
        this.add('Referral Code Not Created!', 'danger');
      } else {
        this.add('Business partner wholscript does not exist', 'danger');
      }
      this.cd.markForCheck();
    }
  );
  }
  add(err: any, type: any): void {
    this.alerts.push({
      type: type,
      msg: err,
      timeout: 2000
    });
  }
 
  onClosed(dismissedAlert: any): void {
    this.alerts = this.alerts.filter((alert) => alert !== dismissedAlert);
  }

  allowAlphanumeric(event: KeyboardEvent) {
    const pattern = /^[a-zA-Z0-9]*$/;
    const inputChar = event.key;
  
    if (!pattern.test(inputChar)) {
      event.preventDefault();
    }
  }

  onReferralCodeInput(event: Event, inputName? :any) {
    const input = event.target as HTMLInputElement;
    const clean = input.value.replace(/[^a-zA-Z0-9]/g, '');
    input.value = clean;
    
    if(inputName == 'referracode') {
      this.referralCodeValue = input.value;
    } else if (inputName == 'lastName') {
      this.lastNameValue = input.value;
    }
  }
  
  isAlphanumericOnly(value: string): boolean {
    if (!value) return false; 
    const pattern = /^[a-zA-Z0-9]+$/;
    return pattern.test(value);
  }

  allowAlphanumericOnDrop(event: DragEvent, value? :any) {
    event.preventDefault();
  }

  handlePaste(event: ClipboardEvent) {
    const pastedInput: string = event?.clipboardData?.getData('text') || '';
    const pattern = /^[a-zA-Z0-9]*$/;
  
    if (!pattern.test(pastedInput)) {
      event.preventDefault();
    }
  }
}
