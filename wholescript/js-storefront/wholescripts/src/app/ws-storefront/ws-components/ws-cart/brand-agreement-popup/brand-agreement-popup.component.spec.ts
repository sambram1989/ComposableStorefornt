import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BrandAgreementPopupComponent } from './brand-agreement-popup.component';

describe('BrandAgreementPopupComponent', () => {
  let component: BrandAgreementPopupComponent;
  let fixture: ComponentFixture<BrandAgreementPopupComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [BrandAgreementPopupComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(BrandAgreementPopupComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
