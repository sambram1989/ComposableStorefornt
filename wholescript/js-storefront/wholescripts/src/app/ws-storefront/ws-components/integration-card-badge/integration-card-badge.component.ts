import { Component } from '@angular/core';
import { CmsBannerComponent } from '@spartacus/core';
import { CmsComponentData } from '@spartacus/storefront';
@Component({
  selector: 'ws-integration-card-badge',
  templateUrl: './integration-card-badge.component.html',
  styleUrl: './integration-card-badge.component.scss'
})
export class IntegrationCardBadgeComponent {
  constructor(protected componentData: CmsComponentData<CmsBannerComponent>,) { }
}