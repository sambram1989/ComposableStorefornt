import { ChangeDetectorRef, Component, DestroyRef, inject, signal } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { WsAddCreditCardService } from '../../ws-add-credit-card/ws-add-credit-card.service';
import { WsCheckoutPaymentService } from '../../../ws-services/ws-checkout/ws-checkout-payment.service';

@Component({
  selector: 'ws-acc-setting-payment',
  templateUrl: './acc-setting-payment.component.html',
  styleUrl: './acc-setting-payment.component.scss',
})
export class AccSettingPaymentComponent {
  private wsAddCreditCard = inject(WsAddCreditCardService);
   private cdr = inject(ChangeDetectorRef);
   private wsCheckoutPaymentService = inject(WsCheckoutPaymentService);
  private destroyRef = inject(DestroyRef);
  showAddCardSection = signal<boolean>(false);
  ngOnInit() {
    this.customEventsHandler();
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
