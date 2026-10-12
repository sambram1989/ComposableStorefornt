import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WsGigyaRaasFlexCdcComponent } from './ws-gigya-raas-flex-cdc.component';

describe('WsGigyaRaasFlexCdcComponent', () => {
  let component: WsGigyaRaasFlexCdcComponent;
  let fixture: ComponentFixture<WsGigyaRaasFlexCdcComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [WsGigyaRaasFlexCdcComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(WsGigyaRaasFlexCdcComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
