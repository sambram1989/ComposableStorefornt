import { HttpClient, HttpHeaders } from '@angular/common/http';
import { DestroyRef, Injectable, inject } from '@angular/core';
import {
  LoggerService,
  OccEndpointsService,
  ConverterService,
  OCC_USER_ID_ANONYMOUS,
  InterceptorUtil,
  USE_CLIENT_TOKEN,
  Occ,
  normalizeHttpError,
  backOff,
  isJaloError,
} from '@spartacus/core';
import { OccOrderAdapter } from '@spartacus/order/occ';
import { Order, ORDER_NORMALIZER } from '@spartacus/order/root';
import { Observable, catchError, take } from 'rxjs';
import { WsAccertifyService } from './ws-accertify.service';
import { WsPurchaseOrderService } from './ws-checkout-po.service';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { WsACHPaymentService } from './ws-ach-payment.service';

@Injectable({
  providedIn: 'root',
})
export class WsOccOrderAdapter extends OccOrderAdapter {
  protected override logger = inject(LoggerService);
  purchaseOrderNumber: string;
  constructor(
    protected override http: HttpClient,
    protected override occEndpoints: OccEndpointsService,
    protected override converter: ConverterService,
    private wsAccertifyService: WsAccertifyService,
    private wsPurchaseOrderService: WsPurchaseOrderService,
    private destroyRef: DestroyRef,
    private achPaymentService: WsACHPaymentService
  ) {
    super(http, occEndpoints, converter);
  }

  public override placeOrder(
    userId: string,
    cartId: string,
    termsChecked: boolean
  ): Observable<Order> {
    let headers = new HttpHeaders();

    if (userId === OCC_USER_ID_ANONYMOUS) {
      headers = InterceptorUtil.createHeader(USE_CLIENT_TOKEN, true, headers);
    }

    const collectorData = this.wsAccertifyService.getCollectorJsData();
    const achPaymentData = this.achPaymentService.achResponseForPlaceOrder;
    const placeOrderEndpoint =  this.getPlaceOrderEndpoint(userId, cartId, termsChecked.toString());
    this.wsPurchaseOrderService.setPurchaseOrderNumber('');
    return this.http
      .post<Occ.Order>(
        placeOrderEndpoint,
        {...collectorData, ...achPaymentData},
        { headers }
      )
      .pipe(
        catchError((error) => {
          throw normalizeHttpError(error, this.logger);
        }),
        backOff({
          shouldRetry: isJaloError,
        }),
        this.converter.pipeable(ORDER_NORMALIZER)
      );
  }

  protected override getPlaceOrderEndpoint(
    userId: string,
    cartId: string,
    termsChecked: string
  ): string {
    let endpoint = '';
    this.wsPurchaseOrderService.purchaseOrderNumber$
    .pipe(take(1))
    .subscribe({
      next: (purchaseOrderNumber: string) => {
        this.purchaseOrderNumber = purchaseOrderNumber;
        console.log('Purchase order number:', purchaseOrderNumber);
        const queryParams: any = { cartId, termsChecked };
        if (this.purchaseOrderNumber) {
          queryParams.poNum = this.purchaseOrderNumber;
        }
        endpoint = this.occEndpoints.buildUrl('placeOrder', {
          urlParams: { userId },
          queryParams: queryParams,
        });
      },
    });
    return endpoint;
  }
}
