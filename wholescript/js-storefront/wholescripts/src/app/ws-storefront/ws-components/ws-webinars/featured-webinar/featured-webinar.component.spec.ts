import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FeaturedWebinarComponent } from './featured-webinar.component';

describe('FeaturedWebinarComponent', () => {
  let component: FeaturedWebinarComponent;
  let fixture: ComponentFixture<FeaturedWebinarComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [FeaturedWebinarComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(FeaturedWebinarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
