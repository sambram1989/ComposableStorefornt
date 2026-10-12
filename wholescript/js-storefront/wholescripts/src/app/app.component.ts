import {
  Component,
  DestroyRef,
  ElementRef,
  inject,
  Renderer2,
} from '@angular/core';
import { OutletPosition } from '@spartacus/storefront';
import { WsOverlay } from './ws-storefront/ws-services/ws-overlay.service';
import { NavigationEnd, NavigationStart, Router } from '@angular/router';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { FavoriteService } from './ws-storefront/ws-services/ws-favorite.service';
import { WsCurrentUserService } from './ws-storefront/ws-services/state-services/currentuser.service';
import { combineLatest, filter, Observable, take } from 'rxjs';
import { DOCUMENT, Location } from '@angular/common';
import { selectedEnv, userMap } from './ws-storefront/ws-env/environment';
import { ActivityLogService } from './ws-storefront/ws-services/ws-activityLog.service';
import { AuthService, UserIdService } from '@spartacus/core';
import { ActiveCartFacade } from '@spartacus/cart/base/root';
import { EMPLOYEE_RESTRICTED_URLS, GUEST_RESTRICTED_URLS, PATIENT_RESTRICTED_URLS, PRACTITIONER_RESTRICTED_URLS } from './ws-storefront/ws-constants/ws-common.constant';

declare var klaviyo: any;
@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss',
})
export class AppComponent {
  title = 'wholescripts';
  afterPosition = OutletPosition.AFTER;
  beforePosition = OutletPosition.BEFORE;
  private overlay = inject(WsOverlay);
  private router = inject(Router);
  private destroyRef = inject(DestroyRef);
  private favoriteService = inject(FavoriteService);
  private document = inject(DOCUMENT);
  private renderer = inject(Renderer2);
  private el = inject(ElementRef);
  private currentUser = inject(WsCurrentUserService);
  private activityLogService = inject(ActivityLogService);

  currentUserRole$: Observable<any> = this.currentUser.currentState();
  currentUserData$: Observable<any> = this.currentUser.getCurrentUserData();
  currentUserDetail$: Observable<any> = this.currentUser.currentUserData$;
  currentUserData: any;
  currentPage: string = window.location.pathname;
  hideMedpaxPage: boolean = false;
  private observer: MutationObserver;
  userRoles: any;

  constructor(private authService: AuthService,private cartService: ActiveCartFacade,  private userIdService: UserIdService) {
    this.getCurrentUser();
  }
  ngOnInit() {
    //this.loadSmarteditInjectorScript();
    this.reloadCartAfterRegistration();
    this.getCartPageRedirection('checkout/ShippingAddresses');
    this.getCartPageRedirection('checkout/ShippingMethods');
    this.getCartPageRedirection('checkout/PaymentMethods');
    this.getCartPageRedirection('checkout/ReviewOrder');
    selectedEnv.analyticsEnable && this.loadKlaviyoScript();
    this.updateRouterEvents();
    this.currentUserRole$.pipe(takeUntilDestroyed(this.destroyRef)).subscribe({
      next: (role) => {
        this.favoriteService
          .fetchFavorites()
          .pipe(takeUntilDestroyed(this.destroyRef))
          .subscribe();
        const bodyClass =
          role.indexOf('practitioner') > -1 ? 'practitioner' : 'patient';
        this.addBodyClass(bodyClass);
        selectedEnv.analyticsEnable && this.loadGTMScript();
      },
      error: () => {
        this.addBodyClass('patient');
        selectedEnv.analyticsEnable && this.loadGTMScript();
      },
    });
    this.currentUserData$.pipe(takeUntilDestroyed(this.destroyRef)).subscribe({
      next: (data) => {
        if (data) {
          this.pushIdentityToKlaviyo(data.email);
        }
      },
    });
    this.activityLogService.initActivityLog();
        this.router.events.subscribe((event) => {
          if (event instanceof NavigationEnd) {
            if (event.url === '/wholescripts-magazine') {
              this.observeForMediaLinks();
            } else {
               this.observer?.disconnect();
            }
          }
          if (event instanceof NavigationStart) {
            this.currentPage = event.url;
            this.roleSpecificPages();
          }
        });
  }

