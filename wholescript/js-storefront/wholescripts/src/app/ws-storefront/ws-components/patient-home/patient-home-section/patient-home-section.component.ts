import { Component, OnInit } from '@angular/core';
import { CmsBannerComponent } from '@spartacus/core';
import { CmsComponentData } from '@spartacus/storefront';

@Component({
  selector: 'app-patient-home-section',
  templateUrl: './patient-home-section.component.html',
})
export class PatientHomeSectionComponent implements OnInit {
  constructor(protected componentData: CmsComponentData<CmsBannerComponent>,) { }

  ngOnInit() {
    this.componentData.data$.subscribe(res => {
      
    })
  }

}
