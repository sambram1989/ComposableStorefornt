import { Component, OnInit } from '@angular/core';
import { CmsService } from '@spartacus/core';
@Component({
  selector: 'ws-as-notifications',
  templateUrl: './ws-as-notifications.component.html',
  styleUrl: './ws-as-notifications.component.scss',
})
export class WsAsNotificationsComponent implements OnInit {
  patientAccount = window.location.href.includes('patient') ?  'PatientNotificationsParaComp' : 'PractitionerNotificationsParaComp';
  heading: any;
  constructor(protected cmsService: CmsService) {}

  ngOnInit(): void {
    this.getPageInfo();
  }

  getPageInfo() {
    this.cmsService.getComponentData(this.patientAccount).subscribe((res) => {
      this.heading = res;
    })
  }
}
