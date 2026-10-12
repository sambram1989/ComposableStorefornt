import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HomePageMedpaxComponent } from './homepage-medpax.component';

describe('HomePageMedpaxComponent', () => {
  let component: HomePageMedpaxComponent;
  let fixture: ComponentFixture<HomePageMedpaxComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [HomePageMedpaxComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(HomePageMedpaxComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
