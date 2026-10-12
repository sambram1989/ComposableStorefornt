import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WsOrderTotalCalculatingErrorComponent } from './ws-order-total-calculating-error.component';

describe('WsOrderTotalCalculatingErrorComponent', () => {
  let component: WsOrderTotalCalculatingErrorComponent;
  let fixture: ComponentFixture<WsOrderTotalCalculatingErrorComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [WsOrderTotalCalculatingErrorComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(WsOrderTotalCalculatingErrorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
