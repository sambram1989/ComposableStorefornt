import { Component, inject, input, output } from '@angular/core';
import { OccConfig } from '@spartacus/core';

@Component({
  selector: 'ws-shopify-catalog-grid-view',
  templateUrl: './ws-shopify-catalog-grid-view.component.html',
  styleUrls: ['./ws-shopify-catalog-grid-view.component.scss', '../ws-shopify-catalog/ws-shopify-catalog.component.scss']
})
export class WsShopifyCatalogGridViewComponent {
  shopifyProductsList = input<any>({});
  favoriteList = input<any>({});
  isLoggedIn = input<boolean>(false);
  importProducts = output<any>();
  deleteProducts = output<any>();
  updateFavorites = output<any>();
  public occ = inject(OccConfig);

  isFavorite(item: any): boolean {
    let product = this.favoriteList()?.wishLists?.wishListEntries?.findIndex((product: any) => product.productCode === item.code);
    return product > -1 ? true : false;
  }

  getBadge(product: any) {
    if (product.isFeatured) return 'Featured';
    if (product.isNew) return 'New';
    if (product.bestSeller) return 'Best seller';
    else return 'Best seller';
  }


}
