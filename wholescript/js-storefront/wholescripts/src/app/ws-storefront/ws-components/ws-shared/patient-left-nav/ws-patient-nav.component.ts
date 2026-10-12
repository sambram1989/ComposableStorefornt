import { Component, inject, Input } from '@angular/core';
import { LEFT_NAV_PATIENT } from '../../../ws-models/page.model';
import { CmsService } from '@spartacus/core';

@Component({
  selector: 'ws-patient-nav',
  templateUrl: './ws-patient-nav.component.html',
  styleUrl: './ws-patient-nav.component.scss',
})
export class WsPatientLeftNavComponent {
  public cmsService = inject(CmsService);
  @Input()
  pageLinks:any;

  @Input()
  leftNavData:any;

  getActiveClass(getComponentData: any) {
    const currentUrl = window.location.pathname;
    return currentUrl === getComponentData?.urlLink ;
  }
}
