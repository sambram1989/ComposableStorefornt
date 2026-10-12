import { Component, DestroyRef, inject, signal } from '@angular/core';
import { PatientDirectService } from '../../../ws-services/ws-patient-direct.service';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';

@Component({
  selector: 'ws-consultationfee-form-button',
  templateUrl:'./ws-consultation-fee-form-button.component.html',
  styleUrls: ['../patient-consultation-fee/patient-consultation-fee.component.scss']
})
export class ConsultationfeeFormButtonComponent {
  private service = inject(PatientDirectService);
  private destroyRef = inject(DestroyRef);

    alerts: any[] = [];
    alertSignal = signal<any>(this.alerts);
  download() {
    this.service.downloadW9Form().pipe(takeUntilDestroyed(this.destroyRef)).subscribe((response: any) => {
      if (response.success === "true") {
        const fileData = response.fileData;
        const fileName = response.fileName;
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
        this.pushAlert('Please Try Again Later!','danger');
        console.error('Download failed:', response.msg);
      }
    });
  }
  onClosed(dismissedAlert: any): void {
    this.alerts = this.alerts.filter((alert) => alert !== dismissedAlert);
    this.alertSignal.set(this.alerts);
  }

  pushAlert(message:string,type:'success'|'info'|'danger'|'warning'){
    this.alerts.push({
      type: type,
      msg:  message,
      timeout: 5000,
    });
    this.alertSignal.set(this.alerts);
  }
}
