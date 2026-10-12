import { Injectable } from '@angular/core';
import {
  HttpEvent,
  HttpHandler,
  HttpInterceptor,
  HttpRequest,
} from '@angular/common/http';
import { Observable, switchMap, take } from 'rxjs'; // Import `of` to create an observable of the user ID response
import { InterceptorUtil, USE_CLIENT_TOKEN, UserIdService } from '@spartacus/core';
import { UserProfileFacade } from '@spartacus/user/profile/root';
import { ORDER_SIMULATE_CART_PATHS } from '../ws-constants/ws-api-endpoint.constant';

@Injectable({
  providedIn: 'root',
})
export class UserInterceptor implements HttpInterceptor {

  constructor(
    private userIdService: UserIdService,
    private userProfile: UserProfileFacade
  ) {
  }

  intercept(
    req: HttpRequest<any>,
    next: HttpHandler
  ): Observable<HttpEvent<any>> {
    if (req.url?.includes('/paymenttypes')) {
      req = req.clone({
        headers: req.headers.delete('sap-commerce-cloud-user-id'),
      });
    }
    const pathname: string = window?.location?.pathname || '';
    if (req.url.includes('/carts/') && pathname && req.url.includes('/users/') && req.method === 'GET' &&
      ORDER_SIMULATE_CART_PATHS?.some((path) => pathname.indexOf(path) === 0)) {
      let existingParams = req.params;
      existingParams = existingParams
        .append('orderSimulate', false);
      req = req.clone({ params: existingParams });
    }
    return this.userIdService.isEmulated().pipe(
      take(1),
      switchMap((isEmulated) => {
        if (isEmulated) {
          if (
            req?.url?.includes('/users/current/') ||
            req?.url?.includes('/orgUsers/current/')
          ) {
            return this.userIdService.takeUserId().pipe(
              switchMap((userId) => {
                let modifiedUrl = req.url;
                if (modifiedUrl?.includes('/users/current/')) {
                  modifiedUrl = req.url?.replace(
                    '/users/current/',
                    `/users/${userId}/`
                  );
                } else if (modifiedUrl?.includes('/orgUsers/current/')) {
                  modifiedUrl = req.url?.replace(
                    '/orgUsers/current/',
                    `/orgUsers/${userId}/`
                  );
                }
                const modifiedReq = req.clone({
                  url: modifiedUrl,
                });
                return this.appendUserHeader(modifiedReq, next);
              })
            );
          } else {
            return this.appendUserHeader(req, next);
          }
        }

       return this.appendClientTokenHeader(req, next);
      })
    );
  }

  private appendUserHeader(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return this.userProfile.get().pipe(
      take(1), 
      switchMap(user => {
        const sessionUser = JSON.parse(sessionStorage.getItem('user_config') ? sessionStorage.getItem('user_config') : '{}');
        const uid = user?.uid || sessionUser?.uid ;
        const modifiedReq = (uid && uid !== 'anonymous') ? req.clone({
          setHeaders: {
            ...this.getAllHeaders(req),
            'emulated-session-user': user?.uid || sessionUser?.uid
          }
        }) : req;
        return next.handle(modifiedReq);
      })
    );
  }

  private appendClientTokenHeader(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return this.userProfile.get().pipe(
      take(1), 
      switchMap(user => {
        const sessionUser = JSON.parse(sessionStorage.getItem('user_config') ? sessionStorage.getItem('user_config') : '{}');
        const uid = user?.uid || sessionUser?.uid ;
        const anonymousUrl =  req.url.indexOf('anonymous') > -1;
        const modifiedReq = anonymousUrl ? req.clone({
          setHeaders: {
            ...this.getAllHeaders(req),
            'cx-use-client-token': 'true'
          }
        }) : req;
        return next.handle(modifiedReq);
      })
    );
  }

  private getAllHeaders(req: HttpRequest<any>): { [key: string]: string } {
    const headers: { [key: string]: string } = {};
    req.headers.keys().forEach((key) => {
      headers[key] = req.headers.get(key) || '';
    });

    return headers;
  }
}
