import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LeftfilterfacetComponent } from './leftfilterfacet.component';

describe('LeftfilterfacetComponent', () => {
  let component: LeftfilterfacetComponent;
  let fixture: ComponentFixture<LeftfilterfacetComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [LeftfilterfacetComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(LeftfilterfacetComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
