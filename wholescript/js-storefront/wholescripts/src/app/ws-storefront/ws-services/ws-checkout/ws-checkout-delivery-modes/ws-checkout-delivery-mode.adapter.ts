import { HttpClient } from '@angular/common/http';
import { Injectable, inject, signal } from '@angular/core';
import { DeliveryMode } from '@spartacus/cart/base/root';
import {
  CheckoutDeliveryModesAdapter,
  DELIVERY_MODE_NORMALIZER,
} from '@spartacus/checkout/base/core';
import {
  LoggerService,
  OccEndpointsService,
  ConverterService,
  normalizeHttpError,
  backOff,
  isJaloError,
  Occ,
} from '@spartacus/core';
import { Observable, catchError, map } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class WsOccCheckoutDeliveryModesAdapter
  implements CheckoutDeliveryModesAdapter
{
  deliveryCost = signal<any>({});
  protected logger = inject(LoggerService);

  constructor(
    protected http: HttpClient,
    protected occEndpoints: OccEndpointsService,
    protected converter: ConverterService
  ) {}

  public setMode(
    userId: string,
    cartId: string,
    deliveryModeId: string
  ): Observable<unknown> {
    return this.http
      .put(this.getSetDeliveryModeEndpoint(userId, cartId, deliveryModeId), {})
      .pipe(
        catchError((error) => {
          throw normalizeHttpError(error, this.logger);
        }),
        backOff({
          shouldRetry: isJaloError,
        })
      );
  }

  protected getSetDeliveryModeEndpoint(
    userId: string,
    cartId: string,
    deliveryModeId?: string
  ): string {
    return this.occEndpoints.buildUrl('setDeliveryMode', {
      urlParams: {
        userId,
        cartId,
      },
      queryParams: { deliveryModeId, deliveryCost: this.deliveryCost() },
    });
  }

  public getSupportedModes(
    userId: string,
    cartId: string
  ): Observable<DeliveryMode[]> {
    return this.http
      .get<Occ.DeliveryModeList>(this.getDeliveryModesEndpoint(userId, cartId))
      .pipe(
        catchError((error) => {
          throw normalizeHttpError(error, this.logger);
        }),
        backOff({
          shouldRetry: isJaloError,
        }),
        map((listResponse) => listResponse.deliveryModes ?? []),
        this.converter.pipeableMany(DELIVERY_MODE_NORMALIZER)
      );
  }

  protected getDeliveryModesEndpoint(userId: string, cartId: string): string {
    return this.occEndpoints.buildUrl('deliveryModes', {
      urlParams: { userId, cartId },
    });
  }

  clearCheckoutDeliveryMode(
    userId: string,
    cartId: string
  ): Observable<unknown> {
    return this.http
      .delete<unknown>(this.getClearDeliveryModeEndpoint(userId, cartId))
      .pipe(
        catchError((error) => {
          throw normalizeHttpError(error, this.logger);
        }),
        backOff({
          shouldRetry: isJaloError,
        })
      );
  }

  protected getClearDeliveryModeEndpoint(
    userId: string,
    cartId: string
  ): string {
    return this.occEndpoints.buildUrl('clearDeliveryMode', {
      urlParams: { userId, cartId },
    });
  }
}
