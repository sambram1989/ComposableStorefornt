import { ComponentFixture, TestBed } from '@angular/core/testing';

import { W9LandingPageComponent } from './w9-landing-page.component';

describe('W9LandingPageComponent', () => {
  let component: W9LandingPageComponent;
  let fixture: ComponentFixture<W9LandingPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [W9LandingPageComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(W9LandingPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
