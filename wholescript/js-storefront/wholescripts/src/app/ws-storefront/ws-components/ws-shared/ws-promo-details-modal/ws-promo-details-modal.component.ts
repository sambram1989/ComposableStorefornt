import { ChangeDetectorRef, Component, ElementRef, EventEmitter, inject, Input, Output, Renderer2, ViewChild } from '@angular/core';
import { WsOverlay } from '../../../ws-services/ws-overlay.service';

@Component({
  selector: 'ws-promo-details-modal',
  templateUrl: './ws-promo-details-modal.component.html',
  styleUrl: './ws-promo-details-modal.component.scss'
})
export class WsPromoDetailsModalComponent {
  @Input() selectedPromotion: any;
  @Output() closeModal = new EventEmitter();
  showTooltip: boolean = false;
  private overlayService = inject(WsOverlay);
  alert: any = {};
  @ViewChild('modalContainer', { static: true }) modalContainer!: ElementRef;
  private keydownListener?: () => void;
  private focusInListener?: () => void;
  

  constructor(private cd: ChangeDetectorRef, 
    protected renderer: Renderer2) {}

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

  ngAfterViewInit(): void {
    setTimeout(() => this.setupFocusTrap());
  }
  
  ngOnDestroy(): void {
    this.removeFocusTrap();
  }

  setupFocusTrap(): void {
    const modalEl = this.modalContainer.nativeElement as HTMLElement;
    const selectors = [
      'a[href]', 'button:not([disabled])', 'textarea:not([disabled])',
      'input:not([disabled])', 'select:not([disabled])',
      '[tabindex]:not([tabindex="-1"])'
    ];
    const focusables = modalEl.querySelectorAll<HTMLElement>(selectors.join(','));
    if (!focusables.length) return;
  
    const first = focusables[0];
    const last = focusables[focusables.length - 1];
  
    this.keydownListener = this.renderer.listen(modalEl, 'keydown', (e: KeyboardEvent) => {
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
    });
  
    this.focusInListener = this.renderer.listen('document', 'focusin', (e: FocusEvent) => {
      if (!modalEl.contains(e.target as Node)) {
        e.preventDefault();
        first.focus();
      }
    });
  
    first.focus();
  }

  removeFocusTrap(): void {
    if (this.keydownListener) this.keydownListener();
    if (this.focusInListener) this.focusInListener();
  }
  
  onClosed(): void {
    this.alert = {};
  }

  promoCodeMouseEnter() {
    this.showTooltip = true;
  }

  promoCodeMouseLeave() {
    this.showTooltip = false;
  }

  closePopup() {
    this.closeModal.emit();
    this.overlayService.closeAllModals();
    this.renderer.removeClass(document.body, 'modal-open');
    const backdrop = document.querySelector('.modal-backdrop');
    if (backdrop) {
      this.renderer.removeClass(backdrop, 'fade');
      this.renderer.removeClass(backdrop, 'in');
      this.renderer.removeClass(backdrop, 'modal-backdrop');
    }
  }
}
