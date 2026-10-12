import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { OccEndpointsService } from '@spartacus/core';

@Injectable({ providedIn: 'root' })
export class ContactUsPageService {
  constructor(
    private httpClient: HttpClient,
    private occEndPointService: OccEndpointsService
  ) {}


  fetchCategories(): Observable<any> {
    return this.httpClient.get(
      this.occEndPointService.buildUrl('Category')
    );
  }

  submitContactUsForm(payload: any): Observable<any> {
    return this.httpClient.post(
      this.occEndPointService.buildUrl('contactUs'),
      payload
    );
  }
}
