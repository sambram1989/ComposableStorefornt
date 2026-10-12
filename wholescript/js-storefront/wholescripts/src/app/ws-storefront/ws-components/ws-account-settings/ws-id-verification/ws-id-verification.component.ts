import { Component, DestroyRef, inject, OnInit, ChangeDetectorRef } from '@angular/core';
import { CmsService, GlobalMessageType,GlobalMessageService } from '@spartacus/core';
import { BsModalRef } from 'ngx-bootstrap/modal';
import Persona from 'persona';
import { selectedEnv, userMap } from '../../../ws-env/environment';
import { RecommendationService } from '../../../ws-services/ws-recommendation.service';
import { PersonaService } from '../../../ws-services/ws-persona.service';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
@Component({
  selector: 'ws-id-verification',
  templateUrl: './ws-id-verification.component.html',
  styleUrl: './ws-id-verification.component.scss',
})
export class WsIdVerificationComponent implements OnInit {
  bsModalRef?: BsModalRef;
  banner: any;
  private client: any;
  private destroyRef = inject(DestroyRef);

  isPractioner = false;
  referenceId = '';
  personaLoading = true;
  perosnaExpirationDate: Date | null = null;
  personaBlockedDate: Date | null = null;
  currentDate = new Date();
  personaStatus = '';
  
  constructor(
    private cmsService: CmsService,
    private recommendationService: RecommendationService,
    private PersonaService: PersonaService,
    private globalMessageService:GlobalMessageService,
    private cdref: ChangeDetectorRef,
  ) {
    this.cmsService
      .getComponentData('IdVerificationBannerComp')
      .subscribe((res) => {
        this.banner = res;
      });
  }

  ngOnInit(): void {
   this.fetchCurrentPersonaStatus();
  }

  fetchCurrentPersonaStatus(): void {
    this.recommendationService
    .getUserDetails()
    .pipe(takeUntilDestroyed(this.destroyRef))
    .subscribe((res: any) => {
      const key = Object.keys(userMap).find(key => userMap[key] === 'practitioner');
      this.isPractioner = (res?.roles || []).includes(key);
      this.referenceId = res?.orgUnit?.uid;
      if (res?.perosnaExpirationDate && !isNaN(Date.parse(res.perosnaExpirationDate))) {
        this.perosnaExpirationDate = new Date(res.perosnaExpirationDate);
      }
      if (res?.personaBlockedDate && !isNaN(Date.parse(res.personaBlockedDate))) {
        this.personaBlockedDate = new Date(res.personaBlockedDate);
      }
      this.personaLoading = false;
      this.cdref.detectChanges();
    });
  }

  onVerifyClick() {
    this.personaLoading = true;
    //I-Frame display for id verification
    this.initializePersonaClient(this.isPractioner, this.referenceId);
  }

  initializePersonaClient(isPractioner: boolean, referenceId: string): void {
    this.client = new Persona.Client({
      templateId: isPractioner
        ? selectedEnv.persona.templateId.practitioner
        : selectedEnv.persona.templateId.patient,
      referenceId: referenceId,
      environmentId: selectedEnv.persona?.environmentId,
      onReady: () => {
        this.personaLoading = false;
        this.client.open();
      },
      onCancel: ({ inquiryId, sessionToken }) =>
        this.handleCancel(inquiryId, sessionToken),
      onError: (error) => this.handleError(error),
      onEvent: (name, metadata) => this.handleEvent(name, metadata),
      onComplete: ({ inquiryId, status, fields }) => {
        this.personaStatus = status;
        this.personaLoading = false;
        if(status !== 'failed') {
          this.handleComplete(inquiryId, status, fields);
        }
        this.cdref.detectChanges();
      }
    });
  }

  handleCancel(inquiryId: string, sessionToken: string): void {
    console.log('onCancel', inquiryId, sessionToken);
  }

  handleError(error: any): void {
    console.log('onError', error);
  }

  handleEvent(name: string, metadata: any): void {
    if (name === 'start') {
      // Collect and save the inquiry ID for future use
      const inquiryId = metadata['inquiryId'];
      console.log('Inquiry started with ID:', inquiryId);
    }
  }

  handleComplete(inquiryId: string, status: string, fields: any): void {
    // Inquiry completed. Optionally tell your server about it.
    console.log(
      `Sending finished inquiry ${inquiryId} to backend with status ${status}`
    );
    this.personaLoading = true;
    this.PersonaService.updateInquiryId(inquiryId, 'current').subscribe(
      (res) => {
        this.personaLoading = false;
        // Optionally, cleanup the client to avoid memory leaks.
        this.client.destroy();
        this.fetchCurrentPersonaStatus();
      },
    () => {
      this.personaLoading = false;
      this.client.destroy();
      this.globalMessageService.add(
        "Internal error occured. Please try again later",
        GlobalMessageType.MSG_TYPE_ERROR
      );
    }
    );
  }

  compareDates(date1: Date, date2 = new Date()): boolean {
    return date1 && !isNaN(Date.parse(date1.toString())) ? date1.getTime() >= date2.getTime() : false;
  }
}
