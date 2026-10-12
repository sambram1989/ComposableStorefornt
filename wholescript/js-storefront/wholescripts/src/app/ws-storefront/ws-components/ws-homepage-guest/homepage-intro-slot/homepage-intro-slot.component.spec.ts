import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HomePageIntroComponent } from './homepage-intro-slot.component';

describe('HomePageIntroComponent', () => {
  let component: HomePageIntroComponent;
  let fixture: ComponentFixture<HomePageIntroComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [HomePageIntroComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(HomePageIntroComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
