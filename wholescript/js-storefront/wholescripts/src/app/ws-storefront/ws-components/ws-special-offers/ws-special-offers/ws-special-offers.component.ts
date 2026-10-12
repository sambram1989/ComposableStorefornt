import { ChangeDetectorRef, Component, DestroyRef, ElementRef, HostListener, inject, Input, OnInit, Renderer2, signal, ViewChild } from '@angular/core';
import { NavigationEnd, Route, Router } from '@angular/router';
import { CmsService, OccConfig } from '@spartacus/core';
import { Observable } from 'rxjs';
import { WsOverlay } from '../../../ws-services/ws-overlay.service';
import { WsPromotionsService } from '../../../ws-services/promotions.service';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';

@Component({
  selector: 'ws-ws-special-offers',
  templateUrl: './ws-special-offers.component.html',
  styleUrl: './ws-special-offers.component.scss'
})
export class WsSpecialOffersComponent implements OnInit {
  private cmsService = inject(CmsService);
  private overlayService = inject(WsOverlay);
  private destroyRef = inject(DestroyRef);
  SpecialOffersHeaderParagraphComp$: Observable<any> = this.cmsService.getComponentData('SpecialOffersHeaderParagraphComp');
  @Input() itemsPerPage: number = 10;
  @ViewChild('modalContent') modalContent!: ElementRef;
  prevFocusElm: HTMLElement | null = null;
  
  paginationSignal = signal<any>({});
  pagination = {
    itemPerPage: this.itemsPerPage,
    totalItems: 0,
    currentPage: 1,
  };
  alert: any = {};
  showPromoDetailsModal: boolean = false;
  showTooltip: boolean = false;
  loader: boolean = false;
  promotions: any[]=[];
  promotionsImage: string = '';
  selectedPromotion: any = {};
  isPractitionerDashboard = false;

  constructor(
    private router: Router,
    private cd: ChangeDetectorRef, 
    protected renderer: Renderer2,
    private promotionService: WsPromotionsService,
    protected occ: OccConfig
  ) { }

  ngOnInit(): void {
    this.getPromotions();
    this.isPractitionerDashboard = window.location.pathname.includes('/practitioner/dashboard');
    this.router.events.subscribe((event) => {
      if (event instanceof NavigationEnd) {
        this.isPractitionerDashboard = window.location.pathname.includes('/practitioner/dashboard');
      }
    });
  }

  ngAfterViewInit(): void {
    if (this.modalContent) {
      this.setFocus(this.modalContent);
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
        this.closeModal();
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

  onKeyDown(event: KeyboardEvent, parameter: string, data: any) {
    if (event.key === 'Enter' || event.key === ' ') {
      event.preventDefault();
      if (parameter === 'copyPromoCode') {
        this.copyPromoCode(data);
      }  else if (parameter === 'seeDetails') {
        this.seeDetails(data);
      } 
    }
  }
  @HostListener('document:keydown', ['$event'])
  handleEscapeKey(event: KeyboardEvent) {
    if (event.key === 'Escape' && this.showPromoDetailsModal) {
      this.closeModal(); 
    }
  }

  getPromotions() {
    this.loader = true;
    this.promotionService.getPromotions()
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe((data: any) => {
        this.promotions = data?.promotions?.filter((promotion: any) => {
          if(this.isPractitionerDashboard) {
            return promotion?.showOnBanners;
          }
          return true;
        }) || [];
        this.pagination = {
          itemPerPage: this.itemsPerPage,
          totalItems: this.promotions?.length || 0,
          currentPage: 1,
        };
        this.paginationSignal.set(this.pagination);
        this.loader = false;
        this.cd.detectChanges();
      }, () => {
        this.loader = false;
        this.cd.detectChanges();
      })
  }

  getProductImageUrl(promotion: any): string | undefined {
    const baseUrl = this.occ?.backend?.occ?.baseUrl ? this.occ?.backend?.occ?.baseUrl : '';
    return promotion ? `${baseUrl}${promotion?.promotionBanner?.url}` : undefined;
  }

  copyPromoCode(text: any) {
    this.add();
    navigator.clipboard.writeText(text).then(() => {
      console.log('Text copied to clipboard successfully!');
    }).catch(err => {
    });
  }

  add(): void {
    this.alert = {
      type: 'success',
      msg: `Code copied to clipboard.`,
      timeout: 2000
    };
  }

  onClosed(): void {
    this.alert = {};
  }

  navigateToCatalog(redirectUrl: string, nonDiscountable: string) {
    let url = redirectUrl?.replace('/?query', '?query') || '/catalog';
    if(nonDiscountable) {
      url = url + '?' + nonDiscountable;
    }
    this.router.navigateByUrl(url);
  }

  pageChanged(event: any) {
    this.pagination.currentPage = event;
    this.pagination = {
      itemPerPage: this.itemsPerPage,
      totalItems: this.promotions?.length || 0,
      currentPage: event,
    };
    this.paginationSignal.set(this.pagination);
  }

  closePopup() {
    this.overlayService.closeAllModals();
    this.renderer.removeClass(document.body, 'modal-open');
    const backdrop = document.querySelector('.modal-backdrop');
    if (backdrop) {
      this.renderer.removeClass(backdrop, 'fade');
      this.renderer.removeClass(backdrop, 'in');
      this.renderer.removeClass(backdrop, 'modal-backdrop');
    }
  }

  closeModal() {
    this.showPromoDetailsModal = false;
    this.closePopup();
    this.cd.detectChanges();
    if (this.prevFocusElm) {
      setTimeout(() => {
        this.prevFocusElm?.focus();
        this.prevFocusElm = null;
      }, 0);
    }
  }

  seeDetails(promotion: any) {
    this.selectedPromotion = promotion;
    this.showPromoDetailsModal = true;
    this.prevFocusElm = document.activeElement as HTMLElement;
    setTimeout(() => this.setFocus(this.modalContent), 0);
  }

  promoCodeMouseEnter() {
    this.showTooltip = true;
  }

  promoCodeMouseLeave() {
    this.showTooltip = false;
  }

}
