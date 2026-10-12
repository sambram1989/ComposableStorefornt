import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CustomMedpaxChipComponent } from './custom-medpax-chip.component';

describe('CustomMedpaxChipComponent', () => {
  let component: CustomMedpaxChipComponent;
  let fixture: ComponentFixture<CustomMedpaxChipComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [CustomMedpaxChipComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(CustomMedpaxChipComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
