import { Component } from '@angular/core';
import { ProductImageZoomProductImagesComponent } from '@spartacus/product/image-zoom/components';

@Component({
  selector: 'ws-product-image-zoom-product-images-component',
  templateUrl: './ws-product-image-zoom-product-images-component.component.html',
  styleUrl: './ws-product-image-zoom-product-images-component.component.scss'
})
export class WsProductImageZoomProductImagesComponent extends ProductImageZoomProductImagesComponent {
  getBadge(product: any) {
    if (product.isFeatured) return 'Featured';
    if (product.isNew) return 'New';
    if (product.isBestSeller) return 'Best seller';
    else return 'Best seller';
  }
}
