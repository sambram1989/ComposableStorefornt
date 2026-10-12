import { ChangeDetectorRef, Component, DestroyRef, inject, input } from '@angular/core';
import { WsCartEvents } from '../ws-cartEvents-Service/ws-cartevent.service';
import { ActiveCartFacade } from '@spartacus/cart/base/root';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { forkJoin, switchMap } from 'rxjs';
import { GlobalMessageService, GlobalMessageType } from '@spartacus/core';
import { WsPromotionsService } from '../../../ws-services/promotions.service';

@Component({
  selector: 'ws-promocode',
  templateUrl: './promocode.component.html',
  styleUrl: './promocode.component.scss'
})
export class PromocodeComponent {
  isUserLoggedOut = input<boolean>(false);
  private cartEvents = inject(WsCartEvents);
  private destroyRef = inject(DestroyRef);
  promoCode: string = '';
  promoCodes: any[] = [];
  isPromoCodeError: boolean = false;
  showPromoDetailsModal: boolean = false;
  showGuestPromoDetailsModal: boolean = false;
  selectedPromotion: any = {};
  loading: boolean = false;
  allPromotionsWithDescription: any[] = [];

  constructor(private activeCartService: ActiveCartFacade, private globalMessageService: GlobalMessageService,
    private cd: ChangeDetectorRef, private promotionService: WsPromotionsService
  ) {
    this.activeCartService
      .getActive()
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe((cart) => {
        this.promoCode = '';
        this.promoCodes = [...cart?.appliedOrderPromotions, ...cart?.appliedProductPromotions];
        this.allPromotionsWithDescription = [];
        if (this.promoCodes?.length > 0) {
          const promotionApi = this.promoCodes.reduce((acc: any, promotion) => {
            acc[promotion.promotion.code] = this.promotionService.getPromotionByCode(promotion.promotion.code);
            return acc;
          }, {});
          forkJoin(promotionApi).subscribe({
            next: (response) => {
              Object.values(response)?.map(promotion => {
                if (promotion?.code) {
                  this.allPromotionsWithDescription.push(promotion);
                }
              })
              this.cd.detectChanges();
            },
            error: (err) => {
              console.error('Error occurred in getPromotionByCode:', err);
            },
          });
        }
        this.loading = false;
        this.isPromoCodeError = false;
        const cartItems: any = cart;
        const invalidPromoCodes = [...cartItems?.headerInvalidsPromoCodes, ...cartItems?.itemInvalidsPromoCodes];
        if (invalidPromoCodes?.length > 0) {
          this.globalMessageService.add(
            `Promotion${invalidPromoCodes?.length > 1 ? 's' : ''} '${invalidPromoCodes?.join(',')}' ${invalidPromoCodes?.length > 1 ? 'are' : 'is'} not valid`,
            GlobalMessageType.MSG_TYPE_ERROR
          );
        }
        this.cd.markForCheck();
      }, () => {
        this.loading = false;
        this.cd.markForCheck();
      });
  }

  redeemCode() {
    this.isPromoCodeError = false;
    if(this.isUserLoggedOut()) {
      this.showGuestPromoDetailsModal = true;
    } else {
      this.loading = true;
      this.activeCartService.takeActiveCartId().pipe(
        switchMap((cartId: any) => this.cartEvents
          .createPromoCode(cartId, this.promoCode))
      ).pipe(takeUntilDestroyed(this.destroyRef))
        .subscribe((data) => {
          this.loading = false;
          this.activeCartService.reloadActiveCart();
          this.cd.markForCheck();
        }, (err) => {
          if(err?.status === 200) {
            this.activeCartService.reloadActiveCart();
            this.loading = false;
          } else {
            this.isPromoCodeError = true;
            this.globalMessageService.add(
              err?.error?.errors?.[0].message || "Internal error occured. Please try again later",
              GlobalMessageType.MSG_TYPE_ERROR
            );
            this.loading = false;
          }
          this.cd.markForCheck();
        })
    }
  }

  removePromoCode(promoCode: string) {
    this.loading = true;
    this.activeCartService.takeActiveCartId().pipe(
      switchMap((cartId: any) => this.cartEvents
        .removePromoCode(cartId, promoCode))
    ).pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe((data) => {
        this.activeCartService.reloadActiveCart();
        this.cd.markForCheck();
      }, (err) => {
        if(err?.status === 200) {
          this.activeCartService.reloadActiveCart();
        } else {
          this.globalMessageService.add(
            err?.error?.errors?.[0].message || "Internal error occured. Please try again later",
            GlobalMessageType.MSG_TYPE_ERROR
          );
          this.loading = false;
        }
        this.cd.markForCheck();
      })
  }

  seeDetails(promotion: any) {
    this.selectedPromotion = promotion;
    this.showPromoDetailsModal = true;
  }

  closeModal() {
    this.showPromoDetailsModal = false;
    this.cd.markForCheck();
  }

  closeGuestModal() {
    this.showGuestPromoDetailsModal = false;
    this.cd.markForCheck();
  }
}
