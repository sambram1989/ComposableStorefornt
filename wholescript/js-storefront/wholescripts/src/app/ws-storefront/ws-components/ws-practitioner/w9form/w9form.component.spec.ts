import { ComponentFixture, TestBed } from '@angular/core/testing';

import { W9formComponent } from './w9form.component';

describe('W9formComponent', () => {
  let component: W9formComponent;
  let fixture: ComponentFixture<W9formComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [W9formComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(W9formComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
