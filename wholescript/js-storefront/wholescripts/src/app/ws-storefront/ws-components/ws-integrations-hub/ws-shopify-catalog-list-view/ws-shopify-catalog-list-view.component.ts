import { Component, input, output } from '@angular/core';

@Component({
  selector: 'ws-shopify-catalog-list-view',
  templateUrl: './ws-shopify-catalog-list-view.component.html',
  styleUrls: ['./ws-shopify-catalog-list-view.component.scss','../../ws-plp/ws-patient-plp/ws-patient-plp.component.scss']
})
export class WsShopifyCatalogListViewComponent {
  shopifyProductsList = input<any>({});
  favoriteList = input<any>({});
  isLoggedIn = input<boolean>(false);
  importProducts = output<any>();
  deleteProducts = output<any>();
  updateFavorites = output<any>();


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
