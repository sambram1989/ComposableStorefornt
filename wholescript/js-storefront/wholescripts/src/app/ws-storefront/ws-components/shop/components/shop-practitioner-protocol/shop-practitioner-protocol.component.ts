import { Component, inject } from '@angular/core';
import { Router } from '@angular/router';
import { PatientProtocolListService } from '../../../patient-my-account/service/patient-protocol-listing.service';
import { HttpParams } from '@angular/common/http';
import { map } from 'rxjs';

@Component({
  selector: 'ws-shop-practitioner-protocol',
  templateUrl: './shop-practitioner-protocol.component.html',
  styleUrl: './shop-practitioner-protocol.component.scss'
})
export class ShopPractitionerProtocolComponent {
  private router = inject(Router);
  private protocolService = inject(PatientProtocolListService);

   protocolCount$ = this.protocolService.getProtocolList(new HttpParams().set('pageSize', 1)).pipe(
    map((data:any) => data?.protocolsListWsDto?.length || 0)
  );
  goToProtocol() {
    this.router.navigate([`/customer/protocol`]);
  }
}