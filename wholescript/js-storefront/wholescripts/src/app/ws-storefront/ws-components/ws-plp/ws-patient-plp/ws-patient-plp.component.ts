import {
  AfterViewInit,
  ChangeDetectorRef,
  Component,
  DestroyRef,
  ElementRef,
  inject,
  OnDestroy,
  OnInit,
  signal,
  ViewChild,
} from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { WsPage } from '../../../ws-services/ws-page.service';
import { filter, first, Observable, Subscription, switchMap, take, map } from 'rxjs';
import { PlpService } from '../../../ws-services/ws-plp.service';
import {
  ActiveCartFacade,
  CartAddEntrySuccessEvent,
} from '@spartacus/cart/base/root';
import {
  AuthService,
  EventService,
  UserIdService,
} from '@spartacus/core';
import { ActivatedRoute, NavigationEnd, Router } from '@angular/router';
import { WsCustomMedPaxService } from '../../custom-medpax/medpaxService/customMedpaxservice.service';
import { WsCurrentUserService } from '../../../ws-services/state-services/currentuser.service';
import { FormControl } from '@angular/forms';
import { BRAND_PREFIX, CATEGORY_PREFIX } from '../../../ws-constants/ws-api-endpoint.constant';
import { DOCUMENT } from '@angular/common';
import { WsPromotionsService } from '../../../ws-services/promotions.service';

@Component({
  selector: 'ws-patient-plp',
  templateUrl: './ws-patient-plp.component.html',
  styleUrl: './ws-patient-plp.component.scss',
})
export class WsPatientPlpComponent implements OnInit, OnDestroy, AfterViewInit {
  private customMedPaxService = inject(WsCustomMedPaxService);
  private document = inject(DOCUMENT);
  private currentuser = inject(WsCurrentUserService);
  payLoad = {
    currentPage: 0,
    pageSize: 24,
    discount: '',
    referralCode: '',
    status: '',
    query: '',
    sort: '',
    practitionerId: '',
    fields: 'FULL',
    filters: '',
  };
  productsList1 = signal<any>([]);
  productsList2 = signal<any>([]);
  promotions: any[] = [];
  sortOptions: any;
  pagination = signal<any>({});
  setFavorite: boolean = false;
  filtersApi$: Observable<any> = this.wsPageService.getFilters();
  activeFilters = signal<any[]>([]);
  facetFilters: any;
  alerts: any[] = [];
  cartItems: any[] = [];
  protected cartEventsSubscription: Subscription = new Subscription();
  brandFilter = '';
  showMore = false;
  visibleBrands = 4;
  showItems: any = this.payLoad.pageSize;
  favoriteList: { wishLists?: { wishListEntries: { productCode: string }[] } } =
    {};
  showGridView: boolean = true;
  isLoggedIn: boolean = false;
  categoryCode =''
  brandCode = '';
  canShowMedPax: boolean = false;
  cartIsLoading$: Observable<boolean> = this.cartService
  .isStable()
  .pipe(map((loaded) => !loaded));
  sortControl = new FormControl('');
  pageSizeControl = new FormControl(24);
  showPagination: boolean = true;
  @ViewChild('sectionTop') sectionTop!: ElementRef;
  pathName: string;
  loader: boolean = true;
  constructor(
    private plpService: PlpService,
    private destroyRef: DestroyRef,
    private cdRef: ChangeDetectorRef,
    private wsPageService: WsPage,
    private cartService: ActiveCartFacade,
    private authService: AuthService,
    private router: Router,
    private eventService: EventService,
    protected userIdService: UserIdService,
    private promotionService: WsPromotionsService,
    private route: ActivatedRoute
  ) {
    this.pathName = window.location.pathname;
    if (!this.pathName.includes('/brand')) {
      this.payLoad.query = ':bestSellers:categoryUrl:' + this.pathName;
    } else {
      this.payLoad.query = ':bestSellers:brandUrl:' + this.pathName;
    }
    this.route.queryParams.subscribe((params: any) => {
      if (params?.query) {
        this.payLoad.query = ':bestSellers:' + params.query;
      }
    });
    this.plpService
      .fetchFavorites()
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe();
    this.plpService.selectedFacetQueryValue = this.payLoad.query;
    this.plpService.resetAllProducts();
    this.getPageInfo();
    this.authService
      .isUserLoggedIn()
      .subscribe((res) => (this.isLoggedIn = res));
  }

