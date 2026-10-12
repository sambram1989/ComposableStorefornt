import {
  ChangeDetectorRef,
  Component,
  DestroyRef,
  inject,
  signal,
} from '@angular/core';
import { PatientDirectService } from '../../../ws-services/ws-patient-direct.service';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { WsOrderHistoryService } from '../../../ws-services/order-history/ws-order-history.service';
import { BreakpointObserver } from '@angular/cdk/layout';
import { DatePipe , Location } from '@angular/common';
import { debounceTime, Subject } from 'rxjs';
import { ActivatedRoute, Router } from '@angular/router';
@Component({
  selector: 'ws-accurals',
  templateUrl: './ws-accurals.component.html',
  styleUrls: ['./ws-accurals.component.scss'],
})
export class WsAccuralsComponent {
  referrals: any[] = [];
  selectedReferral = 0;
  selectedMonth: number = 0;
  selectedYear = new Date().getFullYear();
  selectedLastnameAndReferral: string = '';
  totalConsultationFees: number = 0;
  isAscendingName: boolean = true;
  isAscendingOrder: boolean = true;
  isAscendingInvoicerNumber: boolean = true;
  searchQuery = '';
  accuralsData: any[] = [];
  updatedAccuralsData: any[] = [];
  paginationSignal = signal<any>({});
  pagination = {
    itemPerPage: 10,
    totalItems: 0,
    currentPage: 1,
  };
  loading: boolean = true;
  months: string[] = [];
  years: number[] = [];
  pdfLoading = false;
  isMobile = false;
  private destroyRef = inject(DestroyRef);

  private queryParamsChanged = new Subject<void>();

  constructor(
    public patientDirectService: PatientDirectService,
    private orderHistoryService: WsOrderHistoryService,
    private cdref: ChangeDetectorRef,
    private bObserver: BreakpointObserver,
    private datePipe: DatePipe,
    private route: ActivatedRoute, 
    private router: Router,
    private location: Location,
  ) {
    this.getReferrals();
    this.months = this.getMonthsArray();
    this.years = this.getYearsArray();
    this.bObserver.observe('(max-width: 599px)').subscribe((result) => {
      this.isMobile = false;
      if (result.matches) {
        this.isMobile = true;
      }
    });
    this.route.queryParams.subscribe(params => {
      this.selectedReferral = params['referral'] || 0;
      this.selectedLastnameAndReferral = params['referral'] || 0;
      this.selectedMonth = params['month'] || 0;
      this.selectedYear = params['year'] || new Date().getFullYear();;
      this.searchQuery = params['search'] || '';
      if(this.searchQuery) {
        this.searchAccurals(this.searchQuery);
      }
    });
    //update route
    this.queryParamsChanged.pipe(
      debounceTime(300),
    ).subscribe(() => {
      //This below code will not scroll to top of window on every selection in month , year or referral
      this.location.replaceState(`/patient-direct-hub/Accruals?year=${this.selectedYear}&month=${this.selectedMonth}&referral=${this.selectedLastnameAndReferral}&search=${this.searchQuery}`);
    });
    this.getAccuralData();
   
  }

  searchAccurals(searchKey: string) {
    this.searchQuery = searchKey;
    this.queryParamsChanged.next();
    const query = searchKey?.toLowerCase().trim();
    this.updatedAccuralsData = this.accuralsData.filter((order: any) =>
      order?.invoiceNumber?.toLowerCase().includes(query) ||
      order?.order?.items[0]?.orderNumber.toLowerCase().includes(query) ||
      order?.address?.items[0]?.name?.toLowerCase().includes(query) ||
      searchKey==''
    );
    this.updatePagination();
    this.cdref.detectChanges();
  }
  

  getMonthsArray(): string[] {
    const months: string[] = [];
    for (let i = 0; i < 12; i++) {
      months.push(new Date(0, i).toLocaleString('default', { month: 'long' }));
    }
    return months;
  }

