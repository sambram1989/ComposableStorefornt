import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { OccEndpointsService } from '@spartacus/core';
import { GET_SEARCHED_PRACTITIONER_DETAILS } from '../ws-constants/ws-api-endpoint.constant';

@Injectable({ providedIn: 'root' })
export class FindPractitionerService {
  constructor(
    private httpClient: HttpClient,
    private occEndPointService: OccEndpointsService
  ) {}


  fetchAddressDetails(address: string, latitude: number, longitude: number, currentPage: number): Observable<any> {
    return this.httpClient.get(
      this.occEndPointService.buildUrl(GET_SEARCHED_PRACTITIONER_DETAILS, {
        queryParams: {
            currentPage,
            address: address ? address : null,
            latitude,
            longitude,
            pageSize:10,
            sort: 'asc'
        }
      })
    );
  }
}
