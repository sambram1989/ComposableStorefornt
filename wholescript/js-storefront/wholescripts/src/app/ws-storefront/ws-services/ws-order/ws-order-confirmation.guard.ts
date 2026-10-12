import { Injectable } from "@angular/core";
import { Router, UrlTree } from "@angular/router";
import { SemanticPathService } from "@spartacus/core";
import { OrderFacade } from "@spartacus/order/root";
import { Observable, map } from "rxjs";

@Injectable({
  providedIn: 'root',
})
export class WsOrderConfirmationGuard {
    constructor(
        protected orderFacade: OrderFacade,
        protected router: Router,
        protected semanticPathService: SemanticPathService
      ) {}
    
      canActivate(): Observable<boolean | UrlTree> {
        return this.orderFacade.getOrderDetails().pipe(
          map((orderDetails) => {
            if (orderDetails && Object.keys(orderDetails).length !== 0) {
              return true;
            } else {
             this.router.navigate(['/order/history/all-orders']);
             return false;
            }
          })
        );
      }
}
