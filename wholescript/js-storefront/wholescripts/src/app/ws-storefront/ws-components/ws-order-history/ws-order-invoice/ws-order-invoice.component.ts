import {
  AfterViewInit,
  ChangeDetectorRef,
  Component,
  DestroyRef,
  inject,
  OnDestroy,
  OnInit,
  signal,
} from '@angular/core';
import { Observable, tap } from 'rxjs';
import {
  CmsService,
  Config,
  CurrencyService,
  OccEndpointsService,
} from '@spartacus/core';
import { WsOrderHistoryService } from '../../../ws-services/order-history/ws-order-history.service';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { PatientDirectService } from '../../../ws-services/ws-patient-direct.service';

@Component({
  selector: 'ws-order-invoice',
  templateUrl: './ws-order-invoice.component.html',
  styleUrl: './ws-order-invoice.component.scss',
})
export class WsOrderInvoiceComponent implements OnInit, AfterViewInit, OnDestroy {
  loading = true;
  currency$: Observable<string>;
  paginationSignal = signal<any>({});
  isAscendingOrderDate: boolean = false;
  isAscendingInvoiceDate: boolean = false;
  isAscendingOrderNumber: boolean = false;
  isAscendingInvoicerNumber: boolean = false;
  pdfLoading = false;
  pagination = {
    itemPerPage: 10,
    totalItems: 0,
    currentPage: 1,
  };
  orderInvoices: any[] = [];
  updatedOrderInvoice: any[] = [];
  private destroyRef = inject(DestroyRef);
  startDate: Date = new Date(new Date().getFullYear(), 0, 1);
  endDate: Date = new Date();
  firstDate: Date = new Date(new Date().getFullYear(), 0, 1);
  lastDate: Date = new Date();
  invoiceHeadingParagraph$: Observable<any> = this.cmsService.getComponentData(
    'wsOrderHistoryInvoicesBodyParaComponent'
  );
  searchKey: string = '';
  calendarOpened = false;
  private observer: MutationObserver | null = null;
  private lastFocusedInput: HTMLElement | null = null;
  orderStartDate:any;
  orderEndDate:any;
  minDate:any;

  constructor(
    protected currencyService: CurrencyService,
    private orderHistoryService: WsOrderHistoryService,
    private http: HttpClient,
    private config: Config,
    private occEndPointService: OccEndpointsService,
    private cd: ChangeDetectorRef,
    private cmsService: CmsService,
    private patientDirectService: PatientDirectService
  ) {
    const date = new Date();
    this.minDate = this.setFirstDayOfMonth(date);
    this.orderStartDate = this.formatDateForInput(this.minDate);
    this.orderEndDate = this.formatDateForInput(new Date());
   }

  ngOnInit(): void {
    this.fetchOrderInvoices();
    this.currency$ = this.currencyService.getActive();
  }

