import { ChangeDetectorRef, Component, Input, OnChanges, signal, SimpleChanges } from '@angular/core';
import { Router } from '@angular/router';
import { CurrencyService } from '@spartacus/core';
import { Observable } from 'rxjs';

@Component({
  selector: 'ws-autoships-lists',
  templateUrl: './ws-autoships-lists.component.html',
  styleUrl: './ws-autoships-lists.component.scss'
})
export class WsAutoshipsListsComponent implements OnChanges {
  @Input() autoShipsHeaderItems: any[] = [];
  showGridView: boolean = true;
  isNoSearchResultsFlag: boolean = false;
  searchText: string = '';
  toggleListViewMoreFlag: any = {};
  searchKeys = ['orderNumber', 'shipTo', 'shippingDate'];
  originalAutoShipHeaderItems: any[] = [];
  paginationSignal = signal<any>({});
  pagination = {
    itemPerPage: 10,
    totalItems: 0,
    currentPage: 1,
  };
  currency$: Observable<string>;
  public Number = Number;
  sortOptions = [
    {code: 'Latest', name: 'Latest-oldest', selected: true},
    {code: 'Oldest', name: 'Oldest-latest', selected: false}
  ]

  constructor(private cd: ChangeDetectorRef, protected currencyService: CurrencyService,
    private router: Router
  ) {
    this.currency$ = this.currencyService.getActive();
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['autoShipsHeaderItems']) {
      if (this.autoShipsHeaderItems?.length > 0) {
        this.originalAutoShipHeaderItems = [...this.autoShipsHeaderItems];
        this.autoShipsHeaderItems = this.originalAutoShipHeaderItems?.sort(function (a, b) {
          const dateA = new Date(a.shippingDate).getTime();
          const dateB = new Date(b.shippingDate).getTime();
          return dateB - dateA;
        });
        this.processShipmentDetails();
      }
    }
  }

  processShipmentDetails() {
    this.pagination = {
      itemPerPage: 10,
      totalItems: this.autoShipsHeaderItems?.length || 0,
      currentPage: 1,
    };
    this.paginationSignal.set(this.pagination);
  }

  pageChanged(event: any) {
    this.pagination.currentPage = event;
    this.pagination = {
      itemPerPage: 10,
      totalItems: this.autoShipsHeaderItems.length || 0,
      currentPage: event,
    };
    this.paginationSignal.set(this.pagination);
  }

   isNumeric(value: any) {
    return /^-?\d+$/.test(value);
  }

  updateSearch(event: any) {
    this.searchText = event;
    let searchText = this.searchText?.toLowerCase();
    if (this.originalAutoShipHeaderItems?.length) {
      this.autoShipsHeaderItems = this.originalAutoShipHeaderItems?.filter((item: { [x: string]: { toString: () => string; }; hasOwnProperty: (arg0: string) => any; }) => {
        return true;
      })?.map((item: any) => {
        const products = item?.products?.filter((product: { matDesc: any; orderNumber: any }) => (product?.matDesc.toLowerCase().includes(searchText)) || 
        product?.orderNumber.toLowerCase().includes(searchText)) || [];
        let totalPrice = 0;
        products?.map((product: { price: any; reqQty: any }) => {
          totalPrice += Number((product?.price * Number(product.reqQty)) || 0)
        })
        return {
          ...item,
          products: products,
          totalPrice: item?.totalPrice
        }
      })?.filter((product: { products: string | any[]; }) => product?.products?.length > 0) || [];
      this.pagination = {
        itemPerPage: 10,
        totalItems: this.autoShipsHeaderItems.length || 0,
        currentPage: 1,
      };
      this.paginationSignal.set(this.pagination);
      this.cd.detectChanges();
    }
  }

  goToAutoShipDetails(orderNumber: number) {
    this.router.navigateByUrl('autoships-details?id=' + orderNumber)
  }
  sortAutoships(event: any) {
    if (this.originalAutoShipHeaderItems?.length) { 
      this.autoShipsHeaderItems = this.originalAutoShipHeaderItems?.sort(function (a, b) {
        const dateA = new Date(a.shippingDate).getTime();
        const dateB = new Date(b.shippingDate).getTime();
        return dateA === dateB ? 0 : event.target.value === 'Latest' ? dateB - dateA : dateA - dateB;
      });
      this.cd.detectChanges();
    }
  }

  onKeydown(event: KeyboardEvent, orderNumber: number): void {
    if (event.key === 'Enter' || event.key === ' ') {
      event.preventDefault();
      this.goToAutoShipDetails(orderNumber);
    }
  }
}
