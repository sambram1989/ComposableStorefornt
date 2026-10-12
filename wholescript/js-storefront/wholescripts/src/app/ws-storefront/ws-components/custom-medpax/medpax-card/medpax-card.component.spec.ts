import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MedpaxCardComponent } from './medpax-card.component';

describe('MedpaxCardComponent', () => {
  let component: MedpaxCardComponent;
  let fixture: ComponentFixture<MedpaxCardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [MedpaxCardComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(MedpaxCardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
