import { ComponentFixture, TestBed } from '@angular/core/testing';

import { W9successPageComponent } from './w9success-page.component';

describe('W9successPageComponent', () => {
  let component: W9successPageComponent;
  let fixture: ComponentFixture<W9successPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [W9successPageComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(W9successPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
