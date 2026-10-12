import { inject, Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { CanActivate } from '@spartacus/storefront/cms-structure/services/guards-composer';
import { WsCurrentUserService } from '../../../ws-services/state-services/currentuser.service';

@Injectable({
  providedIn: 'root',
})
export class ReferralCheckGuard implements CanActivate {
      private currentUser = inject(WsCurrentUserService);
      private router = inject(Router);

  canActivate(): Observable<boolean> {
    return this.currentUser.getCurrentUserData().pipe(
      map((response: any) => {
        if (response?.receiveConsultationFee) {
          this.router.navigate(['/practitioner/patient-direct/referral-code']);
          return false; 
        }
        return true; 
      })
    );
  }
}



// 