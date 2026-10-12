import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AccSettingPaymentComponent } from './acc-setting-payment/acc-setting-payment.component';
import { CardParentComponent } from './card-parent/card-parent.component';
import { PractititionerPaymentsTabComponent } from './practititioner-payments-tab/practititioner-payments-tab.component';
import { RouterModule } from '@angular/router';
import { WsSharedModule } from '../ws-shared/ws-shared.module';
import { WsAddCreditCardModule } from '../ws-add-credit-card/ws-add-credit-card.module';
import { WsIntegrationsHubModule } from '../ws-integrations-hub/ws-integrations-hub.module';
import { WsSpinnerModule } from '../ws-spartacus/spinner/ws-spinner.module';

@NgModule({
  declarations: [
    CardParentComponent,
    AccSettingPaymentComponent,
    PractititionerPaymentsTabComponent
  ],
  imports: [
    CommonModule,
    RouterModule,
    WsSharedModule,
    WsSpinnerModule,
    WsAddCreditCardModule,
    WsIntegrationsHubModule,

  ],
  exports:[AccSettingPaymentComponent,
    PractititionerPaymentsTabComponent]
})
export class WsPaymentSettingModule { }
