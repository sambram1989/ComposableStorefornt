
import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { I18nModule, FeaturesConfigModule } from '@spartacus/core';
import { IconModule, KeyboardFocusModule } from '@spartacus/storefront';
import {  WsFacetListComponent } from './facet-list.component';
import { WsFacetModule } from '../facet/facet.module';

@NgModule({
  imports: [
    CommonModule,
    I18nModule,
    IconModule,
    WsFacetModule,
    KeyboardFocusModule,
    FeaturesConfigModule,
  ],
  declarations: [WsFacetListComponent],
  exports: [WsFacetListComponent],
})
export class WsFacetListModule {}
