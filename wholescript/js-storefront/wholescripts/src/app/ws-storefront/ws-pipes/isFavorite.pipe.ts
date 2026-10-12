import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'isFavorite'
})
export class IsFavoritePipe implements PipeTransform {
  transform(item: any, favoriteList: any): boolean {
    return (
      Array.isArray(favoriteList?.wishLists) &&
      favoriteList.wishLists[0]?.wishListEntries.some(
        (favItem: { productCode: string }) => favItem.productCode === item.code
      )
    );
  }
}
