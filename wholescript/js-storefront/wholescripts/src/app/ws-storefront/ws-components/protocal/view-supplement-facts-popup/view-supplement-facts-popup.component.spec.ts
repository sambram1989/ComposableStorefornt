import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ViewSupplementFactsPopupComponent } from './view-supplement-facts-popup.component';

describe('ViewSupplementFactsPopupComponent', () => {
  let component: ViewSupplementFactsPopupComponent;
  let fixture: ComponentFixture<ViewSupplementFactsPopupComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ViewSupplementFactsPopupComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ViewSupplementFactsPopupComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
