import { ChangeDetectionStrategy, ChangeDetectorRef, Component, HostListener, OnInit } from '@angular/core';
import { AuthService, CmsParagraphComponent, CmsService } from '@spartacus/core';
import {
  CmsComponentData,
} from '@spartacus/storefront';
import { WsPage } from '../../../ws-services/ws-page.service';
import { Observable, shareReplay } from 'rxjs';
import { EMR_RECOMMEDATIONS, EMR_RECOMMEDATIONS_CONTENT, EXTERNAL_ORDERS, EXTERNAL_RECOMMEDATIONS_CONTENT } from '../../../ws-models/page.model';

@Component({
  selector: 'ws-api-subpages',
  templateUrl: './ws-api-subpages.component.html',
  styleUrls: ['./ws-api-subpages.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class WsApiSubPagesComponent {
  activeLink: string = '';
  currentPage: string = window.location.pathname;
  isPromoBanner: boolean = false;
  showMobileNav: boolean = false;
  pageContent: any[] = [];
  sections: any[] = [];
  setShadow: boolean = false;
  heading$: Observable<any> = !this.currentPage.includes('EMR') ? this.cmsservice.getComponentData('ExternalOrderSubmissionParagraph') : this.cmsservice.getComponentData('EMRRecommendationsParagraph');
  latestUpdate$: Observable<any> = !this.currentPage.includes('EMR') ? this.cmsservice.getComponentData('LastUpdateParagraph') : this.cmsservice.getComponentData('LastUpdateParagraph')
  subText$: Observable<any> = !this.currentPage.includes('EMR') ? this.cmsservice.getComponentData('ExternalOrderSubmissionBodyParagraph') : this.cmsservice.getComponentData('EMRRecommendationsBodyParagraph')
  alerts: any[] = [];
  isLoggedIn: boolean = false;
  rightNavSections = this.currentPage.includes('EMR') ? EMR_RECOMMEDATIONS : EXTERNAL_ORDERS;
  EMR_sections = this.currentPage.includes('EMR') ? EMR_RECOMMEDATIONS_CONTENT : EXTERNAL_RECOMMEDATIONS_CONTENT;

  constructor(
    protected componentData: CmsComponentData<CmsParagraphComponent>,
    protected cmsservice: CmsService,
    protected wsPageService: WsPage,
    protected cdr: ChangeDetectorRef,
    protected authService: AuthService
  ) {
    this.getApiResourcesData();
      this.authService.isUserLoggedIn().pipe(shareReplay(1)).subscribe((response) => {
              this.isLoggedIn = response;
        })
  }
  getApiResourcesData() {
    this.isPromoBanner = this.currentPage.includes('EMR');
    if(this.isPromoBanner) this.getEMRPageContent();
    else this.getExternalOrderPageContent();
  }
  getEMRPageContent() {
    this.wsPageService.getApiEmrContent().subscribe((res) => {
      this.pageContent = this.rearrangeParameters(res.wholescriptsTopicWsDTO);
      this.rightNavSections.forEach((element, index) => this.sections.push({id: index, name: element}));
      this.activeLink = this.sections[0].id;
      this.cdr.detectChanges();
    })
  }
  getExternalOrderPageContent() {
    this.wsPageService.getApiOrderSubmissionContent().subscribe((res) => {
      this.pageContent = this.rearrangeParameters(res?.wholescriptsTopicWsDTO);
      this.rightNavSections.forEach((element, index) => this.sections.push({id: index, name: element}));
      this.activeLink = this.sections[0].id;
      this.cdr.detectChanges();
    })
  }
   rearrangeParameters(arr: any) {
    const response = this.rightNavSections.map((element) => arr.find((i: any) => i.headline?.entry[0]?.key.toLowerCase() === element.toLowerCase()));
    response.forEach((i) => {
      let item = this.EMR_sections.sections.findIndex((el) => el.heading === i.headline?.entry[0]?.key);
      if(item !== -1) {
        if(this.EMR_sections.sections[item].requestValues?.length)
        i.requestValues.entry = this.EMR_sections.sections[item].requestValues.map((element) => i.requestValues.entry.find((el: any) => el.key.includes(element)));
        if(this.EMR_sections.sections[item].responseValues?.length)
        i.responseValues.entry = this.EMR_sections.sections[item].responseValues.map((element) => i.responseValues.entry.find((el: any) => el.key.includes(element)))
      }
    })
    return response;
}
  getJSONResponse(codeText: string): any {
    if (codeText == null) {
      return null;
    } 
    codeText = codeText.trim();
    if (codeText.length === 0) {
      return null;
    }
    if (!/^(\{.*\}|\[.*\])$/.test(codeText)) {
      return codeText;
    }
    try {
      return this.jsonFormatter(JSON.parse(codeText));
    } catch (error) {
      return null; 
    }
  }
  copyToClipboard(text: any) {
    this.add();
    navigator.clipboard.writeText(text).then(() => {
      console.log('Text copied to clipboard successfully!');
    }).catch(err => {
    });
  }
 
  add(): void {
    this.alerts.push({
      type: 'success',
      msg: `Code copied to clipboard.`,
      timeout: 2000
    });
  }
 
  onClosed(dismissedAlert: any): void {
    this.alerts = this.alerts.filter((alert) => alert !== dismissedAlert);
  }

  //scroll-page right layout
  toView(id: any) {
    const element = document.getElementById(id);
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
        var offsetPosition = elementPosition + window.pageYOffset - headerOffset;
        const sectionBottom = offsetPosition + sectionElement.offsetHeight;
        if (scrollPosition >= offsetPosition && scrollPosition < sectionBottom && section.id !== 'chat-div-wrap') {
            this.activeLink = section.id;
        }
    });
}
  getTableHeading(section: any, type: any, code: any) {
    return section.find((i: any) => i.key === type)?.value ? section.find((i: any) => i.key === 'name')?.value : code;
  }
  formatTableResponse(section: any) {
    if(!section) return;
    return section.filter((i: any) => i.key !== 'name' && i.key !== 'text');
  }
  formatRequestUrl(section: any) {
    if(!section) return;
    return section.filter((i: any) => i.key !== 'name');
  }
  arrangeRequestValues(arr: any) {
    return this.rightNavSections.map((element) => arr.find((i: any) => i.headline?.entry[0]?.key.includes(element)));
}
  replacer(match: string, pIndent: string, pKey: string, pVal: string, pEnd: string): string {
    const key = '<span class="json-key">';
    const val = '<span class="json-value">';
    const str = '<span class="json-string">';
    let r = pIndent || '';
    if (pKey) {
      r += key + pKey.replace(/[": ]/g, '') + '</span>: ';
    }
    if (pVal) {
      r += (pVal[0] === '"' ? str : val) + pVal + '</span>';
    }
    return r + (pEnd || '');
  }

  jsonFormatter(obj: any): string {
    const jsonLine = /^( *)("[\w]+": )?("[^"]*"|[-\w.+]*|\[\])?([,[{])?$/gm;
    return JSON.stringify(obj, null, 3)
      .replace(/&/g, '&amp;').replace(/\\"/g, '&quot;')
      .replace(/</g, '&lt;').replace(/>/g, '&gt;')
      .replace(jsonLine, this.replacer.bind(this));
  }
}
