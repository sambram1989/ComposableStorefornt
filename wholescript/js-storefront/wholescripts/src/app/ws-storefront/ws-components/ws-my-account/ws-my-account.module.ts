import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";
import { provideConfig } from "@spartacus/core";
import { WsMyAccountDashboardComponent } from "./ws-my-account-dashboard/ws-my-account-dashboard.component";
import { PageSlotModule } from "@spartacus/storefront";

@NgModule({
    declarations:[WsMyAccountDashboardComponent],
    imports:[CommonModule, PageSlotModule],
    providers:[provideConfig({cmsComponents:{
        PractitionerTotalBalanceFlexComponent:{component:WsMyAccountDashboardComponent}
    }})]
})
export class WsMyAccountModule{} 