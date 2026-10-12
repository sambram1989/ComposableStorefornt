import { inject, Injectable, InjectionToken } from '@angular/core';
import { Observable, ReplaySubject } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { switchMap, distinctUntilChanged, map, tap, filter, shareReplay } from 'rxjs/operators';
import { userMap } from '../../ws-env/environment';
import { AuthService, OccEndpointsService, UserIdService } from '@spartacus/core';

@Injectable({
  providedIn: 'root',
})
export class WsCurrentUserService {
  private auth = inject(AuthService);
  private http = inject(HttpClient);
  private occ = inject(OccEndpointsService);
  private userId = inject(UserIdService);

 currentUserData$ = new ReplaySubject<any>(1);

  constructor() {
    this.checkUserStatus();
  }

  private checkUserStatus() {
    this.auth.isUserLoggedIn()
      .pipe(distinctUntilChanged())
      .subscribe((isLoggedIn: boolean) => {
        if (isLoggedIn) {
          this.callCurrentApi();
        } else {
          this.currentUserData$.next(null);
        }
      });
  }

  private callCurrentApi() {
    this.userId.getUserId()
      .pipe(
        switchMap((account: string) =>
          this.http.get(this.occ.buildUrl(`orgUsers/${account}`))
        ),
        tap((res) => this.currentUserData$.next(res)),
        shareReplay(1)
      )
      .subscribe();
  }

  getCurrentUserData(): Observable<any> {
    return this.currentUserData$.asObservable();
  }

  currentState(): Observable<string[]> {
    return this.getCurrentUserData().pipe(
      filter((data) => !!data),
      map((data: any) => {

        const roles = [...(data.roles || [])];
        const userRoles = [];
        for (const role of roles) {
          if (userMap[role]) {
            userRoles.push(userMap[role]);
          }
        }
        return userRoles.length > 0 ? userRoles : ['patient'];
      })
    );
  }

  isRoleExist(role: string): Observable<boolean> {
    return this.currentState().pipe(
      map((data) => data.indexOf(role) > -1)
    );
  }

  redirectUser(): Observable<{ user: string; rootUrl: string }> {
    return this.getCurrentUserData().pipe(
      filter((data) => !!data),
      map((data: any) => {
        const roles = [...data?.roles];
        for (const role of roles) {
          if (userMap[role]) {
            return { user: userMap[role], rootUrl: data.rootUrl };
          }
        }
        return { user: 'patient', rootUrl: data.rootUrl };
      })
    );
  }

callCurrentApiToUpdateState(){
this.callCurrentApi();
}

}


export const currentState = new InjectionToken('UserIdService', {
    providedIn: 'root',
    factory: () => {
        const userId = inject(UserIdService);
        const http = inject(HttpClient);
        const occ = inject(OccEndpointsService);

        return userId.getUserId().pipe(
            switchMap((account: string) =>
                http.get(occ.buildUrl('orgUsers/' + account)).pipe(
                    map((data: any) => {
                      if(data?.roles) {
                          const roles = [...data.roles];
                          for (const role of roles) {
                              if (userMap[role]) {
                                  return userMap[role];
                              }
                          }
                      }
                        return 'patient';
                    }),
                )
            ),
            shareReplay(1)
        );
    },
});

export const redirectUser = new InjectionToken('UserIdService', {
    providedIn: 'root',
    factory: () => {
        const userId = inject(UserIdService);
        const http = inject(HttpClient);
        const occ = inject(OccEndpointsService);

        return userId.getUserId().pipe(
            switchMap((account: string) =>
                http.get(occ.buildUrl('orgUsers/' + account))
            ),
            shareReplay(1)
        );
    },
});



export const getUserData = new InjectionToken('UserIdService', {
    providedIn: 'root',
    factory: () => {
        const userId = inject(UserIdService);
        const http = inject(HttpClient);
        const occ = inject(OccEndpointsService);

        return userId.getUserId().pipe(
            switchMap((account: string) =>
                http.get(occ.buildUrl('orgUsers/' + account))), shareReplay(1)
        );
    }
});


