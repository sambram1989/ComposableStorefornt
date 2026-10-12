import { inject, Injectable } from "@angular/core";
import { ActivatedRouteSnapshot, Router, RouterStateSnapshot } from "@angular/router";
import { CanActivate } from "@spartacus/storefront/cms-structure/services/guards-composer";
import { Observable, map, catchError, of } from "rxjs";
import { WsCurrentUserService } from "../../../ws-services/state-services/currentuser.service";

@Injectable({ providedIn: 'root' })
export class WsPatientDirectGuard implements CanActivate {
    private router = inject(Router);
    private current = inject(WsCurrentUserService);
    canActivate(
        route: ActivatedRouteSnapshot,
        state: RouterStateSnapshot
    ): Observable<boolean> {

        return this.current.isRoleExist('patientdirect').pipe(
            map((isPlUser: boolean) => {
                if (isPlUser) {
                    return true;
                } else {
                    this.router.navigate(['/practitioner/patient-direct']);
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

@Injectable({ providedIn: 'root' })
export class WsPatientDirectGuardNotFound implements CanActivate {
    private router = inject(Router);
    private current = inject(WsCurrentUserService);
    canActivate(
        route: ActivatedRouteSnapshot,
        state: RouterStateSnapshot
    ): Observable<boolean> {

        return this.current.isRoleExist('patientdirect').pipe(
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

@Injectable({ providedIn: 'root' })
export class WsBlockPatientDirectGuard implements CanActivate {
    private router = inject(Router);
    private current = inject(WsCurrentUserService);
    canActivate(
        route: ActivatedRouteSnapshot,
        state: RouterStateSnapshot
    ): Observable<boolean> {
        return this.current.isRoleExist('patientdirect').pipe(
            map((isNonPlUser: any) => {
                console.debug('isplUser',isNonPlUser);
                if (isNonPlUser) {
                    return false;
                } else {
                    return true;
                }
            }),
            catchError((error) => {
                this.router.navigate(['']);
                return of(false);
            })
        );
    }
}
