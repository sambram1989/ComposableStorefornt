import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WsProductSummaryBottomComponent } from './ws-product-summary-bottom.component';

describe('WsProductSummaryBottomComponent', () => {
  let component: WsProductSummaryBottomComponent;
  let fixture: ComponentFixture<WsProductSummaryBottomComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [WsProductSummaryBottomComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(WsProductSummaryBottomComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
