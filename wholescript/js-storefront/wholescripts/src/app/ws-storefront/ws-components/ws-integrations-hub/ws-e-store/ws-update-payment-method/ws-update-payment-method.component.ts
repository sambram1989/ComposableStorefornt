import { AfterViewInit, ChangeDetectorRef, Component, DestroyRef, ElementRef, Input, ViewChild } from '@angular/core';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { PaymentService } from '../../../account-setting/payment-service/payment.service';
import { Router } from '@angular/router';
import { DatePipe } from '@angular/common';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';

@Component({
  selector: 'ws-update-payment-method',
  templateUrl: './ws-update-payment-method.component.html',
  styleUrl: './ws-update-payment-method.component.scss',
  providers: [DatePipe]
})

export class WsUpdatePaymentMethodComponent implements AfterViewInit {
  @Input() cards$: any[] = [];
  @Input() defaultCard: any;
  loader: boolean = false;
  @ViewChild('firstFocusElement') firstFocusElement! : ElementRef;

  constructor(
    private bsModalRef: BsModalRef, 
    private paymentService: PaymentService, 
    private cdRef: ChangeDetectorRef,
    private router: Router, 
    private datePipe: DatePipe,
    private destRef: DestroyRef
  ) {
    this.paymentService.previousRoutePath.next(window.location.pathname);
  }

  ngAfterViewInit(): void {
    setTimeout(() => {
      this.firstFocusElement.nativeElement.focus();
    }, 0);
  }

  onActionClick(action: string): void {
    this.bsModalRef.hide();
  }

  getPaymentCardsList() {
    this.paymentService.getPayments().subscribe((res:any) => { 
      this.cards$ = res?.cartData?.item;
      this.cdRef.detectChanges();
    })
  }

  addNewCard() {
    this.router.navigateByUrl('/accounts-settings/Payment');
    this.bsModalRef.hide();
  }

  updateMethod(card: any, selected?:boolean) {
    if (!selected) {
    this.loader = true
    const mt = this.datePipe.transform(card.validTo, 'MM');
    const yr = this.datePipe.transform(card.validTo, 'yyyy');
    this.paymentService.updateActivePaymentSettings(card, mt, yr)
    .pipe(takeUntilDestroyed(this.destRef))
    .subscribe({
      next: (res: any) => {
        this.paymentService.setPayment.next(true);
        this.loader = false;
        this.bsModalRef.hide();
        this.cdRef.detectChanges();
      },
      error: (err) => {
        console.error("Error Updating Payment Method", err);
        this.loader = false;
        this.cdRef.detectChanges();
      }
    });
  }
  }

  getActiveCard(card: any, defaultCard: any) {
    const mt = this.datePipe.transform(card.validTo, 'MM');
    return (card?.cardNumber == defaultCard?.card_accountNumber && mt === defaultCard?.card_expirationMonth)
  }
}

