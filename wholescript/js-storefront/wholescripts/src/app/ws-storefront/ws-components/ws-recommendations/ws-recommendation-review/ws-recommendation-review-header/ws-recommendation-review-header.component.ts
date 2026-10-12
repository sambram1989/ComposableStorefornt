import { Component, inject, Input } from '@angular/core';
import { CmsService } from '@spartacus/core';
import { Observable } from 'rxjs';

@Component({
  selector: 'ws-recommendation-review-header',
  templateUrl: './ws-recommendation-review-header.component.html',
  styleUrl: './ws-recommendation-review-header.component.scss'
})
export class WsRecommendationReviewHeaderComponent {
  private cmsService = inject(CmsService);
  @Input() patientName: string ='';
  reviewHeader$:Observable<any> = this.cmsService.getComponentData('RecommendationReviewHeaderBannerComponent');

}
