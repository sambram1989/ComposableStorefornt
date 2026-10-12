import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WebinarcategoriesComponent } from './webinarcategories.component';

describe('WebinarcategoriesComponent', () => {
  let component: WebinarcategoriesComponent;
  let fixture: ComponentFixture<WebinarcategoriesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [WebinarcategoriesComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(WebinarcategoriesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
