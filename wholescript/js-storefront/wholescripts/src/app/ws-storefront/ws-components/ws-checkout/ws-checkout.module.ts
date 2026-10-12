import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";
import { CheckoutAuthGuard, CartNotEmptyGuard, CheckoutReviewSubmitModule } from "@spartacus/checkout/base/components";
import { provideConfig, I18nModule, UrlModule, AuthGuard } from "@spartacus/core";
import { WsCheckoutPaymentMethodComponent } from "./checkout-payment-method/checkout-payment-method.component";
import { AtMessageModule, CmsPageGuard, MediaModule, PageSlotModule, PromotionsModule } from "@spartacus/storefront";
import { WsCheckoutReviewSubmitComponent } from './ws-review/checkout-review-submit/checkout-review-submit.component';
import { CheckoutrevieworderSummaryComponent } from './ws-review/checkoutrevieworder-summary/checkoutrevieworder-summary.component';
import { WsPlaceOrderComponent } from './ws-review/place-order/place-order.component';
import { CheckoutScheduledReplenishmentPlaceOrderModule } from "@spartacus/checkout/scheduled-replenishment/components";
import { WsCartModule } from "../ws-cart/wsCart.module";
import { CheckoutOrderReviewComponent } from './ws-review/checkout-order-review/checkout-order-review.component';
import { WsOrdersModule } from "../ws-order/ws-orders.module";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { RouterModule, Routes } from "@angular/router";
import { OccConfig } from '@spartacus/core';
import { ValidateAddressComponent } from './validate-address/validate-address.component';
import { WsAddCreditCardModule } from "../ws-add-credit-card/ws-add-credit-card.module";
import { WsSpinnerModule } from "../ws-spartacus/spinner/ws-spinner.module";
import { wsPlaceOrderSpinnerLayoutConfig } from "./checkout-place-order-spinner.config";
import { WsOccOrderAdapter } from "../../ws-services/ws-checkout/ws-order.adapter";
import { OrderAdapter } from "@spartacus/order/core";
import { WsOrderService } from "../../ws-services/ws-checkout/ws-order.service";
import { OrderFacade } from "@spartacus/order/root";
import { WsAccertifyService } from "../../ws-services/ws-checkout/ws-accertify.service";
import { WsSharedModule } from "../ws-shared/ws-shared.module";
import { WsTitleContentModule } from "./ws-review/title-body-content/ws-title-body-content.module";

const routes: Routes = [
  {
    path: 'checkout/PaymentMethods',
    component: WsCheckoutPaymentMethodComponent,
    canActivate: [CmsPageGuard, AuthGuard]
  }
];

export const defaultOccOrderConfig: OccConfig = {
  backend: {
    occ: {
      endpoints: {
        /* eslint-disable max-len */
        orderHistory: 'users/${userId}/orders',
        orderDetail: 'users/${userId}/orders/${orderId}?fields=FULL',
        consignmentTracking:
          'users/${userId}/orders/${orderCode}/consignments/${consignmentCode}/tracking',
        cancelOrder: 'users/${userId}/orders/${orderId}/cancellation',
        returnOrder:
          'users/${userId}/orderReturns?fields=BASIC,returnEntries(BASIC,refundAmount(formattedValue),orderEntry(basePrice(formattedValue),product(name,code,baseOptions,images(DEFAULT,galleryIndex)))),deliveryCost(formattedValue),totalPrice(formattedValue),subTotal(formattedValue)',
        orderReturns: 'users/${userId}/orderReturns?fields=BASIC',
        orderReturnDetail:
          'users/${userId}/orderReturns/${returnRequestCode}?fields=BASIC,returnEntries(BASIC,refundAmount(formattedValue),orderEntry(basePrice(formattedValue),product(name,code,baseOptions,images(DEFAULT,galleryIndex)))),deliveryCost(formattedValue),totalPrice(formattedValue),subTotal(formattedValue)',
        cancelReturn: 'users/${userId}/orderReturns/${returnRequestCode}',
        /* eslint-enable */

        /** scheduled replenishment endpoints start */
        replenishmentOrderDetails:
          'users/${userId}/replenishmentOrders/${replenishmentOrderCode}?fields=FULL,costCenter(FULL),purchaseOrderNumber,paymentType,user',
        replenishmentOrderDetailsHistory:
          'users/${userId}/replenishmentOrders/${replenishmentOrderCode}/orders',
        cancelReplenishmentOrder:
          'users/${userId}/replenishmentOrders/${replenishmentOrderCode}?fields=FULL,costCenter(FULL),purchaseOrderNumber,paymentType,user',
        replenishmentOrderHistory:
          'users/${userId}/replenishmentOrders?fields=FULL,replenishmentOrders(FULL, purchaseOrderNumber)',
        /** scheduled replenishment endpoints end */

        /** placing an order endpoints start **/
        placeOrder: 'users/${userId}/orders?fields=FULL',
        /** placing an order endpoints end **/
      },
    },
  },
};

@NgModule({
    imports: [
        CommonModule,
        I18nModule,
        WsSpinnerModule,
        CheckoutReviewSubmitModule,
        CheckoutScheduledReplenishmentPlaceOrderModule,
        PromotionsModule,
        AtMessageModule,
        WsCartModule,
        FormsModule,
        ReactiveFormsModule,
        RouterModule,
        UrlModule,
        MediaModule,
        PageSlotModule,
        WsOrdersModule,
        WsAddCreditCardModule,
        WsSharedModule,
        WsTitleContentModule,
        RouterModule.forChild(routes),
    ],
    declarations: [WsCheckoutPaymentMethodComponent, WsCheckoutReviewSubmitComponent, CheckoutrevieworderSummaryComponent, WsPlaceOrderComponent, CheckoutOrderReviewComponent, ValidateAddressComponent],
    providers: [
        provideConfig({
            cmsComponents: {
                CheckoutPaymentDetails: {
                    component: WsCheckoutPaymentMethodComponent,
                    guards: [CheckoutAuthGuard, CartNotEmptyGuard],
                },
                CheckoutOrderSummary: {
                    component: CheckoutrevieworderSummaryComponent,
                },
                CheckoutReviewOrder: {
                    component: WsCheckoutReviewSubmitComponent,
                    guards: [CheckoutAuthGuard, CartNotEmptyGuard],
                }
            },
        }),
        provideConfig(wsPlaceOrderSpinnerLayoutConfig),
        provideConfig(defaultOccOrderConfig),
        WsAccertifyService,
        {
          provide: OrderFacade,
          useExisting: WsOrderService,
        },
        {
          provide: OrderAdapter,
          useClass: WsOccOrderAdapter,
        },
    ]
})
export class WsCheckoutPaymentMethod {

}
