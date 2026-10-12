import {
  AfterViewInit,
  ChangeDetectorRef,
  Component,
  DestroyRef,
  inject,
  OnDestroy,
  OnInit,
} from '@angular/core';
import { CmsService } from '@spartacus/core';
import { Subject } from 'rxjs';
import { WsPage } from '../../../ws-services/ws-page.service';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { Router } from '@angular/router';
import { BreakpointObserver } from '@angular/cdk/layout';

@Component({
  selector: 'ws-account-settings',
  templateUrl: './ws-account-settings.component.html',
  styleUrl: './ws-account-settings.component.scss',
})
export class WsAccountSettingsComponent implements OnInit, OnDestroy, AfterViewInit {
  accountSettingsContent: { [k: string]: any } = {};
  private ngUnsubscribe = new Subject<void>();
  private destroyRef = inject(DestroyRef);
  componentListArray: any = [];
  isMobile = false;

  isPasswordTab = window.location.href.toLowerCase().includes('password');
  isNotificationTab = window.location.href
    .toLowerCase()
    .includes('notifications');

  constructor(
    public wsPageService: WsPage,
    public cmsService: CmsService,
    private router: Router,
    private bObserver: BreakpointObserver,
  ) {
    this.getCmsdata();
    this.bObserver.observe('(max-width: 599px)').subscribe((result) => {
      this.isMobile = false;
      if (result.matches) {
        this.isMobile = true;
      }
    });
  }

  ngOnInit(): void {
    this.router.events
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe((val) => {
        this.isPasswordTab = window.location.href
          .toLowerCase()
          .includes('password');
        this.isNotificationTab = window.location.href
          .toLowerCase()
          .includes('notifications');
      });
  }

  ngAfterViewInit(): void {
    if(document.getElementsByTagName('title') && !document.getElementsByTagName('title')[0].innerText) document.getElementsByTagName('title')[0].innerText = 'Account Settings';
  }

  getCmsdata() {
    const component = window.location.href.includes('patient')
      ? 'PatientAccountSettingsTabContainer'
      : 'PractitionerAccountSettingsTabContainer';
    this.cmsService
      .getComponentData(component)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe((data: any) => {
        const componentList = data.components?.split(' ');
        this.componentListArray = componentList;
      });
  }

  getActiveClass(getComponentData: any) {
    const currentUrl = window.location.pathname;
    if(currentUrl === '/accounts-settings' && getComponentData?.url === '/accounts-settings/Addresses') {
      return true;
    } else if(currentUrl === '/patient/accounts-settings' && getComponentData?.url === '/patient/accounts-settings/Addresses') {
      return true;
    }
    return currentUrl === getComponentData?.url;
  }

  navigateTo(e: any) {
    const url = e?.target?.value;
    this.router.navigateByUrl(url);
  }

  ngOnDestroy() {
    this.ngUnsubscribe.next();
    this.ngUnsubscribe.complete();
  }
}
