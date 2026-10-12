import {
  ChangeDetectorRef,
  Component,
  DestroyRef,
  EventEmitter,
  Input,
  OnInit,
  Output,
  signal,
} from '@angular/core';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { WsPage } from '../../../ws-services/ws-page.service';
import { BehaviorSubject, Observable } from 'rxjs';
import { PlpService } from '../../../ws-services/ws-plp.service';
import { AuthService } from '@spartacus/core';
import { ICON_TYPE } from '@spartacus/storefront';
import { FavoriteService } from '../../../ws-services/ws-favorite.service';

@Component({
  selector: 'ws-ws-plp-filter-mobilefacets',
  templateUrl: './ws-plp-filter-mobilefacets.component.html',
  styleUrl: './ws-plp-filter-mobilefacets.component.scss'
})

export class WsPlpFilterMobilefacetsComponent implements OnInit{
  baseLink = window.location.origin;
  @Input() productCode: string;
  payLoad = {
    currentPage: 0,
    pageSize: 10,
    discount: '',
    referralCode: '',
    status: '',
    query: '',
    sort: '',
    practitionerId: '',
    fields: '',
    isMedpax: ''
  };
  filtersApi$: Observable<any> = this.wsPageService.getFilters();
  activeFilters = signal<any[]>([]);
  facetFilters: any;
  brandFilter = '';
  visibleBrands = 4;
  itemsToShow = 5;
  showAll = false;
  allResults: any;
  isLoggedIn: boolean = false;
  brandsFilter: any;
  iconTypes = ICON_TYPE;
  pageUrl = '';
  resetQuery = '';
  private selectedQuerySubject = new BehaviorSubject<string | null>(null);  
  selectedQuery$ = this.selectedQuerySubject.asObservable();
  @Output() filtersApplied = new EventEmitter<string>();
  @Output() selectedFilterValue = new EventEmitter<any>();
  @Input() previousSelectedQuery: string | null = null;
  searchKey: any = '';
  originalFacetFilters: any;
  searchValues: any = {};
  selectedFilter: any = [];

  constructor(
    public bsModalRef: BsModalRef,
    private plpService: PlpService,
    private destroyRef: DestroyRef,
    private cdRef: ChangeDetectorRef,
    private wsPageService: WsPage,
    private authService: AuthService,
   private favoriteService: FavoriteService,) {
    this.pageUrl = window.location.pathname;
    this.authService
    .isUserLoggedIn()
    .subscribe((res: any) => (this.isLoggedIn = res));
    }


  ngOnInit() {
    if (this.productCode === 'fromFav') {
      this.getFavPageInfo();
    } else {
      this.getPageInfo();
    }
  }

  getFavPageInfo() {
    const payLoad = {
      currentPage: 0,
      discount: "",
      fields: "FULL",
      isMedpax: "false",
      pageSize: 12,
      practitionerId: "",
      query: this.previousSelectedQuery || "FavouritesPage",
      referralCode: "",
      sort: "bestSellers",
      status: "",
    };
    this.favoriteService
      .getAllProducts(payLoad)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe((res: any) => {
        if (res) {
          this.allResults = res;
          this.facetFilters = res.facets;
          this.originalFacetFilters = JSON.parse(JSON.stringify(res.facets));
          this.payLoad.pageSize = res.pagination?.pageSize;
          this.facetFilters.forEach((element: any) => {
            if (element.name === 'Brand') this.brandsFilter = element;
          });
          this.cdRef.markForCheck();
        }
      });
  }

      getPageInfo() {
        this.plpService
          .refreshResults
          .pipe(takeUntilDestroyed(this.destroyRef))
          .subscribe((res: any) => {
            if(res) {
              this.allResults = res;
              this.facetFilters = res.facets;
              this.originalFacetFilters = JSON.parse(JSON.stringify(res.facets));
              this.payLoad.pageSize = res.pagination?.pageSize;
              this.facetFilters.forEach((element: any) => {
                if(element.name === 'Brand') this.brandsFilter = element;
              });
              this.cdRef.markForCheck();
            } 
          });
      }
      setFilter(option: any, isReset?: boolean) {
        let query: string;
        if(option?.selected) this.selectedFilter.push(option.name);
        else {
          let i = this.selectedFilter.findIndex((x: any) => x == option?.name);
          if(i > -1)this.selectedFilter.splice(i, 1);
        }
        if (this.resetQuery && isReset) {
          query = option || '';
          this.resetQuery = '';
        } else {
          query = option?.query?.query?.value || '';
        }
        if (this.productCode === 'fromFav') {
          this.selectedQuerySubject.next(query); 
          const sort = this.plpService.selectedSort;
          this.payLoad = {
            currentPage: 0,
            discount: "",
            fields: "FULL",
            isMedpax: "false",
            pageSize: 12,
            practitionerId: "",
            query: `${query}`,
            referralCode: "",
            sort: sort,
            status: ""
          };
          this.favoriteService.getAllProducts(this.payLoad).pipe(takeUntilDestroyed(this.destroyRef)).subscribe();
        } else {
        this.plpService.selectedFacetQueryValue = query;
        const sort = this.plpService.selectedSort;
        this.plpService.filterData(query, this.payLoad.pageSize, sort).subscribe((res) => {
          this.plpService.newProductsFiltered.next(res);
          this.allResults = res;
          this.facetFilters = res.facets;
          this.payLoad.pageSize = res.pagination.pageSize;
          this.facetFilters.forEach((element: any) => {
            if(element.name === 'Brand') this.brandsFilter = element;
          });
          this.cdRef.markForCheck();
        });
      }
    }
    
      getQueryString(): string {
        let baseQuery: any = ':relevance';
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
          ? `${baseQuery}:${selectedQueries}`
          : this.allResults?.currentQuery?.query?.value;
      }
    
      toggleView(items: any) {
        if (this.showAll) {
          this.itemsToShow = 5;
        } else {
          this.itemsToShow = items.length;
        }
        this.showAll = !this.showAll;
      }
    
      resetFilters() {
        // Implement the logic to reset all filters
        this.selectedFilter = [];
        if (!this.pageUrl.includes('/brand')) {
          this.resetQuery = ':bestSellers:categoryUrl:' + window.location.pathname;
        } else {
          this.resetQuery = ':bestSellers:brandUrl:' + window.location.pathname;
        }
        if (this.productCode === 'fromFav') {
          this.resetQuery = 'FavouritesPage';
        }
        this.facetFilters.forEach((element: any) => {
          element.values.forEach((el: any) => {
            el.selected = false;
          });
        });
        this.setFilter(this.resetQuery,true);
      }

      searchFilters(selectedFilter: any, event: any) {
        this.searchKey = event.target.value?.toLowerCase() || '';
        let data = this.originalFacetFilters.find((x: any) => selectedFilter?.name === x?.name);
        let filter = JSON.parse(JSON.stringify(data));
        selectedFilter.values = filter?.values?.filter((value: any) => value?.name?.toLowerCase()?.includes(this.searchKey)) || [];
      }
      

      closeModal() {
        if (this.productCode === 'fromFav') {
          this.selectedQuery$.subscribe(query => {
            if (query) {
              this.filtersApplied.emit(query);
            }
          });
        }
        this.selectedFilterValue.emit(this.selectedFilter);
        this.bsModalRef.hide();
      }

    showSearchFacets(filters: any){
      let filter = filters.filter((x: any) => x.name && x.name !== 'Full Catalog');
      if (filter?.length == 1 && !this.searchKey) {
        return false;
      }
      return true;
    }
}
