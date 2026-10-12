import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WsShopifyCatalogComponent } from './ws-shopify-catalog.component';

describe('WsShopifyCatalogComponent', () => {
  let component: WsShopifyCatalogComponent;
  let fixture: ComponentFixture<WsShopifyCatalogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [WsShopifyCatalogComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(WsShopifyCatalogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
