import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";
import { TitleBodyContentComponent } from "./title-body-content.component";

@NgModule({
    declarations:[TitleBodyContentComponent],
    imports:[CommonModule],
    exports:[TitleBodyContentComponent]
})
export class WsTitleContentModule{}