  ngOnInit(): void {
    this.IsUserIsaPractitioner();
    this.getPromotions();
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
    this.cartService.getEntries().subscribe((res) =>
      res.forEach((el: any) => {
        this.cartItems.push(el.product.code);
      })
    );
    this.plpService.newProductsFiltered.subscribe((data) =>
      this.updateSignalStates(data)
    );

    this.router.events
      .pipe(
        filter((event) => event instanceof NavigationEnd),
        takeUntilDestroyed(this.destroyRef)
      )
      .subscribe((event) => {
        const pageUrl = window.location.pathname;
        if (!pageUrl.includes('/brand')) {
          this.payLoad.query =
            ':bestSellers:categoryUrl:' + window.location.pathname;
        } else {
          this.payLoad.query =
            ':bestSellers:brandUrl:' + window.location.pathname;
        }
        this.plpService.selectedFacetQueryValue = this.payLoad.query;
        this.getPageInfo();
      });
      this.pageSizeUpdate();
      this.sortProductsUpdate();
  }

  ngAfterViewInit(): void {
    if(document.getElementsByTagName('title') && !document.getElementsByTagName('title')[0].innerText) document.getElementsByTagName('title')[0].innerText = 'Patient Product List Page';
  }

  private IsUserIsaPractitioner() {
    let isPatient = false;
    this.currentuser.isRoleExist('patient').pipe(
      switchMap((user: boolean) => {
        isPatient = user;
        return this.authService.isUserLoggedIn();
      })
    )
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe((res: boolean) => this.canShowMedPax = (res && !isPatient));
  }

  getPromotions() {
    this.promotionService.getPromotions()
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe((data: any) => {
        this.promotions = data?.promotions?.filter((promotion: any) => promotion?.showOnPLP) || [];
      })
  }

  private updateSignalStates(data: any) {
    const itemPerRow = this.document.body.classList.contains('ws-practitioner') ? 3 : 4;
    this.productsList1.set(data?.products?.slice(0,itemPerRow));
    this.productsList2.set(data?.products?.slice(itemPerRow));
    if (this.productsList1()?.length > 0) {
      this.loader = false;
    } else {
      this.loader = true;
    }
    this.sortOptions = data?.sorts;
    this.pagination.set(data?.pagination);
    this.categoryCode = data?.currentQuery?.brandCode ? data?.currentQuery?.brandCode?.replace(BRAND_PREFIX, '') :  data?.currentQuery?.categoryCode?.replace(CATEGORY_PREFIX, '');
  }

  getPaginationRange(
    currentPage: number,
    itemsPerPage: number,
    totalItems: number
  ) {
    const start = currentPage * itemsPerPage + 1;
    const end = Math.min((currentPage + 1) * itemsPerPage, totalItems);
    return { start, end };
  }

  getPageInfo() {
    this.payLoad.filters = `isMedpax:false`;
    this.payLoad.query = this.plpService.selectedFacetQueryValue;
    this.plpService.getAllProducts(this.payLoad).pipe(takeUntilDestroyed(this.destroyRef)).subscribe();
    this.plpService
    .allproducts$
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe((res: any) => {
        res?.products?.forEach((element: any) => {
          element.quantity = 1;
        });
        this.plpService.refreshResults.next(res);
        this.updateSignalStates(res);
        const currentSortingCode = res?.sorts?.find((item:any)=> item?.selected)?.code;
        this.sortControl.setValue(currentSortingCode,{emitEvent:false});
        this.pageSizeControl.setValue(res?.pagination?.pageSize,{emitEvent:false});
        this.cdRef.markForCheck();
      });
  }
  sortProducts(event: any) {
    this.payLoad.sort = event.target.value;
    this.resetPagination();
    this.getPageInfo();
  }

  getProductsCount(event: any) {
    this.payLoad.pageSize = event.target.value;
    this.resetPagination();
    this.getPageInfo();
  }

  resetPagination() {
    this.payLoad.currentPage = 0;
    this.showPagination = false;
    setTimeout(() => {
      this.showPagination = true;
      this.cdRef.detectChanges();
    }, 500);
    this.sectionTop.nativeElement.scrollIntoView({ behavior: 'smooth', block: 'start' });
  }

  private pageSizeUpdate(){
    this.pageSizeControl.valueChanges.pipe(takeUntilDestroyed(this.destroyRef)).subscribe((res:number)=> {this.getProductsCount(res)})
  }

  private sortProductsUpdate(){
    this.sortControl.valueChanges.pipe(takeUntilDestroyed(this.destroyRef)).subscribe((res:string)=> {this.sortProducts(res)})
  }

