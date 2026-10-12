import { NgModule } from '@angular/core';
import { provideConfig } from '@spartacus/core';
import { shopConfig } from './adapters/config/shop-endpoint.config';
@NgModule({
  providers: [provideConfig(shopConfig)],
})
export class ShopOccModule {}
