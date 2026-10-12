import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WsRecommendationReviewComponent } from './ws-recommendation-review.component';

describe('WsRecommendationReviewComponent', () => {
  let component: WsRecommendationReviewComponent;
  let fixture: ComponentFixture<WsRecommendationReviewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [WsRecommendationReviewComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(WsRecommendationReviewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
