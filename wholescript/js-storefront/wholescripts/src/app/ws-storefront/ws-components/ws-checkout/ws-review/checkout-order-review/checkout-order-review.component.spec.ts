import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CheckoutOrderReviewComponent } from './checkout-order-review.component';

describe('CheckoutOrderReviewComponent', () => {
  let component: CheckoutOrderReviewComponent;
  let fixture: ComponentFixture<CheckoutOrderReviewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [CheckoutOrderReviewComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(CheckoutOrderReviewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
