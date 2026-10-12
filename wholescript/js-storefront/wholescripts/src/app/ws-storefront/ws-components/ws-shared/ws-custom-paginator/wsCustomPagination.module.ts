import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { WsCustomPaginationComponent } from './ws-custom-pagination.component';
import { FormsModule } from '@angular/forms';


@NgModule({
  imports: [CommonModule,FormsModule],
  declarations: [WsCustomPaginationComponent],
  exports: [ WsCustomPaginationComponent]
})
export class WsCustomPaginationModule {
}
