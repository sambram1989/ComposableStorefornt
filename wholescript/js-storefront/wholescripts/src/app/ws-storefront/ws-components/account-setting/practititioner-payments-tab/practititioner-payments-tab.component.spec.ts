import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PractititionerPaymentsTabComponent } from './practititioner-payments-tab.component';

describe('PractititionerPaymentsTabComponent', () => {
  let component: PractititionerPaymentsTabComponent;
  let fixture: ComponentFixture<PractititionerPaymentsTabComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [PractititionerPaymentsTabComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(PractititionerPaymentsTabComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
