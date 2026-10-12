import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WsAsAddressesDeleteAddressComponent } from './ws-as-addresses-delete-address.component';

describe('WsAsAddressesDeleteAddressComponent', () => {
  let component: WsAsAddressesDeleteAddressComponent;
  let fixture: ComponentFixture<WsAsAddressesDeleteAddressComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [WsAsAddressesDeleteAddressComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(WsAsAddressesDeleteAddressComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
