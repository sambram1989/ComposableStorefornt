import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { OccEndpointsService } from '@spartacus/core';


@Injectable({ providedIn: 'root' })
export class PersonaService {

  constructor(
    private httpClient: HttpClient,
    private occEndPointService: OccEndpointsService,
  ) { }


  updateInquiryId(inquiryId: string, userId: string): Observable<any> {
    const url = this.occEndPointService.buildUrl(`users/${userId}/age/verification?inquiryId=${inquiryId}`);
    return this.httpClient.post(url, {});
  }
}
