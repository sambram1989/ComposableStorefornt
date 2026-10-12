import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ShopLatestRecommendationComponent } from './shop-latest-recommendation.component';

describe('ShopLatestRecommendationComponent', () => {
  let component: ShopLatestRecommendationComponent;
  let fixture: ComponentFixture<ShopLatestRecommendationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ShopLatestRecommendationComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ShopLatestRecommendationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
