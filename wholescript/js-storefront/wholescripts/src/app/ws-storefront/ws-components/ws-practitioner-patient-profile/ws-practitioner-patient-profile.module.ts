import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { WsPractitionerPatientProfileComponent } from './ws-practitioner-patient-profile.component';
import { AuthGuard } from '@spartacus/core';
import { RouterModule, Routes } from '@angular/router';
import { CmsPageGuard } from '@spartacus/storefront';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { WsSpinnerModule } from '../ws-spartacus/spinner/ws-spinner.module';

const routes: Routes = [
  {
    path: 'practitioner/patientprofile',
    component: WsPractitionerPatientProfileComponent,
    canActivate: [AuthGuard, CmsPageGuard]
  }
];


@NgModule({
  declarations: [
    WsPractitionerPatientProfileComponent
  ],
  imports: [
    CommonModule,
    FormsModule,
    WsSpinnerModule,
    ReactiveFormsModule,
    RouterModule.forChild(routes)
  ],
  exports: [
    WsPractitionerPatientProfileComponent
  ]
})
export class WsPractitionerPatientProfileModule { }
