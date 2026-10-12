import { Component, inject, signal } from "@angular/core";
import { ActivatedRoute, Router } from "@angular/router";
import { switchMap } from "rxjs";
import { WsAccertifyVerifyLandingService } from "./ws-accertify-verify-landing-page.service";

@Component({
    template: `
         @if(errorMessage()?.error?.errors && errorMessage()?.error?.errors?.length) {
            <h4 class="text-danger p-5 "><span class="bolder">Error:</span>  {{ errorMessage()?.error?.errors[0]?.message | titlecase }}</h4>
        }@else {
            <cx-page-slot position="emailverifiedListTitle"></cx-page-slot>
        }
    `,
})
export class WsAccertifyVerifyUserComponent {
    private service = inject(WsAccertifyVerifyLandingService);
    private activatedRoute = inject(ActivatedRoute);
    private router = inject(Router);

    errorMessage = signal<any>('');

    ngOnInit() {
        this.activatedRoute.url.pipe(
            switchMap((res: any) =>
                this.service.isUserVerified(res.slice(1).map((item: any) => item.path).join('/'))
            )).subscribe({
                next: () => { this.errorMessage.set(null); this.router.navigate(['/']) },
                error: err => this.errorMessage.set(err)
            })
    }
}
