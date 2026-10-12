import { Component, inject, OnInit } from '@angular/core';
import { CurrentProductService } from '@spartacus/storefront';
import { Observable, tap } from 'rxjs';
import { PdpDataservice } from '../data-pdp/pdpData.service';

@Component({
  selector: 'ws-ws-formulared-to-exclude',
  templateUrl: './ws-formulared-to-exclude.component.html',
  styleUrl: './ws-formulared-to-exclude.component.scss'
})
export class WsFormularedToExcludeComponent implements OnInit {

  product$: Observable<any>;
  public dataService = inject(PdpDataservice);

  constructor(private currentProductService: CurrentProductService) { }

  ngOnInit(): void {
    this.product$ = this.currentProductService.getProduct().pipe(
      tap((res: any) => {
        
        const code = "ERP_CLASSIFICATION_001/ERP_IMPORT/Z_MMPROPERTIES.z_dietary_restrictions";
        const classificationCode = 'Z_MMPROPERTIES';

        res.classifications.forEach((apiResponse: any) => {
          if (apiResponse.code == classificationCode) {
            apiResponse.features.forEach((object: any) => {
              if (object.code == code) {
                this.dataService.images = this.dataService.images.filter((image: any) =>
                  object.featureValues.some((apiItem: any) => apiItem.value === image.key)
                );
                this.dataService.restrictedImages.set(this.dataService.images);
              }
            })
          }
        })
      })
    )
  }
}
