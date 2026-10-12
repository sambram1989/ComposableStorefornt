import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SuccessProductComponent } from './success-product.component';

describe('SuccessProductComponent', () => {
  let component: SuccessProductComponent;
  let fixture: ComponentFixture<SuccessProductComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [SuccessProductComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(SuccessProductComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
