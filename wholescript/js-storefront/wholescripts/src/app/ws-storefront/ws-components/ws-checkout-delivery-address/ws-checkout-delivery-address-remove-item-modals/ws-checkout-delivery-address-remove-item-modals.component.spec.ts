import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WsCheckoutDeliveryAddressRemoveItemModalsComponent } from './ws-checkout-delivery-address-remove-item-modals.component';

describe('WsCheckoutDeliveryAddressRemoveItemModalsComponent', () => {
  let component: WsCheckoutDeliveryAddressRemoveItemModalsComponent;
  let fixture: ComponentFixture<WsCheckoutDeliveryAddressRemoveItemModalsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [WsCheckoutDeliveryAddressRemoveItemModalsComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(WsCheckoutDeliveryAddressRemoveItemModalsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
