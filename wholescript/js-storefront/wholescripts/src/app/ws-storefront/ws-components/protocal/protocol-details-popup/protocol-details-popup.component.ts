import { Component, DestroyRef, inject, Input } from '@angular/core';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { ProtocolService } from '../../../ws-services/protocol/protocol.service';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
@Component({
  selector: 'ws-protocol-details-popup',
  templateUrl: './protocol-details-popup.component.html',
  styleUrl: './protocol-details-popup.component.scss'
})
export class ProtocolDetailsPopupComponent {
  protocolDetail: any;
  @Input() protocolId: string = ''
private destroyRef = inject(DestroyRef);
  constructor(public bsModalRef: BsModalRef, private protocolService: ProtocolService) { }

  ngOnInit() {
    if (this.protocolId) {
      this.protocolService.fetchProtocolDeatils(this.protocolId).subscribe(response => {
        if (response) {
          this.protocolDetail = (response)
        }
      })
    }
  }
  addToCart(){
    this.protocolService.addToCartProtocol(this.protocolId).pipe(takeUntilDestroyed(this.destroyRef)).subscribe();
  }
}
