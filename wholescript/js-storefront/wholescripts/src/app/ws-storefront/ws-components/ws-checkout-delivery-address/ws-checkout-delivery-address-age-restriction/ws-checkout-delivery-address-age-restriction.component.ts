import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  selector: 'ws-checkout-delivery-address-age-restriction',
  templateUrl: './ws-checkout-delivery-address-age-restriction.component.html',
  styleUrl: './ws-checkout-delivery-address-age-restriction.component.scss'
})
export class WsCheckoutDeliveryAddressAgeRestrictionComponent {
  @Input() products: any[] =[];
  @Output() removeItems = new EventEmitter();
  @Output() showDeliveryAddress = new EventEmitter();
  @Output() verificationSuccess = new EventEmitter();


  changeAddress() {
    this.showDeliveryAddress.emit();
  }

  removeItemClick() {
    this.removeItems.emit();
  }

  onVerificationSuccess() {
    this.verificationSuccess.emit(); 
  }

}
