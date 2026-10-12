import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WsCheckoutDeliveryAddressAgeRestrictionComponent } from './ws-checkout-delivery-address-age-restriction.component';

describe('WsCheckoutDeliveryAddressAgeRestrictionComponent', () => {
  let component: WsCheckoutDeliveryAddressAgeRestrictionComponent;
  let fixture: ComponentFixture<WsCheckoutDeliveryAddressAgeRestrictionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [WsCheckoutDeliveryAddressAgeRestrictionComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(WsCheckoutDeliveryAddressAgeRestrictionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
