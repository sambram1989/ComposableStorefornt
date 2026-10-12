import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { ApiCall } from '../ws-components/ws-practitioner/ws-My-patients/my-patient-landing/practitioner-my-patient-listing.service';
import { PRIVATE_LABEL } from '../ws-constants/ws-api-endpoint.constant';
import { OccEndpointsService } from '@spartacus/core';

@Injectable({
  providedIn: 'root',
})
export class WsPrivateLabelService {
  newProductsFiltered = new BehaviorSubject<any>(null);

  constructor(
    private httpClient: HttpClient,
    private occEndPointService: OccEndpointsService
  ) {}

  getAllProducts(payload?: any): Observable<any> {
    let url = `${PRIVATE_LABEL}&query=${payload.query}&pageSize=${payload.pageSize}&currentPage=${payload.currentPage}&sort=${payload.sort}`;
    return this.httpClient.get(this.occEndPointService.buildUrl(url));
  }

}
