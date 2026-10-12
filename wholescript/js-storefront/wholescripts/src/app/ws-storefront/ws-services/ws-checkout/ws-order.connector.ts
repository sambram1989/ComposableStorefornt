import { Injectable } from "@angular/core";
import { WsOccOrderAdapter } from "./ws-order.adapter";
import { Observable } from "rxjs";
import { Order } from "@spartacus/order/root";

@Injectable({
    providedIn: 'root',
  })
export class WsOrderConnector {
  constructor(protected  adapter: WsOccOrderAdapter) {}
   

  public  placeOrder(
    userId: string,
    cartId: string,
    termsChecked: boolean
  ): Observable<Order> {
    return this.adapter.placeOrder(userId, cartId, termsChecked);
  }
}
