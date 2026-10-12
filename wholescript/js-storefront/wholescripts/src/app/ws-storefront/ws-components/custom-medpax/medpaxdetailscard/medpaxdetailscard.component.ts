import { Component, DestroyRef, inject, input } from '@angular/core';
import { Location } from '@angular/common';
import { currentState, WsCurrentUserService } from '../../../ws-services/state-services/currentuser.service';
import { ActivatedRoute, Router } from '@angular/router';
import { ProtocolService } from '../../../ws-services/protocol/protocol.service';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { Observable } from 'rxjs';
@Component({
  selector: 'ws-medpaxdetailscard',
  templateUrl: './medpaxdetailscard.component.html',
  styleUrl: './medpaxdetailscard.component.scss'
})
export class MedpaxdetailscardComponent {
  private activatedRoute = inject(ActivatedRoute);
  private router = inject(Router);
  public location = inject(Location);
  currentUser = inject(currentState);
  private protocolService  = inject(ProtocolService);
  curuserService = inject(WsCurrentUserService);
  currentBaseSite = this.protocolService.getBaseUrl();
  private destroyRef = inject(DestroyRef);
  details = input<any>({});
  total = input<any>();
  currentUrlId = '';

  ngOnInit() {
    this.currentUrlId = this.activatedRoute.snapshot.paramMap.get('id');
  }
 goToPrevPage() {
  const currentPath = this.location.path().toLowerCase();

  const routesMap = {
    recommendation: `recommendation/edit/medpax/review/PrivateLabelSelectCarton/${this.currentUrlId}`,
    protocol: `protocol/edit/carton/${this.currentUrlId}`,
    medpax: `medpax/MedPaxSelectCarton/${this.currentUrlId}`,
  };

  const routeKey = currentPath.includes('recommendation') ? 'recommendation' : currentPath.includes('protocol') ? 'protocol': 'medpax';

  this.router.navigate([routesMap[routeKey]]);
}

 checkRoleExist(role: string): Observable<boolean> {
      return this.curuserService.isRoleExist(role).pipe(takeUntilDestroyed(this.destroyRef));
   }

}
