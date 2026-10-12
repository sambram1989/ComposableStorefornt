import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MyPatientLandingComponent } from './my-patient-landing.component';

describe('MyPatientLandingComponent', () => {
  let component: MyPatientLandingComponent;
  let fixture: ComponentFixture<MyPatientLandingComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [MyPatientLandingComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(MyPatientLandingComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
