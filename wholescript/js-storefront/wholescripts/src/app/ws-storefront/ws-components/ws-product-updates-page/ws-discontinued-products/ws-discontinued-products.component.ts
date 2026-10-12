import { ChangeDetectorRef, Component, OnDestroy } from '@angular/core';
import { ProductUpdatesPageService } from '../../../ws-services/ws-product-updates.service';
import { Subject, takeUntil } from 'rxjs';

@Component({
  selector: 'ws-discontinued-products',
  templateUrl: './ws-discontinued-products.component.html',
  styleUrl: './ws-discontinued-products.component.scss'
})
export class WsDiscontinuedProductsComponent implements OnDestroy {
  currentPage: number = 0;
  products: any[];
  totalPageCount: number = 0;
  private ngUnsubscribe = new Subject<void>();

  constructor(private productUpdatesPageService: ProductUpdatesPageService, private cd: ChangeDetectorRef) {
    this.getDetails();
  }

  getDetails() {
    this.productUpdatesPageService.fetchProductUpdates('DISCONTINUED_PRODUCTS', this.currentPage).pipe(takeUntil(this.ngUnsubscribe)).subscribe(data => {
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

  ngOnDestroy() {
    this.ngUnsubscribe.next();
    this.ngUnsubscribe.complete();
  }

  
}
