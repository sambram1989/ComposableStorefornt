import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { CmsConfig, I18nModule, OccCmsPageAdapter, provideConfig } from '@spartacus/core';
import { GenericLinkModule, IconModule, MediaModule, NavigationModule, OutletModule, OutletRefModule, PageLayoutModule, PageSlotModule } from '@spartacus/storefront';
import { WsNavigationModule } from '../ws-navigation/ws-navigation.module';
import { RouterModule } from '@angular/router';
import { AlertModule } from 'ngx-bootstrap/alert';
import { WsSharedModule } from '../ws-shared/ws-shared.module';
import { WsPatientPlpComponent } from './ws-patient-plp/ws-patient-plp.component';
import { WsPlpFilterFacetsComponent } from './ws-plp-filter-facets/ws-plp-filter-facets.component';
import { WsPlpFilterMobilefacetsComponent } from './ws-plp-filter-mobilefacets/ws-plp-filter-mobilefacets.component';
import { WsCategoryBannerComponent } from './ws-category-banner/ws-category-banner.component';
import { FormsModule } from '@angular/forms';
import { CmsParagraphModule } from '@spartacus/storefront';
import {  WsOccCmsPageAdapter } from './ws-cms-page-adapter';
import { WsSpinnerModule } from '../ws-spartacus/spinner/ws-spinner.module';

@NgModule({
  declarations: [WsPatientPlpComponent, WsPlpFilterFacetsComponent, WsPlpFilterMobilefacetsComponent, WsCategoryBannerComponent],
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
    FormsModule,
    CmsParagraphModule,
    WsSpinnerModule
  ],
  providers: [
    provideConfig(<CmsConfig>{
      cmsComponents: {
        CMSProductListComponent: {
          component: WsPatientPlpComponent
        },
        ProductRefinementComponent: {
          component: WsPlpFilterFacetsComponent
        },
        ProductListingPageCategoryflexComponent: {
          component: WsCategoryBannerComponent
        }
      },
    }),
    {
      provide: OccCmsPageAdapter,
      useClass: WsOccCmsPageAdapter,
    },
  ],
  exports: [WsPatientPlpComponent],
})
export class WsPlpModule {}
