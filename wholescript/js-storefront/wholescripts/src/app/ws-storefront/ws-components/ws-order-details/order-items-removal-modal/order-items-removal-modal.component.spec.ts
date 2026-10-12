import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OrderItemsRemovalModalComponent } from './order-items-removal-modal.component';

describe('OrderItemsRemovalModalComponent', () => {
  let component: OrderItemsRemovalModalComponent;
  let fixture: ComponentFixture<OrderItemsRemovalModalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [OrderItemsRemovalModalComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(OrderItemsRemovalModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
