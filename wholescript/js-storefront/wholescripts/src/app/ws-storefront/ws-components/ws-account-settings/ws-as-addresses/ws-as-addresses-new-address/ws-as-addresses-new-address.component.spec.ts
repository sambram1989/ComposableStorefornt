import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WsAsAddressesNewAddressComponent } from './ws-as-addresses-new-address.component';

describe('WsAsAddressesNewAddressComponent', () => {
  let component: WsAsAddressesNewAddressComponent;
  let fixture: ComponentFixture<WsAsAddressesNewAddressComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [WsAsAddressesNewAddressComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(WsAsAddressesNewAddressComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
