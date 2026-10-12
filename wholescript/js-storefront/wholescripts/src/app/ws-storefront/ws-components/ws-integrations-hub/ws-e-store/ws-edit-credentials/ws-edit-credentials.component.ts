import { AfterViewInit, ChangeDetectorRef, Component, ElementRef, inject, Input, OnInit, ViewChild } from '@angular/core';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { PaymentService } from '../../../account-setting/payment-service/payment.service';
import { shareReplay } from 'rxjs';

@Component({
  selector: 'ws-edit-credentials',
  templateUrl: './ws-edit-credentials.component.html',
  styleUrl: './ws-edit-credentials.component.scss',
})

export class WsEditCredentialsComponent implements OnInit, AfterViewInit {
  @Input() edit: boolean;
  @Input() credentials: any;
  @Input() changeCred: any;
  alerts: any[] = [];
  loader: boolean = false;
  @ViewChild('firstFocusElement') firstFocusElement: ElementRef;

  constructor(private bsModalRef: BsModalRef, private paymentService: PaymentService, private cdRef: ChangeDetectorRef) {
  }
  ngOnInit() {
    console.log(this.edit, this.credentials);
  }
  ngAfterViewInit() {
    setTimeout(() => {
      this.firstFocusElement?.nativeElement?.focus();
    }, 0);
  }

  onActionClick(action: string): void {
    console.log(`${action} button clicked`);
    this.bsModalRef.hide();
  }
  changePassword() {
    this.loader = true;
    this.paymentService.passwordSet.next(true);
    this.edit = true;
      const payload = {
        "sapCustomerId": this.credentials.client?.sapCustomer,
        "userName": this.credentials.client?.username,
        "externalClientStoreId": this.credentials.client?.externalClientStoreId
    }
      this.paymentService.changeCredential(payload).pipe(
        shareReplay(1)
      ).subscribe((res) => {
        this.changeCred = res;
        this.loader = false;
        this.cdRef.detectChanges();
      })
    }
    copyToClipboard(text: any) {
      navigator.clipboard.writeText(text).then(() => {
        this.add('Store password copied');
      }).catch(err => {
      });
    }
    add(message:any): void {
      this.alerts.push({
        type: 'success',
        msg: message,
        timeout: 2000,
      });
      this.cdRef.detectChanges();
    }
  
    onClosed(dismissedAlert: any): void {
      this.alerts = this.alerts.filter((alert) => alert !== dismissedAlert);
    }
}

