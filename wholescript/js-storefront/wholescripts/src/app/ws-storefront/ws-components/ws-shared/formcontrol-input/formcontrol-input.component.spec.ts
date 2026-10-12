import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FormcontrolInputComponent } from './formcontrol-input.component';

describe('FormcontrolInputComponent', () => {
  let component: FormcontrolInputComponent;
  let fixture: ComponentFixture<FormcontrolInputComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [FormcontrolInputComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(FormcontrolInputComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
