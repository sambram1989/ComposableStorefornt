import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {
  ConfigModule
} from '@spartacus/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { WsAvailableSuppliesLastProductsComponent } from './ws-available-supplies-last-products/ws-available-supplies-last-products.component';
import { WsDiscontinuedProductsComponent } from './ws-discontinued-products/ws-discontinued-products.component';
import { WsProductUpdatesPaginationComponent } from './ws-product-updates-pagination/ws-product-updates-pagination.component';
import { RouterModule } from '@angular/router';


@NgModule({
  declarations: [
    WsDiscontinuedProductsComponent,
    WsAvailableSuppliesLastProductsComponent,
    WsProductUpdatesPaginationComponent
  ],
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    RouterModule,
    ConfigModule.withConfig({
      cmsComponents: {
        ProductUpdatesPageBodySlotLeftContentFlexComp: {
          component: WsAvailableSuppliesLastProductsComponent,
        },
        ProductUpdatesPageBodySlotRightContentFlexComp: {
            component: WsDiscontinuedProductsComponent,
          }
      },
    }),
  ],
  exports: [
    WsDiscontinuedProductsComponent,
    WsAvailableSuppliesLastProductsComponent,
    WsProductUpdatesPaginationComponent
  ],
})
export class ProductUpdatesModule {}
