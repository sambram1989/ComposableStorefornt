import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CustomMedpaxBuyAgainFacetComponent } from './custom-medpax-buy-again-facet.component';

describe('CustomMedpaxBuyAgainFacetComponent', () => {
  let component: CustomMedpaxBuyAgainFacetComponent;
  let fixture: ComponentFixture<CustomMedpaxBuyAgainFacetComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [CustomMedpaxBuyAgainFacetComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(CustomMedpaxBuyAgainFacetComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
