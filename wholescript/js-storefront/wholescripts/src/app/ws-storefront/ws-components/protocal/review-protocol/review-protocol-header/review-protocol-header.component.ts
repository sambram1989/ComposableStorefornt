import { Component, inject, Input } from '@angular/core';
import { CmsService } from '@spartacus/core';
import { Observable } from 'rxjs';

@Component({
  selector: 'ws-review-protocol-header',
  templateUrl: './review-protocol-header.component.html',
  styleUrl: './review-protocol-header.component.scss'
})
export class ReviewProtocolHeaderComponent {
  private cmsService = inject(CmsService);
  @Input() patientName: string ='';
  @Input() protocol: any;
  reviewHeader$:Observable<any> = this.cmsService.getComponentData('myProtocolReviewPageParaComponent');

}
