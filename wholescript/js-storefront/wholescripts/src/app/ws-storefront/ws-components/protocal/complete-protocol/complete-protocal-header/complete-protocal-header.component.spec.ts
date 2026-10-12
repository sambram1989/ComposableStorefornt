import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CompleteProtocalHeaderComponent } from './complete-protocal-header.component';

describe('CompleteProtocalHeaderComponent', () => {
  let component: CompleteProtocalHeaderComponent;
  let fixture: ComponentFixture<CompleteProtocalHeaderComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [CompleteProtocalHeaderComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(CompleteProtocalHeaderComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
