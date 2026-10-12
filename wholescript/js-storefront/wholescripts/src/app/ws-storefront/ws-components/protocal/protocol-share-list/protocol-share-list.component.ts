import { Component, computed, DestroyRef, ElementRef, inject, OnDestroy, OnInit, signal, TemplateRef, ViewChild, ViewContainerRef } from '@angular/core';
import { ApiCall, PractitionerMyPatientListingService } from '../../ws-practitioner/ws-My-patients/my-patient-landing/practitioner-my-patient-listing.service';
import { WsLeftFilterFacet } from '../../../ws-services/ws-left-filter-facet.service';
import { WsOverlay } from '../../../ws-services/ws-overlay.service';
import { RecommendationService } from '../../../ws-services/ws-recommendation.service';
import { ActivatedRoute, Router } from '@angular/router';
import { debounceTime, distinctUntilChanged, Observable, Subject } from 'rxjs';
import { TemplatePortal } from '@angular/cdk/portal';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { ProtocolService, ShareProtocol } from '../../../ws-services/protocol/protocol.service';
import { FormControl, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'ws-protocol-share-list',
  templateUrl: './protocol-share-list.component.html',
  styleUrl: './protocol-share-list.component.scss'
})
export class ProtocolShareListComponent {
  private service = inject(PractitionerMyPatientListingService);
  public protoService = inject(ProtocolService);
  private destroyRef = inject(DestroyRef);
  private filterService = inject(WsLeftFilterFacet);
  private overLayService = inject(WsOverlay);
  private _viewContainerRef = inject(ViewContainerRef);
  private recommendationService = inject(RecommendationService);
  private router = inject(Router);
  private activatedRoute = inject(ActivatedRoute);
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
  searchKeyPopup = '';
  sliderState = signal<boolean>(false);

  private searchSubject = new Subject<string>();
  isChecked: boolean = false;
  toDay: Date = new Date();
  PageLoaded = signal<boolean>(false);
  patientsData = signal<any[]>([]);
  activeFilters = signal<any[]>([]);
  filters$: Observable<any> = this.service.getFilters('status');
  protocolDetails$:Observable<any>;

  popupComputedSelectedSignal = computed(()=> this.protoService.selectedShareablePatient());

  checkBoxState = new FormGroup({
    checkBox: new FormControl(false, Validators.requiredTrue)
  });
  protocolId = '';
  viewCount = 0;

  @ViewChild('filterForPopup') filterForPopup: TemplateRef<unknown>;
  @ViewChild('viewPopUp') viewPopUp: TemplateRef<unknown>;
  @ViewChild('shareProtocolPopUp') shareProtocolPopUp: TemplateRef<unknown>;
  @ViewChild('shareProtocolPopUpData') shareProtocolPopUpModalData!: ElementRef;

  shareProtocolPopUpPortal: TemplatePortal<any>;
  viewPopUpPortal: TemplatePortal<any>;
  templatePortal: TemplatePortal<any>;
  prevFocusElm: HTMLElement | null = null;
  private isKeyboardNav = false;

  ngOnInit() {
    this.protocolId = this.activatedRoute.snapshot.paramMap.get('id');
    const state = this.activatedRoute.snapshot.paramMap.get('allpatient');
    const radioState = (state == 'true') ? true : false;
    this.sliderState.set(radioState);
    this.load();
    window.addEventListener('keydown', this.handleKeyDown, true);
    window.addEventListener('mousedown', this.handleMouseDown, true);
    window.addEventListener('touchstart', this.handleMouseDown, true);
  }

  private handleKeyDown = () => {
    this.isKeyboardNav = true;
  };

  private handleMouseDown = () => {
    this.isKeyboardNav = false;
  };

  scrollToViewWithOffset(element: HTMLElement) {
    if (!this.isKeyboardNav) return;
    const stickyHeight = 400; 
    const rect = element.getBoundingClientRect();
    const absoluteElementTop = rect.top + window.scrollY;
    const offsetPosition = absoluteElementTop - stickyHeight - 5;
    window.scrollTo({
      top: offsetPosition,
      behavior: 'smooth'
    });
  }

  ngAfterViewInit() {
    this.protocolDetails$ = this.protoService.fetchEditableProducts(this.protocolId);
    this.templatePortal = new TemplatePortal(this.filterForPopup, this._viewContainerRef);
    this.viewPopUpPortal = new TemplatePortal(this.viewPopUp, this._viewContainerRef);
    this.shareProtocolPopUpPortal = new TemplatePortal(this.shareProtocolPopUp, this._viewContainerRef);

    this.searchSubject
      .pipe(debounceTime(300), distinctUntilChanged())
      .subscribe((data: string) => {
        this.payLoad.searchText = data;
        this.load();
      });
      if (this.shareProtocolPopUpModalData) {
        this.setFocus(this.shareProtocolPopUpModalData);
      }
  }
  
