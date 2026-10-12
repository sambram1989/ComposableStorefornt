import {
  ChangeDetectorRef,
  Component,
  signal,
} from '@angular/core';
import { DatePipe } from '@angular/common';
import { WsOrderHistoryService } from '../../../ws-services/order-history/ws-order-history.service';

@Component({
  selector: 'ws-order-aging',
  templateUrl: './ws-order-aging.component.html',
  styleUrl: './ws-order-aging.component.scss',
  providers: [DatePipe],
})
export class WsOrderHistoryAgingComponent {
  days: any[] = [];
  selectedRange: any;
  agingRecords: any;
  today = new Date();
  initialLoad: any[] = [];
  rangeTotal: number = 0;
  totalBalance = 0;
  paginationSignal = signal<any>({});
  pagination = {
    itemPerPage: 10,
    totalItems: 0,
    currentPage: 1,
  };

  constructor(
    private orderService: WsOrderHistoryService, 
    private datePipe: DatePipe, 
    private cdRef: ChangeDetectorRef
  ) 
  {
    this.loadAgingDetails();
  }

  formatNumberDays(input: string): number {
    return +input; 
  }

  getRastKeyFromRange(range: string): string {
    switch (range) {
      case '0 - 30': return 'rast0';
      case '31 - 44': return 'rast1';
      case '45 - 59': return 'rast2';
      case '60 - 90': return 'rast3';
      case '90+': return 'rast4';
      default: return '';
    }
  }

  loadAgingDetails(isLoad: boolean = true) {
    const formattedDate = this.datePipe.transform(this.today, 'yyyy-MM-dd');
    this.orderService.getOrderAging(formattedDate).subscribe((res) => {
      this.agingRecords = res;
  
      const totals = res.agingTotals;
      this.totalBalance = Object.keys(totals)
        .filter(key => key.startsWith('rast'))
        .reduce((sum, key) => sum + (totals[key] || 0), 0);
  
      if (isLoad) {
        this.setGraphDays(res);
        this.fliterItems(this.days[this.days.length - 1]); 
      }
      this.cdRef.detectChanges();
    });
  }
  
  setGraphDays(res: any) {
    const dayItems = res.itDays?.item;
    this.days = [];

    const parsedDays = dayItems.map((d: any) => parseInt(d.days));
    let lowerBound = 0;

    for (let i = 0; i < parsedDays.length; i++) {
      const upperBound = parsedDays[i] - 1;
      if (i === parsedDays.length - 1) {
        this.days.push({
          dayRange: `${lowerBound} - ${parsedDays[i]}`,
          selected: false,
          startDaysAgo: lowerBound,
          endDaysAgo: parsedDays[i],
        });
        this.days.push({
          dayRange: `90+`,
          selected: true,
          startDaysAgo: parsedDays[i] + 1,
          endDaysAgo: null,
        });
      } else {
        this.days.push({
          dayRange: `${lowerBound} - ${upperBound}`,
          selected: false,
          startDaysAgo: lowerBound,
          endDaysAgo: upperBound,
        });
      }
      lowerBound = parsedDays[i];
    }
    this.selectedRange = '90+';
  }

  setSelectionDaysRange(day: any) {
    this.selectedRange = day.dayRange;
    this.days.forEach((item) => {
      item.selected = item.dayRange === day.dayRange;
    });
    this.agingRecords?.etItems && this.fliterItems(day);
  }

  fliterItems(day: any) {
    this.initialLoad = [];
    this.rangeTotal = 0;
  
    const now = this.today.getTime();
    const start = day.endDaysAgo
      ? now - day.endDaysAgo * 24 * 60 * 60 * 1000
      : 0;
    const end = now - day.startDaysAgo * 24 * 60 * 60 * 1000;
  
    const key = this.getRastKeyFromRange(day.dayRange);
  
    const filteredItems = this.agingRecords?.etItems?.item.filter((element: any) => {
      const due = new Date(element.dueDate).getTime();
      const isInRange = due >= start && due <= end;
      if (isInRange && key in element) {
        this.rangeTotal += parseFloat(element[key]) || 0;
      }
      return isInRange;
    });
  
    this.initialLoad.push(...filteredItems);
    this.updatePagination();
  }

  printPDF(invoice: any) {
    this.downloadPdf(invoice)
  }

  downloadPdf(invoiceNumber: string) {
    this.orderService.pdf(invoiceNumber).subscribe({
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
      },
      error: (error) => {
        console.log('Error downloading PDF:', error);
      },
      complete: () => {
        this.cdRef.detectChanges();
      },
    });
  }

  updatePagination() {
    this.pagination = {
      itemPerPage: 10,
      totalItems: this.initialLoad.length,
      currentPage: 1,
    };
    this.paginationSignal.set(this.pagination);
  }

  pageChanged(event: any) {
    this.pagination.currentPage = event;
    this.pagination = {
      itemPerPage: 10,
      totalItems: this.initialLoad.length,
      currentPage: event,
    };
    this.paginationSignal.set(this.pagination);
  }
}
