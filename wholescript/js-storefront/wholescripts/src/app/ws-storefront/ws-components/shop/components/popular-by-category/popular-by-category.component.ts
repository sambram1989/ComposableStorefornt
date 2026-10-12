import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { ShopService } from '../../core/facade/shop.service';
import { filter, map, Observable, shareReplay, take } from 'rxjs';
import {
  PopularByCategory,
  ShopCategory,
} from '../../core/model/shop-category.model';
import { Image } from '@spartacus/core';
import { MediaService } from '@spartacus/storefront';

@Component({
  selector: 'ws-popular-by-category',
  templateUrl: './popular-by-category.component.html',
  styleUrl: './popular-by-category.component.scss',
})
export class PopularByCategoryComponent implements OnInit {
  selectedCategoryCode: string | null = null;

  categories$: Observable<ShopCategory> = this.shopService.getShopCategory().pipe(
    map((category: ShopCategory) => {
      const withProducts = category.popularByCategory?.filter(c => c.products?.length > 0) ?? [];
      const withoutProducts = category.popularByCategory?.filter(c => !c.products?.length) ?? [];
      const allCategories = [...withProducts, ...withoutProducts];
      if (!this.selectedCategoryCode && allCategories.length > 0) {
        this.selectedCategoryCode = allCategories[0]?.code ?? null;
      }
      return {
        ...category,
        popularByCategory: allCategories
      };
    }),
    shareReplay({ bufferSize: 1, refCount: true })
  );

  items: any = [];

  constructor(
    protected shopService: ShopService,
    protected mediaService: MediaService,
    protected cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.categories$.pipe(take(1)).subscribe((res: ShopCategory) => {
      const selectedCategory = res.popularByCategory.find(
        (c: PopularByCategory) => c.selected
      );
      this.items = selectedCategory
        ? selectedCategory.products
        : res.popularByCategory[0].products;
      this.cdr.detectChanges();
    });
  }

  changesCarouselItemByCategory(category: any) {
    this.categories$.pipe(take(1)).subscribe((res: ShopCategory) => {
      res.popularByCategory.forEach((pc: any) => {
        pc.selected = category?.code === pc?.code;
      });
      const selectedCategory = res.popularByCategory.find(
        (c: PopularByCategory) => c.selected
      );
      this.items = selectedCategory ? selectedCategory.products : [];
      this.cdr.detectChanges();
    });
  }

  goToCategory(category: any) {
    this.selectedCategoryCode = category.code;
    this.changesCarouselItemByCategory(category);
  }

  getMedia(item: any) {
    const image:Image ={
      url:item?.categoryImage?.url,
      altText:item?.name,
      format:'product'
    }
    return this.mediaService.getMedia(image).src;
  }
}
