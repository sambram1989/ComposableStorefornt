import { ChangeDetectorRef, Component, inject, Input, OnInit } from '@angular/core';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { PaymentService } from '../../../account-setting/payment-service/payment.service';
import { shareReplay } from 'rxjs';

@Component({
  selector: 'ws-webkit-details',
  templateUrl: './ws-webkit-details.component.html',
  styleUrl: './ws-webkit-details.component.scss',
})

export class WsWebKitDetailsComponent implements OnInit {
  @Input() name: string;
  @Input() kit: any;
  loader: boolean = false;

  constructor(private bsModalRef: BsModalRef, private paymentService: PaymentService, private cdRef: ChangeDetectorRef) {
  }
  ngOnInit() {
  }

  onActionClick(action: string): void {
    this.bsModalRef.hide();
  }
}

