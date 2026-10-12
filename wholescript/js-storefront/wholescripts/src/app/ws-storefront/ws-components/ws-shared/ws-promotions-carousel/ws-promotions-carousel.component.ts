import { AfterViewInit, ChangeDetectorRef, Component, DestroyRef, ElementRef, HostListener, inject, Input, OnInit, Renderer2, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { WsPromotionsService } from '../../../ws-services/promotions.service';
import { OccConfig } from '@spartacus/core';
import { WsOverlay } from '../../../ws-services/ws-overlay.service';

@Component({
  selector: 'ws-promotions-carousel',
  templateUrl: './ws-promotions-carousel.component.html',
  styleUrl: './ws-promotions-carousel.component.scss'
})
export class WsPromotionsCarouselComponent implements AfterViewInit {
  private overlayService = inject(WsOverlay);
  @Input() promotions: any[] = [];
  alert: any = {};
  showPromoDetailsModal: boolean = false;
  showTooltip: boolean = false;
  selectedPromotion: any = {};
  prevFocusElm: HTMLElement | null = null;
  @ViewChild('modalContent') modalContent!: ElementRef;
  carouselWidth: number = 0;
  itemsPerSlide = 1;
  slickConfig = {
    slidesToShow: this.itemsPerSlide,
    slidesToScroll: this.itemsPerSlide,
      dots: true,
      infinite: true,
      arrows: true,
      autoplay: false,
      speed: 500,
    responsive: [
      {
        breakpoint: 1280,
        settings: {
          slidesToShow: 1,
          slidesToScroll: 1,
          infinite: true,
          dots: true
        }
      },
      {
        breakpoint: 600,
        settings: {
          slidesToShow: 1,
          slidesToScroll: 1
        }
      }
    ]
  };

  constructor(
    private router: Router,
    private cd: ChangeDetectorRef,
    protected renderer: Renderer2,
    private promotionService: WsPromotionsService,
    protected occ: OccConfig
  ) {}

  ngAfterViewInit(): void {
      if (this.modalContent) {
        this.setFocus(this.modalContent);
      }
      const element = document.getElementById('productListId');
      if(element) {
        this.carouselWidth = element.getBoundingClientRect().width - 80;
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


  @HostListener('document:keydown', ['$event'])
    handleEscapeKey(event: KeyboardEvent) {
      if (event.key === 'Escape' && this.showPromoDetailsModal) {
        this.closeModal(); 
      }
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
  
    navigateToCatalog(redirectUrl: string) {
      this.router.navigateByUrl(redirectUrl?.replace('/?query', '?query') || '/catalog');
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

  getProductImageUrl(promotion: any): string | undefined {
    const baseUrl = this.occ?.backend?.occ?.baseUrl ? this.occ?.backend?.occ?.baseUrl : '';
    return promotion ? `${baseUrl}${promotion?.promotionBanner?.url}` : undefined;
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

  getCarouselWidth() {
    return `max-width: ${this.carouselWidth}px !important; width: 100%`;
  }
}
