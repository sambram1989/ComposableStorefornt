import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WsAsChangePasswordComponent } from './ws-as-change-password.component';

describe('WsAsChangePasswordComponent', () => {
  let component: WsAsChangePasswordComponent;
  let fixture: ComponentFixture<WsAsChangePasswordComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [WsAsChangePasswordComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(WsAsChangePasswordComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
