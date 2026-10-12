import { ChangeDetectionStrategy, ChangeDetectorRef, Component, DestroyRef, inject, OnInit, signal } from '@angular/core';
import { AuthService, CmsNavigationComponent, CmsService } from '@spartacus/core';
import { CmsComponentData, OutletPosition } from '@spartacus/storefront';
import { Observable, of } from 'rxjs';
import { debounceTime, filter, map, shareReplay } from 'rxjs/operators';
import { WsNavigationNode, WsNavigationService } from '../../ws-services/ws-navigation.service';
import { WsSidebarService } from '../../ws-services/ws-sidebar.service';
import { currentState, WsCurrentUserService } from '../../ws-services/state-services/currentuser.service';
import { BreakpointObserver } from '@angular/cdk/layout';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { RecommendationService } from '../../ws-services/ws-recommendation.service';
import { NavigationEnd, Router } from '@angular/router';

@Component({
  selector: 'ws-navigation',
  templateUrl: './ws-navigation.component.html',
  styleUrls: ['./ws-navigation.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class WsNavigationComponent implements OnInit {
  private curentUser = inject(currentState);
  private destroyRef = inject(DestroyRef);
  wsUser = signal<string>('');

  node$: Observable<WsNavigationNode> = this.service.createNavigation(
    this.componentData.data$
  );

  name$: Observable<string | undefined> = this.componentData.data$.pipe(
    map((d) => d?.navigationNode?.title)
  );

  styleClass$: Observable<string | undefined> = this.componentData.data$.pipe(
    map((d) => d?.styleClass)
  );
  outletPosition = OutletPosition
  componentDataForAccountNav$: Observable<any>;
  nodeTab$: Observable<WsNavigationNode>;
  mobileLeftNav$: Observable<WsNavigationNode>;



  istoggleOpen = false;
  isLoggedIn$: Observable<boolean>;
  public currentUser$ = inject(currentState);
  curUser$: Observable<any>;
  isMobile = false;
  hideMedpaxPage: boolean = false;
  private currentUser = inject(WsCurrentUserService);
  currentUserData$: Observable<any> = this.currentUser.getCurrentUserData();

  constructor(
    protected componentData: CmsComponentData<CmsNavigationComponent>,
    protected cmsservice: CmsService,
    protected service: WsNavigationService,
    protected wsSidebarService: WsSidebarService,
    protected cdr: ChangeDetectorRef,
    protected authService: AuthService,
    private bObserver: BreakpointObserver,
     private recommendationService: RecommendationService,
     private router: Router
  ) {
    this.isLoggedIn$ = authService.isUserLoggedIn();
    this.bObserver.observe('(max-width: 1023px)').subscribe((result) => {
      this.isMobile = false;
      if (result.matches) {
        this.isMobile = true;
      }
    });
    this.currentUserData$
    .pipe(takeUntilDestroyed(this.destroyRef))
    .subscribe({
      next: (data) => {
          this.hideMedpaxPage = data?.hideMedpaxPage;
      },
    })
  }


  ngOnInit(): void {
    this.getComponentData();
    if(this.isMobile) this.getMobileLinks();
    this.curentUser.pipe(takeUntilDestroyed(this.destroyRef)).subscribe((res: string) => {
      this.wsUser.set(res);
    });
    this.router.events
      .pipe(
        filter((event: any) => event instanceof NavigationEnd)
      )
      .subscribe((event: NavigationEnd) => {
        if (event.url === '/') {
          return;
        }
        this.hideMenu();
      });
  }
  
  getLoggedinUser() {
    this.isLoggedIn$.pipe(debounceTime(300), shareReplay(1)).subscribe((response) => {
      if (response) {
        this.recommendationService.getUserDetails().pipe(shareReplay(1)).subscribe((cred) => {
          sessionStorage.setItem('user_config', JSON.stringify(cred));
          this.curUser$ = of(cred);
        });
      }
    })
  }

  getComponentData() {
      return this.cmsservice.getContentSlot('HeaderLinks').subscribe((res: any) => {
        const WWGLatestFeatureComponent = res.components.filter((res: any) => {
          return res.uid == 'MyAccountTabSideNavigationComponent';
        });
        if (WWGLatestFeatureComponent.length) {
          this.componentDataForAccountNav$ = this.cmsservice.getComponentData(
            WWGLatestFeatureComponent[0].uid
          );
          this.nodeTab$ = this.service.createNavigation(this.componentDataForAccountNav$);
          this.cdr.detectChanges();
        }
      });
  }
  getMobileLinks() {
      return this.cmsservice.getContentSlot('NavigationBar').subscribe((res: any) => {
        const WWGLatestFeatureComponent = res.components.filter((res: any) => {
          return res.uid == 'wholescriptCategoryNavComponent';
        });
        if (WWGLatestFeatureComponent.length) {
          this.componentDataForAccountNav$ = this.cmsservice.getComponentData(
            WWGLatestFeatureComponent[0].uid
          );
          this.mobileLeftNav$ = this.service.createNavigation(this.componentDataForAccountNav$);
          this.cdr.detectChanges();
        }
      });
  }

  toggle(node: WsNavigationNode, index: number) {
    this.closeRemainingMenus(index);
    node.active = !node.active;
    this.istoggleOpen = !this.istoggleOpen;
  }

  closeRemainingMenus(index: number) {
    this.node$.subscribe((res) => {
      if (res && res.children && res.children.length > 0) {
        const nodes = res.children[0];
        Object.values(nodes)
          .filter((menu, i) => i !== index && menu.active)
          .forEach((menu) => (menu.active = !menu.active));
      }
    });
  }

  onMouseEnter(node: { active: boolean; }, index: number) {
    this.closeRemainingMenus(index);
    node.active = true;
  }

  onMouseLeave(node: { active: boolean; }, index: number) {
    this.closeRemainingMenus(index);
    node.active = false;
  }

  hideMenu(subMenu?: WsNavigationNode[]) {
    if (!(subMenu)) {
      this.wsSidebarService.changeMenuState(false);
      document.body.style.overflow = '';
    }
  }
}
