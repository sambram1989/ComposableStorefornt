import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { WsOrderDetailsComponent } from './ws-order-details/ws-order-details.component';
import { AuthGuard, CmsConfig, provideConfig } from '@spartacus/core';
import { RouterModule, Routes } from '@angular/router';
import { MediaModule, PageSlotModule } from '@spartacus/storefront';
import { WsSpinnerModule } from '../ws-spartacus/spinner/ws-spinner.module';
import { OrderItemsRemovalModalComponent } from './order-items-removal-modal/order-items-removal-modal.component';

const routes: Routes = [
  {
    path: 'order/details/:salesOrder',
    component: WsOrderDetailsComponent,
  },
];

@NgModule({
  declarations: [
    WsOrderDetailsComponent,
    OrderItemsRemovalModalComponent
  ],
  imports: [
    CommonModule,
    RouterModule.forChild(routes),
    PageSlotModule,
    WsSpinnerModule,
    MediaModule,
  ],
  providers: [
    provideConfig(<CmsConfig>{
      cmsComponents: {
        wsOrderDetailsRightBodyFlex : {
          component: WsOrderDetailsComponent,
          guards: [AuthGuard],
        },
      },
    }),
  ],
})
export class WsOrderDetailsModule { }
