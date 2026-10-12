import { Component, inject } from '@angular/core';
import { Router } from '@angular/router';
import { RecommendationService } from '../../../../ws-services/ws-recommendation.service';
import { map, Observable, debounceTime } from 'rxjs';
import { WsPatientLoginService } from '../../../../ws-services/ws-patientlogin.service';
import { BsModalService, BsModalRef, ModalOptions } from 'ngx-bootstrap/modal';
import { WsPopupRecommendationComponent } from '../../../ws-recommendations/ws-recommendation-popup/recommendation-popup.component';
import { AuthService } from '@spartacus/core';

@Component({
  selector: 'ws-shop-latest-recommendation',
  templateUrl: './shop-latest-recommendation.component.html',
  styleUrl: './shop-latest-recommendation.component.scss'
})
export class ShopLatestRecommendationComponent {
  private router = inject(Router);
  private recommendationService = inject(RecommendationService);
  private patientService= inject(WsPatientLoginService);
  private dialog = inject(BsModalService);
  newRecommedation$: Observable<any>;
  recommendations: any[] = [];
  bsModalRef?: BsModalRef;
  isLoggedIn$: Observable<any>;
  fetchedUserRecommendations: boolean = false;

  recommendationCouunt$ = this.recommendationService.fetchWholesaleRecommendations().pipe(map(data => data.recommendations.length))

  constructor(
   protected authService: AuthService,
  ) {
    this.isLoggedIn$ = authService.isUserLoggedIn();
    this.getLoggedinUserNewRecommendation();
  }

  goToRecommendation() {
    this.router.navigate(['/my-recommendations']);
  }

  getLoggedinUserNewRecommendation() {
    this.isLoggedIn$.pipe(debounceTime(300)).subscribe((response) => {
      if(response) {
        this.patientService.fetchPatientNewRecommendation().subscribe((res) => {
          if(res && res.recommendationEntries && !this.fetchedUserRecommendations) {
            const initialState: ModalOptions = {
              initialState: {
                entries: res.recommendationEntries,
                subTotal: res.msrpSubTotalPrice,
                notes: res.notes,
                id: res.id
              },
              class: 'recommendation-modal'
            };
            this.fetchedUserRecommendations = true;
            this.bsModalRef = this.dialog.show(WsPopupRecommendationComponent, initialState);
          }
        })
      }
    })
  }
}
