import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {
  AuthGuard,
  ConfigModule,
  provideConfig,
  UrlModule,
} from '@spartacus/core';
import {
  CmsPageGuard,
  IconModule,
  MediaModule,
  OutletModule,
  OutletRefModule,
  PageLayoutModule,
  PageSlotModule,
} from '@spartacus/storefront';
import { CapitalizeFormatPipe, RecommendationListComponent } from './ws-recommendation-list/recommendation-list.component';
import { NewRecommendationComponent } from './ws-new-recommendation/new-recommendation.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { WsSharedModule } from '../ws-shared/ws-shared.module';
import { WsRecommendationReviewForComponent } from './ws-recommendation-review/ws-recommendation-review-for/ws-recommendation-review-for.component';
import { WsRecommendationReviewHeaderComponent } from './ws-recommendation-review/ws-recommendation-review-header/ws-recommendation-review-header.component';
import { WsRecommendationReviewNotesComponent } from './ws-recommendation-review/ws-recommendation-review-notes/ws-recommendation-review-notes.component';
import { WsRecommendationReviewNotificationComponent } from './ws-recommendation-review/ws-recommendation-review-notification/ws-recommendation-review-notification.component';
import { WsRecommendationReviewSidebarComponent } from './ws-recommendation-review/ws-recommendation-review-sidebar/ws-recommendation-review-sidebar.component';
import { WsRecommendationReviewComponent } from './ws-recommendation-review/ws-recommendation-review.component';
import { WsRecommendationConfirmComponent } from './ws-recommendation-confirm/ws-recommendation-confirm.component';
import { WsRecommendationConfirmHeaderComponent } from './ws-recommendation-confirm/ws-recommendation-confirm-header/ws-recommendation-confirm-header.component';
import { wsRecommendationRoutingConfig } from './config/ws-recommendation.routing.config';
import { RouterModule, Routes } from '@angular/router';
import { ViewRecommendationComponent } from './ws-view-recommendation/view-recommendation.component';
import { WsPopupRecommendationComponent } from './ws-recommendation-popup/recommendation-popup.component';
import { WsProductFacetNavigationModule } from '../ws-spartacus/product-facet-navigation/product-facet-navigation.module';
import { WsRecommendationFiltersComponent } from './ws-recommendation-filters/ws-recommendation-filters.component';
import { WsRecommendationDetailsComponent } from './ws-recommendation-details/ws-recommendation-details.component';
import { BsDatepickerModule } from 'ngx-bootstrap/datepicker';
import { AlertModule } from 'ngx-bootstrap/alert';
import { ProtocolRecommendationCommonModule } from '../protocal/commonProtocolToRecommendation.module';
import { TabsModule } from 'ngx-bootstrap/tabs';
import { WsRecommendationSticky } from './recommendationstickComponent.module';
import { CustomMedpaxModule } from '../custom-medpax/custom-medpax.module';
import { ProtocolModule } from '../protocal/protocol.module';
import { WsPatientDirectGuard } from '../ws-practitioner/w9-Guard/ws-patient-direct.guard';
import { SwapProtocolPopupComponent } from './swap-protocol-popup/swap-protocol-popup.component';
import { ConfirmSwapProtocolPopupComponent } from './confirm-swap-protocol-popup/confirm-swap-protocol-popup.component';
import { CdkAccordionModule } from '@angular/cdk/accordion';
import { WsRecommendationFilterMobilefacetsComponent } from './ws-recommendation-filter-mobilefacets/ws-recommendation-filter-mobilefacets.component';
const routes: Routes = [
  {
    path: 'Recommendation',
    canActivate: [AuthGuard, CmsPageGuard],
    children: [
      {
        path: 'RecommendationReview/:id',
        component: WsRecommendationReviewComponent,
        data: {
          pageLabel: '/Recommendation/RecommendationReview',
          cxPath: 'RecommendationReview',
        },
      },
      {
        path: 'BasicRecommendation/:id',
        component: NewRecommendationComponent,
        data: {
          pageLabel: 'Recommendation/BasicRecommendation',
          cxPath: 'RecommendationReview',
        },
      },
      {
        path: 'RecommendationComplete/:id',
        component: WsRecommendationConfirmComponent,
        data: {
          pageLabel: '/Recommendation/RecommendationComplete',
          cxPath: 'RecommendationComplete',
        },
      },
      {
        path: 'BasicRecommendation/:id',
        component: NewRecommendationComponent,
        data: {
          pageLabel: '/Recommendation/BasicRecommendation',
          cxPath: 'BasicRecommendation',
        },
      },
      {
        path: 'RecommendationDetails/:id',
        component: WsRecommendationDetailsComponent,
        data: {
          pageLabel: '/Recommendation/RecommendationDetails',
          cxPath: 'RecommendationDetails',
        },
      },
    ],
  },
];

@NgModule({
  declarations: [
    RecommendationListComponent,
    NewRecommendationComponent,
    WsRecommendationReviewComponent,
    WsRecommendationReviewNotesComponent,
    WsRecommendationReviewForComponent,
    WsRecommendationReviewNotificationComponent,
    WsRecommendationReviewHeaderComponent,
    WsRecommendationReviewSidebarComponent,
    WsRecommendationConfirmComponent,
    WsRecommendationConfirmHeaderComponent,
    ViewRecommendationComponent,
    WsPopupRecommendationComponent,
    WsRecommendationFiltersComponent,
    WsRecommendationDetailsComponent,
    SwapProtocolPopupComponent,
    ConfirmSwapProtocolPopupComponent,
    CapitalizeFormatPipe,
    WsRecommendationFilterMobilefacetsComponent
  ],
  imports: [
    CommonModule,
    MediaModule,
    FormsModule,
    ReactiveFormsModule,
    OutletModule,
    RouterModule,
    PageSlotModule,
    OutletRefModule,
    PageLayoutModule,
    UrlModule,
    AlertModule,
    WsSharedModule,
    WsProductFacetNavigationModule,
    BsDatepickerModule,
    ProtocolRecommendationCommonModule,
    TabsModule,
    WsRecommendationSticky,
    CustomMedpaxModule,
    ProtocolModule,
    IconModule,
    CdkAccordionModule,
    ConfigModule.withConfig({
      cmsComponents: {
        RecommendationSearchResultComponent: {
          component: RecommendationListComponent,
          guards:[AuthGuard,WsPatientDirectGuard]
        },
        RecommendationPatientSearchResultComponent: {
          component: ViewRecommendationComponent,
        },
        RecommendationDialogFlexComponent: {
          component: NewRecommendationComponent,
        },
        RecommendationReviewBodyComponent: {
          component: WsRecommendationReviewComponent,
        },
        RecommendationReviewHeaderComponent: {
          component: WsRecommendationReviewHeaderComponent,
        },
        RecommendationConfirmBodyComponent: {
          component: WsRecommendationConfirmComponent,
        },
        RecommendationConfirmHeaderComponent: {
          component: WsRecommendationConfirmHeaderComponent,
        },
        RecommendationDetailFlexComponent: {
          component: WsRecommendationDetailsComponent,
        },
      },
    }),
    RouterModule.forChild(routes),
      ],
  providers: [
    provideConfig(wsRecommendationRoutingConfig),
    
  ],
  exports: [
    RouterModule,
    RecommendationListComponent,
    WsRecommendationReviewHeaderComponent,
    WsRecommendationConfirmHeaderComponent,
  ],
})
export class RecommendationModule {}
