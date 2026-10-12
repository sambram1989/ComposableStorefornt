import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CustomMedpaxLandingPageComponent } from './custom-medpax-landing-page.component';

describe('CustomMedpaxLandingPageComponent', () => {
  let component: CustomMedpaxLandingPageComponent;
  let fixture: ComponentFixture<CustomMedpaxLandingPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [CustomMedpaxLandingPageComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(CustomMedpaxLandingPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
