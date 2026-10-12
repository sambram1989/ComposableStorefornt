import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'isExpired'
})
export class IsExpiredPipe implements PipeTransform {
  transform(validTo: string): string {
    const currentDate = new Date(); 
    const validToDate = new Date(validTo); 

    return validToDate < currentDate ? 'Expired' : '';
  }
}
