import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WsGuestPromoDetailsModalComponent } from './ws-guest-promo-details-modal.component';

describe('WsGuestPromoDetailsModalComponent', () => {
  let component: WsGuestPromoDetailsModalComponent;
  let fixture: ComponentFixture<WsGuestPromoDetailsModalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [WsGuestPromoDetailsModalComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(WsGuestPromoDetailsModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
