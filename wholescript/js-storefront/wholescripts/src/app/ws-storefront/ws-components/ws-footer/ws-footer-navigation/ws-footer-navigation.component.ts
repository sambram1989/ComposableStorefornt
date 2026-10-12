import { ChangeDetectionStrategy, Component, DestroyRef, inject } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { Router } from '@angular/router';
import { CmsService } from '@spartacus/core';
import { NavigationService } from '@spartacus/storefront';
import { Observable } from 'rxjs';
import { WsCurrentUserService } from '../../../ws-services/state-services/currentuser.service';
declare global {
  interface Window {
    Calendly?: any;
  }
}

@Component({
  selector: 'ws-footer-navigation',
  templateUrl: './ws-footer-navigation.component.html',
  styleUrls: ['./ws-footer-navigation.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class WsFooterNavigationComponent {
  componentDataForAccountNav$: Observable<any>;
  nodeTab$: Observable<any>;
  linkHeadings: any[] = [];
  FooterNav: any[];
  addressSlot: any[];
  wholescripts$: Observable<any> = this.cmsService.getComponentData(
    'FooterParaComponent-Copyright'
  );
  disclaimer$: Observable<any> = this.cmsService.getComponentData(
    'FooterParaComponent-Disclaimer'
  );
  socialIcons: any[];
  private destroyRef = inject(DestroyRef);
  Calendly: any;
  currentYear = new Date().getFullYear();
  private currentUser = inject(WsCurrentUserService);
  isPractitioner: boolean = false;

  constructor(
    private service: NavigationService,
    public cmsService: CmsService,
    private router: Router
  ) {
    this.getComponentData();
    this.currentUser.currentState().pipe(takeUntilDestroyed(this.destroyRef)).subscribe((res) => {
      if(res && res[0] == "practitioner") this.isPractitioner = true;
    })
  }
  getComponentData() {
    this.cmsService.getContentSlot('FooterSocialIconSlot').pipe(takeUntilDestroyed(this.destroyRef)).subscribe((res) => this.socialIcons = res?.components);
    this.cmsService.getContentSlot('FooterAddressAndPhoneinfoSlot').pipe(takeUntilDestroyed(this.destroyRef)).subscribe((res: any) => this.addressSlot = res?.components);
    this.cmsService.getContentSlot('BottomFooterSlot').pipe(takeUntilDestroyed(this.destroyRef)).subscribe((res: any) => this.FooterNav = res?.components);
    return this.cmsService.getContentSlot('Footer').pipe(takeUntilDestroyed(this.destroyRef)).subscribe((res: any) => {
      const WWGLatestFeatureComponent = res.components.filter((res: any) => {
        return res.uid == 'wholescriptFooterNavComponent';
      });
      if (WWGLatestFeatureComponent.length) {
        this.componentDataForAccountNav$ = this.cmsService.getComponentData(
          WWGLatestFeatureComponent[0].uid
        );
        this.nodeTab$ = this.service.createNavigation(
          this.componentDataForAccountNav$
        );
        this.componentDataForAccountNav$.pipe(takeUntilDestroyed(this.destroyRef)).subscribe((res) => {
          this.linkHeadings = res?.navigationNode?.children.map(
            (item: any) => item.title
          );
        });
      }
    });
  }
  getLinkNavigation(link: any) {
    if(link.includes('https:')) window.open(link, '_blank').focus();
    else this.router.navigateByUrl(link);

  }
  openDemoCalender() {
    this.Calendly = window['Calendly'];
    if (this.Calendly) {  
    this.Calendly.initPopupWidget({url:'https://calendly.com/wholescripts-customer-service/help?primary_color=584091'});
    }
  }
}
