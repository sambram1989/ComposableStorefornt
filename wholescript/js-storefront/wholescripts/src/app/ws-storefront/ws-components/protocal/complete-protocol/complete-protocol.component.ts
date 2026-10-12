import { Component, inject, signal } from '@angular/core';
import { ProtocolService } from '../../../ws-services/protocol/protocol.service';
import { ActivatedRoute } from '@angular/router';
import { Observable, tap } from 'rxjs';
import { ActiveCartFacade } from '@spartacus/cart/base/root';

@Component({
  selector: 'ws-complete-protocol',
  templateUrl: './complete-protocol.component.html',
  styleUrl: './complete-protocol.component.scss'
})
export class CompleteProtocolComponent {
  private protocolService = inject(ProtocolService);
  private cartService = inject(ActiveCartFacade);

  protocolId= signal<string>('');
  selectedProduct$: Observable<any>;
  notes: string;
  totalPrice = signal<string>('');

  productsData:any;

  constructor(private activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.protocolId.set(this.activatedRoute.snapshot.paramMap.get('id'));
    if (this.protocolId()) {
      this.LoadSelectedProduct();
    }
  }

  LoadSelectedProduct() { 
    this.selectedProduct$ = this.protocolService.fetchReviewProtocol(
      this.protocolId()
    ).pipe(tap((item:any)=>{
      const totalSum = item.protocolEntries.reduce((accumulator: any, entry: any) => {
        return accumulator + entry.basePrice.value;
      }, 0);
      this.totalPrice.set(totalSum);
    }));
  }

  addToCart(){
   this.cartService.addEntries(this.productsData);
  }
}
