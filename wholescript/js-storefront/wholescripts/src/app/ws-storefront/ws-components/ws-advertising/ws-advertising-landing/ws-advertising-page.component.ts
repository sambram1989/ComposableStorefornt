import { ChangeDetectionStrategy, ChangeDetectorRef, Component, HostListener, OnInit } from '@angular/core';
import { AuthService, CmsNavigationComponent, CmsService } from '@spartacus/core';
import {
  CmsComponentData,
} from '@spartacus/storefront';
import { Observable, shareReplay } from 'rxjs';
import {
  WsNavigationService,
} from '../../../ws-services/ws-navigation.service';
import { API_NAV_LINKS, NAV_LINKS } from '../../../ws-models/page.model';

@Component({
  selector: 'ws-advertising-page',
  templateUrl: './ws-advertising-page.component.html',
  styleUrls: ['./ws-advertising-page.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class WsAdvertisingPageComponent {
  pageUrl = window.location.pathname;
  intro$: Observable<any> = this.pageUrl.includes('advertising') ? this.cmsservice.getComponentData('IntroductionParagraph'): this.cmsservice.getComponentData('IntroParagraph');
  mail$: Observable<any> = this.pageUrl.includes('advertising') ?  this.cmsservice.getComponentData('MailParagraph'): this.cmsservice.getComponentData('EmailParagraph');
  ads$: Observable<any> = this.pageUrl.includes('advertising') ? this.cmsservice.getComponentData('OurAdParagraph') : this.cmsservice.getComponentData('OurApiParagraph');
  brands$: Observable<any> = !this.pageUrl.includes('api') ?  this.cmsservice.getComponentData('NewBrandLink') : this.cmsservice.getComponentData('ExternalOrderSubmissionLink');
  promo$: Observable<any> = !this.pageUrl.includes('api') ?  this.cmsservice.getComponentData('PromoBannersLink') : this.cmsservice.getComponentData('EMRRecommendationLink');
  pageHeadings = !this.pageUrl.includes('api') ? NAV_LINKS : API_NAV_LINKS;
  setShadow: boolean = false;
  isLoggedIn: boolean = false;

  constructor(
    protected componentData: CmsComponentData<CmsNavigationComponent>,
    protected cmsservice: CmsService,
    protected service: WsNavigationService,
    protected authService: AuthService,
  ) {
    this.authService.isUserLoggedIn().pipe(shareReplay(1)).subscribe((response) => {
          this.isLoggedIn = response;
    })
  }
  @HostListener('window:scroll', [])
onScroll() {
    const sections = document.querySelectorAll('div[id]');
    var headerOffset = 78;
    const scrollPosition = window.scrollY + headerOffset; 

    sections.forEach((section) => {
      if(section.id === 'menu') {
        let elementPosition = section.getBoundingClientRect().top;
        if(scrollPosition >= elementPosition) this.setShadow = true;
        else this.setShadow = false;
      }
    });
}

}
