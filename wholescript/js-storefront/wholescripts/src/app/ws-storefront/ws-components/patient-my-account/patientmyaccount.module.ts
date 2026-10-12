import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { I18nModule, provideConfig, UrlModule, } from '@spartacus/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { OverlayModule } from '@angular/cdk/overlay';
import { PortalModule } from '@angular/cdk/portal';
import { CmsPageGuard, MediaModule, PageSlotModule } from '@spartacus/storefront';
import { RouterModule, Routes } from '@angular/router';
import { WsSharedModule } from '../ws-shared/ws-shared.module';
import { WsSpinnerModule } from '../ws-spartacus/spinner/ws-spinner.module';
import { PatientMyAccountProtocolComponent } from './patient-my-account-protocol/patient-my-account-protocol.component';
import { ProtocolListComponent } from './protocol-list/protocol-list.component';
import { PatientProtocolComponent } from './patient-protocol/patient-protocol.component';
import { ProductDataComponent } from './product-data/product-data.component';
import { PatientProtocollandingComponent } from './patient-protocollanding/patient-protocollanding.component';
import { ViewRecommendationComponent } from '../ws-recommendations/ws-view-recommendation/view-recommendation.component';
import { ProtocolRecommendationCommonModule } from '../protocal/commonProtocolToRecommendation.module';

const routes: Routes = [
  { path: 'customer/protocol', canActivate: [CmsPageGuard], component: PatientProtocollandingComponent },
  { path: 'customer/protocol/:id', canActivate: [CmsPageGuard], component: PatientProtocollandingComponent },
  { path: 'patient/recommendations', canActivate: [CmsPageGuard], component: ViewRecommendationComponent },
  { path: 'my-recommendations', canActivate: [CmsPageGuard], component: ViewRecommendationComponent },
];

const modules = {
  cmsComponents: {
    myaccountRightBodyFlexComponent: {
      component: () => import('./patient-my-account-protocol/patient-my-account-protocol.component').then(m => m.PatientMyAccountProtocolComponent),
    },
  },
};

@NgModule({
  declarations: [
    PatientMyAccountProtocolComponent,
    ProtocolListComponent,
    PatientProtocolComponent,
    ProductDataComponent,
    PatientProtocollandingComponent
  ],
  imports: [
    CommonModule,
    WsSpinnerModule,
    FormsModule,
    ReactiveFormsModule,
    OverlayModule,
    PortalModule,
    PageSlotModule,
    WsSharedModule,
    MediaModule,
    UrlModule,
    I18nModule,
    RouterModule.forChild(routes),
    ProtocolRecommendationCommonModule

  ],
  providers: [
    provideConfig(modules)
  ],
  exports: [],
})
export class PatientmyaccountModule { }

