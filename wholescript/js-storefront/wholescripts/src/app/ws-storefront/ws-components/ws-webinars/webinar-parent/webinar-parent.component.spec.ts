import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WebinarParentComponent } from './webinar-parent.component';

describe('WebinarParentComponent', () => {
  let component: WebinarParentComponent;
  let fixture: ComponentFixture<WebinarParentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [WebinarParentComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(WebinarParentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
