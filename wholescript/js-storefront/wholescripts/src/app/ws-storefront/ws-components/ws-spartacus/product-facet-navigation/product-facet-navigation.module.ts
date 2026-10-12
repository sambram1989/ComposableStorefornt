import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";
import { I18nModule } from "@spartacus/core";
import { IconModule } from "@spartacus/storefront";
import { WsProductFacetNavigationComponent } from "./product-facet-navigation.component";
import { WsActiveFacetsModule } from "./active-facets/active-facets.module";
import { WsFacetListModule } from "./facet-list/facet-list.module";

@NgModule({
  imports: [
    CommonModule,
    WsFacetListModule,
    WsActiveFacetsModule,
    IconModule,
    I18nModule,
  ],
  declarations: [WsProductFacetNavigationComponent],
  exports: [WsProductFacetNavigationComponent],
})
export class WsProductFacetNavigationModule {}
