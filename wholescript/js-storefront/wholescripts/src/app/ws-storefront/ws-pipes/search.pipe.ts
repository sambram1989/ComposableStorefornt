import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'search'
})
export class SearchPipe implements PipeTransform {
  transform(items: any[], searchText: string, keyCheck?:string): any[] {
    if (!items) {
      return [];
    }
    if (!searchText) {
      return items;
    }
    searchText = searchText.toLowerCase();
    return items.filter(item => {
      if(keyCheck){
        if (item.hasOwnProperty(keyCheck) && item[keyCheck] !== null && item[keyCheck].toString().toLowerCase().includes(searchText)) {
          return true;
        }
      }else{
        for (let key in item) {
          if (item.hasOwnProperty(key) && item[key] !== null && item[key].toString().toLowerCase().includes(searchText)) {
            return true;
          }
        }
      }
     
      return false;
    });
  }
}
