import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CustomMedpaxReviewComponent } from './custom-medpax-review.component';

describe('CustomMedpaxReviewComponent', () => {
  let component: CustomMedpaxReviewComponent;
  let fixture: ComponentFixture<CustomMedpaxReviewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [CustomMedpaxReviewComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(CustomMedpaxReviewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
