import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {
  ConfigModule
} from '@spartacus/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { WsContactUsPageComponent } from './ws-contact-us-page/ws-contact-us-page.component';
import { RouterModule } from '@angular/router';

@NgModule({
  declarations: [
    WsContactUsPageComponent,
  ],
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    RouterModule,
    ConfigModule.withConfig({
      cmsComponents: {
        FormFlexComponent: {
          component: WsContactUsPageComponent,
        },
      },
    }),
  ],
  exports: [
    WsContactUsPageComponent,
  ],
})
export class ContactUsModule {}
