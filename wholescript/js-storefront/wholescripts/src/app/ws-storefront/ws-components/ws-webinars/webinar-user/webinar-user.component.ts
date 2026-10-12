import { Component, inject, input } from '@angular/core';
import { ProtocolService } from '../../../ws-services/protocol/protocol.service';

@Component({
  selector: 'ws-webinar-user',
  templateUrl: './webinar-user.component.html',
  styleUrl: './webinar-user.component.scss'
})
export class WebinarUserComponent {
  private protocolService = inject(ProtocolService);
  presenterDetails = input<any>();

  imagePrefix = this.protocolService.getBaseUrl();

  
}
