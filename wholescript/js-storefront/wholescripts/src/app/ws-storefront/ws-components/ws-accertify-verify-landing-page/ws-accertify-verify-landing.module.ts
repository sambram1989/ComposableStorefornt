import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";
import { WsAccertifyVerifyUserComponent } from "./ws-accertify-verify-landing-page.component";
import { provideConfig } from "@spartacus/core";
import { PageSlotModule } from "@spartacus/storefront";
import { WsAccertifyVerifyLandingService } from "./ws-accertify-verify-landing-page.service";

const modules = {
    cmsComponents: {
        emailverifiedflexComponent: {
            component: () => import('./ws-accertify-verify-landing-page.component').then(m => m.WsAccertifyVerifyUserComponent),
        }
    },
};
@NgModule(
    {
        declarations: [WsAccertifyVerifyUserComponent],
        imports: [
            CommonModule,
            PageSlotModule,
        ],
        providers: [
            WsAccertifyVerifyLandingService,
            provideConfig(modules)
        ]
    })
export class WsAccertifyVerifyUserModule {

}