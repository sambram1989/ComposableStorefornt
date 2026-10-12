import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PatientProtocollandingComponent } from './patient-protocollanding.component';

describe('PatientProtocollandingComponent', () => {
  let component: PatientProtocollandingComponent;
  let fixture: ComponentFixture<PatientProtocollandingComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [PatientProtocollandingComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(PatientProtocollandingComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
