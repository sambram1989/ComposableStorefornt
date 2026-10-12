import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WebinarTitleIconComponent } from './webinar-title-icon.component';

describe('WebinarTitleIconComponent', () => {
  let component: WebinarTitleIconComponent;
  let fixture: ComponentFixture<WebinarTitleIconComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [WebinarTitleIconComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(WebinarTitleIconComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
