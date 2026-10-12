import { Component, inject } from '@angular/core';
import { CartDetailsComponent } from '@spartacus/cart/base/components';
import { CartConfigService } from '@spartacus/cart/base/core';
import { ActiveCartFacade, SelectiveCartFacade } from '@spartacus/cart/base/root';
import { AuthService, RoutingService } from '@spartacus/core';
import { combineLatest, filter, map, of, tap } from 'rxjs';
import { WsCartResponseModifier } from '../../../ws-services/ws-checkout/ws-cart-response-modifier.service';

@Component({
  selector: 'ws-cart-details',
  templateUrl: './cart-card-details.component.html',
})
export class CartCardDetailsComponent extends CartDetailsComponent {
  private responseModifier = inject(WsCartResponseModifier);
  
  constructor(activeCartService: ActiveCartFacade,
    selectiveCartService: SelectiveCartFacade,
    authService: AuthService,
    routingService: RoutingService,
    cartConfig: CartConfigService) {
    super(activeCartService, selectiveCartService, authService, routingService, cartConfig);
  }

  override ngOnInit() {
    this.cart$ = this.activeCartService.getActive().pipe(
      map((cart: any) => this.responseModifier.mapCartEntries(cart))
    );

    this.entries$ = this.activeCartService
      .getEntries()
      .pipe(filter((entries) => entries.length > 0));

    this.selectiveCartEnabled = this.cartConfig.isSelectiveCartEnabled();

    this.cartLoaded$ = combineLatest([
      this.activeCartService.isStable(),
      this.selectiveCartEnabled
        ? this.selectiveCartService.isStable()
        : of(false),
      this.authService.isUserLoggedIn(),
    ]).pipe(
      tap(([, , loggedIn]) => (this.loggedIn = loggedIn)),
      map(([cartLoaded, sflLoaded, loggedIn]) =>
        loggedIn && this.selectiveCartEnabled
          ? cartLoaded && sflLoaded
          : cartLoaded
      )
    );
  }

}
