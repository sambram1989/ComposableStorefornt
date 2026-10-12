import {
  AfterViewInit,
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component,
  computed,
  DestroyRef,
  inject,
  OnInit,
} from '@angular/core';
import {
  BreadcrumbMeta,
  CmsService,
  isNotNullable,
  PageType,
  RoutingService,
  TranslationService,
} from '@spartacus/core';
import { Title } from '@angular/platform-browser';
import { Observable, combineLatest, delay, filter, map, of } from 'rxjs';
import { WsPageMetaService } from './resolvers/page-meta.service';
import { WebinarlistService } from '../../../ws-webinars/webinarlist.service';
import { ProtocolService } from '../../../../ws-services/protocol/protocol.service';
import { WsCurrentUserService } from '../../../../ws-services/state-services/currentuser.service';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { NavigationEnd, Router } from '@angular/router';

const checkout = [
  {
    url: '/checkout/ShippingAddresses',
    breadcrumb: 'Shipping Address',
  },
  {
    url: '/checkout/ShippingMethods',
    breadcrumb: 'Shipping Method',
  },
  {
    url: '/checkout/PaymentMethods',
    breadcrumb: 'Payment Method',
  },
  {
    url: '/checkout/ReviewOrder',
    breadcrumb: 'Order Review',
  },
  {
    url: '/checkout/completed',
    breadcrumb: 'Order Confirmation',
  },
];
const cart = [
  {
    url: '/cart',
    breadcrumb: 'Cart',
  },
];

const webinars = [
  {
    url: '/webinars/list',
    title: 'Webinars',
  },
  {
    url: '/presenters/',
    title: '',
    breadcrumb: [
      {
        label: 'Dashboard',
        link: '/',
      },
      {
        label: 'Webinars',
        link: '/webinars/list',
      },
    ],
  },
  {
    url: '/webinars/',
    title: '',
    breadcrumb: [
      {
        label: 'Dashboard',
        link: '/',
      },
      {
        label: 'Webinars',
        link: '/webinars/list',
      },
    ],
  },
];

const accountSetting = [
  {
    url: '/order/history',
    title: 'Order History',
    breadcrumb: [
      {
        label: 'Dashboard',
        link: '/',
      },
    ],
  },
  {
    url: '/order/details',
    title: 'Order Details',
    breadcrumb: [
      {
        label: 'Dashboard',
        link: '/',
      },
      {
        label: 'Order history',
        link: '/order/history',
      },
    ],
  },
  {
    url: '/autoships',
    title: 'Autoships',
    breadcrumb: [
      {
        label: 'Dashboard',
        link: '/',
      }
    ],
  },
  {
    url: '/autoships-details',
    title: 'Autoships Details',
    breadcrumb: [
      {
        label: 'Dashboard',
        link: '/',
      },
      {
        label: 'Autoship List',
        link: '/autoships-list',
      },
    ],
  },
  {
    url: '/practitioner/patient-direct',
    title: 'Patient Direct Ordering',
    breadcrumb: [
      {
        label: 'Dashboard',
        link: '/',
      },
    ],
  },
  {
    url: '/practitioner/resell',
    title: 'Reselling',
  },
];

const protocol = [
  {
    url: '/protocol/list',
    breadcrumb: 'My Protocols',
  },
  {
    url: '/protocol/edit',
    breadcrumb: 'New Protocol',
  },
  {
    url: '/protocol/edit/carton',
    breadcrumb: 'Select Carton',
  },
  {
    url: '/protocol/edit/medpax/review',
    breadcrumb: 'MedPax Review',
  },
  {
    url: '/protocol/edit/review/',
    breadcrumb: 'Protocol Review',
  },
  {
    url: '/protocol/complete',
    breadcrumb: '',
  },
];

const recommendation = [
  {
    url: '/Recommendation/PractitionerRecommendations',
    breadcrumb: 'Recommendations',
  },
  {
    url: '/SearchPatientAdvanced/newrecommendation',
    breadcrumb: 'Select Patient',
  },
  {
    url: '/Recommendation/BasicRecommendation',
    breadcrumb: 'New Recommendation',
  },
  {
    url: '/recommendation/edit/medpax/review/PrivateLabelSelectCarton',
    breadcrumb: 'Select Carton',
  },
  {
    url: '/recommendation/edit/medpax/review',
    breadcrumb: 'Review Your MedPax',
  },
  {
    url: '/Recommendation/RecommendationReview',
    breadcrumb: 'Review Recommendation',
  },
  {
    url: '/Recommendation/RecommendationComplete',
    breadcrumb: '',
  },
];

