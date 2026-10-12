import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CartboxstyleComponent } from './cartboxstyle.component';

describe('CartboxstyleComponent', () => {
  let component: CartboxstyleComponent;
  let fixture: ComponentFixture<CartboxstyleComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [CartboxstyleComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(CartboxstyleComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
