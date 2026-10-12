import { Pipe, PipeTransform } from "@angular/core";

@Pipe({
  name: 'parseUrl'
})
export class ParseUrlPipe implements PipeTransform {
  
  transform(url: string, ids: string[]): string {
    if (!url || !ids || ids.length === 0) {
      return url; 
    }

    const joinedIds = ids.map(id => encodeURIComponent(id)).join('/');

    return `${encodeURI(url)}/${joinedIds}`;
  }
}
