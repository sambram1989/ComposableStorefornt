import { HttpClient, HttpParams } from '@angular/common/http';
import { inject, Injectable, signal } from '@angular/core';
import { OccEndpointsService } from '@spartacus/core';
import { BehaviorSubject, Observable, of, switchMap, tap } from 'rxjs';
import {
  ADD_MEDPAX_TO_CART,
  CLONE_PROTOCOL_TO_RECOMMENDATION,
  DELETE_PROTOCOL,
  DUPLICATE_A_PROTOCOL,
  EDIT_ADD_PRODUCT_API,
  EDIT_DELETE_PRODUCT_API,
  EDIT_PROTOCOL_LIST_API,
  EDIT_PROTOCOL_PRODUCT_API,
  FETCH_PROTOCOL_REVIEW_API,
  GET_CUSTOM_DOSING_ENTRIES,
  GET_ICONS,
  POST_CUSTOM_DOSING_ENTRIES,
  SEND_PROTOCOL_REVIEW_API,
  SHARE_PROTOCOL_WITH_ALL,
  STOP_SHARING_WITH_SPECIFIC_USER,
  SWAP_PROTOCOL_PATCH,
  UPDATING_A_PROTOCOL_WITH_ENTRY_NO,
  UPDATING_A_PROTOCOL_WITHOUT_ENTRY_NO,
  USER_PROTOCOLS_ENDPOINT,
} from '../../ws-constants/ws-api-endpoint.constant';
import { ActivatedRoute, Router } from '@angular/router';
import { SearchCriteria } from '@spartacus/storefront';
import { ActiveCartFacade, MultiCartFacade } from '@spartacus/cart/base/root';

export type protocolListFetch = {
  currentPage: number;
  pageSize: number;
  sort: string;
  searchText: string;
};
export interface searchProducts {
  currentPage: number;
  pageSize: number;
  query: string;
};

export type saveAsCustom = { isSavedAsCustom: boolean; productId: string };

export type ProtocolAdd = {
  quantity: number;
  notes: string;
  product: {
    code: string;
  };
  itemDuration: string;
  productItemType?: string;
  dosing: any[];
};

export type ShareProtocol = {
  patients: [];
  sharedWithAll: boolean;
  termsAgreed: boolean;
};

@Injectable({
  providedIn: 'root',
})
export class ProtocolService {
  private activeCartService = inject(ActiveCartFacade);
  private multiCartService = inject(MultiCartFacade);
  currentQuery = '';
  currentQuery$ = new BehaviorSubject<string>(this.currentQuery);
  selectedShareablePatient = signal<any>([]);
  shareSuccessApiResponse = signal<any>({});
  isWsPage = signal<boolean>(false);
  isComeFromWs = signal<boolean>(false);
  isMedPaxPage = signal<boolean>(false);

  isWholeScriptsTab = signal<boolean>(false);
  constructor(
    private occEndPointService: OccEndpointsService,
    private httpClient: HttpClient,
    protected activatedRoute: ActivatedRoute,
    protected router: Router
  ) { }

  fetchProtocolList(payLoad: protocolListFetch): Observable<any> {
    let params = new HttpParams();
    params = params.append('isWSPage', this.isWsPage());
    return this.httpClient.get(
      this.occEndPointService.buildUrl(USER_PROTOCOLS_ENDPOINT(payLoad)),
      { params }
    );
  }

  fetchReviewProtocol(protocolId: string , isWsTabClicked?: any): Observable<any> {
    const isWholeScriptsTab = localStorage.getItem('isWholeScriptsTab') ? localStorage.getItem('isWholeScriptsTab') : this?.isWsPage();
    let params = new HttpParams();
    params = params.append('isWSPage', isWholeScriptsTab);
    localStorage.removeItem('isWholeScriptsTab');   //remove the storage after assigning the value
    return this.httpClient.get(
      this.occEndPointService.buildUrl(
        FETCH_PROTOCOL_REVIEW_API({ protocolId })
      ),
      { params }
    );
  }

  fetchProducts(payLoad?: searchProducts, url?: string): Observable<any> {
    let params = new HttpParams();
    if (payLoad) {
      Object?.keys(payLoad)?.forEach((key: any) => {
        const value = payLoad[key as keyof searchProducts];
        const existing =
          typeof value === 'string' ? value.trim().length > 0 : value > 0;
        if (existing) {
          params =
            typeof value === 'string'
              ? params.append(key, value.trim())
              : params.append(key, value.toString()); 
        }
      });
    }
    const endPoint = url
      ? EDIT_PROTOCOL_LIST_API + url
      : EDIT_PROTOCOL_LIST_API;
    return this.httpClient.get(this.occEndPointService.buildUrl(endPoint), {
      params,
    });
  }

  addProductsToProtocol(protocolId: string, PayLoad: ProtocolAdd) {
    return this.httpClient.post(
      this.occEndPointService.buildUrl(EDIT_ADD_PRODUCT_API(protocolId)),
      PayLoad
    );
  }
  removeProductsFromProtocol(payLoad: any) {
    return this.httpClient.delete(
      this.occEndPointService.buildUrl(EDIT_DELETE_PRODUCT_API(payLoad))
    );
  }

  createNewProtocol(PayLoad: any, type: 'post' | 'patch', endpoint: string) {
    return this.httpClient[type](
      this.occEndPointService.buildUrl(endpoint),
      PayLoad
    );
  }

