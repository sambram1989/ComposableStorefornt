import {
  ChangeDetectorRef,
  Component,
  DestroyRef,
  inject,
  OnInit,
} from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { AuthService, CmsService } from '@spartacus/core';
import { Observable, of, take, tap } from 'rxjs';
import { PaymentService } from '../../account-setting/payment-service/payment.service';
import { Router } from '@angular/router';
import { userMap } from '../../../ws-env/environment';

@Component({
  selector: 'ws-order-history-tabs',
  templateUrl: './ws-order-history-tabs.component.html',
  styleUrl: './ws-order-history-tabs.component.scss',
})
export class WsOrderHistoryTabsComponent implements OnInit {
  componentListArray: any = [];

  curUser: any;
  isLoggedIn$: Observable<any>;
  invoiceHeadingParagraph$: Observable<any> = this.cmsService.getComponentData(
    'wsOrderHistoryInvoicesBodyParaComponent'
  );
  isPractioner: boolean;
  activeTabId: string = 'wsOrderHistoryTabAllOrderLinkComponent';
  startDate: Date = new Date(new Date().getFullYear(), 0, 1);
  endDate: Date = new Date();
  firstDate: Date = new Date(new Date().getFullYear(), 0, 1);
  lastDate: Date = new Date();
  searchKey = '';
  loading = true;
  private destroyRef = inject(DestroyRef);
  private paymentService = inject(PaymentService);

  constructor(
    private cd: ChangeDetectorRef,
    public cmsService: CmsService,
    private authService: AuthService,
    private router: Router
  ) {
    this.isLoggedIn$ = this.authService.isUserLoggedIn();
    // this.getCmsdata();
  }

  ngOnInit(): void {
    if (this.paymentService.isAgingTab()) {
      this.changeTab('wsOrderHistoryAgingParaComponent');
    }

    const user = JSON.parse(sessionStorage.getItem('user_config'));
    if(user) {
        const key = Object.keys(userMap).find(key => userMap[key] === 'practitioner');
        this.isPractioner = (user?.roles || []).includes(key);
        this.curUser = user
      }
    this.getCmsdata();
  }

  getCmsdata() {
    this.cmsService
      .getComponentData('wsOrderHistoryTabContainer')
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe((data: any) => {
        const componentList = data.components?.split(' ');
        this.componentListArray = componentList;

        const currentUrl = this.router.url;

        componentList.forEach((compId: string) => {
          this.cmsService
            .getComponentData(compId)
            .pipe(take(1))
            .pipe(takeUntilDestroyed(this.destroyRef))
            .subscribe((compData: any) => {
              if (currentUrl.includes(compData?.url)) {
                this.activeTabId = compData?.uid;
                this.cd.detectChanges();
              }
            });
        });
      });

  }

  changeTab(component: any) {
    this.activeTabId = component.uid;
    this.router.navigate([component.url])
  }

  changeSelectedTab(e: any) {
    const url = e?.target?.value;
    this.router.navigateByUrl(url);
    this.cd.detectChanges();
  }

  setActiveTab(uid: string) {
    this.activeTabId = uid;
  }

  showingAgingTab(componentUid: string): boolean {
    const paymentCode = this.curUser?.paymentTerms?.code?.toLowerCase() || '';
  
    if (componentUid === 'wsOrderHistoryAgingLinkComponent') {
      return paymentCode.startsWith('nt'); 
    }
  
    return true; 
  }

}
