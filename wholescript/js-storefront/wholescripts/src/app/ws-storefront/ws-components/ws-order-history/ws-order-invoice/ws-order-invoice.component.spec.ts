import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WsOrderInvoiceComponent } from './ws-order-invoice.component';

describe('WsOrderInvoiceComponent', () => {
  let component: WsOrderInvoiceComponent;
  let fixture: ComponentFixture<WsOrderInvoiceComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [WsOrderInvoiceComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(WsOrderInvoiceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
