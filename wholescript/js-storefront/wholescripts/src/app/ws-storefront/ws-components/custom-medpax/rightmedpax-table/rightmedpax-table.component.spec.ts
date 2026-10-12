import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RightmedpaxTableComponent } from './rightmedpax-table.component';

describe('RightmedpaxTableComponent', () => {
  let component: RightmedpaxTableComponent;
  let fixture: ComponentFixture<RightmedpaxTableComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [RightmedpaxTableComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(RightmedpaxTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
