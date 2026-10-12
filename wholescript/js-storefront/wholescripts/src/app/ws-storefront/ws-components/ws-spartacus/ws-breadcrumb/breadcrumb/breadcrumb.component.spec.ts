import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WsBreadcrumbComponent } from './breadcrumb.component';

describe('WsBreadcrumbComponent', () => {
  let component: WsBreadcrumbComponent;
  let fixture: ComponentFixture<WsBreadcrumbComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [WsBreadcrumbComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(WsBreadcrumbComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
