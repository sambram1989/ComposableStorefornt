import {
  ChangeDetectorRef,
  Component,
  DestroyRef,
  inject,
  signal,
  Pipe, 
  PipeTransform,
  AfterViewInit
} from '@angular/core';
import { WsPage } from '../../../ws-services/ws-page.service';
import { Observable } from 'rxjs';
import { PRIMARY_SLOT, slotMarkup } from '../../../ws-models/page.model';
import { CmsBannerComponent, GlobalMessageService, GlobalMessageType } from '@spartacus/core';
import { ActivatedRoute, Router } from '@angular/router';
import {
  PractitionerMyPatientListingService,
} from '../../ws-practitioner/ws-My-patients/my-patient-landing/practitioner-my-patient-listing.service';
import { RecommendationService } from '../../../ws-services/ws-recommendation.service';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { WsCurrentUserService } from '../../../ws-services/state-services/currentuser.service';
import { BsModalRef, BsModalService, ModalOptions } from 'ngx-bootstrap/modal';
import { WsPlpFilterMobilefacetsComponent } from '../../ws-plp/ws-plp-filter-mobilefacets/ws-plp-filter-mobilefacets.component';
import { WsRecommendationFilterMobilefacetsComponent } from '../ws-recommendation-filter-mobilefacets/ws-recommendation-filter-mobilefacets.component';
import { PatientProfileService } from '../../../ws-services/ws-patient-profile.service';

@Component({
  selector: 'ws-recommendation-list',
  templateUrl: './recommendation-list.component.html',
  styleUrl: './recommendation-list.component.scss',
})
export class RecommendationListComponent implements AfterViewInit{
  private currentUser = inject(WsCurrentUserService);
  userRoles: string[] = [];
  displayedColumns: string[] = ['Name', 'Date Created', 'Status', 'Actions'];
  dataSource= signal<any>([]);
  SearchText: string = '';
  recommendationsSlot = new Observable<slotMarkup>();
  pageContent: CmsBannerComponent;
  filtersApi$: Observable<any> = this.wsPageService.getFilters();
  showPatientDetails: boolean = false;
  patientSelected: string = '';
  initialRecommendations: any;
  filters$ = this.filterService.getFilters('discount');
  activeFilters = signal<any[]>([]);
  startDate: any;
  endDate: any;
  payLoad = {
    currentPage: 0,
    pageSize: 10,
    discount: '',
    referral: '',
    status: '',
    searchText: '',
    sort: '',
    practitionerId: '',
    startDate: '',
    endDate: '',
  };
  minDate: any;
  bsModalRef?: BsModalRef;
  patientId: string = '';
  pagination = signal<any>({});

  constructor(
    public wsPageService: WsPage,
    private router: Router,
    private filterService: PractitionerMyPatientListingService,
    private recommendationService: RecommendationService,
    private destroyRef: DestroyRef,
    private route: ActivatedRoute,
    private cdRef: ChangeDetectorRef,
    protected globalMessageService: GlobalMessageService,
    private dialog: BsModalService,
    private patientProfileService: PatientProfileService
    
  ) {
    this.endDate = this.formatDateForInput(new Date());
    const date = new Date();
    this.minDate = this.setFirstDayOfMonth(date);
    this.startDate = this.formatDateForInput(this.minDate);
    this.recommendationsSlot = this.wsPageService.getPage(
      PRIMARY_SLOT,
      'topContent'
    );
    this.recommendationsSlot
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe((response) => {
        response.components.forEach((element: any, index: number) => {
          if (index === 0) this.pageContent = element;
        });
      });
      this.route.queryParams.subscribe((params: any) => {
        if (params?.uid) {
          this.patientId = params.uid;
        } else {
          this.patientId = '';
        }
        this.getPageInfo(this.payLoad);
      });
      this.getCurrentUserRoles();
  }

  ngAfterViewInit(): void {
    if(document.getElementsByTagName('title') && !document.getElementsByTagName('title')[0].innerText) document.getElementsByTagName('title')[0].innerText = 'Recommendation List';
  }
  openMobileFilters() {
    const initialState: ModalOptions = {
      class: 'ws-mobile-filters',
      initialState: {
        referralCode: '',
        filtersApi$: this.filtersApi$
      },
    };
    this.bsModalRef = this.dialog.show(
      WsRecommendationFilterMobilefacetsComponent,
      initialState
    );
  }

  getCurrentUserRoles() {
    this.currentUser.currentState().pipe(takeUntilDestroyed(this.destroyRef)).subscribe((data:any)=>{
      this.userRoles = data || [];
      this.cdRef.markForCheck();
    })
  }

  showSearchResults(event: any) {
    this.SearchText = event;
    if (this.SearchText?.trim()?.length > 0) {
      this.payLoad.searchText = this.SearchText;
      if(this.patientId) {
        this.patientProfileService.fetchPatientRecommendations({...this.payLoad, patientId: this.patientId})
              .pipe(takeUntilDestroyed(this.destroyRef))
              .subscribe(res => {
                this.dataSource.set(res.recommendations);
                this.cdRef.detectChanges();
              });
      } else {
        this.wsPageService
        .fetchWholesaleRecommendations(this.payLoad)
        .pipe(takeUntilDestroyed(this.destroyRef))
        .subscribe((res) => {
          this.dataSource.set(res.recommendations);
          this.pagination.set(res?.pagination);
          this.cdRef.detectChanges();
        });
      }
    } else {
      this.SearchText = '';
      this.payLoad.searchText = this.SearchText;
        this.wsPageService
          .fetchWholesaleRecommendations(this.payLoad)
          .pipe(takeUntilDestroyed(this.destroyRef))
          .subscribe((res) => {
            this.dataSource.set(res.recommendations);
            this.pagination.set(res?.pagination);
            this.cdRef.detectChanges();
          });
      this.dataSource.set(this.initialRecommendations);
      this.cdRef.detectChanges();
    }
  }

