import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { CMS_PAGE_NORMALIZER } from '@spartacus/core';
import { WsOccCmsPageNormalizer } from './ws-occ-cms-page-normalizer';


@NgModule({
  imports: [CommonModule],
  providers: [
    {
      provide: CMS_PAGE_NORMALIZER,
      useClass: WsOccCmsPageNormalizer,
      multi: true,
    }
  ],
})
export class WsOccModule {}
