import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WebinarCartsComponent } from './webinar-carts.component';

describe('WebinarCartsComponent', () => {
  let component: WebinarCartsComponent;
  let fixture: ComponentFixture<WebinarCartsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [WebinarCartsComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(WebinarCartsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
