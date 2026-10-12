import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AuthGuard, CmsConfig, I18nModule, provideConfig, provideDefaultConfig, provideDefaultConfigFactory, UrlModule } from '@spartacus/core';
import { FormsModule } from '@angular/forms';
import { RouterModule, Routes } from '@angular/router';
import { NgSelectModule } from '@ng-select/ng-select';
import { ListNavigationModule, MediaModule, PageSlotModule, CmsPageGuard } from '@spartacus/storefront';
import { WsSharedModule } from '../ws-shared/ws-shared.module';
import { BsDatepickerModule } from 'ngx-bootstrap/datepicker';
import { WsOrderHistoryComponent } from './ws-order-history/ws-order-history.component';
import { WsOrderInvoiceComponent } from './ws-order-invoice/ws-order-invoice.component';
import { WsDefaultOrderRoutingConfig } from './config/ws-default-order.routing.config';
import { WsOrderHistoryAgingComponent } from './ws-order-aging/ws-order-aging.component';
import { WsOrderHistoryTabsComponent } from './ws-order-history-tabs/ws-order-history-tabs.component';
import { WsSpinnerModule } from '../ws-spartacus/spinner/ws-spinner.module';

const appRoute: Routes = [
  {
    path: 'order/history',
    component: WsOrderHistoryTabsComponent,
    canActivate: [CmsPageGuard, AuthGuard],
    children: [
      {
        path: '',
        component: WsOrderHistoryComponent,
        data: {
          pageLabel: '/order/history/all-orders',
          cxPath: 'order/history',
        }
      },
      {
        path: 'all-orders',
        component: WsOrderHistoryComponent,
        data: {
          pageLabel: '/order/history/all-orders',
          cxPath: 'order/history',
        }
      },
      {
        path: 'invoices',
        component: WsOrderInvoiceComponent,
        data: {
          pageLabel: '/order/history/invoices',
          cxPath: 'order/history',
        }
      },
      {
        path: 'aging',
        component: WsOrderHistoryAgingComponent,
        data: {
          pageLabel: '/order/history/aging',
          cxPath: 'order/history',
        }
      },
  ],
  },
];


@NgModule({
  declarations: [ WsOrderHistoryComponent, WsOrderInvoiceComponent, WsOrderHistoryAgingComponent, WsOrderHistoryTabsComponent],

  imports: [
    CommonModule,
    RouterModule,
    FormsModule,
    NgSelectModule,
    ListNavigationModule,
    UrlModule,
    I18nModule,
    WsSpinnerModule,
    MediaModule,
    WsSharedModule,
    BsDatepickerModule,
    PageSlotModule,
    RouterModule.forChild(appRoute)
  ],
  providers: [
    provideConfig(<CmsConfig>{
      cmsComponents: {
        wsOrderHistoryFullBodyFlex : {
          component: WsOrderHistoryComponent,
          guards: [AuthGuard],
        },
      },
    }),
    provideConfig(WsDefaultOrderRoutingConfig)
  ],
  exports: [],
})
export class WsOrderHistoryModule { }
