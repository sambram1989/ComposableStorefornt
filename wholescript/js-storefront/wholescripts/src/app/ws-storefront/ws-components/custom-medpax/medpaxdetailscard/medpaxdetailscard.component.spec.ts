import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MedpaxdetailscardComponent } from './medpaxdetailscard.component';

describe('MedpaxdetailscardComponent', () => {
  let component: MedpaxdetailscardComponent;
  let fixture: ComponentFixture<MedpaxdetailscardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [MedpaxdetailscardComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(MedpaxdetailscardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
