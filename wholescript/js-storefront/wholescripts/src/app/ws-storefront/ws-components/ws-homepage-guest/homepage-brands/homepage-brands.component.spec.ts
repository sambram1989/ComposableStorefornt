import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HomePageBrandsComponent } from './homepage-brands.component';

describe('HomePageBrandsComponent', () => {
  let component: HomePageBrandsComponent;
  let fixture: ComponentFixture<HomePageBrandsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [HomePageBrandsComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(HomePageBrandsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
