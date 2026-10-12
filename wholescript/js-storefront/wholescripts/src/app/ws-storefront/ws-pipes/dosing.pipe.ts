import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'dosingPipe'
})
export class DosingPipe implements PipeTransform {

  transform(value: any,isMedPax = false, ...args: unknown[]): unknown {
    if (!value || !Array.isArray(value)) return '';
    
    return value
      .map((data: any) => {
        
        const itemTimes = isMedPax ?  data?.itemTimes?.parentItemTimeCode  :  data?.itemTimes?.timeOfTheDay;
        return itemTimes ? `${data.quantity} in ${itemTimes}` : '';
      })
      .filter(Boolean)
      .join(' | ');
  }

}


@Pipe({
  name: 'dosingQty'
})
export class SumQuantityPipe implements PipeTransform {

  transform(value: any[]): number {
    if (!value || !Array.isArray(value)) return 0;

    return value.reduce((sum, item) => sum + (item.quantity || 0), 0);
  }

}
