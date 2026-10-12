import { inject, Injectable, signal } from "@angular/core";
import { ActiveCartFacade, OrderEntry } from "@spartacus/cart/base/root";
import { Command, CommandStrategy, UserIdService, CommandService, EventService, OCC_USER_ID_ANONYMOUS } from "@spartacus/core";
import { Order, OrderFacade, OrderPlacedEvent } from "@spartacus/order/root";
import { BehaviorSubject, switchMap, tap, Observable, combineLatest, take, map } from "rxjs";
import { WsOrderConnector } from "./ws-order.connector";
import { WsCartResponseModifier } from "./ws-cart-response-modifier.service";

@Injectable({
    providedIn: 'root'
})
export class WsOrderService extends OrderFacade {
    private responseModifier = inject(WsCartResponseModifier);
  protected  placedOrder$ = new BehaviorSubject<Order | undefined>(undefined);

  protected  placeOrderCommand: Command<boolean, Order> =
    this.commandService.create<boolean, Order>(
      (payload) =>
        this.checkoutPreconditions().pipe(
          switchMap(([userId, cartId]) =>
            this.orderConnector.placeOrder(userId, cartId, payload).pipe(
              tap((order) => {
                console.log("Getting CAlled away ",order);
                if(order){
                  console.log("Getting CAlled");
                  this.placedOrder$.next(order);
                  this.responseModifier.placedOrder$.next(order);
                }
                this.eventService.dispatch(
                  {
                    userId,
                    cartId,
                    /**
                     * As we know the cart is not anonymous (precondition checked),
                     * we can safely use the cartId, which is actually the cart.code.
                     */
                    cartCode: cartId,
                    order,
                  },
                  OrderPlacedEvent
                );
              })
            )
          )
        ),
      {
        strategy: CommandStrategy.CancelPrevious,
      }
    );

  constructor(
    protected  activeCartFacade: ActiveCartFacade,
    protected  userIdService: UserIdService,
    protected  commandService: CommandService,
    protected  orderConnector: WsOrderConnector,
    protected  eventService: EventService
  ) {
    super()
  }

  /**
   * Performs the necessary checkout preconditions.
   */
  protected  checkoutPreconditions(): Observable<[string, string]> {
    return combineLatest([
      this.userIdService.takeUserId(),
      this.activeCartFacade.takeActiveCartId(),
      this.activeCartFacade.isGuestCart(),
    ]).pipe(
      take(1),
      map(([userId, cartId, isGuestCart]) => {
        if (
          !userId ||
          !cartId ||
          (userId === OCC_USER_ID_ANONYMOUS && !isGuestCart)
        ) {
          throw new Error('Checkout conditions not met');
        }
        return [userId, cartId];
      })
    );
  }

   placeOrder(termsChecked: boolean): Observable<Order> {
    return this.placeOrderCommand.execute(termsChecked);
  }

   getOrderDetails(): Observable<Order | undefined> {
    return this.placedOrder$.asObservable();
  }

   clearPlacedOrder(): void {
    this.placedOrder$.next(undefined);
  }

   setPlacedOrder(order: Order): void {
    this.placedOrder$.next(order);
  }

   getPickupEntries(): Observable<OrderEntry[]> {
    return this.getOrderDetails().pipe(
      map(
        (order) =>
          order?.entries?.filter(
            (entry) => entry.deliveryPointOfService !== undefined
          ) || []
      )
    );
  }

   getDeliveryEntries(): Observable<OrderEntry[]> {
    return this.getOrderDetails().pipe(
      map(
        (order) =>
          order?.entries?.filter(
            (entry) => entry.deliveryPointOfService === undefined
          ) || []
      )
    );
  }
}
