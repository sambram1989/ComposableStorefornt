import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { OccEndpointsService } from '@spartacus/core';

@Injectable({ providedIn: 'root' })
export class ProductUpdatesPageService {
  constructor(
    private httpClient: HttpClient,
    private occEndPointService: OccEndpointsService
  ) {}


  fetchProductUpdates(productTableRequested: string, currentPage: number): Observable<any> {
    return this.httpClient.get(
      this.occEndPointService.buildUrl('productUpdates', {
        queryParams: {
            productTableRequested,
            currentPage,
            pageSize: 10,
            sort: 'name-asc'
        }
      })
    );
  }
}
