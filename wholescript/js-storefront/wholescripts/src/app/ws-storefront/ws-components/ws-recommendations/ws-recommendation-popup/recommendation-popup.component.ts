import { Component, DestroyRef, inject, Input, OnInit, signal } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { Router } from '@angular/router';
import { ActiveCartFacade, OrderEntry } from '@spartacus/cart/base/root';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { RecommendationService } from '../../../ws-services/ws-recommendation.service';
import { weekMap } from '../../ws-shared/edit-dosingdetailspopup/editDosingConstants';
import { switchMap } from 'rxjs';

@Component({
  selector: 'ws-recommendation-popup',
  templateUrl: './recommendation-popup.component.html',
  styleUrl: './recommendation-popup.component.scss',
})
export class WsPopupRecommendationComponent implements OnInit {
  @Input() entries: any[];
  @Input() subTotal: any;
  @Input() notes: any;
  @Input() id: any;
  medPaxProducts = signal<any>({});
  payload: string;
  cartItems: OrderEntry[] = [];
  isOutOfStock: boolean = false;
  weekMapItems = weekMap;
  private destroyRef = inject(DestroyRef);
  showAlert: boolean = false;

  constructor(public bsModalRef: BsModalRef, private router: Router, private cartService: ActiveCartFacade, 
              private recommendationService: RecommendationService
  ) {
  }
  ngOnInit() {
    let outOfStockItems = this.entries.filter((el) => el.product.stock.stockLevelStatus !== 'inStock');
    this.isOutOfStock = outOfStockItems.length > 0 ? true : false;
    this.getRecommendation();
    this.goToRecommendations();
  } 

  getRecommendation(){
    this.recommendationService.getMedPaxForOwner(this.id,this.id).subscribe(res=>{
      this.medPaxProducts.set(res);
    })
  }
  close() {
    this.bsModalRef.hide();
  }
  goToRecommendations() {
    this.recommendationService.patientViewed(this.id, this.payload).pipe(takeUntilDestroyed(this.destroyRef)).subscribe();
  }
  
  navigateToRecommendationsPage(){
    this.router.navigate(['Recommendation/MyRecommendations']);
        this.close();
  }
  onClosed(): void {
    this.showAlert = false;
  }
  addToCart() {
    this.cartService.requireLoadedCart()
      .pipe(
        switchMap(() => this.cartService.takeActiveCartId()),
        takeUntilDestroyed(this.destroyRef)
      )
      .subscribe((cartId: string) => {
        this.recommendationService.addRecommendationToCart(this.id, cartId)
          .subscribe(() => {
            this.cartService.reloadActiveCart();
            this.showAlert = true;
          });
      });
  }
}
