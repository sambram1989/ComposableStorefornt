import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WebinarCarouselUiComponent } from './webinar-carousel-ui.component';

describe('WebinarCarouselUiComponent', () => {
  let component: WebinarCarouselUiComponent;
  let fixture: ComponentFixture<WebinarCarouselUiComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [WebinarCarouselUiComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(WebinarCarouselUiComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
