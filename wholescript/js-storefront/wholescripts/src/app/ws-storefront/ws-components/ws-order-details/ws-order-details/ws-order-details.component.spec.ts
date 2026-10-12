import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WsOrderDetailsComponent } from './ws-order-details.component';

describe('WsOrderDetailsComponent', () => {
  let component: WsOrderDetailsComponent;
  let fixture: ComponentFixture<WsOrderDetailsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [WsOrderDetailsComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(WsOrderDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
