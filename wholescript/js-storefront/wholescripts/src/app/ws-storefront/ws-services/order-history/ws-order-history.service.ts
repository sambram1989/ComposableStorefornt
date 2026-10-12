import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { OccEndpointsService, UserIdService } from '@spartacus/core';
import { Observable, of, switchMap, tap } from 'rxjs';
import { CANCEL_ORDER } from '../../ws-constants/ws-api-endpoint.constant';
import { ActiveCartFacade, MultiCartFacade } from '@spartacus/cart/base/root';

@Injectable({
  providedIn: 'root'
})
export class WsOrderHistoryService {

  private readonly originMap: Record<string, string> = {
    API: 'API',
    ABH: 'Better Health',
    WBH: 'Better Health',
    WAH: 'Athena Health',
    WCB: 'Cerbo',
    WCH: 'Charm',
    WOD: 'Optimal DX',
    WOM: 'Optimantra',
    WPB: 'Practice Better',
    AGH: 'Get Healthy',
    ASH: 'Shopify',
    AWP: 'Well ProZ',
    AWS: 'Shopify',
  };

  private user = inject(UserIdService);
  private activeCartFacade = inject(ActiveCartFacade);
  private multiCartService = inject(MultiCartFacade);
  activeUserId: string;

  constructor(private http: HttpClient, private occEndPointService: OccEndpointsService) { 
    this.user.getUserId().subscribe((res) => {
      this.activeUserId = res;
    })
  }

  getApiBadge(orderOrigin: string): string {
    return this.originMap.hasOwnProperty(orderOrigin)
      ? this.originMap[orderOrigin]
      : '';
  }

  getOrders(endDate: string, startDate: string): Observable<any> {
    const url = this.occEndPointService.buildUrl(
      `users/${this.activeUserId}/practitioner/order-list?endDate=${endDate}&fields=DEFAULT&startDate=${startDate}`
    );
    return this.http.post(url, {});
  }

  getOrderInvoice(lastDate: string, firstDate: string): Observable<any> {
    return this.http.post(
      this.occEndPointService.buildUrl(
        `users/${this.activeUserId}/getAccuralsInvoices?endDate=${lastDate}&fields=DEFAULT&isPatient=false&startDate=${firstDate}`
      ),
      {}
    );
  }

  getOrderAging(keyDate: any): Observable<any> {
    return this.http.post(
      this.occEndPointService.buildUrl(
        `users/${this.activeUserId}/get-aging?keyDate=${keyDate}`
      ),
      {}
    );
  }

  getEstoreOrders(payload: any): Observable<any> {
    const url = this.occEndPointService.buildUrl(
      `users/${this.activeUserId}/estore/order/history`
    );
    return this.http.post(url, payload);
  }

  getOrderWebError(): Observable<any> {
    return this.http.get(
      this.occEndPointService.buildUrl(
        `users/${this.activeUserId}/orderWebError`
      )
    )
  }

  pdf(invoiceNumber: string) {
    let headers = new HttpHeaders();
    headers = headers.set('Accept', 'application/json');
    const url = this.occEndPointService.buildUrl(
      `users/${this.activeUserId}/getAccuralsPDF?billingDoc=${invoiceNumber}`
    );
    return this.http.post(url, {
      headers: headers,
    });
  }

  getOrderDetails(salesOrder: string): Observable<any> {
    const url = this.occEndPointService.buildUrl(
      `users/${this.activeUserId}/order-details?fields=DEFAULT&orderNumber=${salesOrder}`
    );
    return this.http.post(url, {});
  }

  addToCart(payLoad: any, cartId: any): Observable<any> {
    const url = this.occEndPointService.buildUrl(
      `users/current/carts/${cartId}/orderhistory/addtocart`
    );
    return this.http.post(url, payLoad);
  }

  addProductToCart(payload: any) {
    // building cart ID
        return this.activeCartFacade
              .getActiveCartId()
              .pipe(
                switchMap((cartId: any) => {
                  const cartAction = cartId?.trim()?.length
                    ? of(cartId)
                    : this.multiCartService.createCart({
                        userId: 'current',
                        extraData: { active: true },
                      });
                  return cartAction;
                })
              )
              .pipe(
                switchMap((resolvedCartId: any) =>
                  this.addToCart(payload, resolvedCartId)
                ),tap(()=>{
                  // Add to cart API
                  this.activeCartFacade.reloadActiveCart();
                })
              );
    }

  cancelOrder(orderId:string,orderItem:string){
    const params = orderItem ? new HttpParams().append('orderEntryNumber',orderItem) : new HttpParams();
    return  this.http.delete(this.occEndPointService.buildUrl(CANCEL_ORDER(orderId)),{responseType:'text',params})
  }
}
