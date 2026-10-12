import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {
   CmsPageGuard,
   MediaModule,
   PageSlotModule
} from '@spartacus/storefront';
import { RouterModule } from '@angular/router';
import { WsWhyMedPaxPageComponent } from './ws-why-med-pax-page/ws-why-med-pax-page.component';
import { WsConditionSpecificMedpaxPageComponent } from './ws-condition-specific-medpax-page/ws-condition-specific-medpax-page.component';
import { WsConditionSpecificMedpaxIndividualPageComponent } from './ws-condition-specific-medpax-individual-page/ws-condition-specific-medpax-individual-page.component';
const routes = [
   {
      path: 'why-medpax',
      canActivate: [CmsPageGuard],
      component: WsWhyMedPaxPageComponent,
   },
   {
      path: 'condition-specific-medpax',
      canActivate: [CmsPageGuard],
      component: WsConditionSpecificMedpaxPageComponent,
   },
   {
      path: 'condition-specific-medpax-energy',
      canActivate: [CmsPageGuard],
      component: WsConditionSpecificMedpaxIndividualPageComponent,
   },
   {
      path: 'condition-specific-medpax-beauty',
      canActivate: [CmsPageGuard],
      component: WsConditionSpecificMedpaxIndividualPageComponent,
   },
   {
      path: 'condition-specific-medpax-gut',
      canActivate: [CmsPageGuard],
      component: WsConditionSpecificMedpaxIndividualPageComponent,
   },
   {
      path: 'condition-specific-medpax-immune',
      canActivate: [CmsPageGuard],
      component: WsConditionSpecificMedpaxIndividualPageComponent,
   },
   {
      path: 'condition-specific-medpax-sleep',
      canActivate: [CmsPageGuard],
      component: WsConditionSpecificMedpaxIndividualPageComponent,
   }
];

@NgModule({
   declarations: [WsWhyMedPaxPageComponent, WsConditionSpecificMedpaxPageComponent, WsConditionSpecificMedpaxIndividualPageComponent],
   imports: [
      CommonModule,
      PageSlotModule,
      MediaModule,
      RouterModule.forChild(routes)
   ],
   exports: [WsWhyMedPaxPageComponent]
})
export class WhyMedPaxPageModule { }