  private observeForMediaLinks(): void {
    if (this.observer) {
      this.observer.disconnect();
    }

    this.observer = new MutationObserver((mutations) => {
      mutations.forEach(() => {
        this.modifyMediaLinks();
      });
    });
    this.observer.observe(document.body, {
      childList: true,
      subtree: true,
    });
    this.modifyMediaLinks();
  }

  private modifyMediaLinks(): void {
    const links = document.querySelectorAll('cx-link cx-generic-link a[href^="/medias"]') as NodeListOf<HTMLAnchorElement>;
  
    links.forEach((link) => {
      const originalHref = link.getAttribute('href');
  
      if (originalHref && !originalHref.startsWith(selectedEnv.occ)) {
        const updatedHref = `${selectedEnv.occ}${originalHref}`;
        link.setAttribute('href', updatedHref);
        link.removeAttribute('routerLink');
        link.removeAttribute('ng-reflect-router-link');
        link.removeAttribute('ng-reflect-query-params');
        link.addEventListener('click', (event) => {
          event.preventDefault();
          event.stopPropagation();
          event.stopImmediatePropagation();
          window.open(updatedHref, '_blank');
        }, true);
      }
    });
  }

  addBodyClass(className: string) {
    this.document.body.classList.add(`ws-${className}`);
  }

  reloadCartAfterRegistration() {
    this.authService.isUserLoggedIn().subscribe(loggedIn => {
      if(loggedIn) {
        combineLatest([
          this.userIdService.getUserId(),
          this.cartService.getActive(),
        ])
          .pipe(
            filter(([userId, cart]) => {
              return (
                userId !== 'anonymous' &&
                cart?.guid &&
                cart?.user?.uid === 'anonymous'
              );
            }),
            take(1)
          )
          .subscribe(() => { });
      }

    })
  }

  loadGTMScript() {
    const script = `(function (w, d, s, l, i) {
        w[l] = w[l] || [];
        w[l].push({ "gtm.start": new Date().getTime(), event: "gtm.js" });
        var f = d.getElementsByTagName(s)[0],
          j = d.createElement(s),
          dl = l != "dataLayer" ? "&l=" + l : "";
        j.async = true;
        j.src = "https://www.googletagmanager.com/gtm.js?id=" + i + dl;
        f.parentNode.insertBefore(j, f);
      })(window, document, "script", "dataLayer", "GTM-MWB533V");`;
    const scripttag = document.createElement('script');
    scripttag.text = script;
    document.getElementsByTagName('head')[0].appendChild(scripttag);
    const noscripttag = document.createElement('noscript');
    const iframe = document.createElement('iframe');
    iframe.src = `https://www.googletagmanager.com/ns.html?id=GTM-MWB533V`;
    iframe.style.display = 'none';
    iframe.style.visibility = 'hidden';
    iframe.width = '0';
    iframe.height = '0';
    noscripttag.appendChild(iframe);
    document.getElementsByTagName('body')[0].appendChild(noscripttag);
  }

  loadKlaviyoScript() {
    const script = document.createElement('script');
    script.src = `https://static.klaviyo.com/onsite/js/klaviyo.js?company_id=${selectedEnv.klaviyo}`;
    script.async = true;
    document.getElementsByTagName('head')[0].appendChild(script);
  }

  loadSmarteditInjectorScript() {
    const script = document.createElement('script');
    script.src = `/assets/js/webApplicationInjector.js`;
    script.setAttribute('type', 'text/javascript');
    script.setAttribute('id', 'smartedit-injector');
    script.setAttribute(
      'data-smartedit-allow-origin',
      selectedEnv.smartEditOrigin
    );
    script.async = true;
    document.getElementsByTagName('head')[0].appendChild(script);
  }

  pushIdentityToKlaviyo(email: string) {
    if (typeof klaviyo !== 'undefined' && klaviyo) {
      // Set identity if customer email is found
      if (email !== null) {
        klaviyo.push(['identify', { $email: email }]);
      }
    } else {
      console.error('Klaviyo library not loaded.');
    }
  }

