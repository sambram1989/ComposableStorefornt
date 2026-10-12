import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WsSearchPageProductsComponent } from './ws-search-page-products.component';

describe('WsSearchPageProductsComponent', () => {
  let component: WsSearchPageProductsComponent;
  let fixture: ComponentFixture<WsSearchPageProductsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [WsSearchPageProductsComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(WsSearchPageProductsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
