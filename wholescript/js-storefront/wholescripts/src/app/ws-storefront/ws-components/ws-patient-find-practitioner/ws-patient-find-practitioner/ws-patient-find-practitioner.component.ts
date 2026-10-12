import { Component } from '@angular/core';
import { CmsService } from '@spartacus/core';
import { Observable } from 'rxjs';

@Component({
  selector: 'ws-patient-find-practitioner',
  templateUrl: './ws-patient-find-practitioner.component.html',
  styleUrl: './ws-patient-find-practitioner.component.scss'
})
export class WsPatientFindPractitionerComponent {

  healthCareParagraph1$: Observable<any> = this.cmsService.getComponentData('WholeHealthCareParagraphHeading');
  healthCareParagraph2$: Observable<any> = this.cmsService.getComponentData('WholeHealthCareParagrap2Content');
  healthCareFindPractitioner$: Observable<any> = this.cmsService.getComponentData('FindPractitionerForPatientRegisterLink');
  healthCareLatestRecommendation$: Observable<any> = this.cmsService.getComponentData('LatestRecommendationComponent');
  healthCarePractionerProtocol$: Observable<any> = this.cmsService.getComponentData('PractitionerProtocolsComponent');
  healthCareIphoneMock$: Observable<any> = this.cmsService.getComponentData('IphoneMockComponent');
  healthCareLatestRecommendationLink$: Observable<any> = this.cmsService.getComponentData('LatestRecommendationLink');
  healthCarePractitionerProtocolsLink$: Observable<any> = this.cmsService.getComponentData('PractitionerProtocolsLink');

  constructor(public cmsService: CmsService){
  }
}
