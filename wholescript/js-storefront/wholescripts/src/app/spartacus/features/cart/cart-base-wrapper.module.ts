import { NgModule } from '@angular/core';
import { CartBaseModule } from "@spartacus/cart/base";
import { EstimatedDeliveryDateModule } from "@spartacus/estimated-delivery-date";
import { CustomBadCartRequestHandler } from '../../../ws-storefront/ws-interceptors/bad-cart-request-handler';
import { HttpErrorHandler } from '@spartacus/core';

@NgModule({
  declarations: [],
  imports: [
    CartBaseModule,
    EstimatedDeliveryDateModule
  ],
  providers: [
    {
      provide: HttpErrorHandler,
      useExisting: CustomBadCartRequestHandler,
      multi: true,
    },
  ]
})
export class CartBaseWrapperModule { }
