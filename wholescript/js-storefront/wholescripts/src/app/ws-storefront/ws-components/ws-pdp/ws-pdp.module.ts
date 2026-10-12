import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { WsPdpOverviewComponent } from './ws-pdp-overview/ws-pdp-overview.component';
import { WsPdpProductFamilyComponent } from './ws-pdp-product-family/ws-pdp-product-family.component';
import { FeaturesConfigModule, I18nModule, ProductModule, provideConfig, UrlModule } from '@spartacus/core';
import { ShopComponentModule } from '../shop/components/shop-component.module';
import { IconModule, ItemCounterModule, KeyboardFocusModule, MediaModule, OutletModule, PageSlotModule, ProductImagesModule,CarouselModule as cxCarousel} from '@spartacus/storefront';
import { WsProductSummaaryComponent } from './ws-product-summaary/ws-product-summaary.component';
import { AddToCartModule } from '@spartacus/cart/base/components/add-to-cart';
import { RouterModule } from '@angular/router';
import { ProductImageZoomModule } from '@spartacus/product/image-zoom';
import { WsAddToCartComponent } from './ws-add-to-cart/ws-add-to-cart.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { WsProductIntroComponent } from './ws-product-intro/ws-product-intro.component';
import { WsFormularedToExcludeComponent } from './ws-formulared-to-exclude/ws-formulared-to-exclude.component';
import { WsProductSummaryBottomComponent } from './ws-product-summary-bottom/ws-product-summary-bottom.component';
import { wsPDPOccConfig } from './ws-pdp-endpoints.config';
import { AccordionModule } from 'ngx-bootstrap/accordion';
import { AlertModule } from 'ngx-bootstrap/alert';
import { CarouselModule } from 'ngx-bootstrap/carousel';
import { WsSpinnerModule } from '../ws-spartacus/spinner/ws-spinner.module';
import { WsProductDetailsPopupComponent } from './ws-product-details-popup/ws-product-details-popup.component';
import { TabsModule } from 'ngx-bootstrap/tabs';
import { WsProductImageZoomProductImagesComponent } from './ws-product-image-zoom-product-images-component/ws-product-image-zoom-product-images-component.component';
import { WsZoomTriggerComponent } from './ws-zoom-trigger/ws-zoom-trigger.component';
import { WsSharedModule } from '../ws-shared/ws-shared.module';
import { SlickCarouselModule } from 'ngx-slick-carousel';

@NgModule({
  declarations: [
    WsPdpOverviewComponent,
    WsProductSummaaryComponent,
    WsAddToCartComponent,
    WsProductIntroComponent,
    WsFormularedToExcludeComponent,
    WsProductSummaryBottomComponent,
    WsPdpProductFamilyComponent,
    WsProductDetailsPopupComponent,
    WsProductImageZoomProductImagesComponent,
    WsZoomTriggerComponent
  ],
  imports: [
    CommonModule,
    ShopComponentModule,
    I18nModule,
    OutletModule,
    FeaturesConfigModule,
    MediaModule,
    cxCarousel,
    RouterModule,
    ProductImagesModule,
    ProductImageZoomModule,
    AddToCartModule,
    ItemCounterModule,
    IconModule,
    ReactiveFormsModule,
    IconModule,
    KeyboardFocusModule,
    AccordionModule,
    FeaturesConfigModule,
    PageSlotModule,
    AlertModule,
    AddToCartModule,
    MediaModule,
    UrlModule,
    FormsModule,
    ReactiveFormsModule,
    WsSpinnerModule,
    WsSharedModule,
    TabsModule.forRoot(),
    CarouselModule.forRoot(),
    SlickCarouselModule
  ],
  
  providers: [
    provideConfig({
      cmsComponents: {

        ProductIntroComponent: {
          component: () =>
            import('./ws-product-intro/ws-product-intro.component').then(
              (m) => m.WsProductIntroComponent)
        },

        ProductSummaryComponent: {
          component: () =>
            import('./ws-product-summaary/ws-product-summaary.component').then(
              (m) => m.WsProductSummaaryComponent)
        },

        ProductAddToCartComponent: {
          component: () =>
            import('./ws-add-to-cart/ws-add-to-cart.component').then(
              (m) => m.WsAddToCartComponent)
        },

        wsPatientPDPPageProductOverviewflex: {
          component: () =>
            import('./ws-pdp-overview/ws-pdp-overview.component').then(
              (m) => m.WsPdpOverviewComponent)
        },

        wsformulatedImageFlex: {
          component: () =>
            import('./ws-formulared-to-exclude/ws-formulared-to-exclude.component').then(
              (m) => m.WsFormularedToExcludeComponent)
        },

        wsProductSummaryBottomFlex: {
          component: () =>
            import('./ws-product-summary-bottom/ws-product-summary-bottom.component').then(
              (m) => m.WsProductSummaryBottomComponent)
        },

        wsPatientPDPPageProductFamilyflex: {
          component: () =>
            import('./ws-pdp-product-family/ws-pdp-product-family.component').then(
              (m) => m.WsPdpProductFamilyComponent)
        },
          ProductImagesComponent: {
            component: () => import('./ws-product-image-zoom-product-images-component/ws-product-image-zoom-product-images-component.component').then(
              (m) => m.WsProductImageZoomProductImagesComponent),
          },
      },
    }),
    provideConfig(wsPDPOccConfig)
  ],
})
export class WsPdpModule { }
