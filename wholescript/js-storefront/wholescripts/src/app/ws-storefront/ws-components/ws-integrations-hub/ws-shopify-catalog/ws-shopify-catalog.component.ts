import {
  ChangeDetectorRef,
  Component,
  DestroyRef,
  inject,
  OnInit,
  signal,
} from '@angular/core';
import { debounceTime, distinctUntilChanged, Observable, Subject } from 'rxjs';
import { AuthService, CmsService, GlobalMessageService, GlobalMessageType, OccConfig } from '@spartacus/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { WsShopifyService } from '../../../ws-services/ws-shopify.service';
import { FavoriteService } from '../../../ws-services/ws-favorite.service';
import { PlpService } from '../../../ws-services/ws-plp.service';

@Component({
  selector: 'ws-shopify-catalog',
  templateUrl: './ws-shopify-catalog.component.html',
  styleUrl: './ws-shopify-catalog.component.scss',
})
export class WsShopifyCatalogComponent implements OnInit {
  shopifyCatalogHeading$: Observable<any> = this.cmsservice.getComponentData(
    'ShopifyHeaderParagraphComp'
  );
  shopifyDownloadGuidePdf$: Observable<any> = this.cmsservice.getComponentData(
    'wholescriptShopifyImportMediaLink'
  );
  shopifyCatalogPara$: Observable<any> = this.cmsservice.getComponentData(
    'ShopifyContentParagraphComp'
  );
  public occ = inject(OccConfig);
  searchTerm: string = '';
  shopifyProductsList: any[] = [];
  productDetails: any;
  filterValue: any[] = [];
  filterImportValue: string = 'Not Imported';
  searchKey = '';
  loader:boolean=true
  QueryString: string = '';
  payLoad = {
    currentPage: 0,
    pageSize: 10,
    discount: '',
    referralCode: '',
    status: '',
    query: ':relevance:',
    sort: '',
    practitionerId: '',
    fields: 'FULL',
    isMedpax: false,
  };
  sortOptions: any[] = [
    {
        "code": "bestSellers",
        "name": "Best Sellers",
        "selected": true
    },
    {
        "code": "name-asc",
        "name": "(A-Z)",
        "selected": false
    },
    {
        "code": "name-desc",
        "name": "(Z-A)",
        "selected": false
    },
    {
        "code": "price-asc",
        "name": "Price: low to high",
        "selected": false
    },
    {
        "code": "price-desc",
        "name": "Price: high to low",
        "selected": false
    }
];
  setFavorite: boolean = false;
  activeFilters = signal<any[]>([]);
  facetFilters: any;
  alerts: any[] = [];
  pagination: any;
  brandFilter = '';
  showMore = false;
  visibleBrands = 4;
  showItems: any = this.payLoad.pageSize;
  currentPage: number = 1;
  itemsPerPage: number = 12;

