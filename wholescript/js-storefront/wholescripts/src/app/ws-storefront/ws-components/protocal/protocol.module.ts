import { NgModule } from '@angular/core';
import { CommonModule, NgOptimizedImage } from '@angular/common';
import {ClipboardModule} from '@angular/cdk/clipboard';
import { ProtocolLandingPageComponent } from './protocol-landing-page/protocol-landing-page.component';
import {
   AuthGuard,
   provideConfig,
} from '@spartacus/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { EditProtocolComponent } from './edit-protocol/edit-protocol.component';
import { ProtocolDetailsPopupComponent } from './protocol-details-popup/protocol-details-popup.component';
import { OverlayModule } from '@angular/cdk/overlay';
import { PortalModule } from '@angular/cdk/portal';
import { ModalModule } from 'ngx-bootstrap/modal';
import { BsDropdownModule } from 'ngx-bootstrap/dropdown';
import { CreateNewProtocolComponent } from './create-new-protocol/create-new-protocol.component';
import {
   CmsPageGuard,
   MediaModule,
   PageSlotModule,
} from '@spartacus/storefront';
import { RouterModule } from '@angular/router';
import { ProtocolDetailsPageComponent } from './protocol-details-page/protocol-details-page.component';
import { ReviewProtocolComponent } from './review-protocol/review-protocol.component';
import { CompleteProtocolComponent } from './complete-protocol/complete-protocol.component';
import { EditDosingPopupComponent } from './edit-dosing-popup/edit-dosing-popup.component';
import { WsSharedModule } from '../ws-shared/ws-shared.module';
import { ProductComponent } from './product-component/product.component';
import { ReviewProtocolHeaderComponent } from './review-protocol/review-protocol-header/review-protocol-header.component';
import { ReviewProtocolNoteComponent } from './review-protocol/review-protocol-note/review-protocol-note.component';
import { CompleteProtocalHeaderComponent } from './complete-protocol/complete-protocal-header/complete-protocal-header.component';
import { WsProductFacetNavigationModule } from '../ws-spartacus/product-facet-navigation/product-facet-navigation.module';
import { ProtocolShareListComponent } from './protocol-share-list/protocol-share-list.component';
import { SuccessProductComponent } from './success-product/success-product.component';
import { SuccessProtocolLandingComponent } from './success-protocol-landing/success-protocol-landing.component';
import { ProtocolShareSuccessComponent } from './protocol-share-success/protocol-share-success.component';
import { WsSpinnerModule } from '../ws-spartacus/spinner/ws-spinner.module';
import { PractitionerMyPatientListingService } from '../ws-practitioner/ws-My-patients/my-patient-landing/practitioner-my-patient-listing.service';
import { MoreInfoPopupComponent } from './more-info/more-info-popup/more-info-popup.component';
import { AlertModule } from 'ngx-bootstrap/alert';
import { TabsModule } from 'ngx-bootstrap/tabs';
import { ProtocolRecommendationCommonModule } from './commonProtocolToRecommendation.module';
import { CustomMedpaxModule } from '../custom-medpax/custom-medpax.module';
import { WsActiveFacetsModule } from '../ws-spartacus/product-facet-navigation/active-facets/active-facets.module';


const routes = [
   {
      path: 'protocol/edit/:id',
      canActivate: [CmsPageGuard,AuthGuard],
      component: EditProtocolComponent,
   },
   {
      path: 'protocol/edit/review/:id',
      canActivate: [CmsPageGuard, AuthGuard],
      component: ReviewProtocolComponent,
   },
   {
      path: 'protocol/complete/:id',
      canActivate: [CmsPageGuard, AuthGuard],
      component: CompleteProtocolComponent,
   },
   {
      path: 'protocol/share/select-patient/:id/:allpatient',
      canActivate: [CmsPageGuard, AuthGuard],
      component: ProtocolShareListComponent,
   },
   {
      path: 'protocol/share/:id',
      canActivate: [CmsPageGuard, AuthGuard],
      component: ProtocolShareSuccessComponent,
   },
   {
      path: 'protocol/details/:id',
      canActivate: [CmsPageGuard, AuthGuard],
      component: ProtocolDetailsPageComponent
   }
];

const modules = {
   cmsComponents: {
      myProtocolflexComponent: {
         component: () => import('./protocol-landing-page/protocol-landing-page.component').then(m => m.ProtocolLandingPageComponent),
      },
      myProtocolEditflexComponent: {
         component: () => import('./edit-protocol/edit-protocol.component').then(m => m.EditProtocolComponent),
      },
      myProtocolDetailPageflexComponent: {
         component: () => import('./protocol-details-page/protocol-details-page.component').then(m => m.ProtocolDetailsPageComponent),
      },
      myProtocolCompletePageflexComponent: {
         component: () => import('./complete-protocol/complete-protocol.component').then(m => m.CompleteProtocolComponent),
      },
      shareProtocolflexComponent: {
         component: () => import('./protocol-share-list/protocol-share-list.component').then(m => m.ProtocolShareListComponent),
      },
      shareProtocolSuccessflexComponent: {
         component: () => import('./protocol-share-success/protocol-share-success.component').then(m => m.ProtocolShareSuccessComponent),
      }
   },
};
@NgModule({
   declarations: [
      ProtocolLandingPageComponent,
      EditProtocolComponent,
      ProtocolDetailsPopupComponent,
      CreateNewProtocolComponent,
      ProtocolDetailsPageComponent,
      ReviewProtocolComponent,
      CompleteProtocolComponent,
      EditDosingPopupComponent,
      ProductComponent,
      ReviewProtocolHeaderComponent,
      ReviewProtocolNoteComponent,
      CompleteProtocalHeaderComponent,
      ProtocolShareListComponent,
      SuccessProductComponent,
      SuccessProtocolLandingComponent,
      ProtocolShareSuccessComponent,
      MoreInfoPopupComponent
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
      NgOptimizedImage,
      WsProductFacetNavigationModule,
      ClipboardModule,
      AlertModule,
      ProtocolRecommendationCommonModule,
      CustomMedpaxModule,
      TabsModule.forRoot(),
      RouterModule.forChild(routes),
      ModalModule.forRoot(),
      BsDropdownModule.forRoot(),
      WsActiveFacetsModule
   ],
   providers: [
      PractitionerMyPatientListingService,
      provideConfig(modules)
   ],
   exports: [ReviewProtocolHeaderComponent,
       ReviewProtocolNoteComponent,
       ProductComponent],
})
export class ProtocolModule { }
