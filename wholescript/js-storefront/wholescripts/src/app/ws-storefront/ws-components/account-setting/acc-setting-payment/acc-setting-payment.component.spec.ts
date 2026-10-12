import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AccSettingPaymentComponent } from './acc-setting-payment.component';

describe('AccSettingPaymentComponent', () => {
  let component: AccSettingPaymentComponent;
  let fixture: ComponentFixture<AccSettingPaymentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [AccSettingPaymentComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(AccSettingPaymentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
