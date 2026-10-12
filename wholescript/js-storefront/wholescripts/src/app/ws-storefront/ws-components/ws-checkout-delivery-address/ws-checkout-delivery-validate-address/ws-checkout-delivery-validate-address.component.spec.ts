import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WsCheckoutDeliveryValidateAddressComponent } from './ws-checkout-delivery-validate-address.component';

describe('WsCheckoutDeliveryValidateAddressComponent', () => {
  let component: WsCheckoutDeliveryValidateAddressComponent;
  let fixture: ComponentFixture<WsCheckoutDeliveryValidateAddressComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [WsCheckoutDeliveryValidateAddressComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(WsCheckoutDeliveryValidateAddressComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
