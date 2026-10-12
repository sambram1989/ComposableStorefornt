import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PopularByBrandShopComponent } from './popular-by-brand-shop.component';

describe('PopularByBrandShopComponent', () => {
  let component: PopularByBrandShopComponent;
  let fixture: ComponentFixture<PopularByBrandShopComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [PopularByBrandShopComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(PopularByBrandShopComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
