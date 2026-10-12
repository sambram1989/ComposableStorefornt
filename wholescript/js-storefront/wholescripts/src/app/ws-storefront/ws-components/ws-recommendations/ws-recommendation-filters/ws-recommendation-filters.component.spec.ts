import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WsRecommendationFiltersComponent } from './ws-recommendation-filters.component';

describe('WsRecommendationFiltersComponent', () => {
  let component: WsRecommendationFiltersComponent;
  let fixture: ComponentFixture<WsRecommendationFiltersComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [WsRecommendationFiltersComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(WsRecommendationFiltersComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
