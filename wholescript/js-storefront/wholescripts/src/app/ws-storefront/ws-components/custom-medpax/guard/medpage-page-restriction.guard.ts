import { inject } from '@angular/core';
import { Router } from '@angular/router';

export const medpaxPageRestrictionGuard = () => {
  const router = inject(Router);
  const userConfig = JSON.parse(sessionStorage.getItem('user_config') || '{}');
  const hideMedpaxPage = userConfig?.hideMedpaxPage ?? false;
  
  if (hideMedpaxPage) {
    return router.parseUrl('/not-found');
  }

  return true;
};
