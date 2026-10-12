import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'thirtyDaySupply'
})
export class ThirtyDaySupplyPipe implements PipeTransform {
  transform(
    currentProduct: any, 
    quantity: number = 0,
    fromRecommendation: boolean = false
  ): number {
    if (!currentProduct?.prod && !currentProduct?.wholeSalePrice ) {
      return 0; 
    }
    if(fromRecommendation) {
      const perCapsulePrice = currentProduct?.prod?.retailPrice?.value || currentProduct?.retailPrice?.value || 0;

      return (30 * quantity) * perCapsulePrice;
    }
    const perCapsulePrice = currentProduct?.prod?.wholeSalePrice?.value || currentProduct?.wholeSalePrice?.value || 0;

    return (30 * quantity) * perCapsulePrice;
  }
}