import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { CmsConfig, I18nModule, provideConfig } from '@spartacus/core';
import { GenericLinkModule, IconModule, MediaModule, NavigationModule, OutletModule, OutletRefModule, PageLayoutModule, PageSlotModule } from '@spartacus/storefront';
import { WsNavigationModule } from '../ws-navigation/ws-navigation.module';
import { RouterModule } from '@angular/router';
import { AlertModule } from 'ngx-bootstrap/alert';
import { WsSharedModule } from '../ws-shared/ws-shared.module';
import { WsFavoritesListComponent } from './ws-favorites.component';
import { FormsModule } from '@angular/forms';

@NgModule({
  declarations: [WsFavoritesListComponent],
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
    WsSharedModule,
    WsNavigationModule,
    RouterModule,
    FormsModule
  ],
  providers: [
    provideConfig(<CmsConfig>{
      cmsComponents: {
        MyFavoritesProductListFlex: {
          component: WsFavoritesListComponent
        },
      },
    }),
  ],
  exports: [WsFavoritesListComponent],
})
export class WsFavoritesModule {}
