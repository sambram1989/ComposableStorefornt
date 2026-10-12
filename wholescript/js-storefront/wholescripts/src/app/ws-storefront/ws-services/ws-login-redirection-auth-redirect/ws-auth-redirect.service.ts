import { inject, Injectable } from '@angular/core';
import { AuthRedirectService } from '@spartacus/core';
import { take } from 'rxjs';
import { redirectUser } from '../state-services/currentuser.service';
import { userMap } from '../../ws-env/environment';

@Injectable({ providedIn: 'root' })
export class WsAuthRedirectService extends AuthRedirectService {
  private currentUser = inject(redirectUser);
  override redirect() {
    this.authRedirectStorageService
      .getRedirectUrl()
      .pipe(take(1))
      .subscribe((redirectUrl) => {
        this.currentUser.subscribe((userType: any) => {
          const  role: boolean =  this.getRole(userType);
          if (redirectUrl === undefined) {
            if (role) {
              this.router.navigateByUrl(userType.rootUrl || '/');
            } else {
              this.routing.go('/');
            }
          } else {
            if (role) {
              this.router.navigateByUrl(userType.rootUrl || '/');
            } else {
              this.routing.goByUrl(redirectUrl || '/');
            }
          }
        });

        this.clearRedirectUrl();
      });
  }

  getRole(data: any) {
    if(data?.roles) {
    const roles = [...data?.roles];
    for (const role of roles) {
      if (userMap[role]) {
        return true;
      }
    }
  }
    return false;
  }
}
