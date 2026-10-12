import { Component, Input, Output, EventEmitter, OnChanges, SimpleChanges, inject } from '@angular/core';
import { Router } from '@angular/router';
import { Location } from '@angular/common';
@Component({
  selector: 'ws-recommendation-sticky',
  templateUrl: './ws-recommendation-sticky.component.html',
  styleUrl: './ws-recommendation-sticky.component.scss',
})
export class WsRecommendationStickyComponent{
  @Output() stickyRecommendationClick = new EventEmitter<any>();
  @Output() openProductPopUP = new EventEmitter<any>();
  @Input() recommendation: any;
  @Input() recommendationTitle: string = '';
  @Input() backToRecommendation: boolean = false;
  @Input() recommendationId: string;
  @Input() isMedPaxReview:boolean = false;
  @Input() msrpSubTotalPrice: any;
  @Input() medPaxName: string = '';
  isMobile: boolean = false;
  public location = inject(Location);

  constructor(private router: Router) {
    const width = window.innerWidth;
    this.isMobile = width <= 991;
  }
  reviewRecommendation() {
    this.stickyRecommendationClick.emit('');
  }
  openProductPopUp() {
    this.openProductPopUP.emit('');
  }
  goBackToRecommendation() {
    this.router.navigate([
      '/Recommendation/BasicRecommendation', this.recommendationId
    ]);
  }
}
