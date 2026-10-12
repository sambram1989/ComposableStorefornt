import { Component } from '@angular/core';
import { CmsBannerComponent } from '@spartacus/core';
import { CmsComponentData } from '@spartacus/storefront';

@Component({
  selector: 'ws-integration-card-content',
  templateUrl: './integration-card-content.component.html',
  styleUrl: './integration-card-content.component.scss'
})
export class IntegrationCardContentComponent {
  constructor(protected componentData: CmsComponentData<CmsBannerComponent>,) { }
}
