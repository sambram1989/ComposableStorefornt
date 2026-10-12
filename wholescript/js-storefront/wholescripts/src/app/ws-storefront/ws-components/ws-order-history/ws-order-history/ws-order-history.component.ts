import {
  AfterViewInit,
  ChangeDetectorRef,
  Component,
  DestroyRef,
  ElementRef,
  OnInit,
  signal,
  ViewChild,
} from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { CurrencyService } from '@spartacus/core';
import { Observable, of } from 'rxjs';
import { WsOrderHistoryService } from '../../../ws-services/order-history/ws-order-history.service';
import { userMap } from '../../../ws-env/environment';
import { ActivatedRoute } from '@angular/router';
import { PatientProfileService } from '../../../ws-services/ws-patient-profile.service';
import { BsDatepickerDirective } from 'ngx-bootstrap/datepicker';

@Component({
  selector: 'ws-order-history',
  templateUrl: './ws-order-history.component.html',
  styleUrl: './ws-order-history.component.scss',
})
export class WsOrderHistoryComponent implements OnInit, AfterViewInit {
  currency$: Observable<string>;
  curUser$: Observable<any>;
  componentListArray: any[] = [];
  orderHistoryList: any[] = [];
  updatedOrdersList: any[] = [];
  estoreOrders: any[] = [];
  webErrorOrders: any[] = [];
  filteredWebErrorOrders: any[] = [];
  paginationSignal = signal<any>({});
  isAscendingDate: boolean = true;
  isAscendingOrder: boolean = true;
  pagination = {
    itemPerPage: 10,
    totalItems: 0,
    currentPage: 1,
  };
  isPractioner: boolean;
  startDate: Date = new Date(new Date().getFullYear(), 0, 1);
  endDate: Date = new Date();
  searchKey = '';
  loading = true;
  user: any;
  patientId: string = '';
  selectedDate: Date = new Date();
  arrowKeyListener: any;
  orderStartDate:any;
  orderEndDate:any;
  minDate:any;

  constructor(
    private destroyRef: DestroyRef,
    private cd: ChangeDetectorRef,
    public orderHistoryService: WsOrderHistoryService,
    protected currencyService: CurrencyService,
    private route: ActivatedRoute,
    private patientProfileService: PatientProfileService
  ) {
    this.currency$ = this.currencyService.getActive();
    const date = new Date();
    this.minDate = this.setFirstDayOfMonth(date);
    this.orderStartDate = this.formatDateForInput(this.minDate);
    this.orderEndDate = this.formatDateForInput(new Date());
    this.route.queryParams.subscribe((params: any) => {
      if (params?.uid) {
        this.orderStartDate = this.formatDateForInput(new Date(2000, 0, 1));
        this.getPatientOrderHistoryDetails(params?.uid);
        this.patientId = params.uid;
      } else {
        this.patientId = '';
        this.fetchOrderHistory();
        this.fetchOrderWebError();
      }
    });
  }

  ngOnInit(): void {
    this.isAscendingDate = false; 
    this.isAscendingOrder = false; 
  }

  ngAfterViewInit(): void {
    if(document.getElementsByTagName('title') && !document.getElementsByTagName('title')[0].innerText) document.getElementsByTagName('title')[0].innerText = 'Order History';
  }

  // formatDate(date: Date): string {
  //   console.log('date', date);
  //   const year = date.getFullYear();
  //   const month = ('0' + (date.getMonth() + 1)).slice(-2);
  //   const day = ('0' + date.getDate()).slice(-2);
  //   return `${year}-${month}-${day}`; //2025-04-02
  // }

  getPatientOrderHistoryDetails(email: string) {
    if (!this.orderStartDate || !this.orderEndDate) {
      return;
    }
    this.loading = true;
    this.patientProfileService.fetchPatientOrderHistoryDetails(email, this.orderStartDate, this.orderEndDate)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe(res => {
        if (res && res.order_list && res.order_list.items) {
          this.orderHistoryList = res.order_list.items;
          this.updatedOrdersList = [...this.orderHistoryList];
          this.sortOrdersByDefault();
        } else {
          this.orderHistoryList = [];
          this.updatedOrdersList = [];
        }
        this.updatePagination();
        this.loading = false;
        this.cd.detectChanges();
      }, () => {
        this.loading = false;
      });
  }

  getUserInfo(orders: any) {
    const user = JSON.parse(sessionStorage.getItem('user_config'));
    if (user) {
      this.user = user;
      const key = Object.keys(userMap).find(
        (key) => userMap[key] === 'practitioner'
      );
      this.isPractioner = (user?.roles || []).includes(key);
      this.fetchEstoreOrderHistory(orders);
      this.curUser$ = of(user);
    }
  }

  onDateChange() {
    if (this.orderStartDate && this.orderEndDate) {
      if(this.patientId) {
        this.getPatientOrderHistoryDetails(this.patientId);
      } else {
        this.fetchOrderHistory();
      }
    }
    this.cd.detectChanges();
  }
  
  formatDateForInput(date: Date): string {
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0'); 
    return `${year}-${month}-${day}`; 
  }

  setFirstDayOfMonth(date: Date): Date {
    return new Date(date.getFullYear(), date.getMonth() - 3, 1);
  }

