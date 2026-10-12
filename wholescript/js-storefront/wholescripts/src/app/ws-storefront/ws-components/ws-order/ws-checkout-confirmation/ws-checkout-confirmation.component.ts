import { Component, inject, OnDestroy, OnInit } from '@angular/core';
import { CurrencyService } from '@spartacus/core';
import { map, Observable } from 'rxjs';
import { WsCartResponseModifier } from '../../../ws-services/ws-checkout/ws-cart-response-modifier.service';
import { Order, OrderFacade } from '@spartacus/order/root';
import { WsOrderService } from '../../../ws-services/ws-checkout/ws-order.service';
import { toObservable } from '@angular/core/rxjs-interop';
import { userMap } from '../../../ws-env/environment';

@Component({
  selector: 'ws-checkout-confirmation',
  templateUrl: './ws-checkout-confirmation.component.html',
  styleUrls: [
    './ws-checkout-confirmation.component.scss',
    '../../ws-order-details/ws-order-details/ws-order-details.component.scss',
  ]
})
export class WsCheckoutConfirmationComponent implements OnDestroy {
  private currencyService = inject(CurrencyService);
  private responseModifier = inject(WsCartResponseModifier);
  cardNumber: String;
  isFriendsFamily: boolean = false;
  currency$ = this.currencyService.getActive();
   order$: Observable<any> = this.responseModifier.placedOrder$.asObservable()
    .pipe(map((cart: any) => {
      return this.responseModifier.mapCartEntries(cart);
    }));

    constructor(protected orderFacade: WsOrderService)
    {
      this.order$.subscribe((cart) => {
        this.cardNumber = cart.paymentInfo?.cardNumber?.replace(/\*/g, ''); 
      });
      const user = JSON.parse(sessionStorage.getItem('user_config'));
      if (user) {
        const key = Object.keys(userMap).find(key => userMap[key] === 'friendsfamily');
        this.isFriendsFamily = (user?.roles || []).includes(key);
      }
    }

    showPO() {
      const user = JSON.parse(sessionStorage.getItem('user_config'));
    
      // If user exists, check if they have the "patient" role
      return user ? !(user?.roles || []).includes('patientcustomergroup') : true;
    }
    
    ngOnDestroy() {
      this.orderFacade.clearPlacedOrder();
    }
}
