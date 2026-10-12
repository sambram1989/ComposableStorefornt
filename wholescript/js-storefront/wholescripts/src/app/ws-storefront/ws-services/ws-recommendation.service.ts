import { inject, Injectable } from '@angular/core';
import { map, Observable, of, switchMap, tap } from 'rxjs';
import { HttpClient, HttpParams } from '@angular/common/http';
import { OccEndpointsService, UserIdService } from '@spartacus/core';
import { ApiCall } from '../ws-components/ws-practitioner/ws-My-patients/my-patient-landing/practitioner-my-patient-listing.service';
import {
  ADD_MEDPAX_TO_CART,
  EDIT_PROTOCOL_LIST_API,
  GET_MEDPAX_FOR_OWNER,
  SHARE_RECOMMENDATION_FROM_REVIEW_PAGE,
  UPDATING_A_RECOMMENDATION_WITH_ENTRY_NO,
  UPDATING_A_RECOMMENDATION_WITHOUT_ENTRY_NO
} from '../ws-constants/ws-api-endpoint.constant';
import { ActiveCartFacade, MultiCartFacade } from '@spartacus/cart/base/root';
import { Router } from '@angular/router';

@Injectable({ providedIn: 'root' })
export class RecommendationService {
  private activeCartService = inject(ActiveCartFacade);
  private multiCartService = inject(MultiCartFacade);
  private router = inject(Router);
  user_id: string = '';
  constructor(
    private httpClient: HttpClient,
    private occEndPointService: OccEndpointsService,
    private userService: UserIdService,
  ) { }

  createRecommendation(payload: any): Observable<any> {
    return this.httpClient.post(
      this.occEndPointService.buildUrl(`users/current/recommendation`),
      payload
    );
  }

  getRecommendation(recommendationId: any): Observable<any> {
    return this.httpClient.get(
      this.occEndPointService.buildUrl(`users/current/recommendation/${recommendationId}`)
    );
  }

  addEntries(recommendation: any): Observable<any> {
    return this.httpClient.post(
      this.occEndPointService.buildUrl(`users/current/recommendation/entries`),
      recommendation
    );
  }

  getUserDetails(): Observable<any> {
    return this.userService.getUserId().pipe(
      switchMap((account: string) =>
        this.httpClient.get(
          this.occEndPointService.buildUrl('orgUsers/' + account)
        )
      )
    )
  }

  fetchWholesaleRecommendations(): Observable<any> {
    return this.httpClient.get(
      this.occEndPointService.buildUrl('users/current/recommendation?fields=FULL')
    ).pipe(map((res: any) => this.modifyRecommendations(res)));
  }
  
 private modifyRecommendations(response: any) {
    if (response?.recommendations && Array.isArray(response.recommendations)) {
      response.recommendations = response.recommendations
        .map((item: any) => item)
        .sort((a: any, b: any) => new Date(b.creationDate).getTime() - new Date(a.creationDate).getTime());
    }
    return response;
  }
  
  addProductsToRecommendation(
    recommendationId: string,
    payload: any
  ): Observable<any> {
    return this.httpClient.post(
      this.occEndPointService.buildUrl(
        `users/current/recommendation/${recommendationId}/entries`
      ),
      payload
    );
  }

  shareRecommendations(recoId: string, payload: any, sms: boolean, email: boolean): Observable<any> {
    let params = new HttpParams()
      .append('isSmsNotificationEnabled', sms)
      .append('isEmailNotificationEnabled', email);

    return this.httpClient.patch(
      this.occEndPointService.buildUrl(SHARE_RECOMMENDATION_FROM_REVIEW_PAGE(recoId)),
      payload,
      { params }
    );
  }

  cloneEntryToProtocol(request: any) {
    return this.httpClient.post(
      this.occEndPointService.buildUrl(
        `users/current/prReference/clone?recommendationId=${request.recommendationId}&name=${request.name}`),
      {}
    );
  }
  cloneEntryToRecommendation(id: string, email: string) {
    return this.httpClient.get(
      this.occEndPointService.buildUrl(
        `users/current/prReference/clone?protocolId=${id}&patientId=${email}`
      )
    );
  }

