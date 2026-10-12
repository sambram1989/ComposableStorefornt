import { Component, inject, OnInit } from '@angular/core';
import { ProtocolService } from '../../../ws-services/protocol/protocol.service';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { ActiveCartFacade } from '@spartacus/cart/base/root';
@Component({
  selector: 'ws-protocol-details-page',
  templateUrl: './protocol-details-page.component.html',
  styleUrl: './protocol-details-page.component.scss'
})
export class ProtocolDetailsPageComponent implements OnInit {
  public protocolService = inject(ProtocolService);
  private cartService = inject(ActiveCartFacade);
  private activatedRoute = inject(ActivatedRoute);

  protocolId: string = '';
  selectedProduct$: Observable<any>;
  productsData: any;


  ngOnInit(): void {
    this.protocolId = this.activatedRoute.snapshot.paramMap.get('id');
    this.protocolService.isWsPage.set(this.protocolService.isComeFromWs());
    if (this.protocolId) {
      this.LoadSelectedProduct();
    }
  }

  LoadSelectedProduct() {
    this.selectedProduct$ = this.protocolService.fetchReviewProtocol(this.protocolId)
  }

  addToCart() {
    this.cartService.addEntries(this.productsData);
  }

  ngOnDestroy(){
    this.protocolService.isComeFromWs.set(false);
  }

}
