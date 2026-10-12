import { ChangeDetectionStrategy, ChangeDetectorRef, Component, HostListener, OnInit } from '@angular/core';
import { AuthService, CmsNavigationComponent, CmsService } from '@spartacus/core';
import {
  CmsComponentData,
  OutletPosition,
} from '@spartacus/storefront';
import * as jsonResponse from '../../../ws-content/advertising.json';
import { WsPage } from '../../../ws-services/ws-page.service';
import { Observable, shareReplay } from 'rxjs';
import { selectedEnv } from '../../../ws-env/environment';

@Component({
  selector: 'ws-advertising-subpages',
  templateUrl: './ws-advertising-subpages.component.html',
  styleUrls: ['./ws-advertising-subpages.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class WsAdvertisingSubPagesComponent {
  envHostname = selectedEnv.occ;
  navigationLinks: any[] = [];
  NAVIGATION_COMPONENT = '';
  outletPosition = OutletPosition;
  bestBrands: any;
  activeLink: string = '';
  currentPage: string = window.location.pathname;
  isPromoBanner: boolean = false;
  showMobileNav: boolean = false;
  pageContent: any[] = [];
  sections: any[] = [];
  setShadow: boolean = false;
  heading$: Observable<any> = !this.currentPage.includes('promo') ? this.cmsservice.getComponentData('ExternalOrderSubmissionParagraph') : this.cmsservice.getComponentData('EMRRecommendationsParagraph');
  latestUpdate$: Observable<any> = !this.currentPage.includes('promo') ? this.cmsservice.getComponentData('LastUpdateParagraph') : this.cmsservice.getComponentData('LastUpdateParagraph')
  subText$: Observable<any> = !this.currentPage.includes('promo') ? this.cmsservice.getComponentData('ExternalOrderSubmissionBodyParagraph') : this.cmsservice.getComponentData('EMRRecommendationsBodyParagraph')
  // CSS class update with loggedIn user
  isLoggedIn: boolean = false;

  constructor(
    protected componentData: CmsComponentData<CmsNavigationComponent>,
    protected cmsservice: CmsService,
    protected wsPageService: WsPage,
    protected cdr: ChangeDetectorRef,
    protected authService: AuthService,
  ) {
       this.authService.isUserLoggedIn().pipe(shareReplay(1)).subscribe((response) => {
              this.isLoggedIn = response;
        })
    this.getAdvertisingComponentData();
  }
  getAdvertisingComponentData() {
    this.bestBrands = !this.currentPage.includes('promo') ? jsonResponse.advertisingBanners : jsonResponse.promoBanners;
    this.isPromoBanner = this.currentPage.includes('promo');
    if(this.isPromoBanner) this.getPromosPageContent();
    else this.getBrandsPageContent();
  }
  getBrandsPageContent() {
    this.wsPageService.getAdvertisingBrandsContent().subscribe((res) => {
      this.pageContent = res.wholescriptsTopicWsDTO;
      this.pageContent.forEach((element) => this.sections.push({id: element.code, name: element.headline?.entry[0]?.value}));
      this.activeLink = this.sections[0].id;
      this.updateDownloadLink();
      this.cdr.detectChanges();
    })
  }
  getPromosPageContent() {
    this.wsPageService.getAdvertisingPromosContent().subscribe((res) => {
      this.pageContent = res.wholescriptsTopicWsDTO;
      this.pageContent.forEach((element) => this.sections.push({id: element.code, name: element.headline?.entry[0]?.value}));
      this.activeLink = this.sections[0].id;
      this.updateDownloadLink();
      this.cdr.detectChanges();
    })
  }

  /* Code changes for downloading the .psd file */
  updateDownloadLink() {
    setTimeout(() => {
      const downloadLinks = document.querySelectorAll('[download]');
      downloadLinks.forEach((link: Element) => {
        const hrefValue = link.getAttribute('href');
        if (hrefValue && !hrefValue.startsWith('http') && !hrefValue.startsWith('https')) {
          const updatedHref = `${this.envHostname}/${hrefValue}`;
          link.setAttribute('href', updatedHref);
        }
      });
    }, 100);
  }

  //scroll-page right layout
  toView(id: any) {
    const element = document.getElementById(id);
    this.activeLink = id;
    if (element) {
      var headerOffset = 78;
      var elementPosition = element.getBoundingClientRect().top;
      var offsetPosition = elementPosition + window.scrollY - headerOffset;
      window.scrollTo({
           top: offsetPosition,
           behavior: "smooth"
      });
    }
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
    const sectionElement = section as HTMLElement;
      var elementPosition = section.getBoundingClientRect().top;
      var offsetPosition = elementPosition + window.scrollY - headerOffset;
      const sectionBottom = offsetPosition + sectionElement.offsetHeight;
      if (scrollPosition >= offsetPosition && scrollPosition < sectionBottom && section.id !== 'chat-div-wrap') {
          this.activeLink = section.id;
      }
  });
}
}

