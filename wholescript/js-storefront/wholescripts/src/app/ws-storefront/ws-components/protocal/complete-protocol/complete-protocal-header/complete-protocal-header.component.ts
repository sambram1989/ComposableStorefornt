import { ChangeDetectorRef, Component, DestroyRef, inject, Input } from '@angular/core';
import { currentState, getUserData, WsCurrentUserService } from '../../../../ws-services/state-services/currentuser.service';
import { Observable, tap } from 'rxjs';
import { PractitionerMyPatientListingService } from '../../../ws-practitioner/ws-My-patients/my-patient-landing/practitioner-my-patient-listing.service';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';

@Component({
  selector: 'ws-complete-protocal-header',
  templateUrl: './complete-protocal-header.component.html',
  styleUrl: './complete-protocal-header.component.scss'
})
export class CompleteProtocalHeaderComponent {
  private currentUser= inject(currentState);
  private currentUserData= inject(getUserData);
  private currentUserService = inject(WsCurrentUserService);
  private PatientService = inject(PractitionerMyPatientListingService);
  private destroyRef = inject(DestroyRef);
  private cd = inject(ChangeDetectorRef);

  currentUser$:Observable<any>=this.currentUser;
  currentUserData$:Observable<any>=this.currentUserData;
  currentUserRole$: Observable<any> = this.currentUserService.currentState();
  
  @Input() protocolId: string ='';
  @Input() protocol:any;
  @Input() caller = false;
  @Input() isComeFromWs=false;
  @Input() isProtocoDetailsPage = false;

  patientList: any;
  constructor(){
    this.currentUserData.pipe(tap((e)=>{console.log(e);
    }))
  }

  ngOnInit() {
    this.getPatientList();
  }

  getPatientList(){
    this.PatientService
      .getPatientList()
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe((res: any) => {
        this.patientList = res
        this.cd.detectChanges();
      });
  }
}