  ngAfterViewInit(): void {
    this.observer = new MutationObserver(() => {
      const dateSpans = document.querySelectorAll('.bs-datepicker-body td span') as NodeListOf<HTMLSpanElement>;
      if (dateSpans.length) {
        this.makeCalendarKeyboardAccessible(dateSpans);
      }
    });
    this.observer.observe(document.body, {
      childList: true,
      subtree: true
    });
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
  
  private makeCalendarKeyboardAccessible(dateSpans: NodeListOf<HTMLSpanElement>): void {
    const spansArray = Array.from(dateSpans);
    const highlightClass = 'keyboard-focus';
  
    const focusSpan = (indexToFocus: number) => {
      spansArray.forEach(s => s.classList.remove(highlightClass));
      const targetSpan = spansArray[indexToFocus];
      if (targetSpan) {
        targetSpan.focus();
        targetSpan.classList.add(highlightClass);
      }
    };
  
    spansArray.forEach((span, index) => {
      if (span.classList.contains('is-other-month')) {
        return;
      }
      if (span.getAttribute('data-keyboard-enhanced') === 'true') {
        return;
      }
  
      span.setAttribute('role', 'button');
      span.setAttribute('tabindex', '0');
  
      const day = span.textContent?.trim() || '';
      span.setAttribute('aria-label', `Select day ${day}`);
  
      span.addEventListener('keydown', (event) => {
        const keyboardEvent = event as KeyboardEvent;
        const currentIndex = index;
  
        switch (keyboardEvent.key) {
          case 'Enter':
          case ' ':
            keyboardEvent.preventDefault();
            span.click();
            break;
  
          case 'ArrowLeft':
            if (spansArray[currentIndex - 1]) {
              keyboardEvent.preventDefault();
              focusSpan(currentIndex - 1);
            }
            break;
  
          case 'ArrowRight':
            if (spansArray[currentIndex + 1]) {
              keyboardEvent.preventDefault();
              focusSpan(currentIndex + 1);
            }
            break;
  
          case 'ArrowUp':
            if (spansArray[currentIndex - 7]) {
              keyboardEvent.preventDefault();
              focusSpan(currentIndex - 7);
            }
            break;
  
          case 'ArrowDown':
            if (spansArray[currentIndex + 7]) {
              keyboardEvent.preventDefault();
              focusSpan(currentIndex + 7);
            }
            break;
        }
      });
  
      span.addEventListener('focus', () => {
        spansArray.forEach(s => s.classList.remove(highlightClass));
        span.classList.add(highlightClass);
      });
  
      span.addEventListener('click', () => {
        console.log(`Day selected: ${span.textContent}`);
        if (this.lastFocusedInput) {
          setTimeout(() => {
            this.lastFocusedInput?.focus();
          }, 0);
        }
      });
  
      span.setAttribute('data-keyboard-enhanced', 'true');
    });
  }
  
  onDateKeydown(event: KeyboardEvent, field: 'first' | 'last') {
    let currentDate = field === 'first' ? this.firstDate : this.lastDate;
    if (!currentDate) {
      currentDate = new Date();
    } else {
      currentDate = new Date(currentDate);
    }
    const key = event.key;
    switch (key) {
      case 'Enter':
      case ' ':
        this.calendarOpened = true;
        this.lastFocusedInput = event.target as HTMLElement;
        const inputEl = event.target as HTMLElement;
        inputEl.dispatchEvent(new Event('click'));
  
        setTimeout(() => {
          const calendarEl = document.querySelector('.bs-datepicker-container');
          if (calendarEl) {
            const firstTabbable = calendarEl.querySelector<HTMLElement>(
              'button:not([disabled]), [tabindex]:not([tabindex="-1"])'
            );
            if (firstTabbable) {
              firstTabbable.focus();
            }
          }
        }, 100);
        break;
      case 'Escape':
        this.calendarOpened = false;
        (document.activeElement as HTMLElement).blur();
        break;
  
      default:
        return;
    }
    if (field === 'first') {
      this.firstDate = currentDate;
    } else {
      this.lastDate = currentDate;
    }
    this.onInvoiceDateChange();
    event.preventDefault();
  }
  

  onInvoiceDateChange() {
    if (this.orderStartDate && this.orderEndDate) {
      this.fetchOrderInvoices();
    }
    this.cd.detectChanges();
  }

  // formatDate(date: Date): string {
  //   const year = date.getFullYear();
  //   const month = ('0' + (date.getMonth() + 1)).slice(-2);
  //   const day = ('0' + date.getDate()).slice(-2);
  //   return `${year}-${month}-${day}`;
  // }

  fetchOrderInvoices() {
    if (!this.orderStartDate || !this.orderEndDate) {
      return;
    }
    this.loading = true;
    this.orderHistoryService
      .getOrderInvoice(
        this.orderEndDate,
        this.orderStartDate
      )
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: (res: any) => {
          if (res?.invoiceList?.items?.length) {
            this.orderInvoices = res.invoiceList.items;
            this.updatedOrderInvoice = [...this.orderInvoices];
          } else {
            this.orderInvoices = [];
            this.updatedOrderInvoice = [];
          }
          this.updatePagination();
          this.sortOrdersOnOrderDate(); 
          this.sortOrdersOnInvoiceDate();
          this.sortOnOrderNumber();
          this.sortOnInvoiceNumber();
          this.loading = false;
          this.cd.detectChanges();
        },
        error: (err) => {
          this.orderInvoices = [];
          this.updatedOrderInvoice = [];
          this.updatePagination();
          this.loading = false;
          this.cd.detectChanges();
          console.error('Error fetching invoices:', err);
        },
        complete: () => {
          this.loading = false;
        },
      });
  }

  searchInvoices(event: string) {
    this.searchKey = event.toLowerCase().trim().replace(/\s+/g, ' ');
    const queryWords = this.searchKey.split(' ');
    this.updatedOrderInvoice = this.orderInvoices.filter((order) => {
      return queryWords.every(
        (word) =>
          order?.invoiceNumber?.toLowerCase().includes(word) ||
          order?.order?.items?.some((item: any) => item?.orderNumber?.toLowerCase().includes(word))
      );
    });
    this.updatePagination();
    this.cd.detectChanges(); 
  }

  downloadPdf(invoiceNumber: string) {
    this.pdfLoading = true;
    this.orderHistoryService.pdf(invoiceNumber).subscribe({
      next: (data: any) => {
        const byteString = window.atob(data.o_XSTRING);
        const arrayBuffer = new ArrayBuffer(byteString.length);
        const int8Array = new Uint8Array(arrayBuffer);
        for (let i = 0; i < byteString.length; i++) {
          int8Array[i] = byteString.charCodeAt(i);
        }
        const blob = new Blob([int8Array], { type: 'application/pdf' });
        const url = URL.createObjectURL(blob);
        window.open(url, '_blank');
        this.pdfLoading = false;
        this.cd.detectChanges();
      },
      error: (error) => {
        this.pdfLoading = false;
        console.log('Error downloading PDF:', error);
        this.cd.detectChanges();
      },
      complete: () => {
        this.pdfLoading = false;
        this.cd.detectChanges();
      },
    });
  }
  exportTable() {
    this.patientDirectService.exportCSV(this.orderInvoices, 'ws_accural list');
  }

  sortOrdersOnInvoiceDate() {
    this.isAscendingInvoiceDate = !this.isAscendingInvoiceDate;
    this.updatedOrderInvoice.sort((a: any, b: any) => {
      const dateA = new Date(a.invoiceDate);
      const dateB = new Date(b.invoiceDate);
      if (isNaN(dateA.getTime()) || isNaN(dateB.getTime())) {
        return 0;
      }
      return this.isAscendingInvoiceDate
        ? dateA.getTime() - dateB.getTime()
        : dateB.getTime() - dateA.getTime();
    });
    this.cd.detectChanges();
  }

  sortOnInvoiceNumber() {
    this.isAscendingInvoicerNumber = !this.isAscendingInvoicerNumber;
    this.updatedOrderInvoice.sort((a: any, b: any) => {
      const orderA = a.invoiceNumber;
      const orderB = b.invoiceNumber;
      if (this.isAscendingInvoicerNumber) {
        return orderA.localeCompare(orderB);
      } else {
        return orderB.localeCompare(orderA);
      }
    });
  }

  sortOnOrderNumber() {
    this.isAscendingOrderNumber = !this.isAscendingOrderNumber;
    this.updatedOrderInvoice.sort((a: any, b: any) => {
      const orderA = a.order?.items[0]?.orderNumber;
      const orderB = b.order?.items[0]?.orderNumber;
      if (this.isAscendingOrderNumber) {
        return orderA.localeCompare(orderB);
      } else {
        return orderB.localeCompare(orderA);
      }
    });
  }

  sortOrdersOnOrderDate() {
    this.isAscendingOrderDate = !this.isAscendingOrderDate;
    this.updatedOrderInvoice.sort((a: any, b: any) => {
      const dateA = new Date(a.order?.items?.[0]?.orderDate); 
      const dateB = new Date(b.order?.items?.[0]?.orderDate);
      if (isNaN(dateA.getTime()) || isNaN(dateB.getTime())) {
        return 0; 
      }
      return this.isAscendingOrderDate
        ? dateA.getTime() - dateB.getTime()
        : dateB.getTime() - dateA.getTime();
    });
    this.cd.detectChanges();
  }

  updatePagination() {
    this.pagination = {
      itemPerPage: 10,
      totalItems: this.updatedOrderInvoice.length,
      currentPage: 1,
    };
    this.paginationSignal.set(this.pagination);
  }
  pageChanged(event: any) {
    this.pagination.currentPage = event;
    this.pagination = {
      itemPerPage: 10,
      totalItems: this.updatedOrderInvoice.length,
      currentPage: event,
    };
    this.paginationSignal.set(this.pagination);
  }

  getMinDate() {
    return new Date(2019, 0, 1);
  }

  ngOnDestroy(): void {
    if (this.observer) {
      this.observer.disconnect();
    }
  }
}
