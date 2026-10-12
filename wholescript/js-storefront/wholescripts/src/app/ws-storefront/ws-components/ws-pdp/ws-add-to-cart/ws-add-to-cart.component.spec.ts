import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WsAddToCartComponent } from './ws-add-to-cart.component';

describe('WsAddToCartComponent', () => {
  let component: WsAddToCartComponent;
  let fixture: ComponentFixture<WsAddToCartComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [WsAddToCartComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(WsAddToCartComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
