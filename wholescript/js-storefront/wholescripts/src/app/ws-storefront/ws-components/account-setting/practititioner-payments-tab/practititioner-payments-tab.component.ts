import { ChangeDetectorRef, Component, DestroyRef, inject, signal } from '@angular/core';
import { PaymentService } from '../payment-service/payment.service';
import { getUserData } from '../../../ws-services/state-services/currentuser.service';
import { WsAddCreditCardService } from '../../ws-add-credit-card/ws-add-credit-card.service';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { WsCheckoutPaymentService } from '../../../ws-services/ws-checkout/ws-checkout-payment.service';
import { userMap } from '../../../ws-env/environment';
import { empty } from 'rxjs';

export const colorMap = { success: 'success', warning: 'warning', danger: 'danger', empty: 'empty' };
@Component({
  selector: 'ws-practititioner-payments-tab',
  templateUrl: './practititioner-payments-tab.component.html',
  styleUrls: ['../acc-setting-payment/acc-setting-payment.component.scss'],
})
export class PractititionerPaymentsTabComponent {
  private service = inject(PaymentService);
  public currentUser$ = inject(getUserData);
  private wsAddCreditCard = inject(WsAddCreditCardService);
  private wsCheckoutPaymentService = inject(WsCheckoutPaymentService);
  private destroyRef = inject(DestroyRef);
  private cdr = inject(ChangeDetectorRef);
  userDetails: any;
  previousPage: string;
  showAddCardSection = signal<boolean>(false);
  progressBarProps = {
    width: 'width: 100%',
    className: `progress-${colorMap.success}`,
  };
  ProgressBar = signal<any>(this.progressBarProps);
  isPractioner: boolean;
  isEmployeeCustomer:boolean;

  ngOnInit() {
    this.customEventsHandler();
    this.service.previousRoutePath.subscribe((res) => this.previousPage = res);
    this.currentUser$.subscribe((data: any) => {
      this.userDetails = data;
      if(this.userDetails?.creditUsed >= 0 && this.userDetails?.creditAmount >= 0) {
        let width = (this.userDetails?.creditUsed / this.userDetails?.creditAmount) * 100;
        let color = colorMap.success;
        if (width > 100) {
          width = 100;
          color = colorMap.danger;
        } else if (width > 75) {
          color = colorMap.danger;
        } else if (width > 49) {
          color = colorMap.warning;
        } 
        this.updateProgressBar(width, color);
      } else if(this.userDetails?.creditAmount < 0) {
        this.updateProgressBar(100, colorMap.empty);

      }
      const key = Object.keys(userMap).find(key => userMap[key] === 'practitioner');
      this.isPractioner = (data?.roles || []).includes(key);
      const empKey = Object.keys(userMap).find(key => userMap[key] === 'employee');
      this.isEmployeeCustomer = (data?.roles || []).includes(empKey);
      this.cdr.detectChanges();
    });
  }

  toggleAddCardSection() {
    this.showAddCardSection.update((value) => !value);
  }

  updateProgressBar(width: number, color: string) {
    let newWidth = `width: ${width}%`;
    let newColor = `progress-${color}`;
    this.ProgressBar.set({ width: newWidth, className: newColor });
  }

  updateTab(currentUrl: string) {
    if (currentUrl == '/order/history') {
      this.service.isAgingTab.set(true);
    }
  }

  customEventsHandler() {
    this.wsAddCreditCard.closeMeButton
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe(() => this.showAddCardSection.set(false));

    this.wsCheckoutPaymentService.addNewCardSucceeded
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: (response: any) => {
          this.showAddCardSection.set(false);
          this.cdr.detectChanges();
        },
      });
  }
}
