import { Component, EventEmitter, inject, Output, Renderer2 } from '@angular/core';
import { WsOverlay } from '../../../ws-services/ws-overlay.service';

@Component({
  selector: 'ws-guest-promo-details-modal',
  templateUrl: './ws-guest-promo-details-modal.component.html',
  styleUrl: './ws-guest-promo-details-modal.component.scss'
})
export class WsGuestPromoDetailsModalComponent {
  @Output() closeGuestModal = new EventEmitter();
  private overlayService = inject(WsOverlay);

  constructor(protected renderer: Renderer2) {}

  closePopup() {
    this.closeGuestModal.emit();
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
