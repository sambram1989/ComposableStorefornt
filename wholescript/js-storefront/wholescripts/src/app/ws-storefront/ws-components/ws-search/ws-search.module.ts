import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { WsHeaderSearchComponent } from './ws-header-search/ws-header-search.component';
import { WsSearchPageComponent } from './ws-search-page/ws-search-page.component';
import { provideDefaultConfig, CmsConfig, I18nModule, UrlModule, provideConfig } from '@spartacus/core';
import { CmsPageGuard, IconModule, MediaModule, PageSlotModule } from '@spartacus/storefront';
import { RouterModule, Routes } from '@angular/router';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';
import { BaseStorefrontModule } from '@spartacus/storefront'; // Import StorefrontModule
import { HighlightPipe } from '../../ws-pipes/highlight.pipe';
import { WsSearchPageProductsComponent } from './ws-search-page/ws-search-page-products/ws-search-page-products.component';
import { WsSearchPageBrandsComponent } from './ws-search-page/ws-search-page-brands/ws-search-page-brands.component';
import { WsSearchPageCategoriesComponent } from './ws-search-page/ws-search-page-categories/ws-search-page-categories.component';
import { WsSearchPageIntegrationsComponent } from './ws-search-page/ws-search-page-integrations/ws-search-page-integrations.component';
import { WsSearchPageMagazinesComponent } from './ws-search-page/ws-search-page-magazines/ws-search-page-magazines.component';
import { WsSearchPageFaqsComponent } from './ws-search-page/ws-search-page-faqs/ws-search-page-faqs.component';
import { WsSearchPageCommonHeadingComponent } from './ws-search-page/ws-search-page-common-heading/ws-search-page-common-heading.component';
import { WsSearchPageCommonPaginationComponent } from './ws-search-page/ws-search-page-common-pagination/ws-search-page-common-pagination.component';
import { WsSearchPagePrivateLabelComponent } from './ws-search-page/ws-search-page-private-label/ws-search-page-private-label.component';
import { WsSearchPageWebinarComponent } from './ws-search-page/ws-search-page-webinar/ws-search-page-webinar.component';
import { WsSharedModule } from "../ws-shared/ws-shared.module";
import { WsSpinnerModule } from '../ws-spartacus/spinner/ws-spinner.module';

const routes: Routes = [
  {
    path: 'search/:query',
    component: WsSearchPageComponent,
    canActivate: [CmsPageGuard]
  },
  {
    path: 'search',
    component: WsSearchPageComponent,
    canActivate: [CmsPageGuard]
  },
];

@NgModule({
  declarations: [
    WsHeaderSearchComponent,
    WsSearchPageComponent,
    HighlightPipe,
    WsSearchPageProductsComponent,
    WsSearchPageBrandsComponent,
    WsSearchPageCategoriesComponent,
    WsSearchPageIntegrationsComponent,
    WsSearchPageMagazinesComponent,
    WsSearchPageFaqsComponent,
    WsSearchPageCommonHeadingComponent,
    WsSearchPageCommonPaginationComponent,
    WsSearchPagePrivateLabelComponent,
    WsSearchPageWebinarComponent
  ],
  imports: [
    CommonModule,
    I18nModule,
    IconModule,
    RouterModule,
    MediaModule,
    UrlModule,
    BrowserModule,
    FormsModule,
    ReactiveFormsModule,
    BaseStorefrontModule,
    PageSlotModule,
    RouterModule.forChild(routes),
    WsSharedModule,
    WsSpinnerModule
],
  providers: [
    provideDefaultConfig(<CmsConfig>{
      cmsComponents: {
        SearchBoxComponent: {
          component: WsHeaderSearchComponent,
        },
        SearchResultsListComponent: {
          component: WsSearchPageComponent
        }
      },
    }),
    provideConfig({
      backend: {
        occ: {
          endpoints: {
            productSearch:
              'products/search?fields=FULL,pagination(DEFAULT)&filters=isMedpax:false'
          }
        }
      }
    })
  ],
  exports: [RouterModule, HighlightPipe, WsHeaderSearchComponent, WsSearchPageComponent]
})
export class WsSearchModule { }
