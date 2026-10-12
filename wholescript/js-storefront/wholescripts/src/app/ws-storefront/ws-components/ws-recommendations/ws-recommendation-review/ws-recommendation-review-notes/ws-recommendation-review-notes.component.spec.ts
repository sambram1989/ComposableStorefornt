import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WsRecommendationReviewNotesComponent } from './ws-recommendation-review-notes.component';

describe('WsRecommendationReviewNotesComponent', () => {
  let component: WsRecommendationReviewNotesComponent;
  let fixture: ComponentFixture<WsRecommendationReviewNotesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [WsRecommendationReviewNotesComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(WsRecommendationReviewNotesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
