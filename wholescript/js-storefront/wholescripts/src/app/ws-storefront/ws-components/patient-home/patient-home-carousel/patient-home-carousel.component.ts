import { Component, HostBinding, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import {
  CmsBannerComponent as model,
  CmsService,
  ContentSlotComponentData,
  Config,
} from '@spartacus/core';
import { CmsComponentData } from '@spartacus/storefront';
//import { data } from 'jquery'; BUild issue fix
import {
  combineLatest,
  distinctUntilChanged,
  filter,
  map,
  Observable,
  switchMap,
  take,
  tap,
} from 'rxjs';

@Component({
  selector: 'app-patient-home-carousel',
  templateUrl: './patient-home-carousel.component.html',
})
export class PatientHomeCarouselComponent implements OnInit {
  items$: Observable<any>
  baseURL: any;
  constructor(
    protected componentData: CmsComponentData<model>,
    protected cmsService: CmsService,
    protected config: Config,
    private router: Router
  ) {}
  slideConfig: any;


  ngOnInit(): void {
    const isPatientsRoute = this.router.url.includes('/patients');

    const tabletSettings = isPatientsRoute
      ? { slidesToShow: 1, centerPadding: '25%' }
      : { slidesToShow: 1.6, centerPadding: '25%' };

    this.slideConfig = {
    autoplay: true,
    autoplaySpeed: 10000,
    speed: 1000,
    centerMode: false,
    slidesToScroll: 1,
    dots: true,
    infinite: true,
    arrows: false,
    dotsClass: 'slick-dots',
    draggable: true,
    responsive: [
      {
        breakpoint: 3000, // desktops
        settings: {
          slidesToShow: 1.6,
          centerPadding: '25%',
        }
      },
      {
        breakpoint: 992, // tablets
        settings: tabletSettings
      },
      {
        breakpoint: 768, // small tablets and large phones
        settings: {
          slidesToShow: 1,
          centerMode: false,         // Disable centerMode on mobile
          centerPadding: '0',        // No adjacent slides
          infinite: true
        }
      },
    ]
  };
    this.items$ = this.componentData.data$.pipe(
      switchMap((data: any) =>
        {
          const components = (data?.carouselList ?? '').split(' ');
          return combineLatest(
            components.map((component: any) =>
              this.cmsService.getComponentData<any>(component).pipe(
                distinctUntilChanged(),
                map((child) => {
                  if (!child) {
                    return undefined;
                  }
                  return {
                    content: child?.content,
                    author: child.headline,
                    media: child.media
                  };
                  // if (!child.flexType) {
                  //   child = {
                  //     ...child,
                  //     flexType: child.typeCode,
                  //   };
                  // }

                  // return child;
                })
              )
            )
          )
        }
      )
    );

    this.baseURL = this.config?.backend?.occ?.baseUrl
    ? this.config.backend?.occ?.baseUrl
    : '';
  }

  getImageUrl(image: any) {
    return image?.url ? `${this.baseURL}${image.url}` : undefined;
  }
}
