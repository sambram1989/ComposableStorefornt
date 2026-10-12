import { ChangeDetectorRef, Component, DestroyRef, ElementRef, EventEmitter, inject, Output, signal, ViewChild } from '@angular/core';
import { WsOverlay } from '../../../ws-services/ws-overlay.service';
import { PatientProtocolListService } from '../service/patient-protocol-listing.service';
import { Observable, tap } from 'rxjs';
import { RecommendationService } from '../../../ws-services/ws-recommendation.service';
import { Router } from '@angular/router';
import { BreakpointObserver } from '@angular/cdk/layout';
import { ActiveCartFacade } from '@spartacus/cart/base/root';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';

@Component({
  selector: 'ws-patient-protocol',
  templateUrl: './patient-protocol.component.html',
  styleUrl:
    '../patient-my-account-protocol/patient-my-account-protocol.component.scss',
})
export class PatientProtocolComponent {
  private overlayService = inject(WsOverlay);
  private service = inject(PatientProtocolListService);
  private recommendationService = inject(RecommendationService);
  private activeCartService = inject(ActiveCartFacade);
  private router = inject(Router);
  private destroyRef = inject(DestroyRef);
  private cdr = inject(ChangeDetectorRef);
  
  @ViewChild('modalContent') modalContent!: ElementRef;
  @ViewChild('closeBtn') closeBtn!: ElementRef;
  @Output() emitAddToCartEvent = new EventEmitter<string>();
  prevFocusElm: HTMLElement | null = null;
  isMobile!: boolean;
  protocolName: string = '';
  toastMessage: string = '';

  protocolId = this.service.currentProtocolId;
  productsData: any;
  product$: Observable<any> = this.service
    .getProducts(this.service.currentProtocolId)
    .pipe(
      tap((item: any) => {
        this.getMedPaxForProtocol(item);
        this.productsData = item?.protocolEntries?.map((data: any) => {
          return {
            product: data?.product,
            quantity: data?.quantity,
          };
        });
        this.protocolName = item?.name 
      })
    );
  isMedPax = signal<boolean>(false);
  currentMedPax = signal<any>({});

  constructor(private bObserver: BreakpointObserver) {
    this.bObserver.observe('(max-width: 599px)').subscribe((result) => {
      this.isMobile = false;
      if (result.matches) {
        this.isMobile = true;
      }
    });
  }

  closePopup() {
    this.overlayService.closeTopModal();
    if (this.prevFocusElm) {
        this.prevFocusElm?.focus();
        this.prevFocusElm = null;
    }
  }

  ngAfterViewInit(): void {
    this.prevFocusElm = document.activeElement as HTMLElement;
    if (this.modalContent) {
      this.setFocus(this.modalContent);
      this.closeBtn.nativeElement.tabIndex = 0;
    }
    document.addEventListener('keydown', this.handleEscape);
  }
  
  handleEscape = (e: KeyboardEvent) => {
    if (e.key === 'Escape') {
      this.closePopup();
    }
  };
  
  ngOnDestroy() {
    document.removeEventListener('keydown', this.handleEscape);
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
          e.preventDefault();
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

    showToasterMsg(msg: string) {
      this.toastMessage = msg;
      this.cdr.detectChanges();
      this.resetToastMessage();
    }
  
    resetToastMessage() {
      setTimeout(() => {
        this.toastMessage = '';
        this.cdr.detectChanges();
      }, 4000)
    }

  addToCart() {
    this.service.addToCart(this.service.currentProtocolId).pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: () => {
          this.activeCartService.reloadActiveCart();
          this.emitAddToCartEvent.emit(this.protocolName); 
        }
      });
  }

  getMedPaxForProtocol(item: any) {
    this.recommendationService
      .getMedPaxForOwner(item.id, false)
      .subscribe((medpax: any) => {
        if (medpax.medpaxEntry.length) {
          this.currentMedPax.set(medpax);
          this.isMedPax.set(true);
        } else {
          this.isMedPax.set(false);
        }
      });
  }
}
