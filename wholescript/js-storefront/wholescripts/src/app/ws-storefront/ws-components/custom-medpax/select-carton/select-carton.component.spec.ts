import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SelectCartonComponent } from './select-carton.component';

describe('SelectCartonComponent', () => {
  let component: SelectCartonComponent;
  let fixture: ComponentFixture<SelectCartonComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [SelectCartonComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(SelectCartonComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
