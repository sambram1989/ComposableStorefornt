import { ChangeDetectorRef, Component } from '@angular/core';
import { CmsService } from '@spartacus/core';

@Component({
  selector: 'ws-as-change-password',
  templateUrl: './ws-as-change-password.component.html',
  styleUrl: './ws-as-change-password.component.scss',
})
export class WsAsChangePasswordComponent {
  patientAccount = window.location.href.includes('patient')
    ? 'PatientChangePasswordParaComp'
    : 'PractitionerChangePasswordParaComp';
  heading: any;

  constructor(public cmsService: CmsService, private cdRef: ChangeDetectorRef) {
    this.getPageInfo();
  }

  getPageInfo() {
    this.cmsService.getComponentData(this.patientAccount).subscribe((res) => {
      this.heading = res;
    });
  }
}
