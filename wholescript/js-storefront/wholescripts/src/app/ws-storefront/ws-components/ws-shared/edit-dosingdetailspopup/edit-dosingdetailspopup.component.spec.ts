import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EditDosingdetailspopupComponent } from './edit-dosingdetailspopup.component';

describe('EditDosingdetailspopupComponent', () => {
  let component: EditDosingdetailspopupComponent;
  let fixture: ComponentFixture<EditDosingdetailspopupComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [EditDosingdetailspopupComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(EditDosingdetailspopupComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
