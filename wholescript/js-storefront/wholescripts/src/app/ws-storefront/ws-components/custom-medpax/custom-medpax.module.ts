import { NgModule } from '@angular/core';
import { CommonModule, NgOptimizedImage } from '@angular/common';
import { CustomMedpaxLandingPageComponent } from './custom-medpax-landing-page/custom-medpax-landing-page.component';
import { ControlNamePipe, CustomMedpaxReviewComponent } from './custom-medpax-review/custom-medpax-review.component';
import { provideConfig } from '@spartacus/core';
import { CustomMedpaxPharmacyPageComponent } from './custom-medpax-pharmacy-page/custom-medpax-pharmacy-page.component';
import { WsWebinarsModule } from '../ws-webinars/wswebinar.module';
import { WsSharedModule } from '../ws-shared/ws-shared.module';
import { CustomCaruselComponent } from './custom-carusel/custom-carusel.component';
import { ReactiveFormsModule } from '@angular/forms';
import { CmsPageGuard, MediaModule, PageSlotModule } from '@spartacus/storefront';
import { MedpaxdetailscardComponent } from './medpaxdetailscard/medpaxdetailscard.component';
import { CustomMedPaxReviewCardComponent, GetDataPipe, GetTitleofPacket } from './custom-med-pax-review-card/custom-med-pax-review-card.component';
import { CustomMedpaxTableComponent, MedpaxPricePipe } from './custom-medpax-table/custom-medpax-table.component';
import { AlertModule } from 'ngx-bootstrap/alert';
import { CustomMedpaxRightContainerComponent } from './custom-medpax-right-container/custom-medpax-right-container.component';
import { SupplementfactsComponent } from './supplementfacts/supplementfacts.component';
import { IsArrayPipe, RightmedpaxTableComponent } from './rightmedpax-table/rightmedpax-table.component';
import { MedpaxCardComponent } from './medpax-card/medpax-card.component';
import { CustomMedpaxChipComponent } from './custom-medpax-chip/custom-medpax-chip.component';
import { AccordionModule } from 'ngx-bootstrap/accordion';
import { WsCustomMedPaxEvents } from './medpaxService/medpaxEventservice.service';
import { RouterModule, Routes } from '@angular/router';
import { TabsModule } from 'ngx-bootstrap/tabs';
import { WsProductFacetNavigationModule } from "../ws-spartacus/product-facet-navigation/product-facet-navigation.module";
import { WsRecommendationSticky } from '../ws-recommendations/recommendationstickComponent.module';
import { WsAddCreditCardModule } from '../ws-add-credit-card/ws-add-credit-card.module';
import { SelectCartonComponent } from './select-carton/select-carton.component';
import { CustomMedpaxFloatingFooterComponent } from './custom-medpax-floating-footer/custom-medpax-floating-footer.component';
import { PrivateLabelTagComponent } from './private-label-tag/private-label-tag.component';
import { WsSpinnerModule } from '../ws-spartacus/spinner/ws-spinner.module';
import { CustomMedpaxBuyAgainFacetModule } from './custom-medpax-buy-again-facet/custom-medpax-buy-again-facet.module';
import { CustomMedpaxGuard } from './guard/custom.medpax.guard';
import { medpaxPageRestrictionGuard } from './guard/medpage-page-restriction.guard';
import { WsActiveFacetsModule } from '../ws-spartacus/product-facet-navigation/active-facets/active-facets.module';

const routes: Routes = [
    {
        path: 'medpax',
        canActivate: [CmsPageGuard, CustomMedpaxGuard, medpaxPageRestrictionGuard],
        component: CustomMedpaxLandingPageComponent,
    },
    {
        path: 'medpax/:ShoppingCartId',
        canActivate: [CmsPageGuard],
        component: CustomMedpaxLandingPageComponent,
    },
    {
        path: 'medpax/review/:id',
        canActivate: [CmsPageGuard],
        component: CustomMedpaxReviewComponent,
    },
    {
        path:'medpax/MedPaxSelectCarton/:id',
        canActivate:[CmsPageGuard],
        component:SelectCartonComponent
    },
    {
        path:'protocol/edit/carton/:id',
        canActivate:[CmsPageGuard],
        component:SelectCartonComponent
    },
    {
        path:'recommendation/edit/medpax/review/PrivateLabelSelectCarton/:id',
        canActivate:[CmsPageGuard],
        component:SelectCartonComponent  
    },
    {
        path:'protocol/edit/medpax/review/:id',
        canActivate: [CmsPageGuard],
        component: CustomMedpaxReviewComponent,
    },
    {
        path:'recommendation/edit/medpax/review/:id',
        canActivate: [CmsPageGuard],
        component: CustomMedpaxReviewComponent,
    }
]
@NgModule({
    declarations: [
        CustomMedpaxLandingPageComponent,
        CustomMedpaxReviewComponent,
        CustomMedpaxPharmacyPageComponent,
        CustomCaruselComponent,
        MedpaxdetailscardComponent,
        CustomMedPaxReviewCardComponent,
        CustomMedpaxTableComponent,
        CustomMedpaxRightContainerComponent,
        SupplementfactsComponent,
        RightmedpaxTableComponent,
        MedpaxCardComponent,
        CustomMedpaxChipComponent,
        ControlNamePipe,
        GetDataPipe,
        GetTitleofPacket,
        SelectCartonComponent,
        CustomMedpaxFloatingFooterComponent,
        PrivateLabelTagComponent,
        MedpaxPricePipe,
        IsArrayPipe
    ],
    imports: [
        CommonModule,
        WsWebinarsModule,
        WsSharedModule,
        ReactiveFormsModule,
        PageSlotModule,
        MediaModule,
        NgOptimizedImage,
        AlertModule,
        AccordionModule,
        WsSharedModule,
        WsRecommendationSticky,
        WsAddCreditCardModule,
        RouterModule.forChild(routes),
        TabsModule.forRoot(),
        WsProductFacetNavigationModule,
        WsSpinnerModule,
        CustomMedpaxBuyAgainFacetModule,
        WsActiveFacetsModule
    ],
    providers: [CustomMedpaxGuard,provideConfig(
        {
            cmsComponents: {
                MedpaxCustomPageProductsflexComponent: {
                    component: () => import('./custom-medpax-landing-page/custom-medpax-landing-page.component').then(m => m.CustomMedpaxLandingPageComponent)
                },
                MedpaxReviewPagePacketflexComponent: {
                    component: () => import('./custom-medpax-review/custom-medpax-review.component').then(m => m.CustomMedpaxReviewComponent)
                },
                PharmacyBodyflexComponent: {
                    component: () => import('./custom-medpax-pharmacy-page/custom-medpax-pharmacy-page.component').then(m => m.CustomMedpaxPharmacyPageComponent)
                }
            }
        }
    ),{
      provide:WsCustomMedPaxEvents,
      useClass:WsCustomMedPaxEvents
    }],
    exports:[
        CustomMedpaxChipComponent,
        MedpaxCardComponent,
        RightmedpaxTableComponent,
        PrivateLabelTagComponent,
        MedpaxPricePipe
    ]
})
export class CustomMedpaxModule { }
