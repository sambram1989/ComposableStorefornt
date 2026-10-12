import { Injectable } from '@angular/core';
import { HttpEvent, HttpInterceptor, HttpHandler, HttpRequest, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { Router } from '@angular/router';
import { throwError } from 'rxjs';

@Injectable()
export class ErrorInterceptor implements HttpInterceptor {
  
  constructor(private router: Router) {}

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return next.handle(req).pipe(
      catchError((error: HttpErrorResponse) => {
        if(error?.error?.errors?.length > 0) {
            error?.error?.errors?.map((e: any) => {
              const type = e.type;
              if(type === 'WholescriptsPageNotFoundError') {
                this.router.navigate(['/not-found']);
              }
            })
          }
        return throwError(error);
      })
    );
  }
}
