import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WsAsAddressesComponent } from './ws-as-addresses.component';

describe('WsAsAddressesComponent', () => {
  let component: WsAsAddressesComponent;
  let fixture: ComponentFixture<WsAsAddressesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [WsAsAddressesComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(WsAsAddressesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
