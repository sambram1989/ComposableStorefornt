import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PresentersDetailsPageComponent } from './presenters-details-page.component';

describe('PresentersDetailsPageComponent', () => {
  let component: PresentersDetailsPageComponent;
  let fixture: ComponentFixture<PresentersDetailsPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [PresentersDetailsPageComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(PresentersDetailsPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
