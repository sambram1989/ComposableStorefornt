import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WsCheckoutDeliveryAddressPaginationComponent } from './ws-checkout-delivery-address-pagination.component';

describe('WsCheckoutDeliveryAddressPaginationComponent', () => {
  let component: WsCheckoutDeliveryAddressPaginationComponent;
  let fixture: ComponentFixture<WsCheckoutDeliveryAddressPaginationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [WsCheckoutDeliveryAddressPaginationComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(WsCheckoutDeliveryAddressPaginationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
