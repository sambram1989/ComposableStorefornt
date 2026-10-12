import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { WsPrivateLabelComponent } from './ws-private-label/ws-private-label.component';
import { AuthGuard, ConfigModule } from '@spartacus/core';
import { WsSharedModule } from '../ws-shared/ws-shared.module';
import { WsPrivateLabelFacetsComponent } from './ws-private-label-facets/ws-private-label-facets.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { WsPlpModule } from '../ws-plp/ws-plp.module';
import { CustomMedpaxModule } from '../custom-medpax/custom-medpax.module';
import { WsSpinnerModule } from '../ws-spartacus/spinner/ws-spinner.module';
import { WsPrivateLabelGuard } from './guard/private-label.guard';
import { AlertModule } from 'ngx-bootstrap/alert';

@NgModule({
  declarations: [
    WsPrivateLabelComponent,
    WsPrivateLabelFacetsComponent
  ],
  imports: [
    CommonModule,
    WsSpinnerModule,
    FormsModule,
    ReactiveFormsModule,
    WsSharedModule,
    AlertModule,
    WsPlpModule,
    CustomMedpaxModule,
    ConfigModule.withConfig({
      cmsComponents: {
        privateLabelflexComponent : {
          component: WsPrivateLabelComponent,
          guards : [AuthGuard, WsPrivateLabelGuard],
        },
      },
    }),
  ],
})
export class WsPrivateLabelModule { }
