import { ChangeDetectorRef, Component } from '@angular/core';
import { CmsBannerComponent, Image } from '@spartacus/core';
import { PATIENT_SLOT, PRACTITIONER_SLOT, PRIMARY_SLOT, slotMarkup } from '../../../ws-models/page.model';
import { Observable } from 'rxjs/internal/Observable';
import { WsPage } from '../../../ws-services/ws-page.service';


@Component({
  selector: 'ws-homepage-intro-slot',
  templateUrl: './homepage-intro-slot.component.html',
  styleUrl: './homepage-intro-slot.component.scss'
})
export class HomePageIntroComponent {

  registerContentSlot = new Observable<slotMarkup>;
  practitionerTab: { [k: string]: any } = {};
  patientTab: { [k: string]: any } = {};

  constructor(
    private wsPageService: WsPage,
    private cdr: ChangeDetectorRef
  ) {
    this.registerContentSlot = this.wsPageService.getPage(PRIMARY_SLOT, 'PatientPractitionerSlot');
    this.registerSectionDetailsRefresh();
  }

  registerSectionDetailsRefresh() {
    this.registerContentSlot.subscribe((response) => {
      response.components.forEach((element: any) => {
        if (PRACTITIONER_SLOT.includes(element.uid)) {
          this.practitionerTab[element.uid] = element;
        }
        if (PATIENT_SLOT.includes(element.uid)) {
          this.patientTab[element.uid] = element;
        }
      });
      this.cdr.markForCheck();
    })
  }

  getImage(data: CmsBannerComponent): Image  {
        return data.media as Image;
  }
}