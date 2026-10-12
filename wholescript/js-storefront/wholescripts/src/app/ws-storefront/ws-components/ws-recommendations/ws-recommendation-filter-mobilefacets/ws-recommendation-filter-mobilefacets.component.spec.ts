import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WsRecommendationFilterMobilefacetsComponent } from './ws-recommendation-filter-mobilefacets.component';

describe('WsRecommendationFilterMobilefacetsComponent', () => {
  let component: WsRecommendationFilterMobilefacetsComponent;
  let fixture: ComponentFixture<WsRecommendationFilterMobilefacetsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [WsRecommendationFilterMobilefacetsComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(WsRecommendationFilterMobilefacetsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
