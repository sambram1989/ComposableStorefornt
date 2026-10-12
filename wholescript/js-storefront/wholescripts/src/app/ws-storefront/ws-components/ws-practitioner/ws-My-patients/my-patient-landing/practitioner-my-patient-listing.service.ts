import { HttpClient, HttpParams } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { OccEndpointsService } from '@spartacus/core';
import { map, Observable, shareReplay } from 'rxjs';
export interface ApiCall {
  practitionerId?: string;
  currentPage: number;
  discount: string;
  pageSize: number;
  searchText: string;
  sortCode?: string;
  referralCode: string;
  status: string;
  sortBy?: string;
  isMedpax?: boolean;
  query?: string;
}

@Injectable()
export class PractitionerMyPatientListingService {
  http = inject(HttpClient);
  occ = inject(OccEndpointsService);

  getPatientList(payLoad?: ApiCall) {
    let params = new HttpParams();
    if(payLoad){
      Object?.keys(payLoad)?.forEach((key: any) => {
        const value = payLoad[key as keyof ApiCall];
        const existing = (typeof value === 'string') ? (value?.trim().length > 0) : (typeof value === 'boolean') ? true : (value > 0);
        if (existing) {
          params = (typeof value === 'string') ? params?.append(key, value?.trim()) : params?.append(key, value);
        }
      });
    }

    return this.http.get(this.occ.buildUrl(`users/current/patients`), { params });
  }

  getFilters(removeFilterType: string): Observable<any> {
    return this.http.get(this.occ.buildUrl(`users/current/apiFilters`)).pipe(map(
      (response: any) => {
        return response.apiFilters
        .filter((item: any) => !item.title.toLowerCase().includes(removeFilterType))
        .map(
          (item: any) => {
            item.config.isopen = item.config.isopen === 'true' ? true : false;
            item.config.viewMore = item.config.viewMore === 'true' ? true : false;
              return item;
          });
      }
    ), shareReplay({bufferSize:1,refCount:true}));
  }

}
