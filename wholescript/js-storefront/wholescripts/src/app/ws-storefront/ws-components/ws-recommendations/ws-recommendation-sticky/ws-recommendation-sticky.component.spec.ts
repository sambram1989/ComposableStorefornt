import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WsRecommendationStickyComponent } from './ws-recommendation-sticky.component';

describe('WsRecommendationStickyComponent', () => {
  let component: WsRecommendationStickyComponent;
  let fixture: ComponentFixture<WsRecommendationStickyComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [WsRecommendationStickyComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(WsRecommendationStickyComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
