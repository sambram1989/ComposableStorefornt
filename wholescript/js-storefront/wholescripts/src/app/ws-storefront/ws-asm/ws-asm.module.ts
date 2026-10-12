import { NgModule } from '@angular/core';

import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { NgSelectModule } from '@ng-select/ng-select';
import { FormErrorsModule, IconModule, KeyboardFocusModule, MessageComponentModule, NgSelectA11yModule, PaginationModule, PasswordVisibilityToggleModule, SortingModule, SpinnerModule } from '@spartacus/storefront';
import { ConfigModule, I18nModule } from "@spartacus/core";
import { WsDotSpinnerComponent } from './dot-spinner/ws-asm-dot-spinner.component';
import { WsAsmCustomerSelectionComponent } from './ws-asm-user-selection/ws-asm-user-selection.component';
import { WsAsmMainUiComponent } from './ws-asm-main-ui.component';
import { AsmComponentsModule } from '@spartacus/asm/components';
import { WsAsmLayoutConfig } from './ws-asm-layout.config';

@NgModule({
  imports: [
    CommonModule,
    ReactiveFormsModule,
    I18nModule,
    FormErrorsModule,
    IconModule,
    NgSelectModule,
    FormsModule,
    SpinnerModule,
    PasswordVisibilityToggleModule,
    KeyboardFocusModule,
    NgSelectA11yModule,
    SortingModule,
    PaginationModule,
    MessageComponentModule,
    AsmComponentsModule,
    ConfigModule.withConfig(WsAsmLayoutConfig)
  ],
  declarations: [WsDotSpinnerComponent, WsAsmCustomerSelectionComponent, WsAsmMainUiComponent],
  exports: [WsDotSpinnerComponent, WsAsmCustomerSelectionComponent, WsAsmMainUiComponent]
})
export class WsAsmModule {}
