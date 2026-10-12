import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WsShopifySettingsComponent } from './ws-shopify-settings.component';

describe('WsShopifySettingsComponent', () => {
  let component: WsShopifySettingsComponent;
  let fixture: ComponentFixture<WsShopifySettingsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [WsShopifySettingsComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(WsShopifySettingsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
