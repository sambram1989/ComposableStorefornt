import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PopularByCategoryComponent } from './popular-by-category.component';

describe('PopularByCategoryComponent', () => {
  let component: PopularByCategoryComponent;
  let fixture: ComponentFixture<PopularByCategoryComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [PopularByCategoryComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(PopularByCategoryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
