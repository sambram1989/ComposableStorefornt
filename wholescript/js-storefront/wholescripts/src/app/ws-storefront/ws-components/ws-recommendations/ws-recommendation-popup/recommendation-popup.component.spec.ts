import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WsPopupRecommendationComponent } from './recommendation-popup.component';

describe('WsPopupRecommendationComponent', () => {
  let component: WsPopupRecommendationComponent;
  let fixture: ComponentFixture<WsPopupRecommendationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [WsPopupRecommendationComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(WsPopupRecommendationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
