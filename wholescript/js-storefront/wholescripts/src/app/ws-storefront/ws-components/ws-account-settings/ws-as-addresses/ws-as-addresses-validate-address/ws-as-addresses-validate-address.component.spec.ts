import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WsAsAddressesValidateAddressComponent } from './ws-as-addresses-validate-address.component';

describe('WsAsAddressesValidateAddressComponent', () => {
  let component: WsAsAddressesValidateAddressComponent;
  let fixture: ComponentFixture<WsAsAddressesValidateAddressComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [WsAsAddressesValidateAddressComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(WsAsAddressesValidateAddressComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
