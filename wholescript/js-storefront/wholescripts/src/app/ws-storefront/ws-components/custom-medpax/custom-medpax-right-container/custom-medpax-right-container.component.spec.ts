import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CustomMedpaxRightContainerComponent } from './custom-medpax-right-container.component';

describe('CustomMedpaxRightContainerComponent', () => {
  let component: CustomMedpaxRightContainerComponent;
  let fixture: ComponentFixture<CustomMedpaxRightContainerComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [CustomMedpaxRightContainerComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(CustomMedpaxRightContainerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
