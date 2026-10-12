import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { OccEndpointsService } from '@spartacus/core';
import { ADD_ADDRESS, COMMERCE_ALL_ADDRESSES, DELETE_ADDRESS, GET_ALL_ADDRESSES, UPDATE_ADDRESS, VALIDATE_ADDRESS } from '../ws-constants/ws-api-endpoint.constant';
import { ActivityLogService } from './ws-activityLog.service';

@Injectable({ providedIn: 'root' })
export class WsAddressesService {
  private occ = inject(OccEndpointsService);
  private http = inject(HttpClient);
  private activityLogService = inject(ActivityLogService);

  getListOfAddresses() {
    return this.http
      .post(this.occ.buildUrl(GET_ALL_ADDRESSES), null)
  }

  getListOfCommerceAddresses() {
    return this.http
      .get(this.occ.buildUrl(COMMERCE_ALL_ADDRESSES))
  }

  addAddress(payload: any) {
    this.activityLogService.sendLog('address_add', { address: JSON.stringify(payload)});
    return this.http.post(this.occ.buildUrl(ADD_ADDRESS), payload);
  }

  updateAddress(payload: any) {
    return this.http.patch(this.occ.buildUrl(UPDATE_ADDRESS), payload);
  }

  validateAddress(payload: any) {
    return this.http.post(this.occ.buildUrl(VALIDATE_ADDRESS), payload);
  }

  deleteAddress(addressNumber: number, creditCardIds: string, payload: any) {
    this.activityLogService.sendLog('address_remove', { address: addressNumber});
    return this.http.delete(
      this.occ.buildUrl(DELETE_ADDRESS(addressNumber, creditCardIds)),
      {
        body: payload
      }
    )
  }
}
