import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WsZoomTriggerComponent } from './ws-zoom-trigger.component';

describe('WsZoomTriggerComponent', () => {
  let component: WsZoomTriggerComponent;
  let fixture: ComponentFixture<WsZoomTriggerComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [WsZoomTriggerComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(WsZoomTriggerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
