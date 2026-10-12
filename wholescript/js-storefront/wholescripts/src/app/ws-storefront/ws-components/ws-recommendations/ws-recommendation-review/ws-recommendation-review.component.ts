import {
  AfterViewInit,
  ChangeDetectorRef,
  Component,
  computed,
  DestroyRef,
  inject,
  OnInit,
  signal,
  TemplateRef,
  ViewChild,
  ViewContainerRef,
} from '@angular/core';
import { ActivatedRoute, Router} from '@angular/router';
import { CmsService } from '@spartacus/core';
import {
  Observable,
  shareReplay,
  switchMap,
  tap,
} from 'rxjs';
import { RecommendationService } from '../../../ws-services/ws-recommendation.service';
import { TemplatePortal } from '@angular/cdk/portal';
import { WsOverlay } from '../../../ws-services/ws-overlay.service';
import { ProtocolService } from '../../../ws-services/protocol/protocol.service';
import { WsCustomMedPaxService } from '../../custom-medpax/medpaxService/customMedpaxservice.service';
import { weekMap } from '../../ws-shared/edit-dosingdetailspopup/editDosingConstants';
import { FormControl } from '@angular/forms';
import { WsCurrentUserService } from '../../../ws-services/state-services/currentuser.service';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { PatientProfileService } from '../../../ws-services/ws-patient-profile.service';

@Component({
  selector: 'ws-ws-recommendation-review',
  templateUrl: './ws-recommendation-review.component.html',
  styleUrl: './ws-recommendation-review.component.scss',
})
export class WsRecommendationReviewComponent implements OnInit, AfterViewInit {
  private cmsService = inject(CmsService);
  private overLayService = inject(WsOverlay);
  private _viewContainerRef = inject(ViewContainerRef);
  private protocolService = inject(ProtocolService);
  private customMedpaxService = inject(WsCustomMedPaxService);
  private patientProfileService = inject(PatientProfileService);
  private currentUser = inject(WsCurrentUserService);
  private destroyRef = inject(DestroyRef);

  weekMapItems = weekMap;
  isMedpax = computed(()=> this.protocolService.isMedPaxPage());
  isMedPaxId = computed(() => this.customMedpaxService.localCurrentmedPaxId());

  reviewId: string;
  recoId$: Observable<string>;
  recommendations$: Observable<any>;
  public privacyPolicyChecked = false;
  patientName = '';
  notes: string;
  itemDurationDefault: string = 'Ongoing';
  recommendationId: string = '';

  @ViewChild('sendRecommendationPoup')
  sendRecommendationPoup: TemplateRef<unknown>;
  templatePortal: TemplatePortal<any>;

  reviewBody$: Observable<any> = this.cmsService.getComponentData(
    'RecommendationReviewBodyComponent'
  );

  smsControl = new FormControl(false);
  emailControl = new FormControl(false);

  userRoles: string[] = [];
  constructor(
    private activatedRoute: ActivatedRoute,
    private router: Router,
    private recommendationService: RecommendationService,
    private cd: ChangeDetectorRef
  ) {
    this.activatedRoute.paramMap
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe((params: any) => {
        this.recommendationId = params.get('id');
      });
    }

  ngOnInit(): void {
    this.getCurrentUserRoles();
    this.recommendations$ = this.activatedRoute.paramMap.pipe(
      switchMap((params) => {
        this.reviewId = params.get("id");
        return this.recommendationService
          .fetchRecommendationsForReview(this.reviewId)
          .pipe(
            tap((res: any) => {
              this.disableEmailAndSmsToggle(res?.patientUID);
            })
          );
      }),
      shareReplay({ bufferSize: 1, refCount: true })
    );

    this.recommendationService?.getRecommendation(this.recommendationId).subscribe(res => {
      this.patientName = res?.patient;
      this.notes = res?.notes;
    });
    
    // this.patientName = localStorage.getItem('patient');
    // TODO: AM_WITH_FOOD - get string from below API
    // this.recommendationService.fetchItemsForReview(this.reviewId).subscribe(res=> console.log(res));
  }

  getCurrentUserRoles() {
    this.currentUser.currentState().pipe(takeUntilDestroyed(this.destroyRef)).subscribe((data:any)=>{
      this.userRoles = data || [];
      this.cd.markForCheck();
    })
  }


  disableEmailAndSmsToggle(email: string) {
    this.patientProfileService.fetchPatientProfileDetails(email)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: (res: any) => {
          if (!res?.isEmailNotificationEnabled) {
            this.emailControl.disable();
          } else {
            this.emailControl.enable();
          }
  
          if (!res?.isSmsNotificationEnabled) {
            this.smsControl.disable();
          } else {
            this.smsControl.enable();
          }
          this.cd.detectChanges()
        }
      });
  }

  ngAfterViewInit() {
    this.templatePortal = new TemplatePortal(
      this.sendRecommendationPoup,
      this._viewContainerRef
    );
  }

  closeFilterModal() {
    this.overLayService.closeAllModals();
  }

  sendeRcommendation() {
    if(this.userRoles?.indexOf("patientdirect") > -1) {
      this.overLayService.openModal(this.templatePortal, '500px');
    } else {
      this.router.navigate(['/practitioner/patient-direct']);
    }
  }

  navigateToCompleteReview() {
    this.recommendations$
      .subscribe((data) => {
        const payload = {
          patient: data.patient,
          notes: this.notes,
        };
        this.recommendationService
          .shareRecommendations(this.reviewId, payload,this.smsControl.value,this.emailControl.value)
          .subscribe(() => {
            this.router.navigate([
              '/Recommendation/RecommendationComplete/', this.reviewId
            ]);
          });
      })
  }
  
handleKeypress(event: KeyboardEvent): void {
  this.privacyPolicyChecked = !this.privacyPolicyChecked;
}
}
