/*
 * SPDX-FileCopyrightText: 2024 SAP Spartacus team <spartacus-team@sap.com>
 *
 * SPDX-License-Identifier: Apache-2.0
 */

import { Injectable } from '@angular/core';
import { CartPageLayoutHandler } from '@spartacus/cart/base/components';
import { CartConfigService, isEmpty } from '@spartacus/cart/base/core';
import {
  ActiveCartFacade,
  Cart,
  SelectiveCartFacade,
} from '@spartacus/cart/base/root';
import { combineLatest, Observable, of } from 'rxjs';
import { filter, map, startWith, switchMap, take } from 'rxjs/operators';

@Injectable({
  providedIn: 'root',
})
export class CustomCartPageLayoutHandler extends CartPageLayoutHandler {
  constructor(
    protected override activeCartService: ActiveCartFacade,
    protected override selectiveCartService: SelectiveCartFacade,
    protected override cartConfig: CartConfigService
  ) {
    super(activeCartService, selectiveCartService, cartConfig);
  }

  override handle(
    slots$: Observable<string[]>,
    pageTemplate?: string,
    section?: string
  ) {
    if (pageTemplate === 'CartPageTemplate' && !section) {
      const loading$ = this.activeCartService.getLoading();

      // Wait until loading is false before combining
      return loading$.pipe(
        // Only continue when loading is false (cart is stable)
        filter((loading) => !loading),
        take(1), // Proceed once cart is stable
        switchMap(() =>
          combineLatest([
            slots$,
            this.activeCartService.getActive(),
            this.getSelectiveCart(),
            this.activeCartService.getLoading()
          ]).pipe(
            map(([slots, cart, selectiveCart, loadingCart]) => {
              const exclude = (arr: string[], args: string[]) =>
                arr.filter((item) => args.every((arg) => arg !== item));
              return isEmpty(cart) && loadingCart
                ? exclude(slots, [
                  'TopContent',
                  'CenterRightContentSlot',
                  'EmptyCartMiddleContent',
                ])
                : cart.totalItems
                  ? exclude(slots, ['EmptyCartMiddleContent'])
                  : selectiveCart?.totalItems
                    ? exclude(slots, [
                      'EmptyCartMiddleContent',
                      'CenterRightContentSlot',
                    ])
                    : exclude(slots, ['TopContent', 'CenterRightContentSlot']);
            })
          )
        )
      );

    }
    return slots$;
  }


  protected override getSelectiveCart(): Observable<Cart | null> {
    return this.cartConfig.isSelectiveCartEnabled()
      ? this.selectiveCartService.getCart().pipe(startWith(null))
      : of({} as Cart);
  }
}
