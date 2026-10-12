import { HttpClient, HttpParams } from "@angular/common/http";
import { inject, Injectable } from "@angular/core";
import { OccEndpointsService } from "@spartacus/core";
import { GET_PRIVATE_LABEL_CARTONS, POST_PRIVATE_LABEL_CARTON } from "../../ws-constants/ws-api-endpoint.constant";
import { ProtocolService } from "../protocol/protocol.service";

@Injectable({providedIn:'root'})
export class WsPrivateLabelCartonService{

private http = inject(HttpClient);
private occ = inject(OccEndpointsService);
private protocolService = inject(ProtocolService);
occBaseUrl = this.protocolService.getBaseUrl();

getMedPaxCartons(){
return this.http.get(this.occ.buildUrl(GET_PRIVATE_LABEL_CARTONS));
}

postSelectedCarton(payLoad: any) {
  let params = new HttpParams()
      .append('cartonName', payLoad?.cartonName)
      .append('medpaxId', payLoad?.medpaxId)
      .append('productCode', payLoad?.productCode);

  return this.http.post(this.occ.buildUrl(POST_PRIVATE_LABEL_CARTON), null, { params, responseType: 'text' });
}



}