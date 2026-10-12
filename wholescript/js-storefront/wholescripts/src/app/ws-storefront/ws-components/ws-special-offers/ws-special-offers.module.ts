import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { WsSpecialOffersComponent } from './ws-special-offers/ws-special-offers.component';
import { RouterModule, Routes } from '@angular/router';
import { CmsPageGuard, PageSlotModule } from '@spartacus/storefront';
import { AlertModule } from 'ngx-bootstrap/alert';
import { WsSharedModule } from '../ws-shared/ws-shared.module';
import { WsSpinnerModule } from '../ws-spartacus/spinner/ws-spinner.module';



const routes: Routes = [
  {
    path: 'specialoffers',
    component: WsSpecialOffersComponent,
    canActivate: [CmsPageGuard]
  }
];


@NgModule({
  declarations: [
    WsSpecialOffersComponent
  ],
  imports: [
    CommonModule,
    PageSlotModule,
    AlertModule,
    WsSpinnerModule,
    WsSharedModule,
    RouterModule.forChild(routes),
  ],
  exports: [
    WsSpecialOffersComponent
  ]
})
export class WsSpecialOffersModule { }