  sendProtocol(protocolId: string, payload: any) {
    return this.httpClient.patch(
      this.occEndPointService.buildUrl(
        SEND_PROTOCOL_REVIEW_API({ protocolId })
      ),
      payload
    );
  }

  duplicateAProtocol(protocolId: string, payLoad: any) {
    let params = new HttpParams();
    params = params.append('isWSPage', this.isWsPage());
    return this.httpClient.post(
      this.occEndPointService.buildUrl(DUPLICATE_A_PROTOCOL(protocolId)),
      payLoad,
      { params }
    );
  }

  deleteAProtocol(protocolId: string) {
    return this.httpClient.delete(
      this.occEndPointService.buildUrl(DELETE_PROTOCOL(protocolId))
    );
  }
  ////Mandati------
  fetchProtocolDeatils(protocolId: string): Observable<any> {
    return this.httpClient.get(
      this.occEndPointService.buildUrl(
        `users/current/protocols/{protocolId}?fields=FULL&protocolId=${protocolId}`
      )
    );
  }

  fetchIcons() {
    return this.httpClient.get(this.occEndPointService.buildUrl(GET_ICONS));
  }

  fetchEditableProducts(protocolId: string): Observable<any> {
    let params = new HttpParams();
    params = params.append('isWSPage', this.isWsPage());
    return this.httpClient.get(
      this.occEndPointService.buildUrl(
        EDIT_PROTOCOL_PRODUCT_API({ protocolId })
      ),
      { params }
    );
  }

  getBaseUrl(): string {
    const updatedUrl = this.occEndPointService
      .getBaseUrl()
      .replace(/(https?:\/\/[^\/]*\.com).*$/, '$1');
    return updatedUrl;
  }

  getCustomDosingEntries(productId: string) {
    return this.httpClient.get(
      this.occEndPointService.buildUrl(GET_CUSTOM_DOSING_ENTRIES(productId))
    );
  }

  postCustomDosingEntries(params: saveAsCustom, payload: any) {
    return this.httpClient.post(
      this.occEndPointService.buildUrl(POST_CUSTOM_DOSING_ENTRIES(params)),
      payload
    );
  }

  addProductTotheProtocolwithDosingEtries(
    protocolId: string,
    payLoad: any,
    type: 'post' | 'patch',
    entryNo?: string
  ) {
    const endPoint =
      type == 'patch'
        ? UPDATING_A_PROTOCOL_WITH_ENTRY_NO({ protocolId, entryNo })
        : UPDATING_A_PROTOCOL_WITHOUT_ENTRY_NO({ protocolId });
    return this.httpClient[type](
      this.occEndPointService.buildUrl(endPoint),
      payLoad
    );
  }

  shareProtocolSuccess(protocolId: string, payload: ShareProtocol) {
    return this.httpClient.post(
      this.occEndPointService.buildUrl(SHARE_PROTOCOL_WITH_ALL(protocolId)),
      payload
    );
  }

  stopSharingProtocol(protocolId: string) {
    return this.httpClient.patch(
      this.occEndPointService.buildUrl(
        STOP_SHARING_WITH_SPECIFIC_USER(protocolId)
      ),
      null
    );
  }

  protocoltorecommendation(params: HttpParams) {
    params = params.append('isWSPage', this.isWsPage());
    return this.httpClient.post(
      this.occEndPointService.buildUrl(CLONE_PROTOCOL_TO_RECOMMENDATION),
      null,
      { params }
    );
  }

  addProtocolToCart(medPaxId: string, cartId: string) {
    let params = new HttpParams()
    .append('protocolId', medPaxId)
    .append('isWSPage',this.isWsPage());
    return this.httpClient.post(
      this.occEndPointService.buildUrl(ADD_MEDPAX_TO_CART(cartId)),
      null,
      { params }
    );
  }

  query(query: string) {
    this.route({ query });
  }

  sort(sortCode: string): void {
    this.route({ sortCode });
  }

  protected route(queryParams: SearchCriteria): void {
    this.router.navigate([], {
      queryParams,
      queryParamsHandling: 'merge',
      relativeTo: this.activatedRoute,
    });
  }

  addToCartProtocol(protocolId: string, isNavigateToCart = true) {
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
          this.addProtocolToCart(protocolId, resolvedCartId)
        ), tap(() => {
          this.activeCartService.reloadActiveCart();
        })
      );
  }

  fetchProtocolListforSwappingWithRecommendation(payLoad: protocolListFetch, isWsPage = false ,isNonDraft?:boolean): Observable<any> {
    let params = new HttpParams();
    params = params.append('isWSPage', isWsPage);
    params = params.append('isNonDraft', isNonDraft);
    return this.httpClient.get(
      this.occEndPointService.buildUrl(USER_PROTOCOLS_ENDPOINT(payLoad)),
      { params }
    );
  }

  fetchProtocolDetailsForSwapping(protocolId: string, isWsPage = false): Observable<any> {
    let params = new HttpParams();
    params = params.append('isWSPage', isWsPage);
    return this.httpClient.get(
      this.occEndPointService.buildUrl(
        FETCH_PROTOCOL_REVIEW_API({ protocolId })
      ),
      { params }
    );
  }

  swapProtocol(recommendationId: string, templateId: string) {
    let params = new HttpParams().append('isWSPage',this.isWholeScriptsTab());
    return this.httpClient.patch(this.occEndPointService.buildUrl(SWAP_PROTOCOL_PATCH(recommendationId, templateId)), {},{params})
  }
  
}
