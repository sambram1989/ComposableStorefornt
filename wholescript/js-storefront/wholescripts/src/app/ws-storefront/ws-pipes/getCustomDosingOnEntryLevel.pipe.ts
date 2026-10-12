import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
    name: 'getDosingOnEntryLevel'
})
export class GetDosingOnEntryLevelPipe implements PipeTransform {

    transform(medpaxData: any, productcode: string, caller: 'protocol' | 'recommendation' | 'medpax'): any {
        if (!medpaxData || !productcode) return null;
      
        const entryMapping: { [key: string]: string } = {
          protocol: 'protocolEntries',
          recommendation: 'recommendationEntries',
          medpax: 'medpaxEntry'
        };
        const entries = medpaxData[entryMapping[caller]];
        const data = entries?.find((item: any) => item.product?.code === productcode);
        return data ? data.dosing : null;
      }
      

}
