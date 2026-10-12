import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WsProductUpdatesPaginationComponent } from './ws-product-updates-pagination.component';

describe('WsProductUpdatesPaginationComponent', () => {
  let component: WsProductUpdatesPaginationComponent;
  let fixture: ComponentFixture<WsProductUpdatesPaginationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [WsProductUpdatesPaginationComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(WsProductUpdatesPaginationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
