import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WsRecommendationConfirmComponent } from './ws-recommendation-confirm.component';

describe('WsRecommendationConfirmComponent', () => {
  let component: WsRecommendationConfirmComponent;
  let fixture: ComponentFixture<WsRecommendationConfirmComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [WsRecommendationConfirmComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(WsRecommendationConfirmComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
