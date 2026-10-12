import { ChangeDetectorRef, Component } from '@angular/core';
import { BsModalRef } from 'ngx-bootstrap/modal';

@Component({
  selector: 'ws-checkout-delivery-address-verify-popup-one',
  templateUrl: './ws-checkout-delivery-address-verify-popup-one.component.html',
  styleUrl: './ws-checkout-delivery-address-verify-popup-one.component.scss'
})
export class WsCheckoutDeliveryAddressVerificationPopupOneComponent {
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
