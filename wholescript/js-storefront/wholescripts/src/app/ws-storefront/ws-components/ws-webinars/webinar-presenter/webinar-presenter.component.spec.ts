import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WebinarPresenterComponent } from './webinar-presenter.component';

describe('WebinarPresenterComponent', () => {
  let component: WebinarPresenterComponent;
  let fixture: ComponentFixture<WebinarPresenterComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [WebinarPresenterComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(WebinarPresenterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
