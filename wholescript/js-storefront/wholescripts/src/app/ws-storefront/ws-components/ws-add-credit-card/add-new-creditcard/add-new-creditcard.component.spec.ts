import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddNewCreditcardComponent } from './add-new-creditcard.component';

describe('AddNewCreditcardComponent', () => {
  let component: AddNewCreditcardComponent;
  let fixture: ComponentFixture<AddNewCreditcardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [AddNewCreditcardComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(AddNewCreditcardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
