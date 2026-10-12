import {
  AfterViewInit,
  ChangeDetectorRef,
  Component,
  DestroyRef,
  inject,
  OnInit,
  signal,
  ElementRef,
  HostListener
} from '@angular/core';
import { getUserData } from '../../../ws-services/state-services/currentuser.service';
import { WsPractitionerDashboardService } from '../../../ws-services/practitioner-dashboard/ws-practitioner-dashboard.service';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { WsPage } from '../../../ws-services/ws-page.service';
import { DatePipe } from '@angular/common';
import { Router } from '@angular/router';
import {
  ActiveCartFacade,
  CartAddEntrySuccessEvent,
} from '@spartacus/cart/base/root';
import { CmsService, EventService } from '@spartacus/core';
import { first, map, Observable, Subscription } from 'rxjs';
import { FavoriteService } from '../../../ws-services/ws-favorite.service';
import { RecommendationService } from '../../../ws-services/ws-recommendation.service';
import { PlpService } from '../../../ws-services/ws-plp.service';
import { userMap } from '../../../ws-env/environment';
import { CurrencyService, Config } from '@spartacus/core';

@Component({
  selector: 'ws-practitioner-dash-board-landing',
  templateUrl: './practitioner-dash-board-landing.component.html',
  styleUrl: './practitioner-dash-board-landing.component.scss',
})
export class PractitionerDashBoardLandingComponent implements OnInit, AfterViewInit {
  currentUserData$ = inject(getUserData);
  isPatientDirectCustomer = signal<boolean>(false); 
  isEmployeeCustomer = signal<boolean>(false);
  loading = signal<boolean>(true); 
  isBuyAgainAvailable: boolean = true;
  loader = true;
  isBuyAgainLoading = true;
  sharedRewards = signal<any>({});
  userData: any;
  nextLevel: any;
  dashBoardDetails: any;
  salesPersonDetails: any;
  userHasDiscount: any;
  buyAgainProducts: any[] = [];
  bestSellersProducts: any[] = [];
  recommendations: any[] = [];
  newPatients: any[] = [];
  topPatients: any[] = [];
  patientMostOrdered: any[] = [];
  patientLatestOrders: any[] = [];
  practitionerMostOrdered: any[] = [];
  practitionerLatestOrders: any[] = [];
  alerts: any[] = [];
  cartItems: any[] = [];
  alert: any = {};
  today = new Date();
  itemsPerSlide = 3;
  slickConfig = {
    slidesToShow: this.itemsPerSlide,
    slidesToScroll: this.itemsPerSlide,
      dots: true,
      infinite: true,
      arrows: true,
      autoplay: false,
      speed: 500,
    responsive: [
      {
        breakpoint: 1280,
        settings: {
          slidesToShow: 2,
          slidesToScroll: 1,
          infinite: true,
          dots: true
        }
      },
      {
        breakpoint: 600,
        settings: {
          slidesToShow: 1,
          slidesToScroll: 1
        }
      }
    ]
  };
  protected cartEventsSubscription: Subscription = new Subscription();
  favoriteList: { wishLists?: { wishListEntries: { productCode: string }[] } } =
    {};
    tooltipText: string = '';
  tooltipStyle: { visibility: string; opacity: string; top: string; left: string; height?: string, position?: string } = { visibility: 'hidden', opacity: '0', top: '0px', left: '0px' };
  wellnessBox$: Observable<any> = this.cmsService.getComponentData('WellnessBoxComp');
  wellnessBoxForMobTab$: Observable<any> = this.cmsService.getComponentData('WellnessBoxMobileBannerComponent');
  wellnessBoxData: Subscription;
  dashboardLearnMore$: Observable<any> = this.cmsService.getComponentData('PatientDirectComp');
  baseURL: any;
  isMobile: boolean;
  cartIsLoading$: Observable<boolean> = this.cartService
      .isStable()
      .pipe(map((loaded) => !loaded));
  
  constructor(
    private router: Router,
    private cdr: ChangeDetectorRef,
    private datePipe: DatePipe,
    private destroyRef: DestroyRef,
    private myAccountService: WsPage,
    private practiitionerService: WsPractitionerDashboardService,
    protected eventService: EventService,
    protected cartService: ActiveCartFacade,
    private favouriteService: FavoriteService,
    private recommendationService: RecommendationService,
    private plpService: PlpService,
    private elRef: ElementRef,
    protected cmsService: CmsService,
    protected config: Config,
  ) {
    this.getPractitionerDashboradDetails();
    this.cartService.getEntries().subscribe((res) =>
      res.forEach((el: any) => {
        this.cartItems.push(el.product.code);
      })
    );
    this.fetchProducts();
    this.checkIfMobile();
  }

