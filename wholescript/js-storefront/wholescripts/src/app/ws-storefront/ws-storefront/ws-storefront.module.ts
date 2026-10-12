import {
  GlobalMessageComponentModule,
  KeyboardFocusModule,
  OutletModule,
  OutletRefModule,
  PageLayoutModule,
  PageSlotModule,
  SkipLinkModule,
} from '@spartacus/storefront';
import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { WsStorefrontComponent } from './ws-storefront.component';
import { UrlModule } from '@spartacus/core';
import { MobileHomeLogoComponent } from '../ws-components/mobile-home-logo/mobile-home-logo.component';
import { LoginModule } from '../ws-components/user-account/components/login/login.module';
import { WsBreadcrumbModule } from "../ws-components/ws-spartacus/ws-breadcrumb/breadcrumb/ws-breadcrumb.module";
import { FreeShippingBannerComponent } from '../ws-components/free-shipping-banner/free-shipping-banner.component';
import { WsSpinnerModule } from '../ws-components/ws-spartacus/spinner/ws-spinner.module';


@NgModule({
  declarations: [WsStorefrontComponent, MobileHomeLogoComponent , FreeShippingBannerComponent,],
  exports: [WsStorefrontComponent, MobileHomeLogoComponent , FreeShippingBannerComponent,],
  imports: [
    CommonModule,
    OutletModule,
    OutletRefModule,
    PageLayoutModule,
    PageSlotModule,
    RouterModule,
    SkipLinkModule,
    GlobalMessageComponentModule,
    KeyboardFocusModule,
    UrlModule,
    LoginModule,
    WsSpinnerModule
],
})
export class WsStorefrontModule {}
