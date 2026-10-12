import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { WsPatientFindPractitionerComponent } from './ws-patient-find-practitioner/ws-patient-find-practitioner.component';
import { CmsPageGuard, MediaModule, PageSlotModule } from '@spartacus/storefront';
import { Routes, RouterModule } from '@angular/router';
import { AuthGuard, CmsConfig, provideConfig } from '@spartacus/core';
import { WsPatientFindPractitionerSearchMapComponent } from './ws-patient-find-practitioner-search-map/ws-patient-find-practitioner-search-map.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

const routes: Routes = [
  {
    path: 'findpractitioner',
    component: WsPatientFindPractitionerComponent,
    canActivate: [CmsPageGuard]
  },
];

@NgModule({
  declarations: [
    WsPatientFindPractitionerComponent,
    WsPatientFindPractitionerSearchMapComponent
  ],
  imports: [
    CommonModule,
    PageSlotModule,
    MediaModule,
    FormsModule,
    ReactiveFormsModule,
    RouterModule.forChild(routes)
  ],
  providers: [
    provideConfig(<CmsConfig>{
      cmsComponents: {
        FindPractitionerForPatientflexComponent: {
          component: WsPatientFindPractitionerSearchMapComponent,
        }
      },
    }),
  ],
  exports: [
    WsPatientFindPractitionerComponent,
    WsPatientFindPractitionerSearchMapComponent
  ]
})
export class WsPatientFindPractitionerModule { }
