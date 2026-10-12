import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AuthService, OccEndpointsService } from '@spartacus/core';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class WsPatientLoginService {
  isLoggedIn: boolean = false;
  constructor(
    private httpClient: HttpClient,
    private occEndPointService: OccEndpointsService,
    private loginService: AuthService
  ) {
    this.loginService.isUserLoggedIn().subscribe((res) => this.isLoggedIn = res)
  }
  fetchPatientNewRecommendation(): Observable<any> {
    return this.httpClient.get(
      this.occEndPointService.buildUrl('users/current/newRecommendation?fields=FULL')
    );
  }
}
