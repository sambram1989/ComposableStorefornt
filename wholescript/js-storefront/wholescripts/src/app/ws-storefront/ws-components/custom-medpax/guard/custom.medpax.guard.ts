import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { CanActivate } from '@spartacus/storefront/cms-structure/services/guards-composer';

@Injectable({
  providedIn: 'root',
})
export class CustomMedpaxGuard implements CanActivate {
  constructor(private router: Router) {}

  canActivate(): boolean {
    const userConfig = sessionStorage.getItem('user_config');
    let userDetails;

    try {
      userDetails = userConfig ? JSON.parse(userConfig) : null;
    } catch (e) {
      return true;
    }

    if (userDetails?.roles && Array.isArray(userDetails.roles) && userDetails.roles.includes('patientcustomergroup')) {
      this.router.navigate(['/shop']);
      return false;
    }

    return true;
  }
}