  removeProductsFromRecommendation(
    recommendationId: string,
    entryNumber: any
  ): Observable<any> {
    return this.httpClient.delete(
      this.occEndPointService.buildUrl(
        `users/current/recommendation/${recommendationId}/entries/${entryNumber}`
      )
    );
  }

  deleteRecommendationId(
    recommendationId: string,
  ): Observable<any> {
    return this.httpClient.delete(
      this.occEndPointService.buildUrl(
        `users/current/recommendation/${recommendationId}/delete`
      )
    );
  }

  cancelRecommendationId(
    recommendationId: string,
    payload: string
  ): Observable<any> {
    return this.httpClient.patch(
      this.occEndPointService.buildUrl(
        `users/current/recommendation/${recommendationId}/updateStatus?fields=DEFAULT&status=PRACTITIONER_CANCELLED`
      ),
      payload
    );
  }

  patientViewed(
    recommendationId: string,
    payload: string
  ): Observable<any> {
    return this.httpClient.patch(
      this.occEndPointService.buildUrl(
        `users/current/recommendation/${recommendationId}/updateStatus?fields=DEFAULT&status=PATIENT_VIEWED`
      ),
      payload
    );
  }

  getRecommendationEntries(recommendationId: string): Observable<any> {
    return this.httpClient.get(
      this.occEndPointService.buildUrl(
        `users/current/recommendation/${recommendationId}?fields=FULL`
      )
    );
  }

  fetchRecommendationsForReview(recoId: string): Observable<any> {
    return this.httpClient.get(
      this.occEndPointService.buildUrl(
        `users/current/recommendation/${recoId}?fields=FULL`
      )
    );
  }
  fetchProductFecetFilters(): Observable<any> {
    return this.httpClient.get(
      this.occEndPointService.buildUrl(EDIT_PROTOCOL_LIST_API)
    );
  }
  getCustomDosing(): Observable<any> {
    return this.httpClient.get(
      this.occEndPointService.buildUrl('users/current/itemtimes')
    );
  }

  getAllProducts(payload?: any): Observable<any> {
    let params = new HttpParams();
    Object.keys(payload).forEach((key: any) => {
      const value = payload[key as keyof ApiCall];
      const existing = (typeof value === 'string') ? (value.trim().length > 0) : key === 'currentPage' ? true :(value > 0);
      if (existing) {
        params = (typeof value === 'string') ? params.append(key, value.trim()) : params.append(key, value);
      }
    });
   
    params.append('isMedpax', false);
    let url = EDIT_PROTOCOL_LIST_API;
    return this.httpClient.get(this.occEndPointService.buildUrl(url), { params });
  }

  getSelectedItems(): Observable<any> {
    return this.httpClient.get(this.occEndPointService.buildUrl('productSearch'));
  }

  filterData(query: string): Observable<any> {
    return this.httpClient.get(this.occEndPointService.buildUrl('products' + query));
  }

  addRecommendationToCart(recommendationId: string, cartId: string) {
    let params = new HttpParams().append('recommendationId', recommendationId);
    return this.httpClient.post(this.occEndPointService.buildUrl(ADD_MEDPAX_TO_CART(cartId)), null, { params });
  }

  getMedPaxForOwner(protocolId: string, isRecommendation = false) {
    const key = isRecommendation ? 'recommendationId' : 'protocolId';
    let params = new HttpParams().append(key, protocolId);
    return this.httpClient.get(this.occEndPointService.buildUrl(GET_MEDPAX_FOR_OWNER), { params });
  }

  addProductTotheRecommendationwithDosingEtries(protocolId: string, payLoad: any, type: 'post' | 'patch', entryNo?: string,) {
    const endPoint = type == 'patch' ? UPDATING_A_RECOMMENDATION_WITH_ENTRY_NO({ protocolId, entryNo }) : UPDATING_A_RECOMMENDATION_WITHOUT_ENTRY_NO({ protocolId });
    return this.httpClient[type](this.occEndPointService.buildUrl(endPoint), payLoad);
  }

  addToCartRecommendation(recommendationId: string) {
      return this.activeCartService
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
                this.addRecommendationToCart(recommendationId, resolvedCartId)
              ),tap(()=>{
                this.activeCartService.reloadActiveCart();
              })
            );
  }
}
