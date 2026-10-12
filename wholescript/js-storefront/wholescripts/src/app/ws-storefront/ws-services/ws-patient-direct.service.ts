import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { OccEndpointsService } from '@spartacus/core';
import * as XLSX from 'xlsx';
import { CONSULTATION_FEES, CONSULTATION_FEES_DOWNLOAD, REFERRAL_CODE_DETAILS, UPLOAD_PROFILE_IMAGE_FOR_REFERRAL } from '../ws-constants/ws-api-endpoint.constant';

@Injectable({ providedIn: 'root' })
export class PatientDirectService {
  user_id: string = '';
  CODE_EDIT: boolean = false;
  referralCodes$: Observable<any>;
  public editReferral = new BehaviorSubject('');

  constructor(
    private httpClient: HttpClient,
    private occEndPointService: OccEndpointsService,
  ) {}

  createNewReferralCode(
    payload: any
  ): Observable<any> {
    return this.httpClient.post(
      this.occEndPointService.buildUrl(
        `users/current/referral-code`
      ),
      payload
    );
  }

  updateReferralCode(
    payload: any
  ): Observable<any> {
    return this.httpClient.patch(
      this.occEndPointService.buildUrl(
        `users/current/referral-code/${payload.code}?discount=${payload.discount}&referralLink=${payload.link}`
      ),
      {}
    );
  }

  addAddressReferralCode(
    payload: any, code: any
  ): Observable<any> {
    return this.httpClient.put(
      this.occEndPointService.buildUrl(
        `users/current/referral-code/${code}/addAddress`
      ),
      payload
    );
  }

  getAddressReferralCode(code: any): Observable<any> {
    return this.httpClient.get(
      this.occEndPointService.buildUrl(REFERRAL_CODE_DETAILS, {
        queryParams: {
          referralCode: code,
          fields:'DEFAULT'
        }
      }),
    );
  }

  uploadProfileImage(file: File, code: string, lastName: string): Observable<any> {
      const formData = new FormData();
      formData.append('file', file);
  
      return this.httpClient.post(
        this.occEndPointService.buildUrl(UPLOAD_PROFILE_IMAGE_FOR_REFERRAL(`${code}|${lastName}`), {
          queryParams: {
            referralCode: code,
            lastName: lastName
          }
        }),
        formData
      );
    }

  getAccurals(firstDate: string, lastDate: string , isPatient?: string
  ): Observable<any> {
    return this.httpClient.post(
      this.occEndPointService.buildUrl(
        `users/current/getAccuralsInvoices?endDate=${lastDate}&fields=DEFAULT&isPatient=${isPatient ? isPatient : 'false'}&startDate=${firstDate}`
      ),
      {}
    );
  }

  refreshReferralCodes() {
    this.referralCodes$ = this.getReferralEntries(0, 10);
  }

  getAllReferralEntriesForCSV(): Observable<any> {
    return this.httpClient.get(
      this.occEndPointService.buildUrl(
        `users/current/all-referral-codes`
      )
    );
  }

  getReferralEntries(page: any, pageSize: any): Observable<any> {
    return this.httpClient.get(
      this.occEndPointService.buildUrl(
        `users/current/referral-codes?currentPage=${page}&fields=DEFAULT&pageSize=${pageSize}`
      )
    );
  }

  exportCSV(data: any, type: string = '') {
    const workBook = XLSX.utils.book_new();
    const workSheet = XLSX.utils.json_to_sheet(data);

    XLSX.utils.book_append_sheet(workBook, workSheet, 'all-codes');
    XLSX.writeFile(workBook, type + '.xlsx');
  }

  checkValidationForDownLoadButton(){
    return this.httpClient.get(this.occEndPointService.buildUrl(CONSULTATION_FEES));
  }

  downloadW9Form(){
    return this.httpClient.get(this.occEndPointService.buildUrl(CONSULTATION_FEES_DOWNLOAD));
  }

  getWebBannerAndScripts(referralCode: string): Observable<any> {
    const refCode = encodeURIComponent(referralCode);
    return this.httpClient.get(
      this.occEndPointService.buildUrl(
       `users/current/referral-code/${refCode}/webBannersScripts`
      )
    );
  }
}
