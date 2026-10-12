import { inject, Injectable, signal } from '@angular/core';
import { GET_PAYMENTS, REMOVE_CARD } from '../../../ws-constants/ws-api-endpoint.constant';
import { HttpClient, HttpParams } from '@angular/common/http';
import { OccEndpointsService, UserIdService } from '@spartacus/core';
import { Observable, shareReplay, switchMap, Subject, BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class PaymentService {
  private http = inject(HttpClient);
  private occ = inject(OccEndpointsService);
  private user = inject(UserIdService);
  isAgingTab = signal<boolean>(false);
  passwordSet: Subject<boolean> = new Subject<boolean>();
  setPayment: Subject<boolean> = new Subject<boolean>();
  previousRoutePath = new BehaviorSubject<string>('');
  activeUserId: string;

  constructor() {
    this.user.getUserId().subscribe((res) => {
      this.activeUserId = res;
    })
  }

  getPayments() {
    return this.http.get(`${this.occ.buildUrl(GET_PAYMENTS(false))}`);
  }

  removeCard(cardId:any,cardNumber:string, payerId: string){
    let params = new HttpParams().append('cardNumber',cardNumber ).append('payerId',payerId );
     return this.http.delete(this.occ.buildUrl(REMOVE_CARD(cardId)),{params, responseType:'text'}).pipe(switchMap(()=>{
       return this.http.get(`${this.occ.buildUrl(GET_PAYMENTS(false))}`);
     }));
   }

  signedAgreementStatus(payload: any) {
    return this.http.post(this.occ.buildUrl(`users/${this.activeUserId}/sendAmendment`), payload )
  }
  getAmendment() {
    return this.http.get(this.occ.buildUrl(`users/${this.activeUserId}/amendment?type=A_APITerms`))
  }
  getExternalClientStores() {
    return this.http.get(this.occ.buildUrl(`users/${this.activeUserId}/externalDetails`))
  }
  getPendingOrders() {
    return this.http.get(this.occ.buildUrl(`users/${this.activeUserId}/estore/pendingOrders`))
  }
  addNewCredential(payload: any) {
    return this.http.post(this.occ.buildUrl(`users/${this.activeUserId}/estore/addCredential`), payload)
  }
  deleteCredential(payload: any) {
    return this.http.post(this.occ.buildUrl(`users/${this.activeUserId}/estore/deleteCredential`), payload)
  }
  changeCredential(payload: any) {
    return this.http.post(this.occ.buildUrl(`users/${this.activeUserId}/estore/changeCredential`), payload)
  }
  sendEmailConfirmation(payload: any) {
    return this.http.post(this.occ.buildUrl(`users/${this.activeUserId}/estore/shippingConfirmation`), payload)
  }
  getActivePaymentSettings() {
    return this.http.get(this.occ.buildUrl(`users/${this.activeUserId}/estore/card`))
  }
  updateActivePaymentSettings(card: any, month: any, year: any) {
    let formattedRegion = card?.billingAddress?.region?.isocode;
    if(formattedRegion) {
      formattedRegion = formattedRegion?.replace('US-', '');
    }

    const request =
    {
      "billTo_firstName" : card?.billingAddress?.firstName,
      "billTo_lastName": card?.billingAddress?.lastName,
      "billTo_street1": card?.billingAddress?.line1,
      "billTo_street2": card?.billingAddress?.line2 || '',
      "billTo_city": card?.billingAddress?.town,
      "billTo_postalCode": card?.billingAddress?.postalCode,
      "billTo_phoneNumber": card?.billingAddress?.phone || '',
      "addressNumber": card?.billingAddress?.cellphone || '',
      "billTo_country": card?.billingAddress?.country?.isocode,
      "billTo_region": formattedRegion,
      "card_accountNumber": card.cardNumber,
      "card_issueNumber": card.cardId,
      "card_cardType": card.cardType,
      "card_nameOnCard": card.creditCardName,
      "card_expirationMonth": month,
      "card_expirationYear": year
  }
  return this.http.post(this.occ.buildUrl(`users/${this.activeUserId}/estore/card`), request)
  }
  imgUrl(url: any) {
    const baseUrl = this.occ.getBaseUrl().split(`/occ`)[0];
    return baseUrl + url;
  }
  getPrivateLabelWebkits() {
    return this.http.get(this.occ.buildUrl(`users/${this.activeUserId}/getPrivateLabelWebKit`))
  }
  getPrivateLabelCartons() {
    return this.http.get(this.occ.buildUrl(`users/${this.activeUserId}/getPrivateLabelCartons`))
  }
}
