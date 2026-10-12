import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WsCheckoutDeliveryAddressComponent } from './ws-checkout-delivery-address.component';

describe('WsCheckoutDeliveryAddressComponent', () => {
  let component: WsCheckoutDeliveryAddressComponent;
  let fixture: ComponentFixture<WsCheckoutDeliveryAddressComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [WsCheckoutDeliveryAddressComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(WsCheckoutDeliveryAddressComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
