import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PatientConsultationFeeComponent } from './patient-consultation-fee.component';

describe('PatientConsultationFeeComponent', () => {
  let component: PatientConsultationFeeComponent;
  let fixture: ComponentFixture<PatientConsultationFeeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [PatientConsultationFeeComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(PatientConsultationFeeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
