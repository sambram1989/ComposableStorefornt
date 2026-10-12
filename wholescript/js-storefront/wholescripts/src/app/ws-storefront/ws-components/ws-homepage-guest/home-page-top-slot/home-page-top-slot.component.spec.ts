import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HomePageTopSlotComponent } from './home-page-top-slot.component';

describe('HomePageTopSlotComponent', () => {
  let component: HomePageTopSlotComponent;
  let fixture: ComponentFixture<HomePageTopSlotComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [HomePageTopSlotComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(HomePageTopSlotComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
