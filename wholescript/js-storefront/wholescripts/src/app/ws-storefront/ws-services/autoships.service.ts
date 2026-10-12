import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { OccEndpointsService } from '@spartacus/core';
import { AUTOSHIP_ADDRESSES_VERIFICATION, GET_AUTOSHIPS, GET_AUTOSHIPS_DETAILS, UPDATE_AUTOSHIP_ORDER } from '../ws-constants/ws-api-endpoint.constant';

@Injectable({ providedIn: 'root' })
export class WsAutoShipsService {
  private occ = inject(OccEndpointsService);
  private http = inject(HttpClient);

  getAutoShips() {
    return this.http
      .get(this.occ.buildUrl(GET_AUTOSHIPS))
  }

  getAutoShipDetails(orderNumber: number) {
    return this.http
      .get(this.occ.buildUrl(GET_AUTOSHIPS_DETAILS(orderNumber)))
  }

  updateAutoShip(payload: any) {
    return this.http.post(
      this.occ.buildUrl(UPDATE_AUTOSHIP_ORDER),
      payload
    )
  }

  verifyAddress(payload: any) {
    return this.http.post(this.occ.buildUrl(AUTOSHIP_ADDRESSES_VERIFICATION), payload);
  }
}
