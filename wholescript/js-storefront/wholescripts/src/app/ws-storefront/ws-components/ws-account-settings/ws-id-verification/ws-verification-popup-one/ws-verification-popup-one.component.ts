import { ChangeDetectorRef, Component } from '@angular/core';
import { BsModalRef } from 'ngx-bootstrap/modal';

@Component({
  selector: 'ws-verification-popup-one',
  templateUrl: './ws-verification-popup-one.component.html',
  styleUrl: './ws-verification-popup-one.component.scss'
})
export class WsVerificationPopupOneComponent {
  pageState = {
    stepSelected: 1,
    status: true
  }
  constructor(public bsModalRef: BsModalRef, private cdRef: ChangeDetectorRef) {}
  close() {
    this.bsModalRef.hide();
  }
  goToStep(step: number) {
    this.pageState.stepSelected = step;
    this.pageState.status = true;
    this.cdRef.detectChanges();
  }
}