  private searchSubject = new Subject<string>();
  favoriteList: { wishLists?: { wishListEntries: { productCode: string }[] } } =
    {};
  showGridView: boolean = true;
  isLoggedIn: boolean = false;
  totalProductCount: number = 0;
  start: number = 0;
  end: number = 0;
  filteredArray: any[] = [];
  filterByImportArray: any[] = [];
  selectedSort: string = 'bestSellers';
  isCalledFromStart: boolean = false;
  searchArray: any[] = [];
  isFiltered: boolean = false;
  message: string = '';
  showPagination: boolean = true;
  constructor(
    private destroyRef: DestroyRef,
    private cdRef: ChangeDetectorRef,
    public cmsservice: CmsService,
    private shopifyService: WsShopifyService,
    private favouriteService: FavoriteService,
    private plpService: PlpService,
    private authService: AuthService,
    protected globalMessageService: GlobalMessageService
  ) {
    this.favouriteService
      .fetchFavorites()
      .pipe(takeUntilDestroyed(this.destroyRef))
    .subscribe();
    this.authService
      .isUserLoggedIn()
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe((res) => (this.isLoggedIn = res));
  }
  ngOnInit(): void {
    this.favouriteService.favoriteItems$
    .pipe(takeUntilDestroyed(this.destroyRef))
    .subscribe((favorites) => {
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
      this.updateFilterData();
      this.loadShopifyProducts();
    }

  ngAfterViewInit() {
    this.searchSubject
      .pipe(debounceTime(300), distinctUntilChanged())
      .subscribe((data: string) => {
        this.searchTerm = data;
        if(data) {
          this.filterByImportArray = this.searchArray.filter((x: any)=> x.name.toLowerCase().includes(data.toLowerCase()));
        } else {
          this.filterByImportStatus();
        }
        this.resetPagination();
        this.cdRef.detectChanges();
      });
  }

  updateSearch(key: string) {
    this.searchSubject.next(key);
  }

  loadShopifyProducts() {
    this.shopifyService
      .getShopifyProducts(this.searchTerm, this.payLoad)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe((res: any) => {
        this.shopifyService.refreshResults.next(res);
        this.productDetails = res;
        this.filterByImportArray = res?.shopifyResults;
        this.shopifyProductsList = res?.shopifyResults;
        this.isCalledFromStart = true;
        this.loader = false
        this.filterByImportStatus();
        if(this.message) this.toastMessage();
      });
  }

  updateFilterData(){
    this.shopifyService.setFilter.pipe(takeUntilDestroyed(this.destroyRef)).subscribe({
      next:(res:any)=>{
        if(res){
          // Reset Filter
          if(res?.filterName == 'Reset'){
            this.filteredArray = [];
            this.filterImportValue = res?.selectedOption;
            this.isCalledFromStart = true;
            this.filterValue = [];
            this.filterByImportStatus();
            this.resetPagination();
            this.shopifyService.refreshResults.next(this.productDetails);
          }
          else if(res?.filterName == 'importStatus'){
            // importStatus filter block
            this.filterImportValue = res?.selectedOption;
            this.isCalledFromStart = true;
            this.filterByImportStatus();
            this.resetPagination();
            if(this.filterValue.length) this.setfilterData();
          } else{
          // rest of facet filter block
            this.setFacetFilter(res);
          }
        }
      }
    })
  }
  
  setFacetFilter(res: any){
    let hasFilterName = this.filterValue.find((x:any) => x.name == res?.filterName);
    let hasFilterIndex = this.filterValue.findIndex((x:any) => x.name == res?.filterName);
    if(hasFilterName) {
      // if filter name already exists then only push selected options
      let index = hasFilterName?.selectedOption.findIndex((x: any) => x == res?.selectedOption);
      if(index == -1) hasFilterName?.selectedOption.push(res?.selectedOption);
      else hasFilterName?.selectedOption.splice(index, 1);
    } else{
    // if filter name doesn't exists then creating a new filter element
      let obj: any = {name: res?.filterName, selectedOption: [res?.selectedOption]};
      this.filterValue.push(obj);
    }
    // If no selected option then removing the filter from array
    if(!hasFilterName?.selectedOption?.length && !res?.isSelected){
      this.filterValue.splice(hasFilterIndex, 1);
    }
    // If filter exists then call method to filter, else go back on filter by import
    if(this.filterValue.length) this.setfilterData();
    else {
      this.filteredArray = [];
      this.filterByImportStatus();
    }
  }

  filterByImportStatus(){
    this.filterByImportArray = this.productDetails?.shopifyResults.filter((item: any) => item.importStatus == this.filterImportValue);
    if(this.isCalledFromStart) {
      this.isCalledFromStart = false;
      this.searchArray = this.filterByImportArray
    }
    this.updateDisplayedProducts();
    if(this.selectedSort) this.sortProducts(this.selectedSort, '')
  }

  setfilterData() {
    this.filteredArray = this.filterByImportArray.filter((item: any) => {
      // Check if the item matches at least one of the filter conditions
      return this.filterValue.some(filterCondition => {
        const filterName = filterCondition.name;
        const selectedOptions = filterCondition.selectedOption;
        // Check if the item has the property matching the filter name
        if (filterName === 'brand') {
          // Check if the item's brandName matches any of the selectedOptions
          return selectedOptions.includes(item.brandName);
        } else if (filterName === 'status') {
          // Check if 'New' is selected and item.isNew is true
          if (selectedOptions.includes('New') && item?.isNew == 'New') {
            return true;
          }
          // Check if 'Featured' is selected and item.isFeatured is true
          if (selectedOptions.includes('Featured') && item?.isFeatured) {
            return true;
          }
          // If neither condition is met, return false
          return false;
        } else if (item[filterName]) {
          // Check if any entry in the array matches the selected options
          return item[filterName].entry.some((entry: any) => selectedOptions.includes(entry.value));
        }
        return false;
      });
    });
    this.resetFilteredPagination();
  }

  importProducts(code: string[], action: string) {
    this.shopifyService.importShopifyProducts(code)
    .pipe(takeUntilDestroyed(this.destroyRef))
    .subscribe((res) => {
      this.message = action;
      this.loadShopifyProducts();
    });
  }

  deleteProducts(code: string[], action: string) {
    const payload = {
      products: code,
    };
    this.shopifyService.deleteShopifyProducts(payload)
    .pipe(takeUntilDestroyed(this.destroyRef))
    .subscribe((res) => {
      this.message = action;
      this.loadShopifyProducts();
    });
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

  sortProducts(code: any, callFrom: string): void {
    this.filterByImportArray = this.productDetails?.shopifyResults.filter((item: any) => item.importStatus == this.filterImportValue);
    let arr = this.filterByImportArray.filter(product => product.favorite );
    this.selectedSort = code;
    switch (this.selectedSort) {
      case 'bestSellers':
        this.filterByImportArray = [
          ...this.filterByImportArray.filter(product => product.favorite ),
          ...this.filterByImportArray.filter(product => product.bestSeller && !product.favorite),
          ...this.filterByImportArray.filter(product => !product.favorite && !product.bestSeller)
        ];
        break;
      case 'name-asc':
        arr.sort((a, b) => a?.name.localeCompare(b?.name));
        this.filterByImportArray.sort((a, b) => a?.name.localeCompare(b?.name));
        this.filterByFavorite(arr);
        break;
      case 'name-desc':
        arr.sort((a, b) => b?.name.localeCompare(a?.name));
        this.filterByImportArray.sort((a, b) => b?.name.localeCompare(a?.name));
        this.filterByFavorite(arr);
        break;
      case 'price-asc':
        arr.sort((a, b) => a?.mapPrice?.value - b?.mapPrice?.value);
        this.filterByImportArray.sort((a, b) => a?.mapPrice?.value - b?.mapPrice?.value);
        this.filterByFavorite(arr);
        break;
      case 'price-desc':
        arr.sort((a, b) => b?.mapPrice?.value - a?.mapPrice?.value);
        this.filterByImportArray.sort((a, b) => b?.mapPrice?.value - a?.mapPrice?.value);
        this.filterByFavorite(arr);
        break;
      default:
        break;
    }
    if(callFrom == 'sort') this.resetPagination();
    else this.updateDisplayedProducts();
  }

  filterByFavorite(arr: any) {
    this.filterByImportArray = [
      ...arr,
      ...this.filterByImportArray.filter(product => !product.favorite)
    ];
  }

  pageChanged(page: number): void {
    this.currentPage = page;
    if(this.filterValue.length) this.updateFilteredDisplayedProducts();
    else this.updateDisplayedProducts();
  }

  getProductsCount(event: Event): void {
    const selectedValue = (event.target as HTMLSelectElement).value;
    this.itemsPerPage = +selectedValue;
    this.resetPagination();
  }

  // To handle pagination and display data
  updateDisplayedProducts(): void {
    const startIndex = (this.currentPage - 1) * this.itemsPerPage;
    const endIndex = startIndex + this.itemsPerPage;
    this.start = (this.currentPage - 1) * this.itemsPerPage;
    this.end = startIndex + this.itemsPerPage;
    if(this.end > this.filterByImportArray?.length) this.end = this.filterByImportArray.length;
    this.shopifyProductsList = this.filterByImportArray.slice(startIndex, endIndex);
    this.isFiltered = false;
    this.cdRef.detectChanges();
  }

  updateFilteredDisplayedProducts(): void {
    const startIndex = (this.currentPage - 1) * this.itemsPerPage;
    const endIndex = startIndex + this.itemsPerPage;
    this.start = (this.currentPage - 1) * this.itemsPerPage;
    this.end = startIndex + this.itemsPerPage;
    if(this.end > this.filteredArray.length) this.end = this.filteredArray.length;
    this.shopifyProductsList = this.filteredArray.slice(startIndex, endIndex);
    this.isFiltered = true;
    this.cdRef.detectChanges();
  }

  importAllProduct(productdata:any, action: string){
    const codeList = productdata.map((el:any)=> el.code)
    let msg = '';
    if(action == 'all products') msg = `All Products(${codeList?.length}) added to import list.`;
    if(action == 'page product') msg = `Page Products(${codeList?.length}) added to import list.`;
    this.importProducts(codeList, msg)
  }

  removeAllProduct(productdata:any){
    const codeList = productdata.map((el:any)=> el.code)
    let action = `All Products(${codeList?.length}) removed from import list.`
    this.deleteProducts(codeList, action)
  }
  updateFavorites(item: any) {
    if (item.favorite) {
      this.favouriteService
        .removeFromFavorites(item.code)
        .pipe(takeUntilDestroyed(this.destroyRef))
        .subscribe((res) => {
          item.isFavorite = !item.isFavorite;
          this.message = 'Product removed from favorites.';
          this.loadShopifyProducts();
          this.cdRef.markForCheck();
        });
    } else {
      this.favouriteService
        .addToFavorites(item.code)
        .pipe(takeUntilDestroyed(this.destroyRef))
        .subscribe((res) => {
          this.message = 'Product added to favorites.';
          this.loadShopifyProducts();
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
    this.loadShopifyProducts();
  }

  isFavorite(item: any): boolean {
    let product = this.favoriteList?.wishLists?.wishListEntries?.findIndex((product: any) => product.productCode === item.code);
    return product > -1 ? true : false;
  }

  onClosed(dismissedAlert: any): void {
    this.alerts = this.alerts.filter((alert) => alert !== dismissedAlert);
  }

  onFacetChange(){

  }

  toastMessage() {
    this.globalMessageService.add(
      this.message,
      GlobalMessageType.MSG_TYPE_INFO,
      3000
    );
    this.message = '';
  }

  resetPagination(): void {
    this.currentPage = 1;
    this.showPagination = false;
    setTimeout(() => {
      this.showPagination = true;
      this.cdRef.detectChanges();
    },500);
    this.updateDisplayedProducts();
  }

  resetFilteredPagination(): void {
    this.currentPage = 1;
    this.showPagination = false;
    setTimeout(() => {
      this.showPagination = true;
      this.cdRef.detectChanges();
    },500);
    this.updateFilteredDisplayedProducts();
  }

  showView(check: boolean) {
    this.showGridView = check;
    if(this.filterValue.length) this.resetFilteredPagination();
    else this.resetPagination();
  }
}
