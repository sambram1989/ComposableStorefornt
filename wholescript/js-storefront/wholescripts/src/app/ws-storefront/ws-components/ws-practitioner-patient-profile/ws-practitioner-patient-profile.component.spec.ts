import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WsPractitionerPatientProfileComponent } from './ws-practitioner-patient-profile.component';

describe('WsPractitionerPatientProfileComponent', () => {
  let component: WsPractitionerPatientProfileComponent;
  let fixture: ComponentFixture<WsPractitionerPatientProfileComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [WsPractitionerPatientProfileComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(WsPractitionerPatientProfileComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
