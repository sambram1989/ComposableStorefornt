import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WsRecommendationConfirmHeaderComponent } from './ws-recommendation-confirm-header.component';

describe('WsRecommendationConfirmHeaderComponent', () => {
  let component: WsRecommendationConfirmHeaderComponent;
  let fixture: ComponentFixture<WsRecommendationConfirmHeaderComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [WsRecommendationConfirmHeaderComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(WsRecommendationConfirmHeaderComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
