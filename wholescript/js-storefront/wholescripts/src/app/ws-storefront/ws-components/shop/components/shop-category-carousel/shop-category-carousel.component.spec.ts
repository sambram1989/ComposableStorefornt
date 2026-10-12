import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ShopCategoryCarouselComponent } from './shop-category-carousel.component';

describe('ShopCategoryCarouselComponent', () => {
  let component: ShopCategoryCarouselComponent;
  let fixture: ComponentFixture<ShopCategoryCarouselComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ShopCategoryCarouselComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ShopCategoryCarouselComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
