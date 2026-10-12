import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WsPromotionsCarouselComponent } from './ws-promotions-carousel.component';

describe('WsPromotionsCarouselComponent', () => {
  let component: WsPromotionsCarouselComponent;
  let fixture: ComponentFixture<WsPromotionsCarouselComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [WsPromotionsCarouselComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(WsPromotionsCarouselComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
