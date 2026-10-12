import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  selector: 'ws-checkout-delivery-address-remove-item-modals',
  templateUrl: './ws-checkout-delivery-address-remove-item-modals.component.html',
  styleUrl: './ws-checkout-delivery-address-remove-item-modals.component.scss'
})
export class WsCheckoutDeliveryAddressRemoveItemModalsComponent {
  @Output() hideRemoveItemsModal = new EventEmitter();
  @Output() removeItems = new EventEmitter();
  @Input() type: 'region' | 'po' | 'restricted';
  @Input() products: any[] = [];

  closeModal() {
    this.hideRemoveItemsModal.emit();
  }

  removeItemClick() {
    this.removeItems.emit();
  }
}
