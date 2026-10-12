import {
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component,
  inject,
  input,
} from '@angular/core';
import { Subject } from 'rxjs';
import { PaymentService } from '../payment-service/payment.service';

@Component({
  selector: 'ws-card-parent',
  templateUrl: './card-parent.component.html',
  styleUrls: [
    './card-parent.component.scss',
    '../acc-setting-payment/acc-setting-payment.component.scss',
  ],
  changeDetection: ChangeDetectionStrategy.OnPush,
  host: {
    '[class.d-flex]': '!isRow()',
    '[class.row]': 'isRow()',
    '[class.mx-0]': 'isRow()',
  },
})
export class CardParentComponent {
  isRow = input<boolean>(true);
  loader: boolean = false;
  private service = inject(PaymentService);

  paymentRes$: Subject<any> = new Subject();
  constructor(private cd: ChangeDetectorRef) {}

  ngOnInit() {
    this.loader = true;
    this.service.getPayments().subscribe((res: any) => {
      this.loader = false;
      this.paymentRes$.next(res);
      this.cd.detectChanges();
    }, () => {
      this.loader = false;
      this.cd.detectChanges();
    });
  }

  removeCard(cardId: string,cardNumber:string, payerId: string) {
    this.loader = true;
    this.service.removeCard(cardId,cardNumber, payerId).subscribe({
      next: (res: any) => {
        this.loader = false;
        this.paymentRes$.next(res);
        this.cd.detectChanges();
      },
      error: () => {
        this.loader = false;
        this.cd.detectChanges();
      }
    });
  }
}
