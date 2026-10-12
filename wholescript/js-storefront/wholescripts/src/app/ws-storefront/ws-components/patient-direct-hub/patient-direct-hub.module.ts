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
  MediaModule,
  OutletModule,
  OutletRefModule,
  PageLayoutModule,
  PageSlotModule,
} from '@spartacus/storefront';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { WsSharedModule } from '../ws-shared/ws-shared.module';
import { RouterModule, Routes } from '@angular/router';
import { AlertModule } from 'ngx-bootstrap/alert';
import { WsReferralsComponent } from './ws-referrals/ws-referrals.component';
import { WsNewReferralCodeComponent } from './ws-new-referral-code/ws-new-referral-code.component';
import { WsAccountSettingsModule } from '../ws-account-settings/ws-account-settings.module';
import { WsShareReferralCodeComponent } from './ws-share-referral/ws-share-referral.component';
import { WsAccuralsComponent } from './ws-accurals/ws-accurals.component';
import { WsPatientDirectTabsComponent } from './ws-patient-direct-tabs/ws-patient-direct-tabs.component';
import { WsWebBannersComponent } from './ws-web-banners/ws-web-banners.component';
import { DatePipe } from '@angular/common'
import { PatientConsultationFeeComponent } from './patient-consultation-fee/patient-consultation-fee.component';
import { ConsultationfeeFormButtonComponent } from './consultationfee-form-button/consultationfee-form-button.component';
import { WsSpinnerModule } from '../ws-spartacus/spinner/ws-spinner.module';
import { WsPatientDirectGuardNotFound } from '../ws-practitioner/w9-Guard/ws-patient-direct.guard';
import { WsEditReferralComponent } from '../ws-account-settings/ws-edit-referral-code/ws-edit-referral-code.component';

const appRoute: Routes = [
  {
    path: 'patient-direct-hub',
    component: WsPatientDirectTabsComponent,
    canActivate: [CmsPageGuard, AuthGuard, WsPatientDirectGuardNotFound],
    children: [
      {
        path: '',
        component: WsReferralsComponent,
        data: {
          pageLabel: '/patient-direct-hub/Referral-code',
          cxPath: 'patient-direct-hub',
        }
      },
      {
        path: 'Referral-code',
        component: WsReferralsComponent,
        data: {
          pageLabel: '/patient-direct-hub/Referral-code',
          cxPath: 'patient-direct-hub',
        }
      },
      {
        path: 'edit-referral',
        component: WsEditReferralComponent,
        data: {
          pageLabel: '/patient-direct-hub',
          cxPath: 'patient-direct-hub',
        }
      },
      {
        path: 'Web-banners',
        component: WsWebBannersComponent,
        data: {
          pageLabel: '/patient-direct-hub',
          cxPath: 'patient-direct-hub',
        }
      },
      {
        path: 'Consultation-fees',
        component: PatientConsultationFeeComponent,
        data: {
          pageLabel: '/patient-direct-hub',
          cxPath: 'patient-direct-hub',
        }
      },
      {
        path: 'Accruals',
        component: WsAccuralsComponent,
        data: {
          pageLabel: '/patient-direct-hub/Accruals',
          cxPath: 'patient-direct-hub',
        }
      }
    ],
  },
];

@NgModule({
  declarations: [WsReferralsComponent, WsNewReferralCodeComponent, WsShareReferralCodeComponent, WsAccuralsComponent, WsPatientDirectTabsComponent, WsWebBannersComponent, PatientConsultationFeeComponent, ConsultationfeeFormButtonComponent],
  imports: [
    CommonModule,
    MediaModule,
    FormsModule,
    ReactiveFormsModule,
    OutletModule,
    PageSlotModule,
    OutletRefModule,
    PageLayoutModule,
    UrlModule,
    AlertModule,
    WsSharedModule,
    WsAccountSettingsModule,
    MediaModule,
    WsSpinnerModule,
    RouterModule.forChild(appRoute),
  ],
  providers:[provideConfig({
    cmsComponents:{
      consultationFeeFormButtonFlex:{
        component:() => import('./consultationfee-form-button/consultationfee-form-button.component').then((m)=> m.ConsultationfeeFormButtonComponent)
      }
    }
  }), DatePipe],
  exports: [WsReferralsComponent],
})
export class WsPatientDirectHubModule {}
