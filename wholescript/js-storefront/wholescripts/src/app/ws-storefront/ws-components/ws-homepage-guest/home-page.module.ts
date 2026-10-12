import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HomePageMedpaxComponent } from './homepage-medpax/homepage-medpax.component';
import { ConfigModule } from '@spartacus/core';
import { HomePageTopSlotComponent } from './home-page-top-slot/home-page-top-slot.component';
import { HomePageIntroComponent } from './homepage-intro-slot/homepage-intro-slot.component';
import { HomePageBrandsComponent } from './homepage-brands/homepage-brands.component';
import { HomePageTestimonialsComponent } from './homepage-testimonials/homepage-testimonials.component';
import { SlickCarouselModule } from 'ngx-slick-carousel';
import { MediaModule } from '@spartacus/storefront';
import { RouterModule } from '@angular/router';
@NgModule({
  declarations: [
    HomePageMedpaxComponent,
    HomePageTopSlotComponent,
    HomePageIntroComponent,
    HomePageBrandsComponent,
    HomePageTestimonialsComponent
  ],
  imports: [
    CommonModule,
    SlickCarouselModule,
    MediaModule,
    RouterModule,
    ConfigModule.withConfig({
      cmsComponents: {
        "Practitioner-Patient-FlexComp": {
          component: HomePageIntroComponent
        },
        BestBrandsFlexComponent: {
          component: HomePageBrandsComponent,
        },
        MedPaxFlexComponent: {
          component: HomePageMedpaxComponent
        },
        HeaderTopBodyContentFlexComp: {
          component: HomePageTopSlotComponent
        },
        TestimonialsFlexComponent: {
          component: HomePageTestimonialsComponent
        }
      }
    })
  ],
  exports: [HomePageIntroComponent],
})
export class HomePageModule { }
