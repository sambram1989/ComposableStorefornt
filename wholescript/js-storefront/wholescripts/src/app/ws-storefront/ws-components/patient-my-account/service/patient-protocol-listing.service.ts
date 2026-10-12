import { HttpClient, HttpParams } from "@angular/common/http";
import { inject, Injectable } from "@angular/core";
import { ActiveCartFacade, MultiCartFacade } from "@spartacus/cart/base/root";
import { OccEndpointsService } from "@spartacus/core";
import { of, switchMap } from "rxjs";
import { ProtocolService } from "../../../ws-services/protocol/protocol.service";

@Injectable({
  providedIn: 'root'
})
export class PatientProtocolListService {
  private occ = inject(OccEndpointsService);
  private http = inject(HttpClient);
  private activeCartService = inject(ActiveCartFacade);
  private multiCartService = inject(MultiCartFacade);
  private protocolService = inject(ProtocolService);
 
  currentProtocolId = '';
  getProtocolList(params: HttpParams) {
    return this.http.get(this.occ.buildUrl(`users/current/patient/protocols`), { params });
  }
  getProducts(protoId: string) {
    return this.http.get(this.occ.buildUrl(`users/current/protocols/${protoId}?fields=FULL`))
  }

  addToCart(protocolId: string) {
    return this.activeCartService
      .getActiveCartId()
      .pipe(switchMap((cartId: any) => {
        const cartAction = cartId?.trim()?.length
          ? of(cartId)
          : this.multiCartService.createCart({
            userId: 'current',
            extraData: { active: true },
          });
        return cartAction
      })).pipe(
        switchMap((resolvedCartId: any) => this.protocolService.addProtocolToCart(protocolId, resolvedCartId))
      )
  }
}