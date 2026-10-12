import { ChangeDetectorRef, Component, inject, Input, OnInit } from '@angular/core';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { PaymentService } from '../../../account-setting/payment-service/payment.service';
import { shareReplay } from 'rxjs';

@Component({
  selector: 'ws-estore-redirect',
  templateUrl: './ws-estore-redirect.component.html',
  styleUrl: './ws-estore-redirect.component.scss',
})

export class WsEstoreRedirectComponent implements OnInit {
  @Input() display: string;
  estoreIntegration: boolean;

  ngOnInit(): void {
    this.estoreIntegration = this.display.includes('integrations');
  }
}

