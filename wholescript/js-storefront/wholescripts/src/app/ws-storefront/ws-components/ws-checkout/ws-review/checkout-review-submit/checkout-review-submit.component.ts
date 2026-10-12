import {
  ChangeDetectorRef,
  Component,
  DestroyRef,
  inject,
} from '@angular/core';
import { ActiveCartFacade, Cart } from '@spartacus/cart/base/root';
import {
  CheckoutReviewSubmitComponent,
  CheckoutStepService,
} from '@spartacus/checkout/base/components';
import {
  CheckoutDeliveryAddressFacade,
  CheckoutPaymentFacade,
  CheckoutDeliveryModesFacade,
} from '@spartacus/checkout/base/root';
import { TranslationService } from '@spartacus/core';
import { WsCartResponseModifier } from '../../../../ws-services/ws-checkout/ws-cart-response-modifier.service';
import { map, Observable } from 'rxjs';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { WsACHPaymentService } from '../../../../ws-services/ws-checkout/ws-ach-payment.service';
import { ActivityLogService } from '../../../../ws-services/ws-activityLog.service';

@Component({
  selector: 'ws-checkout-review-submit',
  templateUrl: './checkout-review-submit.component.html',
  styleUrl: './checkout-review-submit.component.scss',
})
export class WsCheckoutReviewSubmitComponent extends CheckoutReviewSubmitComponent {
  private responseModifier = inject(WsCartResponseModifier);
  private destroyRef = inject(DestroyRef);
  ACH_PAYMENT_TYPE: string = 'ZACH';
  cartData: any;
  cart: any = {};
  isACHPaymentMethod: boolean = false;
  loading$: Observable<boolean> = this.activeCartFacade.getLoading();
  constructor(
    checkoutDeliveryAddressFacade: CheckoutDeliveryAddressFacade,
    checkoutPaymentFacade: CheckoutPaymentFacade,
    activeCartFacade: ActiveCartFacade,
    translationService: TranslationService,
    checkoutStepService: CheckoutStepService,
    checkoutDeliveryModesFacade: CheckoutDeliveryModesFacade,
    private achPaymentService: WsACHPaymentService,
    private cd: ChangeDetectorRef,
    private activityLogService: ActivityLogService
  ) {
    super(
      checkoutDeliveryAddressFacade,
      checkoutPaymentFacade,
      activeCartFacade,
      translationService,
      checkoutStepService,
      checkoutDeliveryModesFacade
    );
    this.activeCartFacade
      .takeActive()
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe((cart: any) => {
        this.cartData = cart;
        this.cart = this.responseModifier.mapCartEntries(cart || null); 
        const paymentTypeCode = this.cartData?.paymentType?.code || '';
        if (paymentTypeCode === this.ACH_PAYMENT_TYPE) {
          this.achPaymentService.isACHPaymentMethod$.next(true);
          this.loadACHPaymentScript();
        }
      });
  }

  loadACHPaymentScript() {
    this.achPaymentService.isACHPaymentMethod$.subscribe((data) => {
      this.isACHPaymentMethod = data;
      this.cd.markForCheck();
    });
    this.achPaymentService.isActivityLogName$.subscribe((data) => {
      if (data) {
        this.activityLogService.sendLog(data);
        this.achPaymentService.isActivityLogName$.next('');
      }
      this.cd.markForCheck();
    });
    this.achPaymentService
      .loadACHPaymentScript()
      .then(() => {
        console.log('ACH Payment script loaded successfully ');
        this.achPaymentService.getDelegoToken().subscribe(
          (data: any) => {
            this.achPaymentService.OpenDelegoIFrame(
              this.cartData?.totalPrice?.value || 0,
              data
            );
            this.cd.markForCheck();
          },
          (error) => {
            console.log('error from delego token =====>', error);
          }
        );
      })
      .catch((error) => {
        console.error('Error loading ACH Payment script:', error);
      });
  }

  ngOnDestory() {
    this.achPaymentService.isACHPaymentMethod$.next(false);
  }
}
