import { ChangeDetectorRef, Component, DestroyRef, EventEmitter, signal,Output, inject } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { AuthService } from '@spartacus/core';
import { Observable } from 'rxjs';
import { WsPage } from '../../../ws-services/ws-page.service';
import { PlpService } from '../../../ws-services/ws-plp.service';
import { WsShopifyService } from '../../../ws-services/ws-shopify.service';
import { BsModalRef, BsModalService, ModalOptions } from 'ngx-bootstrap/modal';
import { WsShopifyMobileFacetsComponent } from '../ws-shopify-mobile-facets/ws-shopify-mobile-facets.component';

@Component({
  selector: 'ws-shopify-facets',
  templateUrl: './ws-shopify-facets.component.html',
  styleUrl: './ws-shopify-facets.component.scss'
})
export class WsShopifyFacetsComponent {
  @Output() emitImportFilterValues = new EventEmitter<string>();
  payLoad = {
    currentPage: 0,
    pageSize: 10,
    discount: '',
    referralCode: '',
    status: '',
    query: '',
    sortCode: '',
    practitionerId: '',
  };
  filtersApi$: Observable<any> = this.wsPageService.getFilters();
  activeFilters = signal<any[]>([]);
  facetFilters: any[] = [];

  brandFilter = '';
  visibleBrands = 4;
  itemsToShow = 5;
  showAll = false;
  allResults: any;
  isLoggedIn: boolean = false;
  priorityOrderForImportStatus: any = {
    "Not Imported": 1,
    "Pending Import": 2,
    "Imported Products": 3,
    "Failed to Import": 4
};
  priorityOrderForFacet: any = {
    "importStatus": 1,
    "status": 2,
    "productGroup": 3,
    "categories": 4,
    "deliveryForm": 5,
    "brand": 6
};
pendingImportCount: any[] = [];
searchTerm: string = '';
brandArray: any[] = [];
bsModalRef?: BsModalRef;
selectedImportStatus: any;
firstImportStatus: any;
selectMobileFilter: any = [];

  constructor(
    private destroyRef: DestroyRef,
    private cdRef: ChangeDetectorRef,
    private wsPageService: WsPage,
    private authService: AuthService,
    private wsShopifyService: WsShopifyService,
    private dialog: BsModalService
  ) {
    this.getPageInfo();
    this.authService
    .isUserLoggedIn()
    .subscribe((res: any) => (this.isLoggedIn = res));
  }

  getPageInfo() {
    this.wsShopifyService
      .refreshResults
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe((res: any) => {
        this.pendingImportCount = res?.shopifyResults.filter((x: any) => x.importStatus == 'Pending Import');
        if(res) {
          this.allResults = res;
          this.setFilterFacet();
          this.cdRef.markForCheck();
        }
        this.setPendingImportFacet();
      });
  }

  setFilter(filterName: any, selectedOption?:any, isSelected?: any) {
    if(filterName == 'importStatus') {
      let data = this.facetFilters.find((x: any) => x.name == filterName);
      data.values.forEach((y: any) => {
        if(y.name !== selectedOption) y.selected = false;
      })
      this.selectedImportStatus = selectedOption;
    }
    let obj = {filterName, selectedOption, isSelected};
    this.wsShopifyService.setFilter.next(obj);
  }

  getQueryString(selectedFacet:any): string {
   return selectedFacet?.query?.query?.value || '';
  }

  toggleView(items: any) {
    if (this.showAll) {
      this.itemsToShow = 5;
    } else {
      this.itemsToShow = items.length;
    }
    this.showAll = !this.showAll;
  }

  resetFilters(){
    this.resetFacet();
  }

  setFilterFacet() {
    this.facetFilters = [];
    let obj: any = {};
    Object.keys(this.allResults?.shopifyFacets).forEach((key: any) => {
      obj.name = key;
      obj.values = this.allResults?.shopifyFacets[key].map((x: any) => {
        return {code: x.code, name: x.name, selected: false}
      });
      obj.visible = true;
      if(obj.name == 'brand') this.brandArray = obj.values;
      if(obj.name == 'importStatus'){
        obj.values = obj.values.sort((a:any, b:any) => this.priorityOrderForImportStatus[a.code] - this.priorityOrderForImportStatus[b.code]);
        this.facetFilters.unshift(JSON.parse(JSON.stringify(obj)));
      } else {
         this.facetFilters.push(JSON.parse(JSON.stringify(obj)));

      }
    })
    this.facetFilters = this.facetFilters.sort((a:any, b:any) => this.priorityOrderForFacet[a.name] - this.priorityOrderForFacet[b.name]);
  }

  getFilterByName(name: any){
    switch(name) {
      case 'importStatus':
        return 'Import Status';
      case 'brand':
        return 'Brand';
      case 'categories':
        return 'Categories';
      case 'deliveryForm':
        return 'Delivery Form';
      case 'productGroup':
        return 'Product Group';
      case 'status':
        return 'Status';
      default:
        return name
    }
  }

  setPendingImportFacet(){
    let importData = this.facetFilters.find((x:any) => x.name == 'importStatus');
    const selectedImportData = importData.values.find((el:any)=>el.name == this.selectedImportStatus);
    if(this.selectedImportStatus && selectedImportData) selectedImportData.selected = true;
    else this.setDefaultImportStatus(importData);
  }

  searchBrand(event: any, filter: any){
    this.searchTerm = event.target.value;  
    if(this.searchTerm) {
      filter.values = this.brandArray.filter((x: any)=> x.name.toLowerCase().includes(this.searchTerm.toLowerCase()));
    } else {
      filter.values = this.brandArray;
    }
    this.cdRef.detectChanges();
  }

  ngOnDestroy(){
    this.resetFacet();
  }

  openMobileFilters() { 
    const initialState: ModalOptions = {
    class: 'ws-mobile-filters',
    initialState: {
      facetFilters: this.facetFilters,
      firstImportStatus: this.firstImportStatus
    },
    };
    this.bsModalRef = this.dialog.show(
      WsShopifyMobileFacetsComponent,
      initialState
    );
    this.bsModalRef.content.selectedFilterValue.subscribe((res: any) => {
      if(res) {
        this.selectMobileFilter = res.selectedFilter;
        if(res?.selectedImportStatus) this.selectedImportStatus = res.selectedImportStatus;
        this.cdRef.detectChanges();
      }
    })
  }

  setDefaultImportStatus(importData: any) {
    importData.values[0].selected = true;
    let obj = { filterName: 'importStatus', selectedOption: importData.values[0].name, isSelected: true };
    this.wsShopifyService.setFilter.next(obj);
    this.firstImportStatus = importData.values[0].name;
    this.selectedImportStatus = importData.values[0].name;
  }

  resetFacet(){
    let obj = {filterName: 'Reset', selectedOption: this.firstImportStatus, isSelected: ''};
    this.wsShopifyService.setFilter.next(obj);
    this.facetFilters.forEach((x:any) => {
      if(x.name == 'importStatus') {
        x.values.forEach((y: any) => {
          if(y.name == this.firstImportStatus) y.selected = true;
          else y.selected = false;
        })
      }
    })
    this.cdRef.detectChanges();
  }

}

