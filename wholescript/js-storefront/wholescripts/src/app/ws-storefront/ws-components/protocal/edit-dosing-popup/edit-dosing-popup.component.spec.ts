import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EditDosingPopupComponent } from './edit-dosing-popup.component';

describe('EditDosingPopupComponent', () => {
  let component: EditDosingPopupComponent;
  let fixture: ComponentFixture<EditDosingPopupComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [EditDosingPopupComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(EditDosingPopupComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
