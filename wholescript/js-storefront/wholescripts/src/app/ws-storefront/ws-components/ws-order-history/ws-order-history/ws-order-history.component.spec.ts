import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WsOrderHistoryComponent } from './ws-order-history.component';

describe('WsOrderHistoryComponent', () => {
  let component: WsOrderHistoryComponent;
  let fixture: ComponentFixture<WsOrderHistoryComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [WsOrderHistoryComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(WsOrderHistoryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
