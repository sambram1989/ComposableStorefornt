import { ChangeDetectorRef, Component, inject } from '@angular/core';
import { CmsService, CurrencyService, GlobalMessageService, GlobalMessageType } from '@spartacus/core';
import { Observable, Subject, takeUntil } from 'rxjs';
import { PatientProfileService } from '../../ws-services/ws-patient-profile.service';
import { ActivatedRoute, Router } from '@angular/router';
import { PatientDirectService } from '../../ws-services/ws-patient-direct.service';
import { RecommendationService } from '../../ws-services/ws-recommendation.service';
import { DatePipe } from '@angular/common';

@Component({
  selector: 'ws-practitioner-patient-profile',
  templateUrl: './ws-practitioner-patient-profile.component.html',
  styleUrl: './ws-practitioner-patient-profile.component.scss',
  providers: [DatePipe]
})
export class WsPractitionerPatientProfileComponent {
  private ngUnsubscribe = new Subject<void>();

  referralCodeSettingsEditFlag: boolean = false;
  patientDetails: any;
  patientRecommendationDetails: any;
  patientOrderHistoryDetails: any;
  referralCodes: any[] = [];
  selectedReferralCode: any = "";
  selectedDiscount: any = "";
  currency$: Observable<string>;
  patientId: string = '';
  loader: boolean = false;

  pageHeadingParagraph$: Observable<any> = this.cmsService.getComponentData('PatientHeadingParaComp');
  basicInfo$: Observable<any> = this.cmsService.getComponentData('PatientBasicInfoFlexComponent');
  referralCodeSettings$: Observable<any> = this.cmsService.getComponentData('PatientReferralCodeSettingsFlexComponent');
  patientAddresses$: Observable<any> = this.cmsService.getComponentData('PatientAddressesFlexComponent');
  latestOrders$: Observable<any> = this.cmsService.getComponentData('PatientLatestOrdersFlexComponent');
  recommendations$: Observable<any> = this.cmsService.getComponentData('PatientRecommendationsFlexComponent');


  constructor(public cmsService: CmsService, private patientProfileService: PatientProfileService,
    private route: ActivatedRoute, private cd: ChangeDetectorRef,
    private patientService: PatientDirectService, private router: Router,
    private recommendationService: RecommendationService, private datePipe: DatePipe,
    protected currencyService: CurrencyService, private globalMessageService: GlobalMessageService
  ) {
    this.route.queryParams.subscribe((params: any) => {
      if (params?.uid) {
        this.getPatientProfileDetails(params?.uid);
        this.getPatientRecommendations(params?.uid);
        this.getPatientOrderHistoryDetails(params?.uid);
        this.patientId = params.uid;
        this.getReferrals();
      }
    });
    this.currency$ = this.currencyService.getActive();
  }

  getPatientProfileDetails(email: string) {
    this.loader = true;
    this.patientProfileService.fetchPatientProfileDetails(email)
      .pipe(takeUntil(this.ngUnsubscribe))
      .subscribe(results => {
        this.loader = false;
        this.patientDetails = results;
        this.selectedDiscount = this.patientDetails?.discount;
        this.selectedReferralCode = this.patientDetails?.referralCode;
        this.cd.detectChanges();
      }, () => {
        this.loader = false;
        this.cd.detectChanges();
      });
  }

  getPatientRecommendations(email: string) {
    this.patientProfileService.fetchPatientRecommendations({
      fields: 'DEFAULT',
      patientId: email,
      currentPage: 0,
      pageSize: 6
    })
      .pipe(takeUntil(this.ngUnsubscribe))
      .subscribe(results => {
        this.patientRecommendationDetails = results;
        this.cd.detectChanges();
      });
  }

  getPatientOrderHistoryDetails(email: string) {
    const startDate = '2000-01-01';
    const endDate = this.datePipe.transform(new Date(new Date().setDate(new Date().getDate() + 2)), 'yyyy-MM-dd');
    this.patientProfileService.fetchPatientOrderHistoryDetails(email, startDate, endDate)
      .pipe(takeUntil(this.ngUnsubscribe))
      .subscribe(results => {
        this.patientOrderHistoryDetails = results?.order_list?.items?.sort(function (a: any, b: any) {
          const dateA = new Date(a.docDate).getTime();
          const dateB = new Date(b.docDate).getTime();
          return dateA === dateB ? 0 : dateB - dateA;
        }) || [];
        if(this.patientOrderHistoryDetails?.length > 0) {
          this.patientOrderHistoryDetails = this.patientOrderHistoryDetails?.slice(0,5);
        }
        this.cd.detectChanges();
      });
  }

