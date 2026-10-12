import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CustomMedpaxTableComponent } from './custom-medpax-table.component';

describe('CustomMedpaxTableComponent', () => {
  let component: CustomMedpaxTableComponent;
  let fixture: ComponentFixture<CustomMedpaxTableComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [CustomMedpaxTableComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(CustomMedpaxTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
