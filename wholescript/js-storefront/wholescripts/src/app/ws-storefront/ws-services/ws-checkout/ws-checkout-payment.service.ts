import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { EventEmitter, inject, Injectable } from '@angular/core';
import { BehaviorSubject, Observable, switchMap } from 'rxjs';
import {
  ADD_CARD_IN_CHECKOUT_FLOW,
  LOCAL_CART,
  PAYMETRICS_GET_ACCESS_TOKEN,
  UPDATE_PAYMENT_DETAILS,
} from '../../ws-constants/ws-api-endpoint.constant';
import { OccEndpointsService } from '@spartacus/core';
import { ActiveCartFacade } from '@spartacus/cart/base/root';
import { Location } from '@angular/common';
import { selectedEnv } from '../../ws-env/environment';

@Injectable({
  providedIn: 'root',
})
export class WsCheckoutPaymentService {
  private activeCartService = inject(ActiveCartFacade);
  private location = inject(Location);
  paymetricsDetail$: BehaviorSubject<any> = new BehaviorSubject(null);
  addNewCardSucceeded = new EventEmitter();
  constructor(
    private http: HttpClient,
    private occEndpointsService: OccEndpointsService
  ) { }

  getPaymentAccessToken() {
    const url = this.occEndpointsService.buildUrl(
      'authorizedToken',
      {},
      { baseUrl: true, prefix: false, baseSite: false }
    );
    return this.http.post(url, {});
  }

  getIFrameUrl(id: string) {
    const url = this.occEndpointsService.buildUrl(
      'accessTokenUrl',
      { urlParams: { hostUrl: window.location.origin } },
      { baseUrl: true, prefix: false, baseSite: false }
    );
    return this.http.get(url, { headers: { Authorization: `Bearer ${id}` } });
  }

  getAccessToken() {
    const params = new HttpParams().set('store', selectedEnv.WHOLESCRIPTS_STORE);
    return this.http
      .get(PAYMETRICS_GET_ACCESS_TOKEN, { params })
      .subscribe((response: any) => {
        this.paymetricsDetail$.next(response);
    });
  }

  loadPaymetricsDetail$(): Observable<any> {
    return this.paymetricsDetail$.asObservable();
  }

  addCardDetailsForBilling(payload: any, accessToken: string, fromMyAcc = false) {
    let formattedRegion = payload?.region?.isocode;
    if(formattedRegion) {
      formattedRegion = formattedRegion?.replace('US-', '');
    }
    let queryParams: any = {
      fields: 'DEFAULT',
      accessToken: accessToken,
      billTo_city: payload?.town,
      billTo_country: payload?.country?.isocode,
      billTo_email: payload?.email ? payload?.email : '',
      billTo_firstName: payload?.firstName,
      billTo_lastName: payload?.lastName,
      billTo_phoneNumber: payload?.phoneNumber || payload?.phone || '',
      billTo_postalCode: payload?.postalCode,
      billTo_state: formattedRegion,
      billTo_street1: payload?.line1,
      billTo_street2: payload?.line2 || '',
      billTo_region: formattedRegion,
      addressNumber: payload?.addressNumber,
      savePaymentInfo: fromMyAcc
    };
    if (!this.location?.path()?.includes('accounts-settings/Payment')) {
      queryParams.cartId = this.getCartIdFromLocalStorage();
      queryParams.checkoutPage = true;
    }
    if (fromMyAcc) {
      queryParams.savePaymentInfo = fromMyAcc;
    }
    let params = new HttpParams();

    Object.keys(queryParams).forEach((key) => {
      if (queryParams.hasOwnProperty(key)) {
        params = params.append(key, queryParams[key]);
      }
    });

    return this.http.post(this.occEndpointsService.buildUrl(ADD_CARD_IN_CHECKOUT_FLOW), {}, { params });
  }

  getCartIdFromLocalStorage(): string {
    const localCart = localStorage.getItem(LOCAL_CART);
    console.log(localCart);
    if (localCart) {
      const cartObject = JSON.parse(localCart);
      return cartObject?.active || '';
    }
    return '';
  }

  addNewCardSucceededClicked() {
    this.addNewCardSucceeded.emit();
  }

  updateNewAddress(paymentDetailsId: string) {
    return this.activeCartService.getActiveCartId().pipe(switchMap(
      (cartId: any) => {
        return this.http.put(this.occEndpointsService.buildUrl(UPDATE_PAYMENT_DETAILS(cartId, paymentDetailsId)), null)
      }
    ))
  }

} 
