
import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";
import { RouterModule } from "@angular/router";
import { I18nModule, UrlModule } from "@spartacus/core";
import { AtMessageModule, IconModule, KeyboardFocusModule } from "@spartacus/storefront";
import { CustomMedpaxBuyAgainFacetComponent } from "./custom-medpax-buy-again-facet.component";

@NgModule({
  imports: [
    AtMessageModule,
    CommonModule,
    I18nModule,
    IconModule,
    KeyboardFocusModule,
    RouterModule,
    UrlModule,
  ],
  declarations: [CustomMedpaxBuyAgainFacetComponent],
  exports: [CustomMedpaxBuyAgainFacetComponent],
})
export class CustomMedpaxBuyAgainFacetModule { }
