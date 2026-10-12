import { Component, DestroyRef, inject, signal } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { CmsService } from '@spartacus/core';
import { RecommendationService } from '../../../ws-services/ws-recommendation.service';
import { Observable } from 'rxjs';
export type myacctab = {
  headline: string,
  url: string
}
@Component({
  selector: 'ws-patient-my-account-protocol',
  templateUrl: './patient-my-account-protocol.component.html',
  styleUrls: ['./patient-my-account-protocol.component.scss', '../../ws-shared/patient-left-nav/ws-patient-nav.component.scss']
})
export class PatientMyAccountProtocolComponent {

  private destroyRef = inject(DestroyRef);
  private cmsService = inject(CmsService);
  private userservice = inject(RecommendationService);

  cmsData = signal<any>({});
  userData$:Observable<any> = this.userservice.getUserDetails();
  componentData: myacctab[] = [];
  componentListArray:any =[];

  ngOnInit() {
    this.getCmsdata();
  }
  getCmsdata() {
    this.cmsService.getComponentData('patientMyAccountTabPanelContainer')
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe((data: any) => {
        const componentList = data?.components?.split(' ');
        this.componentListArray =componentList;
      });
  }

}
