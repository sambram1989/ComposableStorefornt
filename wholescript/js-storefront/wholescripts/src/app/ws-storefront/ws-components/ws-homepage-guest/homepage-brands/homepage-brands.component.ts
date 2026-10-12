import { Component } from '@angular/core';
import { WsPage } from '../../../ws-services/ws-page.service';
import { slotMarkup, PRIMARY_SLOT, BRANDS_SLOT, BRAND_ICONS_SLOT } from '../../../ws-models/page.model';
import { Observable } from 'rxjs';

@Component({
  selector: 'ws-homepage-brands',
  templateUrl: './homepage-brands.component.html',
  styleUrl: './homepage-brands.component.scss'
})
export class HomePageBrandsComponent {

  brandsContentSlot = new Observable<slotMarkup>;
  brandsIconsSlot = new Observable<slotMarkup>;
  brandsContent: { [k: string]: any } = {};
  brandsIcons: { [k: string]: any } = {};
  icons: any;

  constructor(public wsPageService: WsPage) {
    this.brandsContentSlot = this.wsPageService.getPage(PRIMARY_SLOT, 'BestBrands');
    this.brandsIconsSlot = this.wsPageService.getPage(PRIMARY_SLOT, 'BestBrandsIcons');
    this.getBrandInfo();
  }

  getBrandInfo() {
    this.brandsContentSlot.subscribe((response) => {
      response.components.forEach((element: any) => {
        if (BRANDS_SLOT.includes(element.uid)) {
          this.brandsContent[element.uid] = element;
        }
      });
    })
    this.brandsIconsSlot.subscribe((response) => {
      response.components.forEach((element: any) => {
        if (BRAND_ICONS_SLOT.includes(element.uid)) {
          this.brandsIcons[element.uid] = element;
        }
      });
    })
    this.icons = Object.entries(this.brandsIcons);
  }

  

}
