import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { AuthGuard, ConfigModule, I18nModule, UrlModule } from '@spartacus/core';
import { CmsPageGuard, IconModule, MediaModule, OutletModule, OutletRefModule, PageLayoutModule, PageSlotModule, PromotionsModule, SpinnerModule } from '@spartacus/storefront';
import { RouterModule, Routes } from '@angular/router';
import { AlertModule } from 'ngx-bootstrap/alert';
import { WsSharedModule } from '../ws-shared/ws-shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { CmsParagraphModule } from '@spartacus/storefront';
import { WsCheckoutConfirmationComponent } from './ws-checkout-confirmation/ws-checkout-confirmation.component';
import { WsShippingMethodComponent } from './ws-shipping-method/ws-shipping-method.component';
import { CheckoutAuthGuard } from '@spartacus/checkout/base/components';
import { WsNewCreditCardComponent } from './ws-new-creditcard-modal/ws-new-creditcard-modal.component';
import { PaymentPageAddressValidationPopupComponent } from './payment-page-address-validation-popup/payment-page-address-validation-popup.component';
import { WsCartModule } from '../ws-cart/wsCart.module';
import { WsSpinnerModule } from '../ws-spartacus/spinner/ws-spinner.module';
import { OrderConfirmationProductCardComponent } from './ws-checkout-confirmation/order-confirmation-product-card/order-confirmation-product-card.component';

const routes: Routes = [
  {
    path: 'checkout/ShippingMethods',
    component: WsShippingMethodComponent,
    canActivate: [CmsPageGuard, AuthGuard]
  }
];

@NgModule({
  declarations: [WsCheckoutConfirmationComponent, WsShippingMethodComponent, WsNewCreditCardComponent, PaymentPageAddressValidationPopupComponent, OrderConfirmationProductCardComponent],
  imports: [
    CommonModule,
    IconModule,
    I18nModule,
    UrlModule,
    MediaModule,
    AlertModule,
    OutletModule,
    OutletRefModule,
    PageLayoutModule,
    PageSlotModule,
    WsSharedModule,
    RouterModule,
    FormsModule,
    CmsParagraphModule,
    ReactiveFormsModule,
    WsSpinnerModule,
    WsCartModule,
    PromotionsModule,
    RouterModule.forChild(routes),
    ConfigModule.withConfig({
      cmsComponents: {
        OrderConfirmationItemsComponent: {
          component: WsCheckoutConfirmationComponent,
        },
      },
    }),
  ],
  exports: [WsShippingMethodComponent, WsNewCreditCardComponent],
})
export class WsOrdersModule {}
