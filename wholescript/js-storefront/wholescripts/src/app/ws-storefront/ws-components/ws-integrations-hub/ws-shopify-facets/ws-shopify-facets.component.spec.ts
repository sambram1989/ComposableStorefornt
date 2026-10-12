import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WsShopifyFacetsComponent } from './ws-shopify-facets.component';

describe('WsShopifyFacetsComponent', () => {
  let component: WsShopifyFacetsComponent;
  let fixture: ComponentFixture<WsShopifyFacetsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [WsShopifyFacetsComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(WsShopifyFacetsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
