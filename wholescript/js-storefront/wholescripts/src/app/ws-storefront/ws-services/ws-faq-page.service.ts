import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { OccEndpointsService } from '@spartacus/core';
import { GET_FAQ_BY_ID } from '../ws-constants/ws-api-endpoint.constant';

@Injectable({ providedIn: 'root' })
export class FAQPageService {
  constructor(
    private httpClient: HttpClient,
    private occEndPointService: OccEndpointsService
  ) {}


  fetchFaqQusAnsDetails(): Observable<any> {
    return this.httpClient.get(
      this.occEndPointService.buildUrl('getFaqQusAnsDetails')
    );
  }

  getFaqQusAnsById(id: string): Observable<any> {
    return this.httpClient.get(
      this.occEndPointService.buildUrl(GET_FAQ_BY_ID(id))
    );
  }
}
