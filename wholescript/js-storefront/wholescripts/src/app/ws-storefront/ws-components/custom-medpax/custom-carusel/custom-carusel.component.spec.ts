import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CustomCaruselComponent } from './custom-carusel.component';

describe('CustomCaruselComponent', () => {
  let component: CustomCaruselComponent;
  let fixture: ComponentFixture<CustomCaruselComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [CustomCaruselComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(CustomCaruselComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
