import { NgModule } from '@angular/core';
import { AvalaraComponentModule } from './avalara/components/avalara-component.module';
import { AvalaraOccModule } from './avalara/occ/avalara-occ.module';

@NgModule({
  imports: [AvalaraComponentModule, AvalaraOccModule],
})
export class ResellAvalaraModule {}
