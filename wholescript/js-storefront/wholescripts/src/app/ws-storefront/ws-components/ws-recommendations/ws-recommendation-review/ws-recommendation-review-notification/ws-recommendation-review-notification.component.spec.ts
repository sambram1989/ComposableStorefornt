import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WsRecommendationReviewNotificationComponent } from './ws-recommendation-review-notification.component';

describe('WsRecommendationReviewNotificationComponent', () => {
  let component: WsRecommendationReviewNotificationComponent;
  let fixture: ComponentFixture<WsRecommendationReviewNotificationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [WsRecommendationReviewNotificationComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(WsRecommendationReviewNotificationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