    setFocus(modalElement: ElementRef) {
      if (modalElement?.nativeElement) {
        modalElement.nativeElement.setAttribute('tabindex', '-1');
        modalElement.nativeElement.focus();
        this.trapFocus(modalElement.nativeElement);
      }
    }

    trapFocus(modal: HTMLElement) {
      const focusables = modal.querySelectorAll<HTMLElement>(
        'a[href], button:not([disabled]), textarea:not([disabled]), ' +
        'input:not([disabled]), select:not([disabled]), [tabindex]:not([tabindex="-1"])'
      );
    
      if (!focusables.length) return;
    
      const first = focusables[0];
      const last = focusables[focusables.length - 1];
    
      const handleKeydown = (e: KeyboardEvent) => {
        if (e.key === 'Tab') {
          if (e.shiftKey && document.activeElement === first) {
            e.preventDefault();
            last.focus();
          } else if (!e.shiftKey && document.activeElement === last) {
            e.preventDefault();
            first.focus();
          }
        }
        if (e.key === 'Escape') {
          this.closePopup();
        }
      };
    
      const handleFocusIn = (e: FocusEvent) => {
        if (!modal.contains(e.target as Node)) {
          e.preventDefault();
          first.focus();
        }
      };
    
      modal.addEventListener('keydown', handleKeydown);
      document.addEventListener('focusin', handleFocusIn);
      first.focus();
    } 

  load() {
    this.service
      .getPatientList(this.payLoad)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe(this.updateDataInService);
  }

  updateSearch(key: string) {
    this.searchKey = key;
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
          filters[key] += filters[key]
            ? `,${item.selectedFacet.code}`
            : item.selectedFacet.code;
        }
      });
    });
    this.payLoad = {
      ...this.payLoad,
      referralCode: filters.referralCode,
      discount: filters.discount,
      status: filters.status,
    };
    this.load();
  }

  openProtocolModel() {
    this.overLayService.openModal(this.shareProtocolPopUpPortal);
    this.prevFocusElm = document.activeElement as HTMLElement;
    setTimeout(() => this.setFocus(this.shareProtocolPopUpModalData), 0);
  }

  openViewModel() {
    this.overLayService.openModal(this.viewPopUpPortal);
    this.prevFocusElm = document.activeElement as HTMLElement;
  }

  closePopup() {
    this.overLayService.closeTopModal();
    if (this.prevFocusElm) {
      setTimeout(() => {
        this.prevFocusElm?.focus();
        this.prevFocusElm = null;
      }, 0);
    }
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
    localStorage.setItem('patient', item?.name);
    // TODO: Need to remove hardcoded email
    this.recommendationService
      .createRecommendation({
        patient: item.email ? item.email : 'anil.gupta@rustic-hw.com',
      })
      .subscribe((data) => {
        this.router.navigate(['Recommendation/BasicRecommendation', data.id]);
      });
  }

  navigateToPatientProfile(item: any) {
    this.router.navigateByUrl('practitioner/patientprofile?uid=' + item.email);
  }

  pageChanged(event: any) {
    this.payLoad.currentPage = event;
    this.load();
  }

  updateDataInService = (data: any) => {
    this.PageLoaded.set(true);
    this.patientsData.set(data?.patients);
    const updated = this.patientsData().map(item => ({
      ...item,
      selected: false
    }));
    this.protoService.selectedShareablePatient.set(updated);
  }

  updateCheckBoxValue(value: boolean, index: number) {
    let updated = this.protoService.selectedShareablePatient();
    updated[index].selected = value;
    this.protoService.selectedShareablePatient.set(updated);
    let count = 0;
    this.protoService.selectedShareablePatient().map((e: any) => {
      if (e.selected) {
        ++count;
      }
    })
    this.viewCount = count;
  }

  clearAll() {
    let updated = this.protoService.selectedShareablePatient();
    updated.forEach((item: any) => item.selected = false);
    this.protoService.selectedShareablePatient.set(updated);
    this.viewCount = 0;
  }
  callPostApi() {
    let payLoad: ShareProtocol = {
      patients: this.protoService.selectedShareablePatient()
        .filter((item: any) => item.selected)
        .map((item: any) => item.email),
      sharedWithAll: this.sliderState(),
      termsAgreed: this.checkBoxState.controls.checkBox.value
    };
    this.protoService.shareProtocolSuccess(this.protocolId, payLoad)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe((res: any) => {
        this.protoService.shareSuccessApiResponse.set(res);
        this.closeFilterModal();
        this.router.navigate([`protocol/share/${this.protocolId}`]);
      });
  }
  ngOnDestroy() {
    this.filterService.clearFilter.set(false);
    window.removeEventListener('keydown', this.handleKeyDown, true);
    window.removeEventListener('mousedown', this.handleMouseDown, true);
    window.removeEventListener('touchstart', this.handleMouseDown, true);
  }
}
