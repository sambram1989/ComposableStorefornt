import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WsAutoshipsNextShipmentComponent } from './ws-autoships-next-shipment.component';

describe('WsAutoshipsNextShipmentComponent', () => {
  let component: WsAutoshipsNextShipmentComponent;
  let fixture: ComponentFixture<WsAutoshipsNextShipmentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [WsAutoshipsNextShipmentComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(WsAutoshipsNextShipmentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
