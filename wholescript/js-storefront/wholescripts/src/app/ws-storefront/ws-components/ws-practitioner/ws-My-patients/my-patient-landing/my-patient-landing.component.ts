import {
  ChangeDetectorRef,
  Component,
  DestroyRef,
  inject,
  signal,
  TemplateRef,
  ViewChild,
  ViewContainerRef,
} from '@angular/core';
import { PractitionerMyPatientListingService } from './practitioner-my-patient-listing.service';
import { debounceTime, distinctUntilChanged, Observable, Subject } from 'rxjs';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { WsLeftFilterFacet } from '../../../../ws-services/ws-left-filter-facet.service';
import { WsOverlay } from '../../../../ws-services/ws-overlay.service';
import { TemplatePortal } from '@angular/cdk/portal';
import { ActivatedRoute, Router } from '@angular/router';
import { ApiCall } from './practitioner-my-patient-listing.service';
import { RecommendationService } from '../../../../ws-services/ws-recommendation.service';
import { ProtocolService } from '../../../../ws-services/protocol/protocol.service';
import { HttpParams } from '@angular/common/http';
@Component({
  selector: 'ws-my-patient-landing',
  templateUrl: './my-patient-landing.component.html',
  styleUrl: './my-patient-landing.component.scss',
})
export class MyPatientLandingComponent {
  private service = inject(PractitionerMyPatientListingService);
  private destroyRef = inject(DestroyRef);
  private filterService = inject(WsLeftFilterFacet);
  private overLayService = inject(WsOverlay);
  private _viewContainerRef = inject(ViewContainerRef);
  private recommendationService = inject(RecommendationService);
  private router = inject(Router);
  payLoad: ApiCall = {
    currentPage: 0,
    pageSize: 10,
    discount: '',
    referralCode: '',
    status: '',
    searchText: '',
    sortBy: '',
    practitionerId: '',
  };
  searchKey = '';
  private searchSubject = new Subject<string>();
  isChecked: boolean = false;
  fromWholeScriptsProtocolpage: boolean = false;
  toDay: Date = new Date();
  PageLoaded = signal<boolean>(false);
  patientsData = signal<any[]>([]);
  activeFilters = signal<any[]>([]);
  pagination = signal<any>({});
  isNewRecommendation: boolean = false;
  currentProtocolId: string = '';
  loader: boolean = false;

  filters$: Observable<any> = this.service.getFilters('status');

  @ViewChild('filterForPopup') filterForPopup: TemplateRef<unknown>;
  templatePortal: TemplatePortal<any>;

  constructor(private route: ActivatedRoute, public  protocolService: ProtocolService, private cd: ChangeDetectorRef) {
    this.route.queryParamMap.subscribe((params) => {
      this.currentProtocolId = params?.get('protocolId');
    });
  }

  ngOnInit() {
    this.load(); 
    this.fromWholeScriptsProtocolpage = localStorage.getItem('fromWholeScriptsProtocols') === 'true';
    if(this.fromWholeScriptsProtocolpage) {
      this.protocolService.isWsPage.set(true)
    }
    localStorage.removeItem('fromWholeScriptsProtocols');
  }

  ngAfterViewInit() {
    this.templatePortal = new TemplatePortal(
      this.filterForPopup,
      this._viewContainerRef
    );
    this.searchSubject
      .pipe(debounceTime(300), distinctUntilChanged())
      .subscribe((data: string) => {
        this.payLoad.searchText = data;
        this.load();
      });
    if(document.getElementsByTagName('title') && !document.getElementsByTagName('title')[0].innerText) document.getElementsByTagName('title')[0].innerText = 'Patients';
  }

  load() {
    this.loader = true;
    this.isNewRecommendation = window.location.href.includes('newrecommendation') ? true : false;
    this.service
      .getPatientList(this.payLoad)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe((data: any) => {
        this.loader = false;
        this.PageLoaded.set(true);
        this.patientsData.set(data?.patients);
        this.pagination.set(data?.pagination);
        this.cd.detectChanges();
      }, () => {
        this.loader = false;
        this.cd.markForCheck();
      });
  }

  updateSearch(key: string) {
    this.searchSubject.next(key);
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
      referralCode: filters.referralCode,
      discount: filters.discount,
      status: filters.status,
    };
    this.load();
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

  openFilterModal() {
    this.overLayService.openModal(this.templatePortal);
  }

  closeFilterModal() {
    this.overLayService.closeAllModals();
  }

  clearFilters() {
    this.activeFilters.set([]);
    this.filterService.clearFilter.set(true);
  }

  navigateToAddRecommendation(item: any) {
    if (this.currentProtocolId) {
      let params = new HttpParams();
      params = params.append('protocolId', this.currentProtocolId);
      params = params.append('patientId', item.email);
      this.protocolService.protocoltorecommendation(params).pipe(takeUntilDestroyed(this.destroyRef)).subscribe((data: any) => {
        this.router.navigate(['Recommendation/BasicRecommendation', data.Text]);
      })
    } else {
      this.recommendationService
        .createRecommendation({
          patient: item.email,
        })
        .subscribe((data) => {
          this.router.navigate(['Recommendation/BasicRecommendation', data.id]);
        });
    }
  }

  navigateToPatientProfile(item: any) {
    this.router.navigateByUrl('practitioner/patientprofile?uid=' + item.email);
  }

  pageChanged(event: any) {
    this.payLoad.currentPage = event -1;
    this.load();
  }

  ngOnDestroy() {
    this.filterService.clearFilter.set(false);
    this.protocolService.isWsPage.set(false);
  }
}