  getStatusClassName(status: string) {
    let className: any = { 'label-status-draft': true };
    if (status === 'SENT') {
      className = { 'label-status-success': true };
    } else if (status == 'PATIENT_DELETED') {
      className = { 'label-status-disabled': true };
    }
    return className;
  }

  getReferrals() {
    this.patientService.getAllReferralEntriesForCSV()
      .pipe(takeUntil(this.ngUnsubscribe))
      .subscribe((res) => {
        this.referralCodes = res?.referralCodeListWsDto || [];
        this.cd.detectChanges();
      });
  }

  getStatusName(status: string) {
    let rStatus = 'DRAFT';
    if (status === 'SENT') {
      rStatus = 'SENT';
    } else {
      rStatus = status.replace('_',' ');
    }
    return rStatus;
  }

  navigateToRecommendation(id: string, event?: KeyboardEvent , status?:any) {
    if (event && event.type === 'keydown' && event.key !== 'Enter' && event.key !== ' ') {
      return;
    }
    if(status?.toLowerCase() == 'draft') {
      const url = this.router.serializeUrl(
        this.router.createUrlTree(['Recommendation/BasicRecommendation', id])
      );
      window.open(url, '_blank');
    } else {
      const url = this.router.serializeUrl(
        this.router.createUrlTree(['/Recommendation/RecommendationDetails/', id])
      );
      window.open(url, '_blank');
    }
  }

  goToRecommendations(event?: KeyboardEvent) {
    if (event && event.type === 'keydown' && event.key !== 'Enter' && event.key !== ' ') {
      return;
    }
    this.router.navigateByUrl('Recommendation/PractitionerRecommendations?uid=' + this.patientId);
  }

  navigateToOrderDetails(id: string, event?: KeyboardEvent) {
    if (event && event.type === 'keydown' && event.key !== 'Enter' && event.key !== ' ') {
      return;
    }
    localStorage.setItem('navigatedFromPatientProfile', 'true'); 
    const url = this.router.serializeUrl(
      this.router.createUrlTree(['/order/details/', id])
    );
    window.open(url, '_blank');
  }


    goToOrderList(event?: KeyboardEvent) {
    if (event && event.type === 'keydown' && event.key !== 'Enter' && event.key !== ' ') {
      return;
    }
    this.router.navigateByUrl('/order/history?uid=' + this.patientId);
  }

  navigateToAddRecommendation(event?: KeyboardEvent) {
    if (event && event.type === 'keydown' && event.key !== 'Enter' && event.key !== ' ') {
      return;
    }
    localStorage.setItem('patient', this.patientDetails?.name);
    this.recommendationService
      .createRecommendation({ patient: this.patientId })
      .subscribe((data: { id: any }) => {
        this.router.navigate(['Recommendation/BasicRecommendation', data.id]);
      });
  }

  callPatientProfileAfterUpdate() {
    this.referralCodeSettingsEditFlag = false;
    this.getPatientProfileDetails(this.patientDetails?.uid);
    this.cd.detectChanges();
  }

  updateDetails() {
    this.loader = true;
    const [referralCode, lastName] = this.selectedReferralCode.split('|');
    const payload = {
      referralCode: referralCode,
      discount: this.selectedDiscount,
      lastName: lastName
    }

    this.patientProfileService.updatePatientDetails( this.patientId, payload)
      .pipe(takeUntil(this.ngUnsubscribe))
      .subscribe(results => {
        this.globalMessageService.add(
          'The Patient information has been updated.',
          GlobalMessageType.MSG_TYPE_CONFIRMATION,
          3000
        );
        this.callPatientProfileAfterUpdate()
      },(error) => {
        if(error?.status === 200) {
          this.globalMessageService.add(
            'The Patient information has been updated.',
            GlobalMessageType.MSG_TYPE_CONFIRMATION,
            3000
          );
        }
        this.callPatientProfileAfterUpdate();
      });
  }

  changeReferralCode(el: any) {
    this.selectedReferralCode = el?.target?.value;
  }

  changeDiscount(el: any) {
    this.selectedDiscount = el?.target?.value;
  }

}
