import {
  ChangeDetectorRef,
  Component,
  DestroyRef,
  OnInit,
  signal,
} from '@angular/core';
import { RecommendationService } from '../../ws-services/ws-recommendation.service';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { WsPage } from '../../ws-services/ws-page.service';
import { first, map, Observable } from 'rxjs';
import { FavoriteService } from '../../ws-services/ws-favorite.service';
import { ActiveCartFacade, CartAddEntrySuccessEvent } from '@spartacus/cart/base/root';
import { CmsService, EventService } from '@spartacus/core';
import { ICON_TYPE } from '@spartacus/storefront';
import { BsModalRef, BsModalService, ModalOptions } from 'ngx-bootstrap/modal';
import { WsPlpFilterMobilefacetsComponent } from '../ws-plp/ws-plp-filter-mobilefacets/ws-plp-filter-mobilefacets.component';
import { Router } from '@angular/router';
import { BRAND_PREFIX, CATEGORY_PREFIX } from '../../ws-constants/ws-api-endpoint.constant';

@Component({
  selector: 'ws-favorites',
  templateUrl: './ws-favorites.component.html',
  styleUrl: './ws-favorites.component.scss',
})
export class WsFavoritesListComponent implements OnInit {
  payLoad = {
    currentPage: 0,
    pageSize: 12,
    discount: '',
    referralCode: '',
    status: '',
    query: 'FavouritesPage',
    sort: 'bestSellers',
    practitionerId: '',
    isMedpax: 'false',
    fields: 'FULL'
  };
  productsList: any[] = [];
  sortOptions: any;
  pagination: any;
  showGridView: boolean = true;
  cartItems: any[] = [];
  showItems: any = this.payLoad.pageSize;

  setFavorite: boolean = false;
  filtersApi$: Observable<any> = this.wsPageService.getFilters();
  activeFilters = signal<any[]>([]);
  facetFilters: any;
  alerts: any[] = [];

  brandFilter = '';
  showMore = false;
  visibleBrands = 4;
  favoriteList: { wishLists?: { wishListEntries: { productCode: string }[] } } =
    {};
    isLoggedIn: boolean = true;
  iconTypes = ICON_TYPE;
  searchValues: any = {};
  originalFacetFilters: any;
  bsModalRef?: BsModalRef;
  filtersData: any;
  title: string;
  description: string;
  showDescription: boolean = false;
  categoryCode ='';
  favElemets$: Observable<any> = this.cmsService.getComponentData('FavoritesHeaderParagraph');
  showPagination: boolean = true;
  lastSelectedQuery: string | null = null;
  cartIsLoading$: Observable<boolean> = this.cartService
      .isStable()
      .pipe(map((loaded) => !loaded));
  constructor(
    private favoriteService: FavoriteService,
    private destroyRef: DestroyRef,
    private cdRef: ChangeDetectorRef,
    private wsPageService: WsPage,
    private cartService: ActiveCartFacade,
    private eventService: EventService,
    protected cmsService: CmsService,
    private dialog: BsModalService,
    private router: Router
  ) {
    this.getPageInfo();
    this.favoriteService.fetchFavorites().pipe(takeUntilDestroyed(this.destroyRef)).subscribe()
  }

  ngOnInit(): void {
    this.favoriteService.favoriteItems$.subscribe((favorites) => {
      favorites?.wishLists?.forEach((element: any) => {
        if(element.name.includes('plpwishlist')) {
          this.favoriteList = {
            wishLists: {
              wishListEntries: [...element.wishListEntries]
            }
          }
        }
      });
    });
    this.filtersData = this.favElemets$.subscribe((data) => {
      this.separateContent(data.content);
    })
  }
  expandDescription() {
    this.showDescription = true;
  }
  separateContent(data: string) {
    const parser = new DOMParser();
    const doc = parser.parseFromString(data, 'text/html');
    this.title = doc.querySelector('h3')?.textContent || '';
    this.description = doc.querySelector('p')?.textContent || '';
  }
      
  openMobileFilters() {
    const initialState: ModalOptions = {
      class: 'ws-mobile-filters',
      initialState: {
        referralCode: '',
        productCode: 'fromFav',
        previousSelectedQuery: this.lastSelectedQuery
      },
    };
    this.bsModalRef = this.dialog.show(
      WsPlpFilterMobilefacetsComponent,
      initialState
    );
    setTimeout(() => {
      if (this.bsModalRef?.content?.filtersApplied) {
          this.bsModalRef.content.filtersApplied.subscribe((selectedQuery: string) => {
          this.lastSelectedQuery = selectedQuery;
        });
      }
    });
  }
          

  getPageInfo() {
    this.favoriteService.getAllProducts(this.payLoad).pipe(takeUntilDestroyed(this.destroyRef)).subscribe();
    this.favoriteService
      .allproducts$
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe((res: any) => {
        res?.products.forEach((element: any) => {
          element.quantity = 1;
        });
        this.productsList = res.products;
        this.sortOptions = res.sorts;
        this.facetFilters = res.facets;
        this.originalFacetFilters = res.facets;
        this.pagination = res.pagination;
        this.categoryCode = res?.currentQuery?.brandCode ? res?.currentQuery?.brandCode?.replace(BRAND_PREFIX, '') :  res?.currentQuery?.categoryCode?.replace(CATEGORY_PREFIX, '');
        
        this.cdRef.detectChanges();
      });
  }
  setFilter(option: any) {
    const query: string = option?.query?.query?.value || '';
    this.payLoad.query = query;
    this.favoriteService.getAllProducts(this.payLoad).pipe(takeUntilDestroyed(this.destroyRef)).subscribe();
  }

