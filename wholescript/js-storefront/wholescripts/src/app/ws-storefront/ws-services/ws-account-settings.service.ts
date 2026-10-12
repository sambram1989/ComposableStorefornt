import { HttpClient } from '@angular/common/http';
import { Injectable, signal } from '@angular/core';
import { OccEndpointsService } from '@spartacus/core';
import { Observable } from 'rxjs';
import {
  ADD_PRACTITIONER_DETAILS,
  GET_PRACTITIONER_DETAILS,
  GET_REGIONS,
  REMOVE_PRACTITIONER_FROM_DIRECTORY,
  UPDATE_PRACTITIONER_DETAILS,
  UPLOAD_PROFILE_IMAGE,
  UPDATE_PRACTITIONER,
  GET_PRACTITIONER,
} from '../ws-constants/ws-api-endpoint.constant';
@Injectable({
  providedIn: 'root',
})
export class AccountSettingsService {
  constructor(
    private occEndPointService: OccEndpointsService,
    private httpClient: HttpClient
  ) {}

  getRegions(countryIsoCode: string): Observable<any> {
    return this.httpClient.get(
      this.occEndPointService.buildUrl(GET_REGIONS(countryIsoCode), {
        queryParams: {
          fields: 'FULL',
        },
      })
    );
  }

  getPractionerDetails(): Observable<any> {
    return this.httpClient.get(
      this.occEndPointService.buildUrl(GET_PRACTITIONER_DETAILS)
    );
  }

  uploadProfileImage(file: File, practiceName: string): Observable<any> {
    const formData = new FormData();
    formData.append('file', file);

    return this.httpClient.post(
      this.occEndPointService.buildUrl(UPLOAD_PROFILE_IMAGE(practiceName)),
      formData
    );
  }

  addPractionerDetails(payload: any): Observable<any> {
    return this.httpClient.post(
      this.occEndPointService.buildUrl(ADD_PRACTITIONER_DETAILS),
      payload
    );
  }

  updatePractionerDetails(payload: any, name: string): Observable<any> {
    return this.httpClient.patch(
      this.occEndPointService.buildUrl(UPDATE_PRACTITIONER_DETAILS(name)),
      payload
    );
  }

  removePractionerFromDirectory(name: string): Observable<any> {
    return this.httpClient.patch(
      this.occEndPointService.buildUrl(
        REMOVE_PRACTITIONER_FROM_DIRECTORY(name)
      ),
      null
    );
  }

  updateMyPractitioner(payload: any): Observable<any> {
    return this.httpClient.post(
      this.occEndPointService.buildUrl(UPDATE_PRACTITIONER),
      payload
    );
  }

  getMyPractitioner(): Observable<any> {
    return this.httpClient.get(
      this.occEndPointService.buildUrl(GET_PRACTITIONER)
    );
  }
}
