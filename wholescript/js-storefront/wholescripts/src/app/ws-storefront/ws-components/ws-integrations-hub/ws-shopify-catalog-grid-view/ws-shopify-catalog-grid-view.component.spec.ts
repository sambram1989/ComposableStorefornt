import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WsShopifyCatalogGridViewComponent } from './ws-shopify-catalog-grid-view.component';

describe('WsShopifyCatalogGridViewComponent', () => {
  let component: WsShopifyCatalogGridViewComponent;
  let fixture: ComponentFixture<WsShopifyCatalogGridViewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [WsShopifyCatalogGridViewComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(WsShopifyCatalogGridViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
