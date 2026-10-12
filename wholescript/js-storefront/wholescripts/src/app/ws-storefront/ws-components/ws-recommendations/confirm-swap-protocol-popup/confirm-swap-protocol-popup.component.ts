import { Component, inject, input, output } from '@angular/core';
import { WsOverlay } from '../../../ws-services/ws-overlay.service';

@Component({
  selector: 'ws-confirm-swap-protocol-popup',
  templateUrl: './confirm-swap-protocol-popup.component.html',
  styleUrl: './confirm-swap-protocol-popup.component.scss'
})
export class ConfirmSwapProtocolPopupComponent {
  private overlayService = inject(WsOverlay);
  protocolId =  input<string>('');
  swappedProtocolId =  input<string>('');
  SwapProtocol = output<string>();
  closePopup(){
    this.overlayService.closeTopModal();
  }
}
