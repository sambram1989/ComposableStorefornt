import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WsRecommendationReviewHeaderComponent } from './ws-recommendation-review-header.component';

describe('WsRecommendationReviewHeaderComponent', () => {
  let component: WsRecommendationReviewHeaderComponent;
  let fixture: ComponentFixture<WsRecommendationReviewHeaderComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [WsRecommendationReviewHeaderComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(WsRecommendationReviewHeaderComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
