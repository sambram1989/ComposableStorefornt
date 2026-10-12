import { Component, DestroyRef, inject, signal, ChangeDetectorRef } from '@angular/core';
import { W9Formservice } from '../w9-service/w9-form.service';
import { map, Observable } from 'rxjs';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { PatientDirectService } from '../../../ws-services/ws-patient-direct.service';
import { getUserData } from '../../../ws-services/state-services/currentuser.service';

@Component({
  selector: 'ws-w9success-page',
  templateUrl: './w9success-page.component.html',
  styleUrl: './w9success-page.component.scss',
})
export class W9successPageComponent {
  private w9service = inject(W9Formservice);
  private patientDirect = inject(PatientDirectService);
  private destroyRef = inject(DestroyRef);
  private cdr = inject(ChangeDetectorRef);
  currentUserData$ = inject(getUserData);
  isw9ConsultationFormFilled = false;

  getreferralCodes$: Observable<string> = this.w9service
    .getReferalCode()
    .pipe(
      map((data: any) =>
        data?.referralCodeListWsDto?.[0]
          ? `${data?.referralCodeListWsDto?.[0].referralCode} | ${data?.referralCodeListWsDto?.[0].lastName}`
          : ''
      )
    );

  canDownload$: Observable<any> =
    this.patientDirect.checkValidationForDownLoadButton();
  W9FormData: any;
  alerts: any[] = [];
  alertSignal = signal<any>(this.alerts);

  ngOnInit() {
    this.currentUserData$
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe((res: any) => {
        this.isw9ConsultationFormFilled = res?.receiveConsultationFee;
        this.cdr.detectChanges();
      });

    this.getDownload9Form();
  }

  downloadW9Form() {
    if (this.W9FormData?.fileData) {
      const fileData = this.W9FormData.fileData;
      const fileName = this.W9FormData.fileName;
      const byteCharacters = atob(fileData);

      const byteNumbers = new Uint8Array(byteCharacters.length);
      for (let i = 0; i < byteCharacters.length; i++) {
        byteNumbers[i] = byteCharacters.charCodeAt(i);
      }
      const blob = new Blob([byteNumbers], { type: 'application/pdf' });
      const link = document.createElement('a');

      link.href = window.URL.createObjectURL(blob);
      link.download = fileName;
      link.click();
      window.URL.revokeObjectURL(link.href);
    } else {
      this.pushAlert('Please Try Again Later!', 'danger');
    }
  }

  getDownload9Form() {
    this.patientDirect
      .downloadW9Form()
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: (response: any) => {
          this.W9FormData = response;
        },
        error: (error) => {
          this.pushAlert('Please Try Again Later!', 'danger');
          console.error('error: ', error);
        },
      });
  }

  onClosed(dismissedAlert: any): void {
    this.alerts = this.alerts.filter((alert) => alert !== dismissedAlert);
    this.alertSignal.set(this.alerts);
  }

  pushAlert(message: string, type: 'success' | 'info' | 'danger' | 'warning') {
    this.alerts.push({
      type: type,
      msg: message,
      timeout: 5000,
    });
    this.alertSignal.set(this.alerts);
  }
}
