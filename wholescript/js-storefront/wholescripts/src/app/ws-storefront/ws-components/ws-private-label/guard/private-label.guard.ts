import { inject, Injectable } from "@angular/core";
import { ActivatedRouteSnapshot, Router, RouterStateSnapshot } from "@angular/router";
import { CanActivate } from "@spartacus/storefront/cms-structure/services/guards-composer";
import { Observable, map, catchError, of } from "rxjs";
import { WsCurrentUserService } from "../../../ws-services/state-services/currentuser.service";

@Injectable({ providedIn: 'root' })
export class WsPrivateLabelGuard implements CanActivate {
    private router = inject(Router);
    private current = inject(WsCurrentUserService);
    canActivate(
        route: ActivatedRouteSnapshot,
        state: RouterStateSnapshot
    ): Observable<boolean> {

        return this.current.isRoleExist('pluser').pipe(
            map((isPlUser: boolean) => {
                if (isPlUser) {
                    return true;
                } else {
                    this.router.navigate(['/not-found']);
                    return false;
                }
            }),
            catchError((error) => {
                this.router.navigateByUrl('/');
                return of(false);
            })
        );
    }
}