  getYearsArray(): number[] {
    const currentYear = new Date().getFullYear();
    const startYear = currentYear - 10;
    const endYear = currentYear;

    const years: number[] = [];
    for (let year = startYear; year <= endYear; year++) {
      years.push(year);
    }

    return years;
  }

  updatePagination() {
    this.pagination = {
      itemPerPage: 10,
      totalItems: this.updatedAccuralsData.length,
      currentPage: 1,
    };
    this.paginationSignal.set(this.pagination);
  }

  pageChanged(event: any) {
    this.pagination.currentPage = event;
    this.pagination = {
      itemPerPage: 10,
      totalItems: this.updatedAccuralsData.length,
      currentPage: event,
    };
    this.paginationSignal.set(this.pagination);
  }

  getReferrals() {
    this.patientDirectService.getAllReferralEntriesForCSV().subscribe((res) => {
      let listOfLastNameAndReferrals = res?.referralCodeListWsDto.map((code : any) => {
        return (code?.referralCode) + (code?.lastName ? (" | " + code?.lastName) : null);
      });
      this.referrals = listOfLastNameAndReferrals;
    });
  }

  getSelection(el: any, type: string) {
    if (type === 'month') {
      this.selectedMonth = Number(el.target.value); 
    } else if (type === 'referral') {
      this.selectedLastnameAndReferral = el.target.value;  
    } else {
      this.selectedYear = Number(el.target.value);    
    }
    this.queryParamsChanged.next();
    this.getAccuralData();
  }

  daysInMonth(year: any, month: any) {
    return new Date(year, month + 1, 0).getDate();
  }

  sortByName() {
    this.isAscendingName = !this.isAscendingName;
    this.updatedAccuralsData.sort((a: any, b: any) => {
      const nameA = a?.address?.items?.name || ''; 
      const nameB = b?.address?.items?.name || '';
      if (this.isAscendingName) {
        return nameA.localeCompare(nameB);
      } else {
        return nameB.localeCompare(nameA);
      }
    });
    this.cdref.detectChanges(); 
  }
  
  sortByOrderNumber() {
    this.isAscendingOrder = !this.isAscendingOrder;
    this.updatedAccuralsData.sort((a: any, b: any) => {
      const orderA = a?.order?.items?.orderNumber || '';
      const orderB = b?.order?.items?.orderNumber || '';
      if (this.isAscendingOrder) {
        return orderA.localeCompare(orderB);
      } else {
        return orderB.localeCompare(orderA);
      }
    });
  }
  
  sortByInvoiceNumber() {
    this.isAscendingInvoicerNumber = !this.isAscendingInvoicerNumber;
    this.updatedAccuralsData.sort((a: any, b: any) => {
      const orderA = a.invoiceNumber || '';
      const orderB = b.invoiceNumber || '';
      if (this.isAscendingInvoicerNumber) {
        return orderA.localeCompare(orderB);
      } else {
        return orderB.localeCompare(orderA);
      }
    });
  }

  formatDate(date: Date): string {
    const year = date.getFullYear();
    const month = ('0' + (date.getMonth() + 1)).slice(-2);
    const day = ('0' + date.getDate()).slice(-2);
    return `${year}-${month}-${day}`;
  }

