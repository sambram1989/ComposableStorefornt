import { NgModule } from '@angular/core';
import { CommonModule, NgOptimizedImage } from '@angular/common';
import {
    BgColorDirective,
    FontSizeDirective,
    BorderColorDirective,
    FontColorDirective,
    BorderRadiusDirective,
    BoxShadowOnFocusDirective
} from '../../ws-directives/ws-button.directive';
import { LeftfilterfacetComponent } from './leftfilterfacet/leftfilterfacet.component';
import { WsSearchBoxComponent } from './search-box/search-box.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { CdkAccordionModule } from '@angular/cdk/accordion';
import { PaginationComponent } from './pagination/pagination.component';
import { DisableLinkDirective, HighlightCheckboxDirective, MinLengthCheckDirective } from '../../ws-directives/ws-checkbox.directive';
import { FormcontrolInputComponent } from './formcontrol-input/formcontrol-input.component';
import { FloatingFooterComponent } from './floating-footer/floating-footer.component';
import { NgSelectModule } from '@ng-select/ng-select';
import { NgxMaskDirective, NgxMaskPipe, provideEnvironmentNgxMask } from 'ngx-mask';
import { PaginationModule } from 'ngx-bootstrap/pagination';
import { ParseUrlPipe } from '../../ws-pipes/wsParseUrl.pipe';
import {AccordionModule} from 'ngx-bootstrap/accordion';
import { SearchPipe } from '../../ws-pipes/search.pipe';
import { WsPatientLeftNavComponent } from './patient-left-nav/ws-patient-nav.component';
import { RouterModule } from '@angular/router';
import { PaginationEntryListComponent } from './pagination-entry-list/pagination-entry-list.component';
import { SortingComponent } from './sorting/sorting.component';
import { EditDosingdetailspopupComponent } from './edit-dosingdetailspopup/edit-dosingdetailspopup.component';
import { MediaModule } from '@spartacus/storefront';
import { DosingPipe, SumQuantityPipe } from '../../ws-pipes/dosing.pipe';
import { AddOrRemovePipe } from '../../ws-pipes/addOrRemove.pipe';
import { IsFavoritePipe } from '../../ws-pipes/isFavorite.pipe';
import { ThirtyDaySupplyPipe } from '../../ws-pipes/thirtyDaySupply.pipe';
import { GetDosingOnEntryLevelPipe } from '../../ws-pipes/getCustomDosingOnEntryLevel.pipe';
import { CapitalCasePipe } from '../../ws-pipes/capitalCase.pipe';
import { CommonMedpaxCardComponent } from './common-medpax-card/common-medpax-card.component';
import { CalculateQuantityPipe } from '../../ws-pipes/calcQuantity.pipe';
import { IsExpiredPipe } from '../../ws-pipes/ws-is-expired.pipe';
import { SafePipe } from '../../ws-pipes/safe.pipe';
import { WsToggleConfig } from '../protocal/toggle-switch/toggle-config';
import { ToggleSwitchComponent } from '../protocal/toggle-switch/toggle-switch.component';
import { RouterLinkifyDirective } from '../../ws-directives/ws-router-linkify.directive';
import { WsProductStatusComponent } from './ws-product-status/ws-product-status.component';
import { SortFavoritesPipe } from '../../ws-pipes/sort-favorites.pipe';
import { WsPromoDetailsModalComponent } from './ws-promo-details-modal/ws-promo-details-modal.component';
import { AlertModule } from 'ngx-bootstrap/alert';
import { WsOrderTotalCalculatingErrorComponent } from './ws-order-total-calculating-error/ws-order-total-calculating-error.component';
import { KeyboardNavigationDirective } from '../../ws-directives/ws-keyboard-navigation.directive';
import { WsGuestPromoDetailsModalComponent } from './ws-guest-promo-details-modal/ws-guest-promo-details-modal.component';
import { WsPromotionsCarouselComponent } from './ws-promotions-carousel/ws-promotions-carousel.component';
import { SlickCarouselModule } from 'ngx-slick-carousel';

const directives = [
    BgColorDirective,
    FontSizeDirective,
    BorderColorDirective,
    FontColorDirective,
    BorderRadiusDirective,
    BoxShadowOnFocusDirective,
    HighlightCheckboxDirective,
    MinLengthCheckDirective,
    DisableLinkDirective,
    RouterLinkifyDirective,
    KeyboardNavigationDirective,
]
const pipes = [
    ParseUrlPipe,
    SearchPipe,
    DosingPipe,
    AddOrRemovePipe,
    IsFavoritePipe,
    ThirtyDaySupplyPipe,
    GetDosingOnEntryLevelPipe,
    CapitalCasePipe,
    CalculateQuantityPipe,
    IsExpiredPipe,
    SafePipe,
    SortFavoritesPipe,
    SumQuantityPipe
]
const Components = [
    LeftfilterfacetComponent,
    WsSearchBoxComponent,
    PaginationComponent,
    FormcontrolInputComponent,
    FloatingFooterComponent,
    WsPatientLeftNavComponent,
    PaginationEntryListComponent,
    SortingComponent,
    EditDosingdetailspopupComponent,
    CommonMedpaxCardComponent,
    ToggleSwitchComponent,
    WsProductStatusComponent,
    WsPromoDetailsModalComponent,
    WsOrderTotalCalculatingErrorComponent,
    WsGuestPromoDetailsModalComponent,
    WsPromotionsCarouselComponent
]
const NgModules = [
    CommonModule,
    NgOptimizedImage,
    FormsModule,
    CdkAccordionModule,
    ReactiveFormsModule,
    NgSelectModule,
    NgxMaskDirective,
    NgxMaskPipe,
    PaginationModule,
    AccordionModule,
    RouterModule,
    MediaModule,
    AlertModule,
    SlickCarouselModule
]
@NgModule({
    declarations: [...directives, ...Components,...pipes ],
    imports: [...NgModules],
    providers: [  WsToggleConfig, provideEnvironmentNgxMask()],
    exports: [...directives, ...Components,...pipes],
})
export class WsSharedModule { }
