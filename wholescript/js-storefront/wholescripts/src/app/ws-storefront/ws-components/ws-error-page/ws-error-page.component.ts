import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '@spartacus/core';
import { shareReplay } from 'rxjs';
import { userMap } from '../../ws-env/environment';

@Component({
  selector: 'ws-error-page',
  templateUrl: './ws-error-page.component.html',
  styleUrl: './ws-error-page.component.scss',
})
export class WsErrorPageComponent {
  isLoggedIn: boolean = false;
  user: any = {};
  userRole = '';
  constructor(private authService: AuthService, private router: Router) {
    this.authService
      .isUserLoggedIn()
      .pipe(shareReplay(1))
      .subscribe((response) => {
        this.user = JSON.parse(sessionStorage.getItem('user_config'));
        const userRoles = this.getUserRole(this.user?.roles || []);
        this.userRole = userRoles.includes('practitioner')
        ? 'practitioner'
        : 'patient';
        this.isLoggedIn = response;
      });
  }

  redirectToHome() {
    if (this.user && this.user.rootUrl) {
      this.router.navigate([this.user.rootUrl]);
    } else {
      this.router.navigate(['/']);
    }
  }

  private getUserRole(roles: string[]): string {
    let currentRoles = '';
    for (const role of roles) {
      if (userMap[role]) {
        currentRoles += userMap[role];
      }
    }
    return currentRoles;
  }
}
