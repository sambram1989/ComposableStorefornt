import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { CmsConfig, ConfigModule, I18nModule, provideConfig, provideDefaultConfig } from '@spartacus/core';
import { GenericLinkModule, IconModule, MediaModule, NavigationModule, NavigationService, OutletModule, OutletRefModule, PageLayoutModule, PageSlotModule } from '@spartacus/storefront';
import { WsNavigationComponent } from './ws-navigation.component';
import { WsAccountNavigationUIComponent } from './account/ws-account-navigation.component';
import { WsAdvertisingNavigationUIComponent } from './ws-advertising-links/ws-advertising-links.component';
import { RouterModule } from '@angular/router';
import { WsNavigationService } from '../../ws-services/ws-navigation.service';
import { WsCategoryNavigationComponent } from './ws-category-navigation/ws-category-navigation.component';

@NgModule({
  declarations: [WsNavigationComponent, WsAccountNavigationUIComponent, WsAdvertisingNavigationUIComponent, WsCategoryNavigationComponent],
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
        NavigationComponent: {
          component: WsNavigationComponent,
        },
        NewBrandflexComponent: {
          component: WsAdvertisingNavigationUIComponent,
        },
        PromoBannersflexComponent: {
          component: WsAdvertisingNavigationUIComponent,
        },
        CategoryNavigationComponent: {
          component: WsCategoryNavigationComponent
        }
      },
    }),
    {provide: NavigationService, useClass: WsNavigationService}
  ],
  exports: [WsNavigationComponent, WsAccountNavigationUIComponent, WsAdvertisingNavigationUIComponent],
})
export class WsNavigationModule {}
