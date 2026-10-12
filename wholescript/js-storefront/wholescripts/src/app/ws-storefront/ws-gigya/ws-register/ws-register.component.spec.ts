import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WsRegisterComponent } from './ws-register.component';

describe('WsRegisterComponent', () => {
  let component: WsRegisterComponent;
  let fixture: ComponentFixture<WsRegisterComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [WsRegisterComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(WsRegisterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
