import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient, HttpParams } from '@angular/common/http';
import { OccEndpointsService } from '@spartacus/core';
import { GET_PATIENT_ACCOUNT_UPDATE_DETAILS, GET_PATIENT_ORDER_HISTORY_DETAILS, GET_PATIENT_PROFILE_DETAILS, GET_PATIENT_RECOMMENDATION_DETAILS } from '../ws-constants/ws-api-endpoint.constant';
import { ApiCall } from '../ws-components/ws-practitioner/ws-My-patients/my-patient-landing/practitioner-my-patient-listing.service';

@Injectable({ providedIn: 'root' })
export class PatientProfileService {
  constructor(
    private httpClient: HttpClient,
    private occEndPointService: OccEndpointsService
  ) {}


  fetchPatientProfileDetails(email: string): Observable<any> {
    return this.httpClient.get(
      this.occEndPointService.buildUrl(GET_PATIENT_PROFILE_DETAILS, {
        queryParams: {
            fields: 'FULL',
            email: email
        }
      })
    );
  }

  fetchPatientRecommendations(payload? : any): Observable<any> {
    let params = new HttpParams();
    Object.keys(payload).forEach((key: any) => {
      const value = payload[key as keyof ApiCall];
      const existing = (typeof value === 'string') ? (value.trim().length > 0) : (value > 0);
      if (existing) {
        params = (typeof value === 'string') ? params.append(key, value.trim()) : params.append(key, value);
      }
    });
    let url = `${GET_PATIENT_RECOMMENDATION_DETAILS}?${params}`;
    return this.httpClient.get(
      this.occEndPointService.buildUrl(url)
    );
  }

  fetchPatientOrderHistoryDetails(email: string, startDate: string, endDate: string): Observable<any> {
    return this.httpClient.post(
      this.occEndPointService.buildUrl(GET_PATIENT_ORDER_HISTORY_DETAILS, {
        queryParams: {
            fields: 'DEFAULT',
            patientId: email,
            startDate: startDate,
            endDate: endDate
        }
      }), {}
    );
  }

  updatePatientDetails(email: string, payload: any): Observable<any> {
    return this.httpClient.post(
      this.occEndPointService.buildUrl(GET_PATIENT_ACCOUNT_UPDATE_DETAILS, {
        queryParams: {
          patientId: email
        }
      }),
      payload
    );
  }
}
