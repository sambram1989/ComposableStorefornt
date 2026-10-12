import { HttpClient, HttpParams } from '@angular/common/http';
import { EventEmitter, inject, Injectable, signal } from '@angular/core';
import { AuthService, EventService, GlobalMessageService, GlobalMessageType, OccEndpointsService, UserIdService } from '@spartacus/core';
import { CREATE_PROMO_CODE, DELETE_ENTIRE_CART, REMOVE_MEDPAX_FORMULA_FROM_CART, REMOVE_PROMO_CODE, UPDATE_CART_QTY_MEDPAX } from '../../../ws-constants/ws-api-endpoint.constant';
import { distinctUntilChanged, switchMap, Subject, map, merge, take, tap, withLatestFrom } from 'rxjs';
import { ActiveCartFacade, DeleteCartFailEvent, DeleteCartSuccessEvent, MultiCartFacade } from '@spartacus/cart/base/root';
import { Router } from '@angular/router';

@Injectable({ providedIn: 'root' })
export class WsCartEvents {

    private http = inject(HttpClient);
    private occ = inject(OccEndpointsService);
    private auth = inject(AuthService);
    private activeCartService = inject(ActiveCartFacade);
    private userIdService = inject(UserIdService);
    private multiCartFacade = inject(MultiCartFacade);
    private eventService = inject(EventService);
    private globalMessageService = inject(GlobalMessageService);
    private removeMedpaxFromCart = new Subject<void>();
    private router = inject(Router);


    OpenRemoveProductPopup = new EventEmitter<any>();
    RemoveAll = new EventEmitter<any>();
    Remove = new EventEmitter<any>();

    isLoading = signal<boolean>(false);
    remove$ = this.removeMedpaxFromCart.asObservable();
    isRemoveCalled: boolean = false;
  
    emitRemove() {
      this.removeMedpaxFromCart.next();
    }

    emitRemoveAllClick() {
        this.RemoveAll.emit();
    }

    emitRemoveClick(data: any) {
        this.Remove.emit(data);
    }

    emitOpenRemoveProductPopupClick(data: any) {
        this.OpenRemoveProductPopup.emit(data);
    }

    removeCart() {
        return this.activeCartService.isStable().pipe(
            switchMap(() =>
                this.activeCartService.takeActiveCartId().pipe(
                    switchMap((cartId: any) =>
                        this.auth.isUserLoggedIn().pipe(distinctUntilChanged(),
                            switchMap((res: boolean) =>
                                this.http.delete(this.occ.buildUrl(DELETE_ENTIRE_CART(cartId?.code || cartId, res ? 'current' : 'anonymous')), { responseType: 'text' }))
                        ))
                )
            ));
    }
    
    deleteActiveCart(): void {
        this.activeCartService
          .getActiveCartId()
          .pipe(
            withLatestFrom(this.userIdService.getUserId()),
            take(1),
            tap(([cartId, userId]) => {
              this.multiCartFacade.deleteCart(cartId, userId);
            }),
            switchMap(() =>
              merge(
                this.eventService.get(DeleteCartSuccessEvent).pipe(map(() => true)),
                this.eventService.get(DeleteCartFailEvent).pipe(map(() => false))
              ).pipe(take(1))
            )
          )
          .subscribe((success: boolean) => {
            this.router.navigateByUrl('/cart');
            setTimeout(() => {
              this.displayGlobalMessage(success);
            }, 2000)
          });
      }

    updateEnties(cartId: string, entryNumber: number, frequency: string, quantity: any, userId: string) {
        return this.http.patch(this.occ.buildUrl('updateEntries', {
            urlParams: {
                userId: userId,
                cartId,
                entryNumber,
            }
        }), {
            autoShipCode: frequency,
            quantity
        })
    }

    updateMedpaxproductQty(cartId:string,medPaxEntryNumber:string,medpaxId:string,qty:any){
        let params = new HttpParams()
        .append('fields','DEFAULT')
        .append('medpaxId',medpaxId)
        .append('qty',qty)

        return this.http.patch(this.occ.buildUrl(UPDATE_CART_QTY_MEDPAX(cartId,medPaxEntryNumber)),{},{params});

    }

    createPromoCode(cartId:string, promoCode:string){
        let params = new HttpParams()
        .append('promotionId',promoCode)
        return this.http.post(this.occ.buildUrl(CREATE_PROMO_CODE(cartId)),{},{params});
    }

    removePromoCode(cartId:string, promoCode:string){
        return this.http.delete(this.occ.buildUrl(REMOVE_PROMO_CODE(cartId, promoCode)));
    }

    removeMedpaxFormulaEntries(cartId:string,entryNumber:string,medpaxId:string,itemTimeCode:string){
        let params = new HttpParams()
        .append('medpaxId',medpaxId)
        .append('itemTimeCode',itemTimeCode)
        return this.http.post(this.occ.buildUrl(REMOVE_MEDPAX_FORMULA_FROM_CART(cartId,entryNumber)),{},{params,responseType:'text'})
    }

    protected displayGlobalMessage(success: boolean): void {
        if (success) {
          this.globalMessageService.add(
            { key: 'clearCart.cartClearedSuccessfully' },
            GlobalMessageType.MSG_TYPE_CONFIRMATION
          );
        }
      }

}
