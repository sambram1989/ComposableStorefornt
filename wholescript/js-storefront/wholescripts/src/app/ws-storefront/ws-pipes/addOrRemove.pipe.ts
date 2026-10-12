import { Pipe, PipeTransform } from "@angular/core";

@Pipe({
    name: 'addOrRemove'
 })
 export class AddOrRemovePipe implements PipeTransform {
    transform(productCode: string, selectedProductCodes: string[],type?:string): string {
      if(type == 'save'){
         return this.isProductSelected(productCode, selectedProductCodes) ? 'Save' : '+ Add';
      }else{
         return this.isProductSelected(productCode, selectedProductCodes) ? 'Remove' : '+ Add';
      }

    }
 
    private isProductSelected(productCode: string, selectedProductCodes: string[]): boolean {
       return selectedProductCodes?.includes(productCode);
    }
 }