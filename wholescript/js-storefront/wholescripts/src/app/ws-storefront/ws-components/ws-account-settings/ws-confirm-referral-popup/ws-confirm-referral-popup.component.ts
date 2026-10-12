import { Component, EventEmitter, Output } from '@angular/core';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { PatientDirectService } from '../../../ws-services/ws-patient-direct.service';
import { Router } from '@angular/router';

@Component({
  selector: 'ws-confirm-referral-popup',
  templateUrl: './ws-confirm-referral-popup.component.html',
  styleUrls: ['./ws-confirm-referral-popup.component.scss']
})
export class WsConfirmReferralPopupComponent {
  @Output() updateChanges = new EventEmitter<boolean>();

  constructor(private bsModalRef: BsModalRef, private referralService: PatientDirectService,
    private router: Router
  ) { }

  cancel() {
    this.bsModalRef.hide();
  }
  saveChanges() {
    this.updateChanges.emit(true);
    this.referralService.CODE_EDIT = false;
    window.scroll({
      top: 0,
      left: 0,
      behavior: 'smooth'
    });
    this.cancel();
  }

  navigateToSearchPatientAdvanced() {
    this.cancel();
    this.router.navigate(['/SearchPatientAdvanced']);
  }
}
