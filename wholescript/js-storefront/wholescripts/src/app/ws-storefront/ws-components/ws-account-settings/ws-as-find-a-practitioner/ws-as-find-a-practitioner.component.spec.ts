import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WsAsFindAPractitionerComponent } from './ws-as-find-a-practitioner.component';

describe('WsAsFindAPractitionerComponent', () => {
  let component: WsAsFindAPractitionerComponent;
  let fixture: ComponentFixture<WsAsFindAPractitionerComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [WsAsFindAPractitionerComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(WsAsFindAPractitionerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
