import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { CmsConfig, ConfigModule, I18nModule, provideConfig } from '@spartacus/core';
import { GenericLinkModule, IconModule, MediaModule, NavigationModule, NavigationService, OutletModule, OutletRefModule, PageLayoutModule, PageSlotModule } from '@spartacus/storefront';
import { RouterModule } from '@angular/router';
import { WsNavigationService } from '../../ws-services/ws-navigation.service';
import { WsFooterNavigationComponent } from './ws-footer-navigation/ws-footer-navigation.component';
import { wsDisclaimerFlexCdcComponent } from './ws-gigya-raas-flex-cdc/ws-gigya-raas-flex-cdc.component';

@NgModule({
  declarations: [WsFooterNavigationComponent, wsDisclaimerFlexCdcComponent],
  imports: [
    CommonModule,
    NavigationModule,
    GenericLinkModule,
    IconModule,
    I18nModule,
    MediaModule,
    OutletModule,
    OutletRefModule,
    PageLayoutModule,
    PageSlotModule,
    RouterModule,
  ],
    providers: [
      provideConfig(<CmsConfig>{
        cmsComponents: {
          FooterNavigationComponent: {
            component: WsFooterNavigationComponent,
          },
          wsDisclaimerFlex: {
            component: wsDisclaimerFlexCdcComponent,
          },
        },
      }),
      {provide: NavigationService, useClass: WsNavigationService}
    ],
  exports: [],
})
export class WsFooterModule {}
