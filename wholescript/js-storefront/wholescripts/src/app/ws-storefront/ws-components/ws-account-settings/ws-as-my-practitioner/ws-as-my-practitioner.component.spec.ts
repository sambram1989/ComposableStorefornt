import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WsAsMyPractitionerComponent } from './ws-as-my-practitioner.component';

describe('WsAsMyPractitionerComponent', () => {
  let component: WsAsMyPractitionerComponent;
  let fixture: ComponentFixture<WsAsMyPractitionerComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [WsAsMyPractitionerComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(WsAsMyPractitionerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
