import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { CmsConfig, I18nModule, provideConfig } from '@spartacus/core';
import { GenericLinkModule, IconModule, MediaModule, NavigationModule, OutletModule, OutletRefModule, PageLayoutModule, PageSlotModule } from '@spartacus/storefront';
import { WsAdvertisingPageComponent } from './ws-advertising-landing/ws-advertising-page.component';
import { WsNavigationModule } from '../ws-navigation/ws-navigation.module';
import { WsAdvertisingSubPagesComponent } from './ws-advertising-subpages/ws-advertising-subpages.component';
import { WsApiSubPagesComponent } from './ws-api-subpages/ws-api-subpages.component';
import { RouterModule } from '@angular/router';
import { AlertModule } from 'ngx-bootstrap/alert';

@NgModule({
  declarations: [WsAdvertisingPageComponent, WsAdvertisingSubPagesComponent, WsApiSubPagesComponent],
  imports: [
    CommonModule,
    NavigationModule,
    GenericLinkModule,
    IconModule,
    I18nModule,
    MediaModule,
    AlertModule,
    OutletModule,
    OutletRefModule,
    PageLayoutModule,
    PageSlotModule,
    WsNavigationModule,
    RouterModule
  ],
  providers: [
    provideConfig(<CmsConfig>{
      cmsComponents: {
        NewBrandflexComponent: {
          component: WsAdvertisingSubPagesComponent,
        },
        PromoBannersflexComponent: {
          component: WsAdvertisingSubPagesComponent,
        },
        IntroflexComponent: {
          component: WsAdvertisingPageComponent,
        },
        EMRRecommendationsflexComponent: {
          component: WsApiSubPagesComponent
        },
        ExternalOrderSubmissionflexComponent: {
          component: WsApiSubPagesComponent
        },
        APIflexComponent: {
          component: WsAdvertisingPageComponent
        }
      },
    }),
  ],
  exports: [],
})
export class WsAdvertisingModule {}
