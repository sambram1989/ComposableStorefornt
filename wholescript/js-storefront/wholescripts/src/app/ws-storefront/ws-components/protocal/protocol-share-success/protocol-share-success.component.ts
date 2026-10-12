import { Component, inject } from '@angular/core';
import { ProtocolService } from '../../../ws-services/protocol/protocol.service';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

@Component({
  selector: 'ws-protocol-share-success',
  templateUrl: './protocol-share-success.component.html',
  styleUrl: './protocol-share-success.component.scss'
})
export class ProtocolShareSuccessComponent {
  public service = inject(ProtocolService);
  private activatedRoute = inject(ActivatedRoute);
  selectedProduct$: Observable<any>;
  protocolId = '';
  url: string;
  ngOnInit() {
    this.protocolId = this.activatedRoute.snapshot.paramMap.get('id');
    if (this.protocolId) {
      this.LoadSelectedProduct();
    }
    this.url = window.location.origin;
  } 

  LoadSelectedProduct() {
    this.selectedProduct$ = this.service.fetchReviewProtocol(this.protocolId);
  }
}
