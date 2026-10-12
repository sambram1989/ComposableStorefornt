import { Injectable } from "@angular/core";
import { BehaviorSubject, Observable } from "rxjs";

@Injectable({
  providedIn: 'root',
})
export class WsAuthRedirectStorageService {
    constructor() {
        // Intentional empty constructor
      }
    
      private redirectUrl$: Observable<string | undefined> = new BehaviorSubject<
        string | undefined
      >(undefined);
    
   
      getRedirectUrl(): Observable<string | undefined> {
        return this.redirectUrl$;
      }
   
      setRedirectUrl(redirectUrl?: string): void {
        const forbiddenPaths = ['passwordrecovery', 'login', 'reset-password', 'register'];
        if (redirectUrl && forbiddenPaths.some(path => redirectUrl.includes(path))) {
          redirectUrl = '/';
        }
        (this.redirectUrl$ as BehaviorSubject<string | undefined>).next(redirectUrl);
      }
      
    }
