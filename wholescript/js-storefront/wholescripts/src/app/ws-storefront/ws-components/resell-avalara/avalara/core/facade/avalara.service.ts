import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { OccEndpointsService } from '@spartacus/core';

@Injectable({
  providedIn: 'root',
})
export class AvalaraService {
  constructor(
    private occEndPointService: OccEndpointsService,
    private http: HttpClient
  ) {}

  getAvalaraLink() {
    const url = this.occEndPointService.buildUrl('avalaraUrl');
    return this.http.get(url);
  }
}
