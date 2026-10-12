import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { CmsConfig, I18nModule, provideConfig } from '@spartacus/core';
import { CmsPageGuard, GenericLinkModule, IconModule, MediaModule, NavigationModule, OutletModule, OutletRefModule, PageLayoutModule, PageSlotModule } from '@spartacus/storefront';
import { WsNavigationModule } from '../ws-navigation/ws-navigation.module';
import { RouterModule, Routes } from '@angular/router';
import { AlertModule } from 'ngx-bootstrap/alert';
import { WsSharedModule } from '../ws-shared/ws-shared.module';
import { WsAboutUsComponent } from './ws-about-us.component';
import { FormsModule } from '@angular/forms';


const routes: Routes = [
    {
        path: 'about-us',
        canActivate: [CmsPageGuard],
        component: WsAboutUsComponent,
    }
]

@NgModule({
  declarations: [WsAboutUsComponent],
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
    RouterModule.forChild(routes),
    FormsModule
  ],
  exports: [WsAboutUsComponent],
})
export class WsAboutUsModule {}
