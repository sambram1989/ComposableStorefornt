import { Component } from '@angular/core';
import { ShopService } from '../../core/facade/shop.service';
import { filter } from 'rxjs';
import { MediaService } from '@spartacus/storefront';
import { Image } from '@spartacus/core';
@Component({
  selector: 'ws-popular-category-shop',
  templateUrl: './popular-category-shop.component.html',
  styleUrl: './popular-category-shop.component.scss'
})
export class PopularCategoryShopComponent {
  categories$ = this.shopService.getShopCategory().pipe(filter(cate => Boolean(cate)))
  constructor(protected shopService:ShopService,  protected mediaService: MediaService,){}

  getMedia(item: any) {
    const image:Image ={
      url:item?.categoryImage?.url,
      altText:item?.Name,
      format:'product'
    }
    return this.mediaService.getMedia(image).src;
  }
}
