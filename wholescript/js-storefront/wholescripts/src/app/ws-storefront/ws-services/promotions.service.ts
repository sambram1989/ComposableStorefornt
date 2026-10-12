import { HttpClient, HttpHeaders } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { InterceptorUtil, OccEndpointsService, USE_CLIENT_TOKEN } from '@spartacus/core';
import { GET_PROMO_DETAILS, GET_PROMOTIONS } from '../ws-constants/ws-api-endpoint.constant';
import { catchError, Observable, of } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class WsPromotionsService {
  private occ = inject(OccEndpointsService);
  private http = inject(HttpClient);

  getPromotions() {
    return this.http
      .get(this.occ.buildUrl(GET_PROMOTIONS, {
        queryParams: {
          type: 'all',
          fields: 'FULL'
        }
      }))
  }

  getPromotionByCode(promoCode: string): Observable<any>  {
    return this.http.get(this.occ.buildUrl(GET_PROMO_DETAILS(promoCode))).pipe(
      catchError(() => {
        return of(null);
      })
    );;
  }
}
