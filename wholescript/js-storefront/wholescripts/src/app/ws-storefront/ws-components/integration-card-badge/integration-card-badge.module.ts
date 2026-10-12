import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CmsConfig, provideConfig } from '@spartacus/core';
import { IntegrationCardBadgeComponent } from './integration-card-badge.component';

@NgModule({
  declarations: [
    IntegrationCardBadgeComponent
  ],
  imports: [
    CommonModule
  ],
  providers: [
    provideConfig(<CmsConfig>{
      cmsComponents: {
        WsIntegrationBadgeParagraphComponent: {
          component: IntegrationCardBadgeComponent,
        },
      },
    }),
  ],
})
export class IntegrationCardBadgeModule { }