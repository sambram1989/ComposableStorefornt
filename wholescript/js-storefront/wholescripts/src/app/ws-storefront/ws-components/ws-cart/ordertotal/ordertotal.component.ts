import { Component, inject, signal } from '@angular/core';
import { ActiveCartFacade, Cart } from '@spartacus/cart/base/root';
import { RoutingService, AuthService } from '@spartacus/core';
import { Observable, tap, map } from 'rxjs';
import { BASE_PATH } from '../../../ws-constants/ws-api-endpoint.constant';
import { userMap } from '../../../ws-env/environment';

@Component({
  selector: 'ws-ordertotal',
  templateUrl: './ordertotal.component.html',
  styleUrl: './ordertotal.component.scss',
  host: {
    '[class.margin-right-anonymousUser]': '!isUserLoggedIn()'
  }
})
export class OrdertotalComponent {
  isFriendsFamily: boolean = false;

  constructor() {
    const user = JSON.parse(sessionStorage.getItem('user_config'));
    if (user) {
      const key = Object.keys(userMap).find(key => userMap[key] === 'friendsfamily');
      this.isFriendsFamily = (user?.roles || []).includes(key);
    }
  }
  
  images = [
    { name: 'American Express', src: BASE_PATH('amex') },
    { name: 'Visa', src: BASE_PATH('visa') },
    { name: 'Mastercard', src: BASE_PATH('mastercard') },
    { name: 'Discover', src: BASE_PATH('discover') },
  ];

  private activeCartFacade = inject(ActiveCartFacade);
  private authService = inject(AuthService);
  private routingService = inject(RoutingService);

  isUserLoggedIn = signal<boolean>(false);

  isUserLoggedIn$:Observable<any>= this.authService.isUserLoggedIn().pipe(tap((res:boolean)=> this.isUserLoggedIn.set(res) ));
  isCartPage$: Observable<boolean> = this.routingService.getRouterState().pipe(
    map((state) => state.state.context.id === '/cart')
  );
  
  get cart$(): Observable<Cart> {
    return this.activeCartFacade.getActive();
  }

  getCardImage(paymentInfo: any): any | undefined {
    const matchedCard = this.images.find(img => img.name === paymentInfo?.cardType?.name);
    return matchedCard ? matchedCard.src : BASE_PATH('default-card'); 
  }

}
