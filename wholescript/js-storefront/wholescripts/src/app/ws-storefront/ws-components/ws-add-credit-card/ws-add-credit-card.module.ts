import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { WsOrdersModule } from '../ws-order/ws-orders.module';
import { AddNewCreditcardComponent } from './add-new-creditcard/add-new-creditcard.component';
import { AddNewAddressComponent } from './add-new-address/add-new-address.component';
import { ReactiveFormsModule } from '@angular/forms';
import { WsSharedModule } from '../ws-shared/ws-shared.module';
import { I18nModule } from '@spartacus/core';
import { AddExistingAddressComponent } from './add-existing-address/add-existing-address.component';
import { AddressCardSelectorComponent } from './address-card-selector/address-card-selector.component';
import { WsCheckoutDeliveryAddressModule } from '../ws-checkout-delivery-address/ws-checkout-delivery-address.module';
import { WsSpinnerModule } from '../ws-spartacus/spinner/ws-spinner.module';

@NgModule({
  declarations: [
    AddNewCreditcardComponent,
    AddNewAddressComponent,
    AddExistingAddressComponent,
    AddressCardSelectorComponent,
  ],
  imports: [
    CommonModule,
    WsOrdersModule,
    ReactiveFormsModule,
    WsSharedModule,
    I18nModule,
    WsSpinnerModule,
    WsCheckoutDeliveryAddressModule
  ],
  exports: [AddNewCreditcardComponent],
})
export class WsAddCreditCardModule {}
