

import { NgModule } from '@angular/core';
import { CommonModule, NgOptimizedImage } from '@angular/common';

import { I18nModule } from '@spartacus/core';
import { WsSpinnerComponent } from './spinner.component';

@NgModule({
  imports: [CommonModule, I18nModule,NgOptimizedImage],
  declarations: [WsSpinnerComponent],
  exports: [WsSpinnerComponent],
})
export class WsSpinnerModule {}
