import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CustomMedPaxReviewCardComponent } from './custom-med-pax-review-card.component';

describe('CustomMedPaxReviewCardComponent', () => {
  let component: CustomMedPaxReviewCardComponent;
  let fixture: ComponentFixture<CustomMedPaxReviewCardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [CustomMedPaxReviewCardComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(CustomMedPaxReviewCardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
