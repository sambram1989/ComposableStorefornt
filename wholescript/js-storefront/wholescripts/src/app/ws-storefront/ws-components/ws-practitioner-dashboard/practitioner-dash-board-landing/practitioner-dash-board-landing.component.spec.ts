import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PractitionerDashBoardLandingComponent } from './practitioner-dash-board-landing.component';

describe('PractitionerDashBoardLandingComponent', () => {
  let component: PractitionerDashBoardLandingComponent;
  let fixture: ComponentFixture<PractitionerDashBoardLandingComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [PractitionerDashBoardLandingComponent]
    })
    .compileComponents(); 
    
    fixture = TestBed.createComponent(PractitionerDashBoardLandingComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
