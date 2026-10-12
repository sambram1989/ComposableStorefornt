import { Component } from '@angular/core';
import { CartItemComponent } from '@spartacus/cart/base/components';
import { ICON_TYPE } from "@spartacus/storefront";
@Component({
  selector: 'ws-cart-item',
  templateUrl: './cart-item.component.html',
})
export class WsCartItemComponent extends CartItemComponent  {
closeIcon = ICON_TYPE.CLOSE;
}
