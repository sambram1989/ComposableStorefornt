import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PaymentPageAddressValidationPopupComponent } from './payment-page-address-validation-popup.component';

describe('PaymentPageAddressValidationPopupComponent', () => {
  let component: PaymentPageAddressValidationPopupComponent;
  let fixture: ComponentFixture<PaymentPageAddressValidationPopupComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [PaymentPageAddressValidationPopupComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(PaymentPageAddressValidationPopupComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
