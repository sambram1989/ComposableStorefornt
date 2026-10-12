import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WsProductImageZoomProductImagesComponentComponent } from './ws-product-image-zoom-product-images-component.component';

describe('WsProductImageZoomProductImagesComponentComponent', () => {
  let component: WsProductImageZoomProductImagesComponentComponent;
  let fixture: ComponentFixture<WsProductImageZoomProductImagesComponentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [WsProductImageZoomProductImagesComponentComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(WsProductImageZoomProductImagesComponentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
