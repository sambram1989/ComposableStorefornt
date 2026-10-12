import {
  ChangeDetectorRef,
  Component,
  DestroyRef,
  signal,
} from '@angular/core';
import { BsModalRef, BsModalService, ModalOptions } from 'ngx-bootstrap/modal';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { WsPage } from '../../../ws-services/ws-page.service';
import { Observable } from 'rxjs';
import { PlpService } from '../../../ws-services/ws-plp.service';
import { AuthService } from '@spartacus/core';
import { ICON_TYPE } from '@spartacus/storefront';
import { BreakpointObserver } from '@angular/cdk/layout';
import { WsPlpFilterMobilefacetsComponent } from '../ws-plp-filter-mobilefacets/ws-plp-filter-mobilefacets.component';

@Component({
  selector: 'ws-plp-filter-facets',
  templateUrl: './ws-plp-filter-facets.component.html',
  styleUrl: './ws-plp-filter-facets.component.scss',
})
export class WsPlpFilterFacetsComponent {
  payLoad = {
    currentPage: 0,
    pageSize: 10,
    discount: '',
    referralCode: '',
    status: '',
    query: '',
    sort: '',
    practitionerId: '',
  };
  filtersApi$: Observable<any> = this.wsPageService.getFilters();
  activeFilters = signal<any[]>([]);
  facetFilters: any;
  originalFacetFilters: any;
  searchValues: any = {};

  brandFilter = '';
  visibleBrands = 4;
  itemsToShow = 5;
  showAll = false;
  allResults: any;
  isLoggedIn: boolean = false;
  brandsFilter: any;
  isMobile: boolean = false;
  iconTypes = ICON_TYPE;
  bsModalRef?: BsModalRef;
  searchedText = '';
  pageUrl:string;
  resetQuery = '';
  showResetOption = false;
  selectedMobileFilter: any = [];
  constructor(
    private dialog: BsModalService,
    private plpService: PlpService,
    private destroyRef: DestroyRef,
    private cdRef: ChangeDetectorRef,
    private wsPageService: WsPage,
    private authService: AuthService,
    private bObserver: BreakpointObserver,
  ) {
    this.getPageInfo();
    this.authService
    .isUserLoggedIn()
    .subscribe((res: any) => (this.isLoggedIn = res));
    //Check For Mobile
    this.bObserver.observe('(max-width: 599px)').subscribe((result) => {
      this.isMobile = false;
      if (result.matches) {
        this.isMobile = true;
      }
    });
    this.showResetOption = false;
  }

  getPageInfo() {
    this.plpService
      .refreshResults
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe((res: any) => {
        if(res && Object.keys(res).length) {
          this.allResults = res;
          this.facetFilters = res.facets;
          this.originalFacetFilters = res.facets;
          this.payLoad.pageSize = res.pagination.pageSize;
          this.facetFilters.forEach((element: any) => {
            if(element.name === 'Brand') this.brandsFilter = element;
          });
          this.cdRef.markForCheck();
        }
      });
  }
  setFilter(option: any, isReset?: boolean) {
    const isAnySelected = this.facetFilters
      ?.flatMap((f: any) => f.values)
      ?.some((o: any) => o.selected);
    this.showResetOption = isAnySelected;
    let query: string;
    if (this.resetQuery && isReset) {
      query = option || '';
      this.resetQuery = '';
      this.showResetOption = false;
    } else {
      query = option?.query?.query?.value || '';
    }
    this.plpService.selectedFacetQueryValue = query;
    const sort = this.plpService.selectedSort;
    this.plpService.filterData(query, this.payLoad.pageSize, sort).subscribe((res) => {
      this.plpService.newProductsFiltered.next(res);
      this.allResults = res;
      this.facetFilters = res.facets;
      this.originalFacetFilters = res.facets;
      this.payLoad.pageSize = res.pagination.pageSize;
      this.facetFilters.forEach((element: any) => {
        if(element.name === 'Brand') this.brandsFilter = element;
      });
      this.cdRef.markForCheck();
    });
  }

  getQueryString(): string {
    let baseQuery: any = '';
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

  openMobileFilters() {
    const initialState: ModalOptions = {
      class: 'ws-mobile-filters',
      initialState: {
        referralCode: '',
      },
    };
    this.bsModalRef = this.dialog.show(
      WsPlpFilterMobilefacetsComponent,
      initialState
    );
    this.bsModalRef.content.selectedFilterValue.subscribe((res: any) => {
      if(res) {
        this.selectedMobileFilter = res;
        this.cdRef.detectChanges();
      }
    })
  }

  resetFilters() {
    // Implement the logic to reset all filters
    if (!window.location.pathname.includes('/brand')) {
      this.resetQuery = ':bestSellers:categoryUrl:' + window.location.pathname;
    } else {
      this.resetQuery = ':bestSellers:brandUrl:' + window.location.pathname;
    }
    this.facetFilters.forEach((element: any) => {
      element.values.forEach((el: any) => {
        el.selected = false;
      });
    });
    this.setFilter(this.resetQuery, true);
  }
  searchFilters(selectedFilter: any, event: any) {
    this.searchValues = {};
    this.searchedText = event.target.value.toLowerCase();
    this.searchValues[selectedFilter.name] = this.searchedText;
    this.facetFilters = this.originalFacetFilters?.map((filter: any) => {
      if(selectedFilter?.name !== filter?.name) {
        return {
          ...filter
        }
      }
      return {
        ...filter,
        values: filter?.values?.filter((value: any) => {
          if(value?.name?.toLowerCase().includes(this.searchedText)) {
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

  showSearchFacets(filters: any){
    let filter = filters.filter((x: any) => x.name && x.name !== 'Full Catalog');
    if (filter?.length == 1 && !this.searchedText) {
      return false;
    }
    return true;
  }
  
}
