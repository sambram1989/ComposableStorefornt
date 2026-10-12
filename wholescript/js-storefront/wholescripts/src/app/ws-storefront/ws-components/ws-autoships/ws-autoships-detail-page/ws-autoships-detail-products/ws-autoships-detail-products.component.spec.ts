import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WsAutoshipsDetailProductsComponent } from './ws-autoships-detail-products.component';

describe('WsAutoshipsDetailProductsComponent', () => {
  let component: WsAutoshipsDetailProductsComponent;
  let fixture: ComponentFixture<WsAutoshipsDetailProductsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [WsAutoshipsDetailProductsComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(WsAutoshipsDetailProductsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
