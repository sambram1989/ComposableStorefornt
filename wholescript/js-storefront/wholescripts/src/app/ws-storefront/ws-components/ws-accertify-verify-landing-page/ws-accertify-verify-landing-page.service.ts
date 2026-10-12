import { HttpClient, HttpParams } from "@angular/common/http";
import { inject, Injectable } from "@angular/core";
import { OccEndpointsService } from "@spartacus/core";

@Injectable()
export class WsAccertifyVerifyLandingService {
    private http = inject(HttpClient);
    private occ = inject(OccEndpointsService);

    isUserVerified(token: string) {
        const params = new HttpParams().append('token', token);
        return this.http.post(this.occ.buildUrl('accertify/email/verified'), null, { params });
    }

}