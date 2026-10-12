import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CheckoutrevieworderSummaryComponent } from './checkoutrevieworder-summary.component';

describe('CheckoutrevieworderSummaryComponent', () => {
  let component: CheckoutrevieworderSummaryComponent;
  let fixture: ComponentFixture<CheckoutrevieworderSummaryComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [CheckoutrevieworderSummaryComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(CheckoutrevieworderSummaryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
