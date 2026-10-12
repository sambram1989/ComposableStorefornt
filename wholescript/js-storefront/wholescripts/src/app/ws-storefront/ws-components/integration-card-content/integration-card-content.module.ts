import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CmsConfig, provideConfig } from '@spartacus/core';
import { IntegrationCardContentComponent } from './integration-card-content.component';



@NgModule({
  declarations: [
    IntegrationCardContentComponent
  ],
  imports: [
    CommonModule
  ],
  providers: [
    provideConfig(<CmsConfig>{
      cmsComponents: {
        WsIntegrationContentParagraphComponent: {
          component: IntegrationCardContentComponent,
        },
      },
    }),
  ],
})
export class IntegrationCardContentModule { }
