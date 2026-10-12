import { ChangeDetectorRef, Component, inject, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { PaymentService } from '../../account-setting/payment-service/payment.service';
import { CmsService } from '@spartacus/core';
import { Observable } from 'rxjs';

@Component({
  selector: 'ws-e-store',
  templateUrl: './ws-e-store.component.html',
  styleUrl: './ws-e-store.component.scss',
})
export class WsEStoreComponent { 
  showAgreement: any;
  loader: boolean = false;
  heading$: Observable<any> = this.cmsservice.getComponentData('EStoreSettingsParaComp');
  card1$: Observable<any> = this.cmsservice.getComponentData('ConnectYourOnlineStoreParaComp');
  card2$: Observable<any> = this.cmsservice.getComponentData('StreamlineYourOnlineStoreParaComp');

  constructor(private router: Router, private paymentService: PaymentService, private cdRef: ChangeDetectorRef, private cmsservice: CmsService) {
    this.loader = true;
    this.getSignedAmendment()
  }
  getSignedAmendment() {
    this.paymentService.getAmendment().subscribe((res: any) => {
      this.showAgreement = res?.success;
      this.loader = false;
      this.cdRef.detectChanges();
    })
  }

  startIntegration() {
    this.router.navigateByUrl('/integration-agreement')
  }
  apiResources() {
    this.router.navigateByUrl('/api')
  }
}