    @HostListener('window:resize', ['$event'])
      onResize(event: Event) {
        this.checkIfMobile();
      }

  private checkIfMobile(): void {
    const width = window.innerWidth;
    this.isMobile = width <= 1024;
  }

  ngOnInit(): void {
    this.currentUserData$
    .pipe(takeUntilDestroyed(this.destroyRef))
    .subscribe({
      next: (res: any) => {
        if (res?.discount) {
          this.userHasDiscount = res.discount;
          this.loading.set(false); 
        } else {
          this.getUserSharedRewards();
        }
        this.userData = res;
        const keyForPatientDirect = Object.keys(userMap).find(
          (key) => userMap[key] === 'patientdirect'
        );
        const isPatientDirect = keyForPatientDirect
          ? res.roles.includes(keyForPatientDirect)
          : false;
        this.isPatientDirectCustomer.set(isPatientDirect);
        const isEmployeeCustomer = res.roles.includes('employeecustomergroup');
        this.isEmployeeCustomer.set(isEmployeeCustomer);
        this.cdr.detectChanges();
      }
    });
  
    this.plpService.favoriteItems$.subscribe((favorites) => {
      favorites?.wishLists?.forEach((element: any) => {
        if (element.name.includes('plpwishlist')) {
          this.favoriteList = {
            wishLists: {
              wishListEntries: [...element.wishListEntries]
            }
          }
        }
      });
    });
    this.baseURL = this.config?.backend?.occ?.baseUrl
    ? this.config.backend?.occ?.baseUrl
    : '';
  }

  getImageUrl(image: any) {
    return image?.url ? `${this.baseURL}${image.url}` : undefined;
    console.log();
    
  }

  ngAfterViewInit(): void {
    const waitForPagination = setInterval(() => {
      const targetNode = this.elRef.nativeElement.querySelector(
        'ws-ws-special-offers ws-pagination pagination ul'
      );
      if (targetNode) {
        clearInterval(waitForPagination); 
        const observer = new MutationObserver(() => {
          this.hideListItems();
        });
        observer.observe(targetNode, { childList: true, subtree: true });
        this.hideListItems(); 
      }
    }, 200); 
  }

  hideListItems(): void {
    // to hide the pagination numbers on dashboard section we only need to show arrows
    const listItems: NodeListOf<HTMLLIElement> = this.elRef.nativeElement.querySelectorAll(
      'ws-ws-special-offers ws-pagination pagination ul li'
    );
    listItems.forEach((li) => {
      const anchorTag = li.querySelector('a');
      if (anchorTag) {
        const hasSpan = anchorTag.querySelector('span') !== null;
        if (!hasSpan) {
          li.style.display = 'none'; 
        } else {
          li.style.display = '';
        }
      }
    });
  }

