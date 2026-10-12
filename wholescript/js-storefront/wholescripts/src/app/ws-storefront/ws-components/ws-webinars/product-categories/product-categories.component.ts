import { Component, Input, input } from '@angular/core';
import { Image } from '@spartacus/core';
import { MediaService } from '@spartacus/storefront';

@Component({
  selector: 'ws-product-categories',
  templateUrl: './product-categories.component.html',
  styleUrl: './product-categories.component.scss'
})
export class ProductCategoriesComponent {
    constructor(
      protected mediaService: MediaService,
    ) {}
  categoryName = input<string>('');
  categoryUrl = input<string>('');
  @Input() categoryImage: any = {}
 
    getMedia(item: any) {
      const image:Image ={
        url:item.url
      }
      return this.mediaService.getMedia(image).src;
    }
}
