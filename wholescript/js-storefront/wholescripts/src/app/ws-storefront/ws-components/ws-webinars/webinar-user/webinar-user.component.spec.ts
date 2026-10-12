import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WebinarUserComponent } from './webinar-user.component';

describe('WebinarUserComponent', () => {
  let component: WebinarUserComponent;
  let fixture: ComponentFixture<WebinarUserComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [WebinarUserComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(WebinarUserComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
