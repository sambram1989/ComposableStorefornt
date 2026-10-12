import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'sortFavorites'
})
export class SortFavoritesPipe implements PipeTransform {

  transform(products: any[], favoriteList: any): any[] {
    if (!products || !favoriteList || !favoriteList.wishLists || !favoriteList.wishLists.wishListEntries) {
      return products;  
    }

    const favoriteCodes = favoriteList.wishLists.wishListEntries.map((entry:any) => entry.productCode);

    return products.sort((a, b) => {
      const isAFavorite = favoriteCodes.includes(a.code);
      const isBFavorite = favoriteCodes.includes(b.code);

      if (isAFavorite && !isBFavorite) {
        return -1;  
      } else if (!isAFavorite && isBFavorite) {
        return 1;   
      }
      return 0;  
    });
  }

}