  fetchProducts() {
    this.isBuyAgainLoading = true;
    this.practiitionerService.getBestSellerProducts()
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: (res) => {
            this.buyAgainProducts = (res.buyAgainProducts ?? []).slice(0, 5);
            this.bestSellersProducts = (res.bestSellerProducts ?? []).slice(0, 5);
            this.isBuyAgainAvailable = this.buyAgainProducts.length > 0;
            if (!this.isBuyAgainAvailable) {
              this.buyAgainProducts = [...this.bestSellersProducts];
            }
            this.isBuyAgainLoading = false;
            this.cdr.markForCheck();
        },
        error: (err) => {
          this.isBuyAgainLoading = false;
          console.error("Error fetching products:", err);
        }
      });
  }

  getPractitionerDashboradDetails() {
    this.loader = true;
    this.practiitionerService
      .practitionerDashorardDetails()
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: (res = {}) => {
            this.dashBoardDetails = res ?? [];
            this.salesPersonDetails = res.salesPersonDetails ?? [];
            this.recommendations = (res.recommendations ?? []).slice(0, 5);
            this.newPatients = (res.TNewPatients?.items ?? []).slice(0, 5);
            this.topPatients = (res.TTopPatients?.items ?? []).slice(0, 5);
            this.patientMostOrdered = (res.TPaMostOrdered?.items ?? []).slice(0, 5);
            this.patientLatestOrders = (res.TPaLatestOrders?.items ?? []).slice(0, 5);
            this.practitionerMostOrdered = (res.TPraMostOrdered?.items ?? []).slice(0, 5);
            this.practitionerLatestOrders = (res.TPraLatestOrders?.items ?? []).slice(0, 5);
            this.loader =false;
            this.cdr.detectChanges(); 
        },
        error: (err) => {
          this.loader =false;
          this.cdr.detectChanges(); 
          console.error('Error fetching dashboard details:', err);
        },
        complete: () => {
          this.loader =false;
          this.cdr.detectChanges(); 
        },
      });
  }

  getUserSharedRewards() {
    this.loading.set(true);
    const formattedDate = this.datePipe.transform(this.today, 'yyyy-MM-dd');
    if (!formattedDate) {
      console.error('Error: Unable to format date.');
      this.loading.set(false);
      return;
    }
    this.myAccountService
      .getSharedRewards(formattedDate)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: (res) => {
          this.sharedRewards.set(res);
          this.loading.set(false);
          this.cdr.detectChanges();
        },
        error: (err) => {
          console.error('Error fetching shared rewards:', err);
          this.loading.set(false);
          this.cdr.detectChanges();
        },
      });
  }

  loadRewardLevel(level: string): { name: string; value: string; discount: string } {
    if (!this.sharedRewards() || !this.sharedRewards().rewardsLevel) {
      return { name: "Unknown", value: "", discount: "" };
    }
    const rewardLevels: { [key: string]: { name: string } } = {
      "01": { name: "Standard" },
      "02": { name: "Silver" },
      "03": { name: "Gold" },
      "04": { name: "Platinum" }
    };
    const rewards = this.sharedRewards().rewardsLevel.item;
  
    let value = "";
    const currentIndex = rewards.findIndex((reward: any) => reward.konda === level);
  
    if (currentIndex >= 0) {
      const currentReward = rewards[currentIndex];
      const nextReward = rewards[currentIndex + 1]; 
  
      if (level === "04") {
        value = `$${currentReward.zrewardsValue.toLocaleString()}+`;
      } else if (nextReward) {
        // Other levels: Show range from current to next - 0.01
        const minValue = Math.floor(currentReward.zrewardsValue); 
        const maxValue = parseFloat((nextReward.zrewardsValue - 0.01).toFixed(2)); 
        value = `$${minValue.toLocaleString()} - $${maxValue.toLocaleString(undefined, { minimumFractionDigits: 2, maximumFractionDigits: 2 })}`;
      }
    }
  
    return {
      name: rewardLevels[level]?.name || "Unknown",
      value: value,
      discount: this.sharedRewards()?.discountPercentage || "0",
    };
  }

  copyToClipboard(text: any) {
    this.add();
    navigator.clipboard
      .writeText(text)
      .then(() => {
        console.log('Text copied to clipboard successfully!');
      })
      .catch((err) => {});
  }

  add(): void {
    this.alerts.push({
      type: 'success',
      msg: `Code copied to clipboard.`,
      timeout: 2000,
    });
  }

  onClosed(dismissedAlert: any): void {
    this.alerts = this.alerts.filter((alert) => alert !== dismissedAlert);
  }

  isFavorite(item: any): boolean {
    let product = this.favoriteList?.wishLists?.wishListEntries?.findIndex((product: any) => product.productCode === item.code);
    return product > -1 ? true : false;
  }

  updateFavorites(item: any, isFav: boolean) {
    if (isFav) {
      this.plpService
        .removeFromFavorites(item.code)
        .pipe(takeUntilDestroyed(this.destroyRef))
        .subscribe((res) => {
          item.isFavorite = !item.isFavorite;
          this.addFav(!isFav);
          this.cdr.markForCheck();
        });
    }
    else {
      this.plpService
        .addToFavorites(item.code)
        .pipe(takeUntilDestroyed(this.destroyRef))
        .subscribe((res) => {
          this.addFav(!isFav);
          this.cdr.markForCheck();
        });
    }
  }

  addFav(isFavorite: boolean): void {
    this.alerts.push({
      type: 'info',
      msg: isFavorite
        ? `Product added to favorites.`
        : `Product removed from favorites.`,
      timeout: 2000,
    });
    // this.fetchBestSellers();
  }

  addProductToCart(item: any): void {
    this.watchAddEntrySuccessEvent();
    this.cartService.addEntry(item.matnr || item.code , item.quantity);
  }

  protected watchAddEntrySuccessEvent(): void {
    this.cartEventsSubscription.add(
      this.eventService
        .get(CartAddEntrySuccessEvent)
        .pipe(first())
        .subscribe(() => {
          this.addCart();
        })
    );
  }

  addCart(): void {
    this.alert = {
      type: 'info',
      msg: `Product added to cart`,
      timeout: 3000,
    };
    this.cdr.markForCheck();
  }

  addQuantity(item: any) {
    if(!item.quantity) {
      item.quantity = 1;
    }
    item.quantity = Number(item.quantity) + 1;
  }

  removeQuantity(item: any) {
    if(!item.quantity) {
      item.quantity = 1;
    }
    if (Number(item.quantity) > 1) item.quantity = Number(item.quantity) - 1;
  }

  setQuantity(product: any, ev: any) {
    const value = ev.target.value;
    if (value !== '' && /^[1-9][0-9]{0,3}$/.test(value)) {
      product.quantity = +value;
    } else {
      product.quantity = 1;
      ev.target.value = '1';
    }
  }

  restrictNumeric(event: KeyboardEvent) {
    const input = event.target as HTMLInputElement;
    const char = event.key;
    if (!/^[0-9]$/.test(char)) {
      event.preventDefault();
      return;
    }
    if (input.selectionStart === 0 && char === '0') {
      event.preventDefault();
    }
  }
  
  validateQuantity(event: any) {
    let value = event.target.value.replace(/[^0-9]/g, '');
    value = value.replace(/^0+/, '');
    if (value.length > 4) {
      value = value.substring(0, 4);
    }
    if (value === '') {
      value = '1';
    }
    event.target.value = value;
  }
  
  onalertClosed(): void {
    this.alert = {};
  }

  navigateToPatientProfile(item: any) {
    this.router.navigateByUrl(
      'practitioner/patientprofile?uid=' + item.patientId
    );
  }

  navigateToReferralCodePage() {
    this.router.navigate(['/patient-direct-hub']);
  }

  navigateToPatientdirect() {
    this.router.navigate(['/practitioner/patient-direct']);
  }

  navigateToRecomendationPage() {
    this.router.navigate(['/Recommendation/PractitionerRecommendations']);
  }

  navigateToRecommendationDetailPage(item: any) {
    this.router.navigate([
      '/Recommendation/RecommendationDetails/',
      item.recommendationCode,
    ]);
  }

  navigateToSendRecommendation(item: any) {
    this.recommendationService
    .createRecommendation({
      patient: item.patientId,
    })
    .subscribe((data) => {
      this.router.navigate(['Recommendation/BasicRecommendation', data.id]);
    });
  }

  navigateToOrderDetails(item: any) {
    this.router.navigate(['/order/details', item.vbeln]);
  }

  navigateToOrderHistory() {
    this.router.navigate(['/order/history']);
  }

  navigateToOrderHistoryAllOrder() {
    this.router.navigate(['/order/history/all-orders']);
  }

  navigateToSearchPatientAdvanced() {
    this.router.navigate(['/SearchPatientAdvanced']);
  }

  onKeyDown(event: KeyboardEvent, parameter: string, product: any) {
    if (event.key === 'Enter' || event.key === ' ') {
      event.preventDefault();
      if (parameter === 'addQuantity') {
        this.addQuantity(product);
      }  else if (parameter === 'removeQuantity') {
        this.removeQuantity(product);
      } else if (parameter === 'productImage') {
        this.router.navigate([product.url]);
      } else if (parameter === 'patientProfile') {
        this.navigateToPatientProfile(product);
      } else if (parameter === 'sendRecommendation') {
        this.navigateToSendRecommendation(product);
      } else if (parameter === 'recommendationDetailPage') {
        this.navigateToRecommendationDetailPage(product);
      } else if (parameter === 'copyToClipboard') {
        this.copyToClipboard(product);
      } else if (parameter === 'orderDetails') {
        this.navigateToOrderDetails(product);
      } else if (parameter === 'copyToClipboard') {
        this.copyToClipboard(product);
      } else if (parameter === 'OrderHistory') {
        this.navigateToOrderHistory();
      } else if (parameter === 'OrderHistoryAllOrder') {
        this.navigateToOrderHistoryAllOrder();
      } else if (parameter === 'RecomendationPage') {
        this.navigateToRecomendationPage();
      } else if (parameter === 'searchPatientAdvanced') {
        this.navigateToSearchPatientAdvanced();
      }
    }
  }

  showTooltip(event: MouseEvent, text: string) {
    const rect = (event.target as HTMLElement).getBoundingClientRect();
    this.tooltipText = text;
    this.tooltipStyle = {
      visibility: 'visible',
      opacity: '1',
      position: "fixed",
      top: `${rect.top - 38}px`,  
      left: `${(rect.left + rect.width / 2)}px`,
      height: `30px`
    };
  }

  hideTooltip() {
    this.tooltipStyle = { visibility: 'hidden', opacity: '0', top: '0px', left: '0px' };
  }

  gotToPDP(url: string): void {
    this.router.navigate([url]);
  }
}
