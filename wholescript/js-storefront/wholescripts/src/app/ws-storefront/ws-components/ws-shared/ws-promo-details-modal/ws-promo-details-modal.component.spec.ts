import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WsPromoDetailsModalComponent } from './ws-promo-details-modal.component';

describe('WsPromoDetailsModalComponent', () => {
  let component: WsPromoDetailsModalComponent;
  let fixture: ComponentFixture<WsPromoDetailsModalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [WsPromoDetailsModalComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(WsPromoDetailsModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
