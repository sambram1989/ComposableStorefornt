import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TitleBodyContentComponent } from './title-body-content.component';

describe('TitleBodyContentComponent', () => {
  let component: TitleBodyContentComponent;
  let fixture: ComponentFixture<TitleBodyContentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [TitleBodyContentComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(TitleBodyContentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
