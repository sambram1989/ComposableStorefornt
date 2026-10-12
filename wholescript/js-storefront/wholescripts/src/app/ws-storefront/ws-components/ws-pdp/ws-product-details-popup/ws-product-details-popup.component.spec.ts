import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WsProductDetailsPopupComponent } from './ws-product-details-popup.component';

describe('WsProductDetailsPopupComponent', () => {
  let component: WsProductDetailsPopupComponent;
  let fixture: ComponentFixture<WsProductDetailsPopupComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [WsProductDetailsPopupComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(WsProductDetailsPopupComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
