import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { WsAutoshipsLandingPageComponent } from './ws-autoships-landing-page/ws-autoships-landing-page.component';
import { Routes, RouterModule } from '@angular/router';
import { AuthGuard } from '@spartacus/core';
import { CmsPageGuard, MediaModule } from '@spartacus/storefront';
import { WsAutoshipsListPageComponent } from './ws-autoships-list-page/ws-autoships-list-page.component';
import { WsAutoshipsListsComponent } from './ws-autoships-list-page/ws-autoships-lists/ws-autoships-lists.component';
import { WsAutoshipsNextShipmentComponent } from './ws-autoships-list-page/ws-autoships-next-shipment/ws-autoships-next-shipment.component';
import { WsSharedModule } from '../ws-shared/ws-shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { WsAutoshipsDetailPageComponent } from './ws-autoships-detail-page/ws-autoships-detail-page.component';
import { WsAutoshipsDetailComponent } from './ws-autoships-detail-page/ws-autoships-detail/ws-autoships-detail.component';
import { WsAutoshipsDetailProductsComponent } from './ws-autoships-detail-page/ws-autoships-detail-products/ws-autoships-detail-products.component';
import { BsDatepickerModule } from 'ngx-bootstrap/datepicker';
import { WsSpinnerModule } from '../ws-spartacus/spinner/ws-spinner.module';
import { WsAutoshipCarouselComponent } from './ws-autoships-list-page/ws-autoship-carousel/ws-autoship-carousel.component';

const appRoute: Routes = [
  {
    path: 'autoships',
    component: WsAutoshipsLandingPageComponent,
    canActivate: [CmsPageGuard, AuthGuard]
  },
  {
    path: 'autoships-list',
    component: WsAutoshipsListPageComponent,
    canActivate: [CmsPageGuard, AuthGuard]
  },
  {
    path: 'autoships-details',
    component: WsAutoshipsDetailPageComponent,
    canActivate: [CmsPageGuard, AuthGuard]
  },
]

@NgModule({
  declarations: [
    WsAutoshipsLandingPageComponent,
    WsAutoshipsListPageComponent,
    WsAutoshipsListsComponent,
    WsAutoshipsNextShipmentComponent,
    WsAutoshipsDetailPageComponent,
    WsAutoshipsDetailComponent,
    WsAutoshipsDetailProductsComponent,
    WsAutoshipCarouselComponent,
  ],
  imports: [
    CommonModule,
    MediaModule,
    WsSharedModule,
    FormsModule,
    ReactiveFormsModule,
    BsDatepickerModule,
    WsSpinnerModule,
    RouterModule.forChild(appRoute),
  ],
  exports: [
    WsAutoshipsLandingPageComponent,
    WsAutoshipsListPageComponent,
    WsAutoshipsListsComponent,
    WsAutoshipsNextShipmentComponent,
    WsAutoshipsDetailPageComponent,
    WsAutoshipCarouselComponent,
  ]
})
export class WsAutoshipsModule { }
