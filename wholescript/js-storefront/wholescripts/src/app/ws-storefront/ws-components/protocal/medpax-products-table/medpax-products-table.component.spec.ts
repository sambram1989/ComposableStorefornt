import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MedpaxProductsTableComponent } from './medpax-products-table.component';

describe('MedpaxProductsTableComponent', () => {
  let component: MedpaxProductsTableComponent;
  let fixture: ComponentFixture<MedpaxProductsTableComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [MedpaxProductsTableComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(MedpaxProductsTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
