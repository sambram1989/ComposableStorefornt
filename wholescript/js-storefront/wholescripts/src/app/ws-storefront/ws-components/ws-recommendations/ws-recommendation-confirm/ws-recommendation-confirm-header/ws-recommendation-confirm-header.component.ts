import { Component, inject, Input } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'ws-recommendation-confirm-header',
  templateUrl: './ws-recommendation-confirm-header.component.html',
  styleUrl: './ws-recommendation-confirm-header.component.scss'
})
export class WsRecommendationConfirmHeaderComponent {

  @Input() recommendationId: string ='';
  private router = inject(Router);

  goToRecommendations() {
    this.router.navigate(['Recommendation/PractitionerRecommendations'])
  }

}
