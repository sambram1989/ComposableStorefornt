import { NgModule } from '@angular/core';
import { ShopComponentModule } from './components/shop-component.module';
import { ShopOccModule } from './occ/shop-occ.module';

@NgModule({
  imports: [
    ShopComponentModule,
    ShopOccModule
  ],
})
export class ShopModule { }
