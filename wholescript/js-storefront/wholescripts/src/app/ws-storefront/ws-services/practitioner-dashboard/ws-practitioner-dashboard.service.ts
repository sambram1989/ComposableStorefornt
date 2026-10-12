import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { OccEndpointsService } from '@spartacus/core';
import { GET_DASHBOARD_DETAILS, GET_BEST_SELLERS } from '../../ws-constants/ws-api-endpoint.constant';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class WsPractitionerDashboardService {

  constructor(
    private http: HttpClient,
    private occEndPointService: OccEndpointsService
  ) { }

  practitionerDashorardDetails(): Observable<any> {
    const url = this.occEndPointService.buildUrl(GET_DASHBOARD_DETAILS);
    return this.http.get(url);
  }

  getBestSellerProducts(): Observable<any> {
    const url = this.occEndPointService.buildUrl(GET_BEST_SELLERS);
    return this.http.get(url);
  }
}
