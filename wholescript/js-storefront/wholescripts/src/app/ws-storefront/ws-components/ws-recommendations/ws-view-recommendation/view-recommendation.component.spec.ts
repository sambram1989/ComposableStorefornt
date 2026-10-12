import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ViewRecommendationComponent } from './view-recommendation.component';

describe('ViewRecommendationComponent', () => {
  let component: ViewRecommendationComponent;
  let fixture: ComponentFixture<ViewRecommendationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ViewRecommendationComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(ViewRecommendationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
