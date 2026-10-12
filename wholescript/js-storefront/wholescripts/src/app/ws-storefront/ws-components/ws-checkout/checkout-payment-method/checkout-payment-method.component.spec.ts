import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WsCheckoutPaymentMethodComponent } from './checkout-payment-method.component';

describe('WsCheckoutPaymentMethodComponent', () => {
  let component: WsCheckoutPaymentMethodComponent;
  let fixture: ComponentFixture<WsCheckoutPaymentMethodComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [WsCheckoutPaymentMethodComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(WsCheckoutPaymentMethodComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
