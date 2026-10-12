import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CartItemsRemovalModalComponent } from './cart-items-removal-modal.component';

describe('CartItemsRemovalModalComponent', () => {
  let component: CartItemsRemovalModalComponent;
  let fixture: ComponentFixture<CartItemsRemovalModalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [CartItemsRemovalModalComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(CartItemsRemovalModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