  getPaginationRange(
    currentPage: number,
    itemsPerPage: number,
    totalItems: number
  ) {
    const start = currentPage * itemsPerPage + 1;
    const end = Math.min((currentPage + 1) * itemsPerPage, totalItems);
    return { start, end };
  }

  formatDateForInput(date: Date): string {
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0'); 
    return `${year}-${month}-${day}`; 
  }
  
  deleteRecommendation(id: string) {
    if(this.userRoles?.indexOf("patientdirect") > -1) {
      this.recommendationService
        .deleteRecommendationId(id)
        .pipe(takeUntilDestroyed(this.destroyRef))
        .subscribe(() => this.getPageInfo(this.payLoad));
        this.globalMessageService.add(
          'Recommendation deleted',
          GlobalMessageType.MSG_TYPE_INFO,
          5000
        );
    } else {
      this.router.navigate(['/practitioner/patient-direct']);
    }
  }
  
  getPageInfo(request: any) {
    this.payLoad.startDate = this.startDate;
    this.payLoad.endDate = this.endDate;
    if(this.patientId) {
      this.patientProfileService.fetchPatientRecommendations({...request, patientId: this.patientId})
            .pipe(takeUntilDestroyed(this.destroyRef))
            .subscribe(res => {
              this.dataSource.set(res.recommendations);
              this.initialRecommendations = res.recommendations;
              this.cdRef.detectChanges();
            });
    } else {
      this.wsPageService
      .fetchWholesaleRecommendations(request)
      .subscribe((res) => {
        this.dataSource.set(res.recommendations);
        this.pagination.set(res?.pagination);
        this.initialRecommendations = res.recommendations;
        this.cdRef.detectChanges();
      });
    }
  }
  setFirstDayOfMonth(date: Date): Date {
    return new Date(date.getFullYear(), date.getMonth() - 3, 1);
  }
  goToPatientDetails(patient: string, id: string) {
    if(this.userRoles?.indexOf("patientdirect") > -1) {
      this.showPatientDetails = true;
      this.patientSelected = patient;
      localStorage.setItem('patient', this.patientSelected);
      this.router.navigate(['Recommendation/BasicRecommendation', id]);
    } else {
      this.router.navigate(['/practitioner/patient-direct']);
    }
  }
  navigateToUrl() {
    if(this.userRoles?.indexOf("patientdirect") > -1) {
      this.router.navigate(['SearchPatientAdvanced/newrecommendation']);
    } else {
      this.router.navigate(['/practitioner/patient-direct']);
    }
  }
  pageChanged(event: any) {
    this.payLoad.currentPage = event - 1;;
    this.getPageInfo(this.payLoad);
  }
  goToDetails(item: any) {
    if(this.userRoles?.indexOf("patientdirect") > -1) {
      this.router.navigate(['/Recommendation/RecommendationDetails/', item.id]);
    } else {
      this.router.navigate(['/practitioner/patient-direct']);
    }
  }
  onFacetSelection(value: any) {
    this.activeFilters.set(value);
    const filters: any = { referralCode: '', discount: '', status: '' };
    value.forEach((item: any) => {
      const lowerTitle = item.title.toLowerCase();
      ['referral', 'discount', 'status'].forEach((key) => {
        if (lowerTitle.includes(key)) {
          key = key == 'referral' ? 'referralCode' : key;
          if(key == 'referralCode') {
            filters[key] += filters[key]
            ? `,${item.selectedFacet.value}`
            : item.selectedFacet.value;
          } else{
            filters[key] += filters[key]
              ? `,${item.selectedFacet.code}`
              : item.selectedFacet.code;
          }
        }
      });
    });
    filters.referralCode = this.transformReferral(filters.referralCode);
    this.payLoad = {
      ...this.payLoad,
      referral: filters.referralCode,
      discount: filters.discount,
      status: filters.status,
      currentPage: 0,
    };
    this.getPageInfo(this.payLoad);
  }

   transformReferral(input: string): string {
    if (!input) return "";

    if (!input.includes(',')) {
      return input.replace(' | ', '%7C');
  }

  return input
      .split(',')
      .map(item => item.replace(' | ', '%7C'))
      .join(',');
}

  getStartDateResponse(event: Event): void {
    const input = event.target as HTMLInputElement;
    const selectedDate = input.value;
    this.payLoad.startDate = selectedDate;
    this.startDate = selectedDate;
    this.getPageInfo(this.payLoad);
  }

  navigateToPatientProfile(item: any) {
    this.router.navigateByUrl('practitioner/patientprofile?uid=' + item.patientUID);
  }
  
  getEndDateResponse(event: Event): void {
    const input = event.target as HTMLInputElement;
    const selectedDate = input.value;
    this.payLoad.endDate = selectedDate;
    this.endDate = selectedDate;
    this.getPageInfo(this.payLoad);
  }
}




@Pipe({
  name: 'capitalizeFormat'
})
export class CapitalizeFormatPipe implements PipeTransform {
  transform(value: string): string {
    if (!value) return '';

    return value
      .toLowerCase()
      .replace(/[_-]/g, ' ') // Replace _ and - with space
      .replace(/\b\w/g, char => char.toUpperCase()); // Capitalize each word
  }
}
