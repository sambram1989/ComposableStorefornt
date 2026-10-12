import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PopularCategoryShopComponent } from './popular-category-shop.component';

describe('PopularCategoryShopComponent', () => {
  let component: PopularCategoryShopComponent;
  let fixture: ComponentFixture<PopularCategoryShopComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [PopularCategoryShopComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(PopularCategoryShopComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
