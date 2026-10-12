import { Component, inject } from '@angular/core';
import { ActiveCartFacade } from '@spartacus/cart/base/root';

@Component({
  selector: 'ws-cart-shipping-address',
  templateUrl: './cart-shipping-address.component.html',
  styleUrl: './cart-shipping-address.component.scss'
})
export class CartShippingAddressComponent {
  private activeCartService = inject(ActiveCartFacade);

  cart$ = this.activeCartService.getActive();
}
