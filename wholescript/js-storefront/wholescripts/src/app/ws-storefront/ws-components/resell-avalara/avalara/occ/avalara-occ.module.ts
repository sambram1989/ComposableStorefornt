import { NgModule } from '@angular/core';
import { provideConfig } from '@spartacus/core';
import { avalaraConfig } from './adapters/config/avalara-endpoint.config';
@NgModule({
  providers: [provideConfig(avalaraConfig)],
})
export class AvalaraOccModule {}
