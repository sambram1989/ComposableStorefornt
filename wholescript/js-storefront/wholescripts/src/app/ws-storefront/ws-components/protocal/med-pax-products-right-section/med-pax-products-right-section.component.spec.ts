import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MedPaxProductsRightSectionComponent } from './med-pax-products-right-section.component';

describe('MedPaxProductsRightSectionComponent', () => {
  let component: MedPaxProductsRightSectionComponent;
  let fixture: ComponentFixture<MedPaxProductsRightSectionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [MedPaxProductsRightSectionComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(MedPaxProductsRightSectionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
