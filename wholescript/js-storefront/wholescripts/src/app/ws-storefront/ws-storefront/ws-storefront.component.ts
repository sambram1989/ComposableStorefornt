import { ChangeDetectionStrategy, ChangeDetectorRef, Component, ElementRef, HostListener, inject, OnInit, Renderer2 } from '@angular/core';
import { AuthService, RoutingService } from '@spartacus/core';
import { Router , NavigationEnd, NavigationStart } from '@angular/router';
import { HamburgerMenuService, KeyboardFocusService, OutletPosition, StorefrontComponent } from '@spartacus/storefront';
import { debounceTime, Observable, of, shareReplay } from 'rxjs';
import { WsSidebarService } from '../ws-services/ws-sidebar.service';
import { RecommendationService } from '../ws-services/ws-recommendation.service';
import { currentState } from '../ws-services/state-services/currentuser.service';
import { AsmEnablerService } from '@spartacus/asm/root';
import { ActiveCartFacade } from '@spartacus/cart/base/root';
@Component({
  selector: 'ws-storefront',
  styleUrls: ['./ws-storefront.component.scss'],
  templateUrl: './ws-storefront.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class WsStorefrontComponent extends StorefrontComponent implements OnInit {
  public currentUser$ = inject(currentState);
  isLoggedIn$: Observable<boolean>;
  outletPosition = OutletPosition;
  wsSidebarService: WsSidebarService;
  menuToggle$!: Observable<Boolean>;
  isMobile: boolean;
  curUser$: Observable<any>;
  loader: boolean = false;

  @HostListener('window:resize', ['$event'])
    onResize(event: Event) {
      this.checkIfMobile();
    }

    private checkIfMobile(): void {
      const width = window.innerWidth;

      // You can also add user agent detection here if needed
      this.isMobile = width <= 991; // You can adjust the width threshold
    }
  constructor(
    wsSidebarService: WsSidebarService,
    hamburgerMenuService: HamburgerMenuService,
    routingService: RoutingService,
    elementRef: ElementRef<HTMLElement>,
    keyboardFocusService: KeyboardFocusService,
     private renderer: Renderer2,
    protected asmEnablerService: AsmEnablerService,
    authService: AuthService,
    private router: Router,
    private recommendationService: RecommendationService,
    private cd: ChangeDetectorRef,
    private activeCartService: ActiveCartFacade
  ) {
    super(hamburgerMenuService, routingService, elementRef, keyboardFocusService
    );
    this.isLoggedIn$ = authService.isUserLoggedIn();
    this.wsSidebarService = wsSidebarService;
    this.menuToggle$ = this.wsSidebarService.loadMenuState$();
    this.getLoggedinUser();
    this.checkIfMobile();
      this.activeCartService.getLoading().subscribe(flag => {
        if (!flag) {
          this.loader = false;
        } else if(window.location.pathname === '/cart') {
          this.loader = true;
        }
        this.cd.markForCheck();
      })
  }

  override ngOnInit(): void {
    this.router.events.subscribe(event => {
      if (event instanceof NavigationEnd) {
        const container = document.getElementById('idForContentBlock');
        if (container) {
          var headerOffset = 78;
          var elementPosition = container.getBoundingClientRect().top;
          var offsetPosition = elementPosition + window.scrollY - headerOffset;
          container.scrollTo({ top: offsetPosition, behavior: 'smooth' });
        }
      }
      if(event instanceof NavigationStart) {
        if(event?.url === '/cart') {
          this.loader = true;
          this.cd.detectChanges();
        }
      }
    });
  }

  ngAfterViewInit(): void {
    const waitForPagination = setInterval(() => {
      const targetNode = this.elementRef.nativeElement.querySelector('cx-tab-paragraph-container');
      if (targetNode) {
        clearInterval(waitForPagination); 
        const observer = new MutationObserver(() => {
          this.removeSearchTabs();
        });
        observer.observe(targetNode, { childList: true, subtree: true });
        this.removeSearchTabs(); 
      }
    }, 200); 
   // add title to chat iframe - ada 
    const chatIframe = setInterval(() => {
      const targetNode = document.querySelector('.button-iframe-wrap');
      if (targetNode) {
        clearInterval(chatIframe); 
        const observer = new MutationObserver(() => {
          this.addTitleToIframe();
        });
        observer.observe(targetNode, { childList: true, subtree: true });
        this.addTitleToIframe(); 
      }
    }, 200); 
  }

  addTitleToIframe() {
      const iframe = document.querySelector('.button-iframe-wrap > iframe#icChatButton');
      if (iframe) {
        iframe.setAttribute('title', 'Chat with us'); 
      }
  }

  removeSearchTabs() {
    const searchTabs = this.elementRef.nativeElement.querySelector('.SearchBox cx-tab-paragraph-container');
    if (searchTabs) {
     this.renderer.removeChild(searchTabs.parentNode, searchTabs);
    } 
  }
  showMenu() {
    this.wsSidebarService.changeMenuState(true);
    document.body.style.overflow = 'hidden';
  }
  getLoggedinUser() {
    this.isLoggedIn$.pipe(debounceTime(300), shareReplay(1)).subscribe((response) => {
      if(response) {
        this.recommendationService.getUserDetails().pipe(shareReplay(1)).subscribe((cred) => {
          sessionStorage.setItem('user_config', JSON.stringify(cred));
          this.curUser$ = of(cred);
        });
      }
      })
  }

  onOverlayClick(event: MouseEvent): void {
    event.stopPropagation();
    this.wsSidebarService.changeMenuState(false);
     document.body.style.overflow = '';
  }
}

