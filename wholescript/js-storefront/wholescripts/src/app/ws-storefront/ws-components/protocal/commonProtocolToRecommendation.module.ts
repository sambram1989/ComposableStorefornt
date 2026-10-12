import { NgModule } from '@angular/core';
import { MedPaxProductsRightSectionComponent } from './med-pax-products-right-section/med-pax-products-right-section.component';
import { MedpaxProductsTableComponent } from './medpax-products-table/medpax-products-table.component';
import { MedpaxProtocolReviewCardComponent } from './medpax-protocol-review-card/medpax-protocol-review-card.component';
import { ViewSupplementFactsPopupComponent } from './view-supplement-facts-popup/view-supplement-facts-popup.component';
import { CommonModule } from '@angular/common';
import { CustomMedpaxModule } from '../custom-medpax/custom-medpax.module';
import { WsSharedModule } from '../ws-shared/ws-shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { ProtocolMedpaxRightTable } from './ws-protocol-right-medpax/wsRightProtocolMedpax.component';
import { WsSpinnerModule } from '../ws-spartacus/spinner/ws-spinner.module';
import { AlertModule } from 'ngx-bootstrap/alert';
import { MediaModule } from '@spartacus/storefront';

@NgModule({
  imports: [
    CommonModule,
    CustomMedpaxModule,
    WsSharedModule,
    FormsModule,
    ReactiveFormsModule,
    WsSpinnerModule,
    AlertModule,
    MediaModule
  ],
  exports: [
    MedpaxProductsTableComponent,
    MedPaxProductsRightSectionComponent,
    ViewSupplementFactsPopupComponent,
    MedpaxProtocolReviewCardComponent,
    ProtocolMedpaxRightTable,
  ],
  declarations: [
    MedpaxProductsTableComponent,
    MedPaxProductsRightSectionComponent,
    ViewSupplementFactsPopupComponent,
    MedpaxProtocolReviewCardComponent,
    ProtocolMedpaxRightTable,
  ],
})
export class ProtocolRecommendationCommonModule {}
