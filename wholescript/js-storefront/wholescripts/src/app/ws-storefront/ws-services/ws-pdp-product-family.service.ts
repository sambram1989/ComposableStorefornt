import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { OccEndpointsService } from '@spartacus/core';
import { PDP_PRODUCT_FAMILY } from '../ws-constants/ws-api-endpoint.constant';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class WsPdpProductFamilyService {
  constructor(
    private http: HttpClient,
    private occEndPointService: OccEndpointsService
  ) {}

  getProductFamily(productCode: string): Observable<any> {
    return this.http.get(
      this.occEndPointService.buildUrl(PDP_PRODUCT_FAMILY(productCode))
    );
  }
}