  getBadge(product: any) {
    if (product.isFeatured) return 'Featured';
    if (product.isNew) return 'New';
    if (product.isBestSeller) return 'Best seller';
    else return '';
  }
  setQuantity(product: any, ev: any) {
    if (ev.target.value !== '') {
      const reg = new RegExp(/^\d+$/);
      if(!reg.test(ev.target.value) || ev.target.value == 0) ev.target.value = 1; 
      product.quantity = ev.target.value;
    } 
    else product.quantity = 1;
  }
  addQuantity(item: any) {
    item.quantity = Number(item.quantity) + 1;
  }
  addProductToCart(item: any) {
    this.watchAddEntrySuccessEvent();
    let index = this.cartItems.findIndex((entry: any) => entry === item.code);
    if (index !== -1) this.cartService.updateEntry(index, item.quantity);
    else this.cartService.addEntry(item.code, item.quantity);
  }
  protected watchAddEntrySuccessEvent(): void {
    this.eventService
      .get(CartAddEntrySuccessEvent)
      .pipe(first())
      .subscribe((data) => {
        this.addCart();
      })
  }
  addCart(): void {
    this.alerts.push({
      type: 'info',
      msg: `Product added to cart`,
      timeout: 3000,
    });
  }

  sortProducts(event: any) {
    this.payLoad.sort = event.target.value;
    this.getPageInfo();
  }
  removeQuantity(item: any) {
    if (Number(item.quantity) > 1) item.quantity = Number(item.quantity) - 1;
  }

  getQueryString(): string {
    const baseQuery = 'FavouritesPage:relevance';
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

    return selectedQueries
      ? `?query=${baseQuery}:${selectedQueries}`
      : '?query=FavouritesPage';
  }
  pageChanged(event: any) {
    this.payLoad.currentPage = event - 1;
    this.getPageInfo();
  }

  toggleShowMore() {
    this.showMore = !this.showMore;
  }

  resetFilters() {
    this.payLoad = {
      currentPage: 0,
      pageSize: 12,
      discount: '',
      referralCode: '',
      status: '',
      query: 'FavouritesPage',
      sort: 'bestSellers',
      practitionerId: '',
      isMedpax: 'false',
      fields: 'FULL'
    };
    this.getPageInfo();
  }
  updateFavorites(item: any, isFav: boolean) {
    if (isFav) {
      this.favoriteService
        .removeFromFavorites(item.code)
        .pipe(takeUntilDestroyed(this.destroyRef))
        .subscribe((res) => {
          item.isFavorite = !item.isFavorite;
          this.add(!isFav);
          this.cdRef.markForCheck();
        });
    } else {
      this.favoriteService
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

  getProductsCount(event: any) {
    this.payLoad.pageSize = event.target.value;
    this.payLoad.currentPage = 0;
    this.getPageInfo();
    this.showPagination = false;
    setTimeout(() => {
      this.showPagination = true;
      this.cdRef.detectChanges();
    },500);
  }

  isFavorite(item: any): boolean {
    let product = this.favoriteList?.wishLists?.wishListEntries?.findIndex((product: any) => product.productCode === item.code);
    return product > -1 ? true : false;
  }

  onClosed(dismissedAlert: any): void {
    this.alerts = this.alerts.filter((alert) => alert !== dismissedAlert);
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

  getAddToCartDisableFlag(selectedProduct: any): boolean {
    if(selectedProduct?.forceOutOfStock) {
      return true;
    }
    return false;
  }

  searchFilters(selectedFilter: any, event: any) {
    this.searchValues = {};
    const searchText = event.target.value.toLowerCase();
    this.searchValues[selectedFilter.name] = searchText;
    this.facetFilters = this.originalFacetFilters?.map((filter: any) => {
      if(selectedFilter?.name !== filter?.name) {
        return {
          ...filter
        }
      }
      return {
        ...filter,
        values: filter?.values?.filter((value: any) => {
          if(value?.name?.toLowerCase().includes(searchText)) {
            return true;
          }
          return false;
        }) || []
      }
    });
  
    this.cdRef.detectChanges();
  }

  trackById(index: number, item: any): number {
    return item.name;
  }
  goToMedPax(productName: string) {
    const queryString = productName.replace('?q=', '');
    this.router.navigate(['/medpax'],{ queryParams: { query: queryString } });
  }
  gotToPDP(url: string): void {
    this.router.navigate([url], {
      queryParams: { origin: this.categoryCode }
    });
  }

  showSearchFacets(filters: any){
    let filter = filters?.values?.filter((x: any) => x.name && x.name !== 'Full Catalog');
    if(filter?.length <= 1 && !this.searchValues[filters.name]) {
      return false;
    }
    return true;
  }
}
