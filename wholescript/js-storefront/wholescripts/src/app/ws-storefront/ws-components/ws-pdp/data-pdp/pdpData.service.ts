import { HttpClient } from '@angular/common/http';
import { DestroyRef, inject, Injectable, signal } from '@angular/core';
import { OccEndpointsService } from '@spartacus/core';
import { PDP_ADD_TO_CART } from '../../../ws-constants/ws-api-endpoint.constant';
import { WsCurrentUserService } from '../../../ws-services/state-services/currentuser.service';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { MultiCartFacade } from '@spartacus/cart/base/root';

@Injectable({ providedIn: 'root' })
export class PdpDataservice {
  private http = inject(HttpClient);
  private occ = inject(OccEndpointsService);
  private currentUser = inject(WsCurrentUserService);
  private multiCartService = inject(MultiCartFacade);
  private destroyRef = inject(DestroyRef);
  userId: string = '';

  images = [
    { key: 'Dairy', value: '../../../../../assets/icons/pdp_icons/dairy.png' },
    { key: 'Egg', value: '../../../../../assets/icons/pdp_icons/egg.png' },
    {
      key: 'Gluten',
      value: '../../../../../assets/icons/pdp_icons/gluten.png',
    },
    {
      key: 'Shellfish',
      value: '../../../../../assets/icons/pdp_icons/shellfish.png',
    },
    { key: 'Soy', value: '../../../../../assets/icons/pdp_icons/soy.png' },
    {
      key: 'Tree Nuts',
      value: '../../../../../assets/icons/pdp_icons/treenuts.png',
    },
    { key: 'Wheat', value: '../../../../../assets/icons/pdp_icons/wheat.png' },
    {
      key: 'Peanuts',
      value: '../../../../../assets/icons/pdp_icons/peanuts.png',
    },
    { key: 'Fish', value: '../../../../../assets/icons/pdp_icons/fish.png' },
    { key: 'Yeast', value: '../../../../../assets/icons/pdp_icons/yeast.png' },
    { key: 'GMO', value: '../../../../../assets/icons/pdp_icons/gmo.png' },
  ];

  restrictedImages = signal<any>(this.images);

  autoShipFrequency = signal<string>('One-Time');
  constructor() {
    this.currentUser
      .getCurrentUserData()
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe((data: any) => {
        this.userId = data ? 'current' : 'anonymous';
      });
  }

  addToCart(cartId: string, code: number, frequency: string, quantity: any) {
    let pdpUrl = PDP_ADD_TO_CART(cartId);
    if (this.userId === 'anonymous') {
      pdpUrl = pdpUrl.replace('orgUsers/current', 'orgUsers/anonymous');
    }
    return this.http.post(
      this.occ.buildUrl(pdpUrl, {
        queryParams: {
          autoShipCode: frequency,
          quantity,
          code,
        },
      }),
      {}
    );
  }

  createCart() {
    return this.multiCartService.createCart({ userId: this.userId, extraData: { active: true } });
  }
}
