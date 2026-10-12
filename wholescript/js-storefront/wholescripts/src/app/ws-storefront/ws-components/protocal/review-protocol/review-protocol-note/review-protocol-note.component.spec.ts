import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReviewProtocolNoteComponent } from './review-protocol-note.component';

describe('ReviewProtocolNoteComponent', () => {
  let component: ReviewProtocolNoteComponent;
  let fixture: ComponentFixture<ReviewProtocolNoteComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ReviewProtocolNoteComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ReviewProtocolNoteComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
