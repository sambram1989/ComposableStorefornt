import { ChangeDetectorRef, Component, OnDestroy } from '@angular/core';
import { ProductUpdatesPageService } from '../../../ws-services/ws-product-updates.service';
import { Subject, takeUntil } from 'rxjs';

@Component({
  selector: 'ws-available-supplies-last-products',
  templateUrl: './ws-available-supplies-last-products.component.html',
  styleUrl: './ws-available-supplies-last-products.component.scss'
})
export class WsAvailableSuppliesLastProductsComponent implements OnDestroy{
  currentPage: number = 0;
  products: any[];
  totalPageCount: number = 0;
  private ngUnsubscribe = new Subject<void>();

  constructor(private productUpdatesPageService: ProductUpdatesPageService, private cd: ChangeDetectorRef) {
    this.getDetails();
  }

  getDetails() {
    this.productUpdatesPageService.fetchProductUpdates('AVAILABLE_WHILE_SUPPLIES_LAST', this.currentPage).pipe(takeUntil(this.ngUnsubscribe)).subscribe(data => {
      this.products = data?.products || [];
      this.currentPage = data?.currentPage || 0;
      this.totalPageCount = data?.totalPageCount || 0;
      this.cd.detectChanges();
    })
  }

  setCurrentPage(page: number) {
    this.currentPage = page;
    this.getDetails();
  }

  goToTable(id: string) {
    let el = document.getElementById(id);
    if(id && el) { el.scrollIntoView({behavior:"smooth", block: "start",inline: "nearest"})};
  }
  
  ngOnDestroy() {
    this.ngUnsubscribe.next();
    this.ngUnsubscribe.complete();
  }

}
