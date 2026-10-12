import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {
  provideConfig,
  CmsConfig,
  UrlModule,
  I18nModule,
  FeaturesConfigModule,
} from '@spartacus/core';
import { PopularByBrandShopComponent } from './popular-by-brand-shop/popular-by-brand-shop.component';
import { PopularByCategoryComponent } from './popular-by-category/popular-by-category.component';
import { PopularCategoryShopComponent } from './popular-category-shop/popular-category-shop.component';
import { RouterModule } from '@angular/router';
import { IconModule, MediaModule } from '@spartacus/storefront';
import { ShopCategoryCarouselComponent } from './shop-category-carousel/shop-category-carousel.component';
import { CarouselModule } from 'ngx-bootstrap/carousel';
import { ShopPractitionerProtocolComponent } from './shop-practitioner-protocol/shop-practitioner-protocol.component';
import { ShopLatestRecommendationComponent } from './shop-latest-recommendation/shop-latest-recommendation.component';
import { AddToCartModule } from '@spartacus/cart/base/components/add-to-cart';
import { AlertModule } from 'ngx-bootstrap/alert';
import { SlickCarouselModule } from 'ngx-slick-carousel';

@NgModule({
  imports: [
    CommonModule,
    CarouselModule,
    MediaModule,
    RouterModule,
    UrlModule,
    IconModule,
    I18nModule,
    AlertModule,
    AddToCartModule,
    CarouselModule.forRoot(),
    SlickCarouselModule
  ],
  declarations: [
    PopularCategoryShopComponent,
    PopularByCategoryComponent,
    PopularByBrandShopComponent,
    ShopCategoryCarouselComponent,
    ShopPractitionerProtocolComponent,
    ShopLatestRecommendationComponent,
  ],
  providers: [
    provideConfig(<CmsConfig>{
      cmsComponents: {
        popularByCategoryFlex: {
          component: () =>
            import('./popular-by-category/popular-by-category.component').then(
              (c) => c.PopularByCategoryComponent
            ),
        },
        popularCategoryShopFlex: {
          component: () =>
            import(
              './popular-category-shop/popular-category-shop.component'
            ).then((c) => c.PopularCategoryShopComponent),
        },
        popularByBrandShopFlex: {
          component: () =>
            import(
              './popular-by-brand-shop/popular-by-brand-shop.component'
            ).then((c) => c.PopularByBrandShopComponent),
        },
        latestRecommendationFlex: {
          component: () => import('./shop-latest-recommendation/shop-latest-recommendation.component').then((c) => c.ShopLatestRecommendationComponent)
        },
        practitionerProtocolFlex: {
          component: () => import('./shop-practitioner-protocol/shop-practitioner-protocol.component').then((c) => c.ShopPractitionerProtocolComponent)
        }
      },
    }),
  ],
})
export class ShopComponentModule {}
