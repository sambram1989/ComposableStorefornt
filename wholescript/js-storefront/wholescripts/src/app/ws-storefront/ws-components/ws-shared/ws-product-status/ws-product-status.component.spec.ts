import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WsProductStatusComponent } from './ws-product-status.component';

describe('WsProductStatusComponent', () => {
  let component: WsProductStatusComponent;
  let fixture: ComponentFixture<WsProductStatusComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [WsProductStatusComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(WsProductStatusComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
