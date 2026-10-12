import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { AuthGuard, provideConfig } from '@spartacus/core';
import { CmsPageGuard, PageSlotModule } from '@spartacus/storefront';
import { WsIntegrationsTabsComponent } from './ws-integrations-tabs/ws-integrations-tabs.component';
import { WsEStoreComponent } from './ws-e-store/ws-e-store.component';
import { RouterModule, Routes } from '@angular/router';
import { WsEStoreAgreementComponent } from './ws-e-store-agreement/ws-e-store-agreement.component';
import { WsEStoreIntegrationComponent } from './ws-e-store-integration/ws-e-store-integration.component';
import { WsEditCredentialsComponent } from './ws-e-store/ws-edit-credentials/ws-edit-credentials.component';
import { WsUpdatePaymentMethodComponent } from './ws-e-store/ws-update-payment-method/ws-update-payment-method.component';
import { WsSharedModule } from '../ws-shared/ws-shared.module';
import { MediaModule } from '@spartacus/storefront';
import { WsShopifyCatalogComponent } from './ws-shopify-catalog/ws-shopify-catalog.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { AlertModule } from 'ngx-bootstrap/alert';
import { WsShopifyFacetsComponent } from './ws-shopify-facets/ws-shopify-facets.component';
import { WsShopifySettingsComponent } from './ws-shopify-settings/ws-shopify-settings.component';
import { WsSpinnerModule } from '../ws-spartacus/spinner/ws-spinner.module';
import { WsWebKitDetailsComponent } from './ws-e-store/ws-webkit-details/ws-webkit-details.component';
import { WsEstoreRedirectComponent } from './ws-e-store/ws-estore-redirect/ws-estore-redirect.component';
import { WsShopifyCatalogListViewComponent } from './ws-shopify-catalog-list-view/ws-shopify-catalog-list-view.component';
import { WsShopifyCatalogGridViewComponent } from './ws-shopify-catalog-grid-view/ws-shopify-catalog-grid-view.component';
import { WsShopifyMobileFacetsComponent } from './ws-shopify-mobile-facets/ws-shopify-mobile-facets.component';
import { CdkAccordionModule } from '@angular/cdk/accordion';

const appRoute: Routes = [
  {
    path: 'integrations-hub',
    component: WsIntegrationsTabsComponent,
    canActivate: [CmsPageGuard, AuthGuard],
    children: [
      {
        path: '',
        component: WsEStoreComponent,
        data: {
          pageLabel: '/integrations-hub/estore-settings',
          cxPath: 'integrations-hub',
        },
      },
      {
        path: 'estore-settings',
        component: WsEStoreComponent,
        data: {
          pageLabel: '/integrations-hub/estore-settings',
          cxPath: 'integrations-hub',
        },
      },
      {
        path: 'Shopify-settings',
        component: WsShopifySettingsComponent,
        data: {
          pageLabel: '/integrations-hub/Shopify-settings',
          cxPath: 'integrations-hub',
        },
      },
    ],
  },
];

@NgModule({
  imports: [
    CommonModule,
    PageSlotModule,
    WsSharedModule,
    FormsModule,
    ReactiveFormsModule,
    MediaModule,
    AlertModule,
    WsSpinnerModule,
    CdkAccordionModule,
    RouterModule.forChild(appRoute),
  ],
  declarations: [
    WsIntegrationsTabsComponent,
    WsEStoreComponent,
    WsEStoreAgreementComponent,
    WsEStoreIntegrationComponent,
    WsEditCredentialsComponent,
    WsUpdatePaymentMethodComponent,
    WsShopifySettingsComponent,
    WsShopifyCatalogComponent,
    WsShopifyFacetsComponent,
    WsWebKitDetailsComponent,
    WsEstoreRedirectComponent,
    WsShopifyCatalogListViewComponent,
    WsShopifyCatalogGridViewComponent,
    WsShopifyMobileFacetsComponent
  ],
  providers: [
    provideConfig({
      cmsComponents: {
        ShoppifySettingsFlexComponent: {
          component: WsIntegrationsTabsComponent,
        },
        ShopifyFlexComponent: {
          component: WsShopifyCatalogComponent,
        },
        SelectCreditCardFlexComponent: {
          component: WsEStoreAgreementComponent,
        },
      },
    }),
  ],
  exports: [WsIntegrationsTabsComponent, WsEstoreRedirectComponent],
})
export class WsIntegrationsHubModule {}
