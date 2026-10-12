import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReviewProtocolComponent } from './review-protocol.component';

describe('ReviewProtocolComponent', () => {
  let component: ReviewProtocolComponent;
  let fixture: ComponentFixture<ReviewProtocolComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ReviewProtocolComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ReviewProtocolComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
