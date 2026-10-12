import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'calculateQuantity'
})
export class CalculateQuantityPipe implements PipeTransform {

  transform(price: number, medPaxproducts: any, code: any, isDosingList: boolean = false): number {
    if (!medPaxproducts || !code || !price) {
      return 0; 
    }

    let qty = 0;

    if (isDosingList) {
      qty = medPaxproducts.dosingList.reduce((prev: number, curr: any) => prev + curr.quantity, 0);
    } else {
      const product = medPaxproducts.medpaxEntry.find((item: any) => item.product.code === code);
      qty = product?.dosing.reduce((prev: number, curr: any) => prev + curr.quantity, 0) || 0;
    }

    return qty * 30 * price; 
  }
}
