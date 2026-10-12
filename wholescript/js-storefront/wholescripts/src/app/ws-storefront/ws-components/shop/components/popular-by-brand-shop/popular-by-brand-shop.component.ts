import { Component, inject } from '@angular/core';
import { ShopService } from '../../core/facade/shop.service';
import { filter, map, take } from 'rxjs';
import { Image } from '@spartacus/core';
import { MediaService } from '@spartacus/storefront';
import { Router } from '@angular/router';

@Component({
  selector: 'ws-popular-by-brand-shop',
  templateUrl: './popular-by-brand-shop.component.html',
  styleUrl: './popular-by-brand-shop.component.scss',
})
export class PopularByBrandShopComponent {
  protected router = inject(Router)
  categories$ = this.shopService
    .getShopTopBrand()
    .pipe(filter((cate) => !!cate));

  primaryBrand$ = this.categories$.pipe(
    take(1),
    map((cate) => cate?.topBrands?.find((pri) => pri.code === 'Z_MM_BRANDS_01'))
  );

  otherBrand$ = this.categories$.pipe(
    take(1),
    map((cate) => cate?.topBrands?.filter((pri) => pri.code !== 'Z_MM_BRANDS_01'))
  );

  otherCardClass = [
    'card-top card-left',
    'card-top card-center',
    'card-top card-right',
    'card-bottom card-left on-top',
    'card-bottom card-center',
    'card-bottom card-right',
  ];

  constructor(
    protected shopService: ShopService,
    protected mediaService: MediaService,
  ) {}

  getMedia(name: string, url: string): string {
    const image: Image = {
      url: url,
      altText: name,
      format: 'product',
    };
    return this.mediaService.getMedia(image).src;
  }

  navigateTo(url: string): void {
    if (url) {
      this.router.navigateByUrl(url);
    }
  }
}

