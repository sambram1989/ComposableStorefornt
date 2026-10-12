import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WsCartItemCounterComponent } from './ws-cart-item-counter.component';

describe('WsCartItemCounterComponent', () => {
  let component: WsCartItemCounterComponent;
  let fixture: ComponentFixture<WsCartItemCounterComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [WsCartItemCounterComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(WsCartItemCounterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
