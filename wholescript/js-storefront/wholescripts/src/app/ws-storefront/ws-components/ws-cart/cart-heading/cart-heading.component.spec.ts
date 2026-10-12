import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CartHeadingComponent } from './cart-heading.component';

describe('CartHeadingComponent', () => {
  let component: CartHeadingComponent;
  let fixture: ComponentFixture<CartHeadingComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [CartHeadingComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(CartHeadingComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
