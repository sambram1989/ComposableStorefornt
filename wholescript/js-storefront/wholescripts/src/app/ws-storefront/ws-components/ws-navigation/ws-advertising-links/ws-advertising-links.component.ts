import { ChangeDetectionStrategy, Component } from '@angular/core';
import { CmsService } from '@spartacus/core';
import { Observable } from 'rxjs';
import { API_NAV_LINKS, NAV_LINKS } from '../../../ws-models/page.model';

@Component({
  selector: 'ws-advertising-links',
  templateUrl: './ws-advertising-links.component.html',
  styleUrls: ['./ws-advertising-links.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class WsAdvertisingNavigationUIComponent {
  currentPage = window.location.pathname;
  showMobileNav: boolean = false;
  intro$: Observable<any> = !this.currentPage.includes('api') ?  this.cmsservice.getComponentData('AdvertisingIntroLink') :  this.cmsservice.getComponentData('ApiResourceIntroLink');
  brands$: Observable<any> = !this.currentPage.includes('api') ?  this.cmsservice.getComponentData('NewBrandLink') : this.cmsservice.getComponentData('ExternalOrderSubmissionLink');
  promo$: Observable<any> = !this.currentPage.includes('api') ?  this.cmsservice.getComponentData('PromoBannersLink') : this.cmsservice.getComponentData('EMRRecommendationLink');
  pageHeadings = !this.currentPage.includes('api') ? NAV_LINKS : API_NAV_LINKS;
  subHeading = !this.currentPage.includes('api') ? 'Our Ad Products:' : 'Our APIs:';
  activeLink = NAV_LINKS.intro;
  isAdvertising: boolean = !this.currentPage.includes('api');

  constructor(
    protected cmsservice: CmsService,
  ) {
    this.getPageLink();
  }
  getPageLink() {
    if(this.isAdvertising) {
      this.activeLink = this.currentPage.includes('promo') ? NAV_LINKS.link2 : this.currentPage.includes('onboarding') ? NAV_LINKS.link1 : NAV_LINKS.intro;
    }
  }
}
