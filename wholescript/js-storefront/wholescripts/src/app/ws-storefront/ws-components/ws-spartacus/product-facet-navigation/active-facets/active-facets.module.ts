import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";
import { RouterModule } from "@angular/router";
import { I18nModule } from "@spartacus/core";
import { IconModule, KeyboardFocusModule } from "@spartacus/storefront";
import { WsActiveFacetsComponent } from "./active-facets.component";


@NgModule({
  imports: [
    CommonModule,
    RouterModule,
    I18nModule,
    IconModule,
    KeyboardFocusModule,
  ],
  declarations: [WsActiveFacetsComponent],
  exports: [WsActiveFacetsComponent],
})
export class WsActiveFacetsModule {}
