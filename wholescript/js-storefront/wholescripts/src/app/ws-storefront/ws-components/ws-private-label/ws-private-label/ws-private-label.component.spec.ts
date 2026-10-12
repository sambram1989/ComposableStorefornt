import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WsPrivateLabelComponent } from './ws-private-label.component';

describe('WsPrivateLabelComponent', () => {
  let component: WsPrivateLabelComponent;
  let fixture: ComponentFixture<WsPrivateLabelComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [WsPrivateLabelComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(WsPrivateLabelComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