  fetchOrderHistory() {
    if (!this.orderStartDate || !this.orderEndDate) {
      return;
    }
    this.loading = true;
    this.orderHistoryService
      .getOrders(this.orderEndDate, this.orderStartDate)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: (res) => {
          if (res && res.order_list && res.order_list.items) {
            this.orderHistoryList = res.order_list.items;
            this.updatedOrdersList = [...this.orderHistoryList];
            this.sortOrdersByDefault();
            this.getUserInfo(res.order_list.items);
          } else {
            this.orderHistoryList = [];
            this.updatedOrdersList = [];
          }
          this.updatePagination();
          this.loading = false;
          this.cd.detectChanges();
        },
        error: (err) => {
          this.orderHistoryList = [];
          this.updatedOrdersList = [];
          this.updatePagination();
          this.loading = false;
          this.cd.detectChanges();
          console.error('Error fetching orders:', err);
        },
        complete: () => {
          this.loading = false;
        },
      });
  }

  fetchOrderWebError() {
    this.orderHistoryService.getOrderWebError()
    .pipe(takeUntilDestroyed(this.destroyRef))
    .subscribe({
      next: (res) => {
        this.webErrorOrders = res?.t_ORDERS?.item;
        this.filterWebErrorOrders();
        this.cd.detectChanges();
      },
      error: (err) => {
        this.cd.detectChanges();
      }
    })
  }

  fetchEstoreOrderHistory(orders: any) {
    let entries: any[] = [];
    orders.forEach((element: any) => {
      entries.push(element.salesOrder);
    });
    const payload = {
      sapCustomerId: this.user?.orgUnit?.uid,
      sapOrderNumbers: entries,
    };
    this.orderHistoryService
      .getEstoreOrders(payload)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe((res) => {
        this.estoreOrders = res && res.orders ? res.orders : [];
        this.updateOrdersWithEstoreData();
        this.cd.detectChanges();
      });
  }

  filterWebErrorOrders(): void {
    if (!this.webErrorOrders || !this.estoreOrders) {
      this.filteredWebErrorOrders = [];
      return;
    }
    const webErrorOrderNumbers = this.webErrorOrders.map((order: any) => order.VBELN);
    const matchedOrders = this.estoreOrders.filter((order: any) => {
      return webErrorOrderNumbers.includes(order?.sapOrderNum);
    });
    this.filteredWebErrorOrders = matchedOrders.length > 0  
    ? [...matchedOrders, ...this.webErrorOrders.filter(order => !webErrorOrderNumbers.includes(order.VBELN))]  
    : [...this.webErrorOrders];
    this.cd.detectChanges(); 
  }

  customOrders(): any {
    this.orderHistoryList.forEach((order) => {
      let i = this.estoreOrders.findIndex(
        (spOrder) => spOrder.sapOrderNum === order.salesOrder
      );
      if (i !== -1) order.estore = this.estoreOrders[i];
    });
    return this.orderHistoryList;
  }

  updateOrdersWithEstoreData() {
    const estoreOrderMap = new Map(
      this.estoreOrders.map((order) => [order.sapOrderNum, order])
    );
    this.updatedOrdersList.forEach((order) => {
      const estoreOrder = estoreOrderMap.get(order.salesOrder);
  
      if (estoreOrder) {
        order.badge = estoreOrder.apiStoreName || 'API';
        order.orderNumber =
          estoreOrder.apiStoreName === 'Shopify'
            ? estoreOrder.externalOrderId
            : order.apiRefNo;
        order.externalOrderURL =
          estoreOrder.apiStoreName === 'Shopify'
            ? estoreOrder.externalOrderURL
            : null;
      } else if (order.apiRefNo) {
        order.orderNumber = order.apiRefNo;
        order.badge = 'API';
        order.externalOrderURL = null; 
      }
    });
  
    this.cd.detectChanges();
  }

  sortOrdersByDefault() {
    if (this.updatedOrdersList && this.updatedOrdersList.length > 0) {
      this.updatedOrdersList.sort((a: any, b: any) => {
        return new Date(b.docDate).getTime() - new Date(a.docDate).getTime();
      });
      this.isAscendingDate = false;
    }
  }

  sortOrdersOnDate() {
    this.isAscendingDate = !this.isAscendingDate;
    this.updatedOrdersList.sort((a: any, b: any) => {
      const dateA = new Date(a.docDate).getTime();
      const dateB = new Date(b.docDate).getTime();
      return this.isAscendingDate ? dateA - dateB : dateB - dateA;
    });
  }

  sortOnOrderId() {
    this.isAscendingOrder = !this.isAscendingOrder;
    this.updatedOrdersList.sort((a: any, b: any) => {
      const orderA = a.salesOrder || '';
      const orderB = b.salesOrder || '';
      return this.isAscendingOrder ? orderA.localeCompare(orderB) : orderB.localeCompare(orderA);
    });
  }

  searchOrders(searchKey: string) {
    const query = searchKey.toLowerCase().trim().replace(/\s+/g, ' ');
    const queryWords = query.split(' ');
    this.updatedOrdersList = this.orderHistoryList.filter((order) => {
      return queryWords.every(
        (word) =>
          order.salesOrder.toLowerCase().includes(word) ||
          order.shipName.toLowerCase().includes(word)
      );
    });
    this.updatePagination();
    this.cd.detectChanges();
  }

  updatePagination() {
    this.pagination = {
      itemPerPage: 10,
      totalItems: this.updatedOrdersList.length,
      currentPage: 1,
    };
    this.paginationSignal.set(this.pagination);
  }

  pageChanged(event: any) {
    this.pagination.currentPage = event;
    this.pagination = {
      itemPerPage: 10,
      totalItems: this.updatedOrdersList.length,
      currentPage: event,
    };
    this.paginationSignal.set(this.pagination);
  }

  getMinDate() {
    return new Date(2019, 0, 1);
  }
}
