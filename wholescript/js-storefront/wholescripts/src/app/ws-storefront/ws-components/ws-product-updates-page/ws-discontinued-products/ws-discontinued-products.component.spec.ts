import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WsDiscontinuedProductsComponent } from './ws-discontinued-products.component';

describe('WsDiscontinuedProductsComponent', () => {
  let component: WsDiscontinuedProductsComponent;
  let fixture: ComponentFixture<WsDiscontinuedProductsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [WsDiscontinuedProductsComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(WsDiscontinuedProductsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
