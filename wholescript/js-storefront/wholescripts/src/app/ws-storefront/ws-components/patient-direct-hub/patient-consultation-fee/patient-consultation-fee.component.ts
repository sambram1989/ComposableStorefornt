import { Component, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { PatientDirectService } from '../../../ws-services/ws-patient-direct.service';

@Component({
  selector: 'ws-patient-consultation-fee',
  templateUrl: './patient-consultation-fee.component.html',
  styleUrl: './patient-consultation-fee.component.scss'
})
export class PatientConsultationFeeComponent {
  private service = inject(PatientDirectService);
  canDownload$: Observable<any> = this.service.downloadW9Form();
}
