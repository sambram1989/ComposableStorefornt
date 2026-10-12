import { NgModule } from "@angular/core";
import { WsRecommendationStickyComponent } from "./ws-recommendation-sticky/ws-recommendation-sticky.component";
import { CommonModule } from "@angular/common";

@NgModule({
    declarations:[WsRecommendationStickyComponent],
    imports:[CommonModule],
    exports:[WsRecommendationStickyComponent]
})
export class WsRecommendationSticky{}