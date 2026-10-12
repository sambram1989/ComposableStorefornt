import { Injectable } from '@angular/core';
import { Location } from '@angular/common';
import { ASM_ENABLED_LOCAL_STORAGE_KEY, AsmAuthStorageService } from '@spartacus/asm/root';
import {
  AuthRedirectService,
  AuthStorageService,
  OCC_USER_ID_ANONYMOUS,
  UserIdService,
  WindowRef,
} from '@spartacus/core';

@Injectable({
  providedIn: 'root',
})
export class AsmAutoLoginService {
  constructor(
    protected location: Location,
    protected winRef: WindowRef,
    protected authStorageService: AuthStorageService,
    protected userIdService: UserIdService,
    protected authRedirectService: AuthRedirectService,
    protected asmAuthStorageService: AsmAuthStorageService
  ) {}

  isEmulateInURL(): boolean {
    return this.location.path().indexOf('assisted-service/emulate?') > 0;
  }

  autoLoginAsmAgent(): void {
    if (this.isEmulateInURL()) {
      localStorage.clear();
      const param = new URLSearchParams(window.location.search);
      const token = param.get('token');
      const decodedToken = this.decodeJWT(token);
     
      this.asmAuthStorageService.switchTokenTargetToCSAgent();

      this.setTokenData({
        ...decodedToken.payload,
        granted_scopes: ['basic', 'openid'],
      });

      // OCC specific code
      this.userIdService.setUserId(OCC_USER_ID_ANONYMOUS);
      if (this.winRef.localStorage) {
        this.winRef.localStorage.setItem(ASM_ENABLED_LOCAL_STORAGE_KEY, 'true');
      }
      this.authRedirectService.redirect();
    }
  }

  protected setTokenData(token: any): void {
    this.authStorageService.setItem('access_token', token.access_token);

    if (token.granted_scopes && Array.isArray(token.granted_scopes)) {
      this.authStorageService.setItem(
        'granted_scopes',
        JSON.stringify(token.granted_scopes)
      );
    }

    this.authStorageService.setItem('access_token_stored_at', '' + Date.now());

    if (token.exp) {
      const expiresInMilliseconds = token.exp * 1000;
      this.authStorageService.setItem('expires_at', '' + expiresInMilliseconds);
    }

    if (token.refresh_token) {
      this.authStorageService.setItem('refresh_token', token.refresh_token);
    }
  }

  protected base64UrlDecode(str: string) {
    // Convert base64url to base64
    const base64 = str.replace(/-/g, '+').replace(/_/g, '/');
    // Decode Base64
    return decodeURIComponent(escape(atob(base64)));
  }

  protected decodeJWT(token: string) {
    const [header, payload] = token.split('.'); // JWT: header, payload, signature
    return {
      header: JSON.parse(this.base64UrlDecode(header)),
      payload: JSON.parse(this.base64UrlDecode(payload)),
    };
  }

}
