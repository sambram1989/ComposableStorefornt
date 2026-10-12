import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WsAsNotificationsComponent } from './ws-as-notifications.component';

describe('WsAsNotificationsComponent', () => {
  let component: WsAsNotificationsComponent;
  let fixture: ComponentFixture<WsAsNotificationsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [WsAsNotificationsComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(WsAsNotificationsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
