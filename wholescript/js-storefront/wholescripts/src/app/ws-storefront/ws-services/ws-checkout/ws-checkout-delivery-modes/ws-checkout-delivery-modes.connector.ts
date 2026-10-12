/*
 * SPDX-FileCopyrightText: 2024 SAP Spartacus team <spartacus-team@sap.com>
 *
 * SPDX-License-Identifier: Apache-2.0
 */

import { Injectable } from '@angular/core';
import { DeliveryMode } from '@spartacus/cart/base/root';
import { Observable } from 'rxjs';
import { WsOccCheckoutDeliveryModesAdapter } from './ws-checkout-delivery-mode.adapter';

@Injectable({ providedIn: 'root' })
export class WsCheckoutDeliveryModesConnector {
  constructor(protected adapter: WsOccCheckoutDeliveryModesAdapter) {}

  public setMode(
    userId: string,
    cartId: string,
    deliveryModeId: string
  ): Observable<unknown> {
    return this.adapter.setMode(userId, cartId, deliveryModeId);
  }

  public getSupportedModes(
    userId: string,
    cartId: string
  ): Observable<DeliveryMode[]> {
    return this.adapter.getSupportedModes(userId, cartId);
  }

  public clearCheckoutDeliveryMode(
    userId: string,
    cartId: string
  ): Observable<unknown> {
    return this.adapter.clearCheckoutDeliveryMode(userId, cartId);
  }
}
