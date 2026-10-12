import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WsRecommendationReviewSidebarComponent } from './ws-recommendation-review-sidebar.component';

describe('WsRecommendationReviewSidebarComponent', () => {
  let component: WsRecommendationReviewSidebarComponent;
  let fixture: ComponentFixture<WsRecommendationReviewSidebarComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [WsRecommendationReviewSidebarComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(WsRecommendationReviewSidebarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
