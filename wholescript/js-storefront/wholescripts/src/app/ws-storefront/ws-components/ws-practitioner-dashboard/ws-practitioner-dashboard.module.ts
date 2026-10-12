import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { AuthGuard, provideConfig } from '@spartacus/core';
import { PractitionerDashBoardLandingComponent } from './practitioner-dash-board-landing/practitioner-dash-board-landing.component';
import { RouterModule, Routes } from '@angular/router';
import { TabsModule } from 'ngx-bootstrap/tabs';
import { AlertModule } from 'ngx-bootstrap/alert';
import { WsSpecialOffersModule } from '../ws-special-offers/ws-special-offers.module';
import { MediaModule, PageSlotModule } from '@spartacus/storefront';
import { BsDropdownModule } from 'ngx-bootstrap/dropdown';
import { WsSpinnerModule } from '../ws-spartacus/spinner/ws-spinner.module';
import { SlickCarouselModule } from 'ngx-slick-carousel';

@NgModule({
  declarations: [PractitionerDashBoardLandingComponent],
  imports: [
    CommonModule,
    RouterModule,
    TabsModule.forRoot(),
    AlertModule,
    MediaModule,
    PageSlotModule,
    WsSpinnerModule,
    WsSpecialOffersModule,
    BsDropdownModule.forRoot(),
    SlickCarouselModule
  ],
  providers: [
    provideConfig({
      cmsComponents: {
        WSPractitionerDashboardPageFlexComponent: {
          component: PractitionerDashBoardLandingComponent,
        },
      },
    }),
  ],
})
export class WsPractitionerDashBoard {}