  getCurrentUser(): void {
    this.currentUserDetail$
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: (data) => {
          if (data) {
            this.currentUserData = data;
            const userRole = this.getUserRole(data?.roles || []);

            setTimeout(() => {
              this.updateLogoUrl();
            }, 3000);

            window.location.pathname === '/' && this.homeRedirection();

            // Fetch user favorites
            this.favoriteService
              .fetchFavorites()
              .pipe(takeUntilDestroyed(this.destroyRef))
              .subscribe();

            // Add appropriate body class
            let bodyClass = userRole.includes('practitioner')
              ? 'practitioner'
              : 'patient';
            this.addBodyClass(bodyClass);


            this.userRoles = this.getUserRoles(data?.roles || []);

            this.roleSpecificPages();
            this.pushIdentityToKlaviyo(data.email);
          } else {
            this.checkPageAccess('guest');
          }
          // Load GTM and other tracking
          selectedEnv.analyticsEnable && this.loadGTMScript();
        },
        error: () => {
          this.checkPageAccess('patient');
          selectedEnv.analyticsEnable && this.loadGTMScript();
        },
      });
  }

  roleSpecificPages() {
    if (this.currentUserData) {
      if (this.userRoles?.length > 0) {
        this.userRoles?.map((role: any) => {
          this.checkPageAccess(role);
        })
      } else {
        this.checkPageAccess('patient');
      }
    } else {
      this.checkPageAccess('guest');
    }
  }

  checkPageAccess(userRole: string) {
    switch (userRole) {
      case 'doctor':
      case 'employee':
      case 'friendsfamily':
      case 'salesrep':
      case 'distributorcustomer':
      case 'studentcustomer':
        EMPLOYEE_RESTRICTED_URLS?.map(url => {
          this.getPageRedirection(url);
        })
        break;
      case 'practitioner':
        PRACTITIONER_RESTRICTED_URLS?.map(url => {
          this.getPageRedirection(url);
        });
        break;
      case 'patient':
        PATIENT_RESTRICTED_URLS?.map(url => {
          this.getPageRedirection(url);
        });
        break;
      case 'guest':
        GUEST_RESTRICTED_URLS?.map(url => {
          this.getLoginPageRedirection(url);
        });
        break;
      default:
        break;
    }
  }
  getPageRedirection(type: string) {
    if (this.currentPage.includes(type)) {
      window.location.replace('/not-found');
    }
  }
  
  getCartPageRedirection(type: string) {
    if (this.currentPage.includes(type)) {
      this.router.navigate(['/cart']);
    }
  }

  getLoginPageRedirection(type: string) {
    if (this.currentPage.includes(type)) {
      this.router.navigate(['/login']);
    }
  }

  private getUserRoles(roles: string[]): string[] {
    let currentRoles: string[] = [];
    for (const role of roles) {
      if (userMap[role]) {
        currentRoles.push(userMap[role]);
      }
    }
    return currentRoles;
  }

  private getUserRole(roles: string[]): string {
    let currentRoles = '';
    for (const role of roles) {
      if (userMap[role]) {
        currentRoles += userMap[role];
      }
    }
    return currentRoles;
  }

  updateLogoUrl(): void {
    if (!this.currentUserData) return;

    const existingAnchor = this.el.nativeElement.querySelector('.SiteLogo a');
    if (existingAnchor) {
      const parent = existingAnchor.parentNode;

      const newAnchor = this.renderer.createElement('a');
      this.renderer.setStyle(newAnchor, 'cursor', 'pointer');
      this.renderer.listen(newAnchor, 'click', () => {
        this.router.navigate([this.currentUserData?.rootUrl || '/']);
      });

      this.renderer.insertBefore(parent, newAnchor, existingAnchor);
      this.renderer.removeChild(parent, existingAnchor);
    }
  }

  updateRouterEvents() {
    this.router.events
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe((event: any) => {
        if (event.url?.includes('logout')) {
          this.currentUserData.rootUrl = '/';
          sessionStorage.removeItem('user_config')
        }
        if (event instanceof NavigationEnd) {
          this.overlay.setBodyOverflow(false);
        }
        this.updateLogoUrl();
       
      });
  }

  homeRedirection() {
    const user = JSON.parse(sessionStorage.getItem('user_config'));
    if (user && user.rootUrl) {
      this.router.navigate([user.rootUrl]);
    } else {
      this.router.navigate(['/']);
    }
  }
}
