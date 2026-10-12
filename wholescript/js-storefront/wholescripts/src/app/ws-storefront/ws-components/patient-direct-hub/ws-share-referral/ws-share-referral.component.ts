import { Component, Input, ChangeDetectorRef} from '@angular/core';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { PatientDirectService } from '../../../ws-services/ws-patient-direct.service';

@Component({
  selector: 'ws-share-referral',
  templateUrl: './ws-share-referral.component.html',
  styleUrls: ['./ws-share-referral.component.scss']
})
export class WsShareReferralCodeComponent {
  @Input() referralCode: any;
  baseLink = window.location.origin;
  toastMessage :string = '';

  constructor(public bsModalRef: BsModalRef, private referralsService: PatientDirectService , private cdr: ChangeDetectorRef,) {}

  closeModal() {
    this.bsModalRef.hide();
  }
  copyLink() {
    const link = this.baseLink + '/register/' + this.referralCode.referralLink;
    this.copyToClipboard(link,"Referral link copied");
  }

  onKeyDown(event: KeyboardEvent) {
    if (event.key === 'Enter' || event.key === ' ') {
      event.preventDefault();
      this.closeModal()
    }
  }

  copyCode() {
    this.copyToClipboard(this.referralCode.referralCode,"Referral code copied");
  }

  copyLastName() {
    this.copyToClipboard(this.referralCode.lastName,"Lastname copied");
  }

  copyToClipboard(text: any , textToInformCopied?: any) {
    navigator.clipboard.writeText(text).then(() => {
      console.log('Text copied to clipboard successfully!');
      this.showToasterMsg(textToInformCopied);
    }).catch(err => {
    });
  }

  showToasterMsg(msg: string) {
    this.toastMessage = msg;
    this.cdr.detectChanges();
    this.resetToastMessage();
  }

  resetToastMessage() {
    setTimeout(() => {
      this.toastMessage = '';
      this.cdr.detectChanges();
    }, 2000)
  }

}

