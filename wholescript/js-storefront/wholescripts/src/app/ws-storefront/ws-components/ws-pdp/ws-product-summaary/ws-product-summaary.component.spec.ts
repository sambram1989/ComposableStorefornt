import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WsProductSummaaryComponent } from './ws-product-summaary.component';

describe('WsProductSummaaryComponent', () => {
  let component: WsProductSummaaryComponent;
  let fixture: ComponentFixture<WsProductSummaaryComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [WsProductSummaaryComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(WsProductSummaaryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
