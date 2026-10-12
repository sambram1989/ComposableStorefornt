import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WsPatientFindPractitionerComponent } from './ws-patient-find-practitioner.component';

describe('WsPatientFindPractitionerComponent', () => {
  let component: WsPatientFindPractitionerComponent;
  let fixture: ComponentFixture<WsPatientFindPractitionerComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [WsPatientFindPractitionerComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(WsPatientFindPractitionerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
