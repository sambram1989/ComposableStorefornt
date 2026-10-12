import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { WsCheckoutDeliveryAddressComponent } from './ws-checkout-delivery-address/ws-checkout-delivery-address.component';
import { RouterModule, Routes } from '@angular/router';
import { CmsPageGuard, MediaModule, PageSlotModule } from '@spartacus/storefront';
import { provideConfig, UrlModule } from '@spartacus/core';
import { CheckoutStepType } from '@spartacus/checkout/base/root';
import { WsCheckoutDeliveryNewAddressComponent } from './ws-checkout-delivery-new-address/ws-checkout-delivery-new-address.component';
import { WsCheckoutDeliveryValidateAddressComponent } from './ws-checkout-delivery-validate-address/ws-checkout-delivery-validate-address.component';
import { WsCheckoutDeliveryAddressPaginationComponent } from './ws-checkout-delivery-address-pagination/ws-checkout-delivery-address-pagination.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { WsOrdersModule } from '../ws-order/ws-orders.module';
import { WsSpinnerModule } from '../ws-spartacus/spinner/ws-spinner.module';
import { WsSharedModule } from '../ws-shared/ws-shared.module';
import { WsCheckoutDeliveryAddressAgeRestrictionComponent } from './ws-checkout-delivery-address-age-restriction/ws-checkout-delivery-address-age-restriction.component';
import { WsCheckoutDeliveryAddressRemoveItemModalsComponent } from './ws-checkout-delivery-address-remove-item-modals/ws-checkout-delivery-address-remove-item-modals.component';
import { WsCheckoutDeliveryAddressIdVerificationComponent } from './ws-checkout-delivery-address-age-restriction/ws-id-verification/ws-checkout-delivery-address-id-verification.component';
import { WsCheckoutDeliveryAddressVerificationPopupOneComponent } from './ws-checkout-delivery-address-age-restriction/ws-id-verification/ws-verification-popup-one/ws-checkout-delivery-address-verify-popup-one.component';

const routes: Routes = [
  {
    path: 'checkout/ShippingAddresses',
    component: WsCheckoutDeliveryAddressComponent,
    canActivate: [CmsPageGuard]
  }
];

@NgModule({
  declarations: [
    WsCheckoutDeliveryAddressComponent,
    WsCheckoutDeliveryNewAddressComponent,
    WsCheckoutDeliveryValidateAddressComponent,
    WsCheckoutDeliveryAddressPaginationComponent,
    WsCheckoutDeliveryAddressAgeRestrictionComponent,
    WsCheckoutDeliveryAddressRemoveItemModalsComponent,
    WsCheckoutDeliveryAddressIdVerificationComponent,
    WsCheckoutDeliveryAddressVerificationPopupOneComponent
  ],
  imports: [
    CommonModule,
    PageSlotModule,
    FormsModule,
    ReactiveFormsModule,
    WsOrdersModule,
    WsSpinnerModule,
    WsSharedModule,
    MediaModule,
    UrlModule,
    RouterModule.forChild(routes)
  ],
  exports: [
    WsCheckoutDeliveryAddressComponent,
    WsCheckoutDeliveryNewAddressComponent,
    WsCheckoutDeliveryValidateAddressComponent,
    WsCheckoutDeliveryAddressPaginationComponent,
    WsCheckoutDeliveryAddressRemoveItemModalsComponent,
    WsCheckoutDeliveryAddressAgeRestrictionComponent,
    WsCheckoutDeliveryAddressIdVerificationComponent,
    WsCheckoutDeliveryAddressVerificationPopupOneComponent
  ],
  providers: [provideConfig({
    checkout: {
      steps: [
        {
          id: 'deliveryAddress',
          name: 'checkoutProgress.deliveryAddress',
          routeName: 'checkoutDeliveryAddress',
          type: [CheckoutStepType.DELIVERY_ADDRESS]
        },
        {
          id: 'deliveryMode',
          name: 'checkoutProgress.deliveryMode',
          routeName: 'checkoutDeliveryMode',
          type: [CheckoutStepType.DELIVERY_MODE],
        },
        {
          id: 'paymentDetails',
          name: 'checkoutProgress.paymentDetails',
          routeName: 'checkoutPaymentDetails',
          type: [CheckoutStepType.PAYMENT_DETAILS],
        },
        {
          id: 'reviewOrder',
          name: 'checkoutProgress.reviewOrder',
          routeName: 'checkoutReviewOrder',
          type: [CheckoutStepType.REVIEW_ORDER],
        },
      ],
    },
  })]
})
export class WsCheckoutDeliveryAddressModule { }
