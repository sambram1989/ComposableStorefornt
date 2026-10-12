import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { WsBreadcrumbComponent } from './breadcrumb.component';
import { RouterModule } from '@angular/router';
import { PageMetaModule, I18nModule, provideDefaultConfig, CmsConfig, CategoryPageMetaResolver, PageMetaResolver, BasePageMetaResolver, ProductPageMetaResolver } from '@spartacus/core';
import { WsSharedModule } from '../../../ws-shared/ws-shared.module';
import { WsCategoryPageMetaResolver } from './resolvers/category-page-meta-resolver';
import { WsContentPageMetaResolver } from './resolvers/content-page-meta.resolver';
import { WsBasePageMetaResolver } from './resolvers/base-page-meta-resolver';
import { WsProductPageMetaResolver } from './resolvers/product-meta-resolver';
import { WsCheckoutPageMetaResolver } from './resolvers/checkout-page-meta-resolver';
import { WsSearchPageMetaResolver } from './resolvers/search-page-meta-resolver';
import { CheckoutPageMetaResolver } from '@spartacus/checkout/base/core';



@NgModule({
  imports: [CommonModule, RouterModule, PageMetaModule, I18nModule,WsSharedModule],
  providers: [
      // WsCheckoutPageMetaResolver,
    provideDefaultConfig(<CmsConfig>{
      cmsComponents: {
        BreadcrumbComponent: {
          component: WsBreadcrumbComponent,
        },
      },
    }),
    {
      provide: PageMetaResolver,
      useExisting: WsCategoryPageMetaResolver,
      multi: true,
    },
    {
      provide: PageMetaResolver,
      useExisting: WsContentPageMetaResolver,
      multi: true,
    },
    {
      provide: BasePageMetaResolver,
      useExisting: WsBasePageMetaResolver,
      multi: true
    },
    {
      provide: PageMetaResolver,
      useExisting: WsProductPageMetaResolver,
      multi: true
    },
    // {
    //   provide: PageMetaResolver,
    //   useExisting: WsCheckoutPageMetaResolver,
    //   multi: true
    // },
    {
      provide: PageMetaResolver,
      useExisting: WsSearchPageMetaResolver,
      multi: true,
    }
  ],
  declarations: [WsBreadcrumbComponent],
  exports: [WsBreadcrumbComponent],
})
export class WsBreadcrumbModule { }
