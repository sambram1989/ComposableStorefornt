import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CmsConfig, ConfigModule, provideConfig } from '@spartacus/core';
import { PatientHomeSectionComponent } from './patient-home-section/patient-home-section.component';
import { PatientHomeCarouselComponent } from './patient-home-carousel/patient-home-carousel.component';
import { CarouselModule, MediaModule, PageComponentModule } from '@spartacus/storefront';
import { WsSharedModule } from '../ws-shared/ws-shared.module';
import { SlickCarouselModule } from 'ngx-slick-carousel';



@NgModule({
  declarations: [PatientHomeSectionComponent, PatientHomeCarouselComponent],
  imports: [
    CommonModule,
    CarouselModule, MediaModule,
    PageComponentModule,
    WsSharedModule,
    SlickCarouselModule
  ],
  providers: [
    provideConfig(<CmsConfig>{
      cmsComponents: {
        WholescriptsBannerComponent: {
          component: ()=> import('./patient-home-section/patient-home-section.component').then(c => c.PatientHomeSectionComponent),
        },
        WholescriptsCarouselListComponent: {
          component: ()=> import('./patient-home-carousel/patient-home-carousel.component').then(c => c.PatientHomeCarouselComponent),
        },
      },
    }),
  ],
  exports:[PatientHomeSectionComponent, PatientHomeCarouselComponent],
})
export class PatientHomeModule { }
