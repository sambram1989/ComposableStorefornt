import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SupplementfactsComponent } from './supplementfacts.component';

describe('SupplementfactsComponent', () => {
  let component: SupplementfactsComponent;
  let fixture: ComponentFixture<SupplementfactsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [SupplementfactsComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(SupplementfactsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
