import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WsShopifyMobileFacetsComponent } from './ws-shopify-mobile-facets.component';

describe('WsShopifyMobileFacetsComponent', () => {
  let component: WsShopifyMobileFacetsComponent;
  let fixture: ComponentFixture<WsShopifyMobileFacetsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [WsShopifyMobileFacetsComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(WsShopifyMobileFacetsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
