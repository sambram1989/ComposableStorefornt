import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HomePageTestimonialsComponent } from './homepage-testimonials.component';

describe('HomePageTestimonialsComponent', () => {
  let component: HomePageTestimonialsComponent;
  let fixture: ComponentFixture<HomePageTestimonialsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [HomePageTestimonialsComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(HomePageTestimonialsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
