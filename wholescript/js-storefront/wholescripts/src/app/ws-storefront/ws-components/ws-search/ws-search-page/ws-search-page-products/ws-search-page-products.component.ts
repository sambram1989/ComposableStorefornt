import { Component, EventEmitter, Input, OnChanges, Output, SimpleChanges } from '@angular/core';
import { SearchResults } from '@spartacus/storefront';
import { Observable, switchMap } from 'rxjs';
import { CustomSearchboxService } from '../../../../ws-services/ws-custom-search.service';
import { Router } from '@angular/router';

@Component({
  selector: 'ws-search-page-products',
  templateUrl: './ws-search-page-products.component.html',
  styleUrl: './ws-search-page-products.component.scss'
})
export class WsSearchPageProductsComponent implements OnChanges {
  @Input() searchTerm: string = '';
  @Input() viewAllFlag: boolean = false;
  @Output() viewAll = new EventEmitter();
  currentPage: number = 0;
  pageSize: number = 9;

  constructor(private customSearchboxService: CustomSearchboxService, private router: Router) {}

  results$: Observable<any> = this.customSearchboxService.getResults();

  ngOnChanges(changes: SimpleChanges): void {
    if(changes?.['searchTerm'] || changes?.['viewAllFlag']) {
      this.currentPage = 0;
      if(!this.viewAllFlag) {
        this.pageSize = 9;
      } else {
        this.pageSize = this.pageSize > 9 ? this.pageSize : 24;
      }
      this.searchProducts();
    }
  }
  
  setCurrentPage(page: number) {
    this.currentPage = page;
    this.searchProducts();
  }

  viewAllEmit() {
    this.viewAll.emit();
    this.pageSize = 24;
    this.currentPage = 0;
    this.searchProducts();
  }

  showNumberOfResults(pageSize: number) {
    this.pageSize = pageSize;
    this.searchProducts();
  }

  searchProducts() {
    this.customSearchboxService.searchResults(this.searchTerm, {
      currentPage: this.currentPage,
      pageSize: this.pageSize
    })
  }
  gotToPDP(url: string): void {
    this.router.navigate([url]);
  }
}
