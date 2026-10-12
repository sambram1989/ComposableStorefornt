import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WsPdpProductFamilyComponent } from './ws-pdp-product-family.component';

describe('WsPdpProductFamilyComponent', () => {
  let component: WsPdpProductFamilyComponent;
  let fixture: ComponentFixture<WsPdpProductFamilyComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [WsPdpProductFamilyComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(WsPdpProductFamilyComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
