import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";
import { AuthGuard, ConfigModule, I18nModule, provideConfig } from "@spartacus/core";
import { WebinarParentComponent } from "./webinar-parent/webinar-parent.component";
import { WebinarCarouselUiComponent } from "./webinar-carousel-ui/webinar-carousel-ui.component";
import { WebinarCartsComponent } from './webinar-carts/webinar-carts.component';
import { WebinarUserComponent } from './webinar-user/webinar-user.component';
import { WebinarPresenterComponent } from './webinar-presenter/webinar-presenter.component';
import { SlickCarouselModule } from "ngx-slick-carousel";
import { CmsPageGuard, MediaModule, PageComponentModule, PageSlotModule, PaginationModule, ProductListModule, SortingModule } from "@spartacus/storefront";
import { FeaturedWebinarComponent } from './featured-webinar/featured-webinar.component';
import { InfiniteScrollModule } from "ngx-infinite-scroll";
import { WsSharedModule } from "../ws-shared/ws-shared.module";
import { WsProductFacetNavigationModule } from "../ws-spartacus/product-facet-navigation/product-facet-navigation.module";
import { WebDetailsComponent } from "./web-details/web-details.component";
import { ProductCategoriesComponent } from './product-categories/product-categories.component';
import { WebinarcategoriesComponent } from './webinarcategories/webinarcategories.component';
import { RouterModule, Routes } from "@angular/router";
import { WebinarTitleIconComponent } from "./webinar-title-icon/webinar-title-icon.component";
import { PresentersDetailsPageComponent } from './presenters-details-page/presenters-details-page.component';

const routes:Routes =[
    {
        path: 'webinars/list',
        canActivate: [CmsPageGuard],
        component: WebinarParentComponent,
     },
     {
        path: 'webinars/:id',
        canActivate: [CmsPageGuard, AuthGuard],
        component: WebDetailsComponent,
     },
     {
        path: 'presenters/:id',
        canActivate: [CmsPageGuard, AuthGuard],
        component: PresentersDetailsPageComponent,
     }
];

const components = [
    WebinarParentComponent,
    WebinarCarouselUiComponent,
    WebinarUserComponent,
    WebinarCartsComponent,
    WebinarPresenterComponent,
    FeaturedWebinarComponent,
    ProductCategoriesComponent,
    WebinarcategoriesComponent,
    WebinarTitleIconComponent,
    WebDetailsComponent,
    PresentersDetailsPageComponent
];

const componentConfig = {
    cmsComponents: {
        WebinarListingflexComponent: {
            component : () => import('./webinar-parent/webinar-parent.component').then((m) => m.WebinarParentComponent),
        },
        WebinarProfilePageBodyflexComponent:{
            component : () => import('./presenters-details-page/presenters-details-page.component').then((m)=>m.PresentersDetailsPageComponent),
        },
        WebinarDetailsPageBodyflexComponent:{
            component: () => import('./web-details/web-details.component').then((m)=>m.WebDetailsComponent),
        }
    },
};

@NgModule({
    declarations: [...components, PresentersDetailsPageComponent ],
    imports: [CommonModule,
         SlickCarouselModule,
         PageSlotModule,
         ProductListModule,
         I18nModule,
         PageComponentModule,
         InfiniteScrollModule,
         SortingModule,
         PaginationModule,
         WsSharedModule,
         MediaModule,
         WsProductFacetNavigationModule,
         RouterModule.forChild(routes),
        
    ],
    providers: [
         provideConfig(componentConfig)
    ],
    exports: [WebinarTitleIconComponent]
})
export class WsWebinarsModule {

}