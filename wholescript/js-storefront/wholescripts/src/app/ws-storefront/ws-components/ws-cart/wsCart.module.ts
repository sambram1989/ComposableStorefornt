import { NgModule } from "@angular/core";
import { CartboxstyleComponent } from './cartboxstyle/cartboxstyle.component';
import { CartComponent } from './cart/cart.component';
import { PromocodeComponent } from './promocode/promocode.component';
import { OrdertotalComponent } from './ordertotal/ordertotal.component';
import { CartCardDetailsComponent } from './cart-card-details/cart-card-details.component';
import { CmsConfig, I18nModule, provideConfig, provideDefaultConfigFactory, UrlModule } from "@spartacus/core";
import { CartCouponModule, CartDetailsModule, CartProceedToCheckoutModule, CartSharedModule, CartValidationWarningsModule } from "@spartacus/cart/base/components";
import { CommonModule } from "@angular/common";
import { RouterModule } from "@angular/router";
import { AtMessageModule, IconModule, ItemCounterModule, MediaModule, OutletModule, PageSlotModule, ProgressButtonModule, PromotionsModule, provideOutlet } from "@spartacus/storefront";
import { WsCartItemListComponent } from './ws-cart-item-list/ws-cart-item-list.component';
import { ADD_TO_CART_FEATURE, CART_BASE_CORE_FEATURE, CART_BASE_FEATURE, CartOutlets, MINI_CART_FEATURE } from "@spartacus/cart/base/root";
import { WsCartItemComponent } from './cart-item/cart-item.component';
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { AddToCartModule } from "@spartacus/cart/base/components/add-to-cart";
import { HasMedpaxPipe, WsCartItemListRowComponent } from "./cart-item-list-row/cart-item-list-row.component";
import { WsSharedModule } from "../ws-shared/ws-shared.module";
import { ProceedToCheckoutComponent } from './proceed-to-checkout/proceed-to-checkout.component';
import { CartShippingAddressComponent } from "./cart-shipping-address/cart-shipping-address.component";
import { MedpaxEntryCartComponent, TotalEntriesCheckPipe } from './medpax-entry-cart/medpax-entry-cart.component';
import { WsCartItemCounterComponent } from './ws-cart-item-counter/ws-cart-item-counter.component';
import { CartHeadingComponent } from './cart-heading/cart-heading.component';
import { WsTitleContentModule } from "../ws-checkout/ws-review/title-body-content/ws-title-body-content.module";
import { WsSpinnerModule } from "../ws-spartacus/spinner/ws-spinner.module";
import { BrandAgreementPopupComponent } from './brand-agreement-popup/brand-agreement-popup.component';
import { SafeUrlPipe } from "./safe-url.pipe";
import { CartItemsRemovalModalComponent } from './cart-items-removal-modal/cart-items-removal-modal.component';

export function defaultCartComponentsConfig() {
  const config = {
    featureModules: {
      [CART_BASE_FEATURE]: {
        cmsComponents: [
          'CartApplyCouponComponent',
          'CartComponent',
          'CartProceedToCheckoutComponent',
          'CartTotalsComponent',
        ],
      },
      [MINI_CART_FEATURE]: {
        cmsComponents: ['MiniCartComponent'],
      },
      [ADD_TO_CART_FEATURE]: {
        cmsComponents: ['ProductAddToCartComponent'],
      },
      [CART_BASE_CORE_FEATURE]: CART_BASE_FEATURE,
    },
  };
  return config;
}
@NgModule({
  imports: [
    CommonModule,
    ReactiveFormsModule,
    CartDetailsModule,
    CartValidationWarningsModule,
    CartSharedModule,
    CartCouponModule,
    PromotionsModule,
    AddToCartModule,
    ProgressButtonModule,
    CartProceedToCheckoutModule,
    RouterModule,
    UrlModule,
    I18nModule,
    MediaModule,
    OutletModule,
    AtMessageModule,
    IconModule,
    FormsModule,
    ReactiveFormsModule,
    WsSharedModule,
    PageSlotModule,
    ItemCounterModule,
    WsTitleContentModule,
    WsSpinnerModule
  ],
  declarations: [
    CartboxstyleComponent,
    CartComponent,
    PromocodeComponent,
    OrdertotalComponent,
    CartCardDetailsComponent,
    WsCartItemListComponent,
    WsCartItemComponent,
    WsCartItemListRowComponent,
    ProceedToCheckoutComponent,
    CartShippingAddressComponent,
    MedpaxEntryCartComponent,
    HasMedpaxPipe,
    TotalEntriesCheckPipe,
    WsCartItemCounterComponent,
    CartHeadingComponent,
    BrandAgreementPopupComponent,
    SafeUrlPipe,
    CartItemsRemovalModalComponent
  ],
  providers: [provideConfig(<CmsConfig>{
    cmsComponents: {
      CartComponent: {
        component: CartComponent,
      },
      CartTotalsComponent: {
        component: OrdertotalComponent,
      },
    },
  }),
  provideOutlet({
    id: CartOutlets.CART_ITEM_LIST,
    component: WsCartItemListComponent,
  }),
  provideDefaultConfigFactory(defaultCartComponentsConfig)],
  exports: [CartComponent, WsCartItemListComponent,MedpaxEntryCartComponent,CartHeadingComponent, CartItemsRemovalModalComponent]
})
export class WsCartModule {

}