  restrictNumeric(event: KeyboardEvent) {
    const charCode = event.key;
    if (!/^[0-9]$/.test(charCode)) {
      event.preventDefault(); 
    }
  }
  
  validateQuantity(event: any) {
    let value = event.target.value.replace(/[^0-9]/g, '');
    if (value.length > 4) {
      value = value.substring(0, 4); 
    }
    event.target.value = value; 
  }

  setQuantity(product: any, ev: any, type?: any) {
    console.log(type)
    if (ev.target.value !== '') {
      const reg = new RegExp(/^\d+$/);
      if(!reg.test(ev.target.value) || ev.target.value == 0) ev.target.value = 1; 
      product.quantity = ev.target.value;
    } 
    else product.quantity = 1;
  }

  getQueryString(): string {
    const baseQuery = 'products:bestSellers';
    const selectedQueries = this.facetFilters
      .map((level: any) =>
        level.values
          .filter((value: any) => value.selected)
          .map((value: any) =>
            value.query.query.value.replace(`${baseQuery}:`, '')
          )
      )
      .flat()
      .join(':');

    return selectedQueries ? `?query=${baseQuery}:${selectedQueries}` : '';
  }

  toggleShowMore() {
    this.showMore = !this.showMore;
  }

  resetFilters() {
    // Implement the logic to reset all filters
    this.facetFilters.forEach((element: any) => {
      element.values.forEach((el: any) => {
        el.selected = false;
      });
    });
    this.getPageInfo();
  }
  updateFavorites(item: any, isFav: boolean) {
    if (isFav) {
      this.plpService
        .removeFromFavorites(item.code)
        .pipe(takeUntilDestroyed(this.destroyRef))
        .subscribe((res) => {
          item.isFavorite = !item.isFavorite;
          this.add(!isFav);
          this.cdRef.markForCheck();
        });
    } else {
      this.plpService
        .addToFavorites(item.code)
        .pipe(takeUntilDestroyed(this.destroyRef))
        .subscribe((res) => {
          this.add(!isFav);
          this.cdRef.markForCheck();
        });
    }
  }

  add(isFavorite: boolean): void {
    this.alerts.push({
      type: 'info',
      msg: isFavorite
        ? `Product added to favorites.`
        : `Product removed from favorites.`,
      timeout: 2000,
    });
    this.getPageInfo();
  }
  addCart(): void {
    this.alerts.push({
      type: 'info',
      msg: `Product added to cart`,
      timeout: 3000,
    });
    this.cdRef.markForCheck();
  }

  getBadge(product: any) {
    if (product.isFeatured) return 'Featured';
    if (product.isNew) return 'New';
    if (product.isBestSeller) return 'Best seller';
    else return '';
  }

  pageChanged(event: any) {
    this.sectionTop.nativeElement.scrollIntoView({ behavior: 'smooth', block: 'start' });
    this.payLoad.currentPage = event - 1;
    this.getPageInfo();
  }

  isFavorite(item: any): boolean {
    let product = this.favoriteList?.wishLists?.wishListEntries?.findIndex((product: any) => product.productCode === item.code);
    return product > -1 ? true : false;
  }

  addProductToCart(item: any): void {
    this.watchAddEntrySuccessEvent();
    this.cartService.addEntry(item.code, item.quantity);
  }

  protected watchAddEntrySuccessEvent(): void {
    this.eventService
      .get(CartAddEntrySuccessEvent)
      .pipe(first(), takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: () => {
          this.addCart();
        }
      });
  }

  addQuantity(item: any) {
    item.quantity = Number(item.quantity) + 1;
  }
  removeQuantity(item: any) {
    if (Number(item.quantity) > 1) item.quantity = Number(item.quantity) - 1;
  }

  onClosed(dismissedAlert: any): void {
    this.alerts = this.alerts.filter((alert) => alert !== dismissedAlert);
  }

  ngOnDestroy(): void {
    this.cartEventsSubscription.unsubscribe();
  }

  goToMedPax(productName: string) {
    const queryString = productName.replace('?q=', '');
    this.router.navigate(['/medpax'],{ queryParams: { query: queryString } });
  }

  getAddToCartDisableFlag(selectedProduct: any): boolean {
    if (selectedProduct?.forceOutOfStock) {
      return true;
    }
    return false;
  }

  gotToPDP(url: string): void {
    this.router.navigate([url], {
      queryParams: { origin: this.categoryCode }
    });
  }
}
