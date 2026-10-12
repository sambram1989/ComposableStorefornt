import { CUSTOM_ELEMENTS_SCHEMA, NgModule, NO_ERRORS_SCHEMA } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AuthGuard, provideConfig } from '@spartacus/core';
import { MyPatientLandingComponent } from './my-patient-landing/my-patient-landing.component';
import { WsSharedModule } from '../../ws-shared/ws-shared.module';
import { PractitionerMyPatientListingService } from './my-patient-landing/practitioner-my-patient-listing.service';
import { WsSpinnerModule } from '../../ws-spartacus/spinner/ws-spinner.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { W9formComponent } from '../w9form/w9form.component';
import { W9referralcodeformComponent } from '../w9referralcodeform/w9referralcodeform.component';
import { W9LandingPageComponent } from '../w9-landing-page/w9-landing-page.component';
import { MediaModule, PageSlotModule } from '@spartacus/storefront';
import { W9successPageComponent } from '../w9success-page/w9success-page.component';
import { RouterModule } from '@angular/router';
import { WsBlockPatientDirectGuard, WsPatientDirectGuard } from '../w9-Guard/ws-patient-direct.guard';
import { AlertModule } from 'ngx-bootstrap/alert';
import { ReferralCheckGuard } from '../w9-Guard/w9-form-guard.guard';


@NgModule({

  declarations: [MyPatientLandingComponent,
    W9formComponent,
    W9referralcodeformComponent,
    W9LandingPageComponent,
    W9successPageComponent],
  imports: [
    CommonModule,
    WsSharedModule,
    WsSpinnerModule,
    FormsModule,
    ReactiveFormsModule,
    PageSlotModule,
    MediaModule,
    RouterModule,
    AlertModule
  ],
  providers: [
    provideConfig({
      cmsComponents: {
        practitionerMyPatientsBodyFlexComponent: {
          component: () =>
            import('./my-patient-landing/my-patient-landing.component').then(
              (m) => m.MyPatientLandingComponent),
          guards: [AuthGuard, WsPatientDirectGuard]
        },
        recommendationMyPatientsBodyFlexComponent: {
          component: () =>
            import('./my-patient-landing/my-patient-landing.component').then(
              (m) => m.MyPatientLandingComponent),
          guards: [AuthGuard, WsPatientDirectGuard]
        },
        PatientDirectReferralCodePageBodySlotFlexComp: {
          component: () => import('../w9referralcodeform/w9referralcodeform.component').then(
            (m) => m.W9referralcodeformComponent),
          guards: [AuthGuard,WsBlockPatientDirectGuard]
        },
        PatientDirectSuccessPageBodySlotFlexComp: {
          component: () => import('../w9success-page/w9success-page.component').then(
            (m) => m.W9successPageComponent),
          guards: [AuthGuard, WsPatientDirectGuard]
        },
        PatientDirectStartPageBodySlotFlexComp: {
          component: () =>
            import('../w9-landing-page/w9-landing-page.component').then(
              (m) => m.W9LandingPageComponent),
          guards: [AuthGuard,WsBlockPatientDirectGuard]
        },
        PatientDirectW9FormPageBodySlotFlexComp: {
          component: () =>
            import('../w9form/w9form.component').then(
              (m) => m.W9formComponent),
          guards: [AuthGuard,WsBlockPatientDirectGuard,ReferralCheckGuard]
        }
      }
    }),
    PractitionerMyPatientListingService
  ],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class WsPractitionerMyPatientsModule { }
