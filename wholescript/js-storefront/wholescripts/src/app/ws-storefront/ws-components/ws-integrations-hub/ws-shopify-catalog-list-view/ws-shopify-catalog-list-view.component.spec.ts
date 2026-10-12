import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WsShopifyCatalogListViewComponent } from './ws-shopify-catalog-list-view.component';

describe('WsShopifyCatalogListViewComponent', () => {
  let component: WsShopifyCatalogListViewComponent;
  let fixture: ComponentFixture<WsShopifyCatalogListViewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [WsShopifyCatalogListViewComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(WsShopifyCatalogListViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
