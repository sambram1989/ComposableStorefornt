import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { OccEndpointsService } from '@spartacus/core';
import {
  SHOPIFY_STATUS,
  SHOPIFY_PRODUCTS,
  SHOPIFY_PRODUCTS_SEARCH,
  ADD_SHOPIFY_PRODUCT,
  DELETE_SHOPIFY_PRODUCT,
} from '../ws-constants/ws-api-endpoint.constant';
import { BehaviorSubject, catchError, Observable } from 'rxjs';
import { ActivatedRoute, Router } from '@angular/router';
import { SearchCriteria } from '@spartacus/storefront';

@Injectable({
  providedIn: 'root',
})
export class WsShopifyService {
  private http = inject(HttpClient);
  private occEndpointService = inject(OccEndpointsService);
  protected activatedRoute = inject(ActivatedRoute);
  protected router = inject(Router);

  refreshResults = new BehaviorSubject<any>('');
  setFilter = new BehaviorSubject<any>('');

  getShopifyStatus() {
    return this.http.get(this.occEndpointService.buildUrl(SHOPIFY_STATUS));
  }

  getShopifyProducts(
    searchText: string,
    payload: any
  ): Observable<any> {
    return this.http.get(
      this.occEndpointService.buildUrl(SHOPIFY_PRODUCTS_SEARCH, {
        // queryParams: {
        //   query: `${searchText}`,
        //   currentPage: payload.currentPage,
        //   sort: payload.sort,
        //   pageSize: payload.pageSize
        // },
      })
    );
  }

  sort(sortCode: string): void {
    this.route({ sortCode });
  }

  protected route(queryParams: SearchCriteria): void {
    this.router.navigate([], {
      queryParams,
      queryParamsHandling: 'merge',
      relativeTo: this.activatedRoute,
      replaceUrl: false,
      onSameUrlNavigation: 'reload',
      skipLocationChange: false,
    });
  }

  importShopifyProducts(code: string[]): Observable<any> {
    const payload = {
      products: code,
    };
    return this.http.post(
      this.occEndpointService.buildUrl(ADD_SHOPIFY_PRODUCT),
      payload
    );
  }

  deleteShopifyProducts(payload: any): Observable<any> {
    const url = this.occEndpointService.buildUrl(DELETE_SHOPIFY_PRODUCT);
    return this.http.request('DELETE', url, { body: payload });
  }

  filterData(query: string, pageSize: any): Observable<any> {
    return this.http.get(
      this.occEndpointService.buildUrl(`wsSearch/users/current/shopify/products?query=${query}&pageSize=${pageSize}&fields=FULL&filters=availableForEstore:Yes:isMedpax:false`)
    );
  }

}
