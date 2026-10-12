import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WsAvailableSuppliesLastProductsComponent } from './ws-available-supplies-last-products.component';

describe('WsAvailableSuppliesLastProductsComponent', () => {
  let component: WsAvailableSuppliesLastProductsComponent;
  let fixture: ComponentFixture<WsAvailableSuppliesLastProductsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [WsAvailableSuppliesLastProductsComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(WsAvailableSuppliesLastProductsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
