import { ChangeDetectorRef, Component, DestroyRef, EventEmitter, Input, Output } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { AuthService } from '@spartacus/core';
import { WsShopifyService } from '../../../ws-services/ws-shopify.service';
import { BsModalRef } from 'ngx-bootstrap/modal';

@Component({
  selector: 'ws-shopify-mobile-facets',
  templateUrl: './ws-shopify-mobile-facets.component.html',
  styleUrl: './ws-shopify-mobile-facets.component.scss'
})
export class WsShopifyMobileFacetsComponent {
  @Input() facetFilters: any[] = [];
  @Input() firstImportStatus: any = '';
  @Output() selectedFilterValue = new EventEmitter<any>();

  isLoggedIn: boolean = false;
  selectedFilter: any[] = [];
  selectedImportStatus: any = '';

  constructor(
    private authService: AuthService,
    private wsShopifyService: WsShopifyService,
    private bsModalRef: BsModalRef
  ) {
    this.authService
    .isUserLoggedIn()
    .subscribe((res: any) => (this.isLoggedIn = res));
  }

  ngOnInit() {
    this.facetFilters.forEach((res: any) => {
      if(res.name !== 'importStatus') {
        res.values.forEach((y: any) => {
          if(y.selected){
            res.isopen = true;
            let obj = {filterName: res.name, selectedOption: y.name, isSelected: y.selected};
            this.selectedFilter.push(obj);
          }
        });
      }
    });
  }

  resetFilters(){
    this.selectedFilter = [];
    let obj = {filterName: 'Reset', selectedOption: this.firstImportStatus, isSelected: ''};
    this.wsShopifyService.setFilter.next(obj);
    this.facetFilters.forEach((res: any) => {
      res.values.forEach((y: any) => {
        y.selected = false;
        if(y.name == this.firstImportStatus) y.selected = true;
      });
    });
  }

  closeModal() {
    this.bsModalRef.hide();
    let obj = {
      selectedImportStatus: this.selectedImportStatus,
      selectedFilter: this.selectedFilter
    }
    this.selectedFilterValue.emit(obj);
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
    if(filterName !== 'importStatus') {
      if(isSelected) {
        this.selectedFilter.push(obj);
      } else {
        this.clearUserSpecificFilter(obj);
      }
    }
  }

  clearUserSpecificFilter(facet: any) {
    let index = this.selectedFilter.findIndex((item: any) => item.selectedOption === facet.selectedOption);
    if (index !== -1) {
      this.selectedFilter.splice(index, 1);
      facet.isSelected = false;
      this.facetFilters.forEach((res: any) => {
        if(res.name == facet.filterName) {
          res.values.forEach((y: any) => {
            if(y.name == facet.selectedOption) y.selected = false;
            this.setFilter(res.name, y.name, y.selected);
          });
        }
      });
    }
  }
}
