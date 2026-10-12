import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WsCheckoutDeliveryNewAddressComponent } from './ws-checkout-delivery-new-address.component';

describe('WsCheckoutDeliveryNewAddressComponent', () => {
  let component: WsCheckoutDeliveryNewAddressComponent;
  let fixture: ComponentFixture<WsCheckoutDeliveryNewAddressComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [WsCheckoutDeliveryNewAddressComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(WsCheckoutDeliveryNewAddressComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
