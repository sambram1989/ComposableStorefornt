import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReviewProtocolHeaderComponent } from './review-protocol-header.component';

describe('ReviewProtocolHeaderComponent', () => {
  let component: ReviewProtocolHeaderComponent;
  let fixture: ComponentFixture<ReviewProtocolHeaderComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ReviewProtocolHeaderComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ReviewProtocolHeaderComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
