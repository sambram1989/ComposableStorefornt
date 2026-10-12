import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MedpaxProtocolReviewCardComponent } from './medpax-protocol-review-card.component';

describe('MedpaxProtocolReviewCardComponent', () => {
  let component: MedpaxProtocolReviewCardComponent;
  let fixture: ComponentFixture<MedpaxProtocolReviewCardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [MedpaxProtocolReviewCardComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(MedpaxProtocolReviewCardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