  getAccuralData() {
    const month = this.selectedMonth + 1; 
    const startDate = new Date(this.selectedYear, this.selectedMonth, 1);
    const endDate = new Date(this.selectedYear, month, 0);
    this.loading = true;
    this.patientDirectService
      .getAccurals(this.formatDate(startDate), this.formatDate(endDate) , "true")
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: (res: any) => {
          if (res?.invoiceList?.items?.length) {
            this.accuralsData = res.invoiceList.items;

            // code to filter based on referral code and then update the list
            if(this.selectedLastnameAndReferral == '0' || this.selectedLastnameAndReferral == '') 
              this.updatedAccuralsData = [...this.accuralsData];
            else {
              this.accuralsData = this.accuralsData.filter((element) => {
                let incomingAccrLastnameAndReferral = (element?.patientReferralCode + " | " + element?.patientLastName);
                
                if(this.selectedLastnameAndReferral.toLocaleLowerCase() == "unassigned")
                  return (!element?.patientReferralCode ||  !element?.patientLastName);   // doesn't have either referral code or lastname
                else
                  return (incomingAccrLastnameAndReferral.toLocaleLowerCase() == this.selectedLastnameAndReferral.toLocaleLowerCase()); // matched the referral code and lastname

              });
              this.updatedAccuralsData = [...this.accuralsData];
            }
            // this.updatedAccuralsData = [...this.accuralsData];

            // below code is to get the total consultation fees
            this.totalConsultationFees = 0;
            this.updatedAccuralsData.map((item , i) => {
              this.totalConsultationFees = this.totalConsultationFees + (item?.docCommission < 0 ? 0 : item?.docCommission)
            });
            console.log("this.totalConsultationFees : ",this.totalConsultationFees);
          } else {
            this.accuralsData = [];
            this.updatedAccuralsData = [];
            this.totalConsultationFees = 0;
          }
          this.updatePagination();
          this.loading = false;
          this.cdref.detectChanges();
        },
        error: (err) => {
          this.accuralsData = [];
          this.updatedAccuralsData = [];
          this.totalConsultationFees = 0;
          this.updatePagination();
          this.loading = false;
          this.cdref.detectChanges();
          console.error('Error fetching accruals:', err);
        },
      });
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
        
        if (this.isIOS()) {
          console.log("ios");
          const reader = new FileReader();
          reader.onloadend = () => {
            const dataUrl = reader.result as string;
            const win = window.open(dataUrl, '_blank');
            if (!win) {
              alert('Please allow popups to view the PDF.');
            }
          };
          reader.readAsDataURL(blob);
        } else {
          console.log("ios else");
          window.open(url, '_blank');
        }
        this.pdfLoading = false;
        this.cdref.detectChanges();
      },
      error: (error) => {
        this.pdfLoading = false;
        console.log('Error downloading PDF:', error);
        this.cdref.detectChanges();
      },
      complete: () => {
        this.pdfLoading = false;
        this.cdref.detectChanges();
      },
    });
  }
  
  isIOS() {
    const userAgent = navigator.userAgent || '';
  
    // Newer Chromium-based browsers support userAgentData
    const uaData = (navigator as any).userAgentData;
    const isMobile = uaData?.mobile ?? /Mobi|Android/i.test(userAgent);
  
    // iOS detection via user agent (still works in Safari)
    const isIOSUA = /iPhone|iPad|iPod/i.test(userAgent);
  
    // iPadOS 13+ can appear as 'Mac' but supports touch
    const isTouchMac = navigator.userAgent.includes('Macintosh') && 'ontouchend' in document;
  
    return isIOSUA || isTouchMac;
  }
  
  exportTable() {
    const exportAccuralsData = this.accuralsData?.map(order => {
      return {
        'Patient Name': order?.address?.items[0]?.name.split(",")[0] || '',
        'Invoice Number': order.invoiceNumber,
        'Order Number': order.order?.items?.map((order : any) => order.orderNumber).join(", "),
        'Order Date': order.order?.items?.map((order : any) => this.datePipe.transform(new Date(order?.orderDate), 'MM/dd/yy')).join(", "),
        'Invoice Date': this.datePipe.transform(new Date(order?.invoiceDate), 'MM/dd/yy'),
        'Invoice Total': '$' + (order.total || 0),
        'Product Total': '$' + (order.netPrice || 0),
        'Standard Consultation': '$' + ((this.getAbsoluteValue(order.docCommission) + this.getAbsoluteValue(order?.discount)) || 0),
        'Discount': '$' + (this.getAbsoluteValue(order?.discount) || 0),
        'Consultation Fee': '$' + this.getAbsoluteValue(order.docCommission || 0),
      }
    });
    this.patientDirectService.exportCSV(exportAccuralsData, 'invoices');
  }

  getAbsoluteValue(value: number) {
    return Math.abs(value);
  }
}
