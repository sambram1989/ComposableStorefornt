import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WsRecommendationReviewForComponent } from './ws-recommendation-review-for.component';

describe('WsRecommendationReviewForComponent', () => {
  let component: WsRecommendationReviewForComponent;
  let fixture: ComponentFixture<WsRecommendationReviewForComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [WsRecommendationReviewForComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(WsRecommendationReviewForComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
