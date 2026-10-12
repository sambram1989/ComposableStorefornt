import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PatientMyAccountProtocolComponent } from './patient-my-account-protocol.component';

describe('PatientMyAccountProtocolComponent', () => {
  let component: PatientMyAccountProtocolComponent;
  let fixture: ComponentFixture<PatientMyAccountProtocolComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [PatientMyAccountProtocolComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(PatientMyAccountProtocolComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
