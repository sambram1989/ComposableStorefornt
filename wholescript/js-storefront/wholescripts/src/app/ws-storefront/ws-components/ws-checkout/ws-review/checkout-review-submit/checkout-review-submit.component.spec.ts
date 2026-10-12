import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CheckoutReviewSubmitComponent } from './checkout-review-submit.component';

describe('CheckoutReviewSubmitComponent', () => {
  let component: CheckoutReviewSubmitComponent;
  let fixture: ComponentFixture<CheckoutReviewSubmitComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [CheckoutReviewSubmitComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(CheckoutReviewSubmitComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
