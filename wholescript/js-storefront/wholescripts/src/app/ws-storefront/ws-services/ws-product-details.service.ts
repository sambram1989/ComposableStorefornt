import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { OccEndpointsService } from '@spartacus/core';
import { Observable } from 'rxjs';
import { PRODUCT_DETAILS, SUPPLEMENT_FACTS } from '../ws-constants/ws-api-endpoint.constant';

@Injectable({
  providedIn: 'root',
})
export class WsProductDetailsService {
  constructor(
    private http: HttpClient,
    protected occEndpointService: OccEndpointsService
  ) {}

  fetchProductDetail(productId: string): Observable<any> {
    return this.http.get(
      this.occEndpointService.buildUrl(PRODUCT_DETAILS(productId))
    );
  }

  fetchSupplimentFacts(productId: string[]): Observable<any> {
    return this.http.get(
      this.occEndpointService.buildUrl(SUPPLEMENT_FACTS(productId))
    );
  }
}
