import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CartShippingAddressComponent } from './cart-shipping-address.component';

describe('CartShippingAddressComponent', () => {
  let component: CartShippingAddressComponent;
  let fixture: ComponentFixture<CartShippingAddressComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [CartShippingAddressComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(CartShippingAddressComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
