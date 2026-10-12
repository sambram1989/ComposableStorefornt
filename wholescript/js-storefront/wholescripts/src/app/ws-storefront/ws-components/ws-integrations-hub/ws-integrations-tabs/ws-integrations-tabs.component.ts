import { ChangeDetectorRef, Component, DestroyRef, inject, OnDestroy, OnInit } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { Router } from '@angular/router';
import { CmsService } from '@spartacus/core';
import { Subject } from 'rxjs';
import { WsPage } from '../../../ws-services/ws-page.service';

@Component({
  selector: 'ws-integrations-tabs',
  templateUrl: './ws-integrations-tabs.component.html',
  styleUrl: './ws-integrations-tabs.component.scss',
})
export class WsIntegrationsTabsComponent implements OnInit, OnDestroy {
  integrationsContent: { [k: string]: any } = {};
  private ngUnsubscribe = new Subject<void>();
  private destroyRef = inject(DestroyRef);
  componentListArray: any = [];

  constructor(
    public wsPageService: WsPage,
    private cd: ChangeDetectorRef,
    public cmsService: CmsService,
    private router: Router
  ) {
    this.getCmsdata();
  }

  ngOnInit(): void {
  }

  getCmsdata() {
    this.cmsService
      .getComponentData('IntegrationsHubTabContainer')
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe((data: any) => {
        const componentList = data.components?.split(' ');
        this.componentListArray = componentList;
      });
  }

  getActiveClass(getComponentData: any) {
    const currentUrl = window.location.pathname;
    if(currentUrl === '/integrations-hub' && getComponentData?.url === '/integrations-hub/estore-settings') {
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
