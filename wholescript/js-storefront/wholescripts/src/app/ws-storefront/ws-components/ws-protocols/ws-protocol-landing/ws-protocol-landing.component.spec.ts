import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WsProtocolLandingComponent } from './ws-protocol-landing.component';

describe('WsProtocolLandingComponent', () => {
  let component: WsProtocolLandingComponent;
  let fixture: ComponentFixture<WsProtocolLandingComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [WsProtocolLandingComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(WsProtocolLandingComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