const medpax = [
  {
    url: '/medpax',
    breadcrumb: 'Medpax',
  },
  {
    url: '/medpax/MedPaxSelectCarton',
    breadcrumb: 'Select Carton',
  },
  {
    url: '/medpax/review',
    breadcrumb: 'Review Your MedPax',
  },
];

const apiResource = [
  {
    url: '/api/',
    breadcrumb: [
      {
        label: 'Dashboard',
        link: '/',
      },
      {
        label: 'API Resource',
        link: '/api',
      },
    ]
  }
];

const patientprofile = [
  {
    url: '/practitioner/patientprofile',
    breadcrumb: [
      {
        label: 'My Patients',
        link: '/SearchPatientAdvanced',
      }
    ]
  }
];

const myProtocol = [
  {
    url: '/protocol/details',
    breadcrumb: [
      {
        label: 'My Protocol',
        link: '/protocol/list',
      },
      {
        label: 'WholeScripts Protocol',
        link: '/wsprotocols/list',
      }
    ]
  }
]

@Component({
  selector: 'cx-breadcrumb',
  templateUrl: './breadcrumb.component.html',
  styleUrl: './breadcrumb.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class WsBreadcrumbComponent implements OnInit, AfterViewInit {
  crumbs$: Observable<any[]>;
  title$: Observable<string>;
  lastestTitle$: Observable<string>;
  private protocolService = inject(ProtocolService);
  curuserService = inject(WsCurrentUserService);
  private destroyRef = inject(DestroyRef);
  pluser = false;
  isMedpax = computed(() => this.protocolService.isMedPaxPage());
  isBackToDashboard = false;
  constructor(
    private cmsService: CmsService,
    protected ngTitle: Title,
    protected pageMetaService: WsPageMetaService,
    private translation: TranslationService,
    protected routingService: RoutingService,
    protected webinarlistService: WebinarlistService,
    private cd: ChangeDetectorRef,
    private router: Router
  ) {
    this.checkPluserRole();
  }

  ngOnInit(): void {
    this.routingService.getRouterState().subscribe((state) => {
      this.setTitle();
      this.setCrumbs();
      const bIndex = checkout.findIndex((c) => c.url === state.state.url);
      if (bIndex > -1) {
        const breadcrumbs: BreadcrumbMeta[] = [];
        breadcrumbs.push({ label: 'Cart', link: '/' } as BreadcrumbMeta);
        const bb = checkout.slice(0, bIndex + 1);

        bb.forEach((b) => {
          breadcrumbs.push({
            label: b.breadcrumb,
            link: b.url,
          } as BreadcrumbMeta);
        });
        this.crumbs$ = of(breadcrumbs);
      }
      const pIndex = protocol.findIndex((c) => {
        const regex = new RegExp(`^${c.url.replace(/\/$/, '')}(\\/\\d+)?$`);
        return regex.test(state.state.url);
      });
      if (pIndex > -1) {
        if (pIndex === 5) {
          this.title$ = of('');
          this.crumbs$ = of([]);
          return;
        }
        const breadcrumbs: BreadcrumbMeta[] = [];
        breadcrumbs.push({
          label: this.isPatient() ? 'Shop' : 'Dashboard',
          link: this.getHomeUrl(),
        } as BreadcrumbMeta);
        const bb = protocol.slice(0, pIndex + 1);
        let paramindex = 1;
        if (!this.isMedpax()) {
          bb.splice(2, 2);
          paramindex = 1;
        }
        if (this.isMedpax() && !this.pluser) bb.splice(2, 1);
        bb.forEach((b, index) => {
          if (index === bb.length - 1) {
            this.title$ = of(b.breadcrumb);
          } else {
            breadcrumbs.push({
              label: b.breadcrumb,
              link:
                b.url +
                (index >= paramindex && state.state.params['id']
                  ? '/' + state.state.params['id']
                  : ''),
            } as BreadcrumbMeta);
          }
        });

        this.crumbs$ = of(breadcrumbs);
      }
      const rIndex = recommendation.findIndex((c) => {
        const regex = new RegExp(`^${c.url.replace(/\/$/, '')}(\\/\\d+)?$`);
        return regex.test(state.state.url);
      });
      if (rIndex > -1) {
        if (rIndex === 6) {
          this.title$ = of('');
          this.crumbs$ = of([]);
          return;
        }

        const bb = recommendation.slice(0, rIndex + 1);
        if (
          this.isMobile() &&
          bb.length > 0 &&
          (bb[0].breadcrumb === 'Recommendations' || bb[0].breadcrumb === 'New Recommendations')
        ) {
          this.isBackToDashboard = true;
          this.crumbs$ = of([
            {
              label: 'Back to Dashboard',
              link: this.getHomeUrl(),
            } as BreadcrumbMeta,
          ]);
          this.router.events.subscribe((event) => {
            if (event instanceof NavigationEnd) {
              if (this.router.url.endsWith('/dashboard')) {
                this.isBackToDashboard = false;
              }
            }
          });
          this.title$ = of('');
          return;
        }
        const breadcrumbs: BreadcrumbMeta[] = [];
        breadcrumbs.push({
          label: this.isPatient() ? 'Shop' : 'Dashboard',
          link: this.getHomeUrl(),
        } as BreadcrumbMeta);

        if (!this.isMedpax()) bb.splice(3, 2);
        if (this.isMedpax() && !this.pluser) bb.splice(3, 1);

        bb.forEach((b, index) => {
          if (index === bb.length - 1) {
            this.title$ = of(b.breadcrumb);
          } else {
            breadcrumbs.push({
              label: b.breadcrumb,
              link:
                b.url +
                (index >= 2 && state.state.params['id']
                  ? '/' + state.state.params['id']
                  : ''),
            } as BreadcrumbMeta);
          }
        });
        // Optional custom logic for rIndex >= 3
        if (rIndex >= 3) {
          // breadcrumbs.splice(2, 1);
        }

        this.crumbs$ = of(breadcrumbs);
      }

      //medpax
      const mIndex = medpax.findIndex((c) => {
        const regex = new RegExp(`^${c.url.replace(/\/$/, '')}(\\/\\d+)?$`);
        return regex.test(state.state.url);
      });
      if (mIndex > -1) {
        const breadcrumbs: BreadcrumbMeta[] = [];
        breadcrumbs.push({
          label: this.isPatient() ? 'Shop' : 'Dashboard',
          link: this.getHomeUrl(),
        } as BreadcrumbMeta);
        const bb = medpax.slice(0, mIndex + 1);
        if (!this.pluser) bb.splice(1, 1);
        bb.forEach((b, index) => {
          if (index === bb.length - 1) {
            this.title$ = of(b.breadcrumb);
          } else {
            breadcrumbs.push({
              label: b.breadcrumb,
              link:
                b.url +
                (index === 1 && state.state.params['id']
                  ? '/' + state.state.params['id']
                  : ''),
            } as BreadcrumbMeta);
          }
        });
        this.crumbs$ = of(breadcrumbs);
      }
      const cIndex = cart.findIndex((c) => c.url === state.state.url);
      if (cIndex > -1) {
        const breadcrumbs: BreadcrumbMeta[] = [];
        breadcrumbs.push({ label: 'Cart', link: '/' } as BreadcrumbMeta);
        this.crumbs$ = of(breadcrumbs);
        this.title$ = of('');
        this.cd.detectChanges();
      }
      const webinarsListIndex = webinars.findIndex(
        (c) => state.state.url.indexOf(c.url) > -1
      );
      if (webinarsListIndex > -1) {
        const webinar = webinars[webinarsListIndex];
        if (webinar) {
          const title = webinars[webinarsListIndex]?.title;
          if (webinar.url === '/presenters/') {  // it's presenters page
            this.title$ = title === '' ? this.webinarlistService.presenter$ : of(title);
          } else if (webinar.url === '/webinars/') { // it's webinar details page
            this.title$ = title === '' ? this.webinarlistService.webinarDetails$ : of(title);
          }
        }

        if (webinars[webinarsListIndex]?.breadcrumb) {
          this.crumbs$ = of(webinars[webinarsListIndex].breadcrumb);
        }
      }
      this.setAccountCrumb(state);
      this.setApiResourceCrumb(state);
      this.setPatientProfileCrumb(state);
      this.cd.detectChanges();
    });
  }

  ngAfterViewInit(): void {
    this.lastestTitle$ = this.title$;
  }

  private setAccountCrumb(state: any): void {
    const accountSettingIndex = accountSetting.findIndex(
      (c) => state.state.url.indexOf(c.url) > -1
    );
    if (accountSettingIndex > -1) {
      if (
        accountSetting[accountSettingIndex]?.title ||
        accountSetting[accountSettingIndex]?.title === ''
      ) {
        this.title$ =
          accountSetting[accountSettingIndex]?.title === ''
            ? this.webinarlistService.presenter$
            : of(accountSetting[accountSettingIndex].title);
      }
      if (accountSetting[accountSettingIndex]?.breadcrumb) {
        this.crumbs$ = of(
          accountSetting[accountSettingIndex].breadcrumb.map((item) => {
            if (item.label === 'Dashboard') {
              return this.isPatient()
                ? { ...item, label: 'Shop', link: this.getHomeUrl() }
                : { ...item, link: this.getHomeUrl() };
            }
            return item;
          })
        );
      }
    }
  }

  setApiResourceCrumb(state: any): void {
    const apiResourceIndex = apiResource.findIndex(
      (c) => state.state.url.indexOf(c.url) > -1
    );
    if (apiResourceIndex > -1) {
      if (apiResource[apiResourceIndex]?.breadcrumb) {
        this.crumbs$ = of(
          apiResource[apiResourceIndex].breadcrumb
        );
      }
    }
  }

  setPatientProfileCrumb(state: any): void {
    const patientProfileIndex = patientprofile.findIndex(
      (c) => state.state.url.indexOf(c.url) > -1
    );
    if (patientProfileIndex > -1) {
      if (patientprofile[patientProfileIndex]?.breadcrumb) {
        this.crumbs$ = this.crumbs$.pipe(
          map((crumbs) => [
            ...crumbs,
            ...patientprofile[patientProfileIndex].breadcrumb,
          ])
        );
      }
    }
  }

  private setTitle(): void {
    this.title$ = this.pageMetaService.getMeta().pipe(
      filter(isNotNullable),
      map((meta) => (meta.heading || meta.title) ?? ''),
      filter((title) => {
        this.isPdpPage()
          .pipe(delay(250))
          .subscribe((isPdp) => {
            // isPdp && this.ngTitle.setTitle(title || '');
          });
        return (
          title.toLocaleLowerCase() !== 'cart' &&
          title.toLocaleLowerCase() !== 'checkout' &&
          title.toLocaleLowerCase() !== 'dashboard'
        );
      })
    );
  }

  private setCrumbs(): void {
    this.crumbs$ = combineLatest([
      this.pageMetaService.getMeta(),
      this.translation.translate('common.home'),
    ]).pipe(
      map(([meta, textHome]) => {
        if (meta.title === 'Cart') {
          return [{ label: 'Cart', link: '/cart' }];
        }
        return meta?.breadcrumbs
          ? meta.breadcrumbs.map((item) => {
            if (item.label === 'Dashboard') {
              return this.isPatient()
                ? { ...item, label: 'Shop', link: this.getHomeUrl() }
                : { ...item, link: this.getHomeUrl() };
            }
            return item;
          })
          : [{ label: textHome, link: this.getHomeUrl() }];
      })
    );
  }

  isPdpPage(): Observable<boolean> {
    return this.cmsService
      .getCurrentPage()
      .pipe(map((page) => page?.type === PageType.PRODUCT_PAGE));
  }

  isPatient(): boolean {
    const user = JSON.parse(sessionStorage.getItem('user_config'));
    return user?.roles?.includes('patientcustomergroup');
  }

  getHomeUrl(): string {
    const user = JSON.parse(sessionStorage.getItem('user_config'));
    return user && user.rootUrl ? user.rootUrl : '/';
  }

  checkPluserRole() {
    return this.curuserService
      .isRoleExist('pluser')
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe((res) => {
        this.pluser = res;
      });
  }

  isMobile(): boolean {
    return window.innerWidth <= 768;
  }
}
