import {
  Component,
  Input,
  input,
  OnChanges,
  SimpleChanges,
} from '@angular/core';

@Component({
  selector: 'ws-checkout-order-review',
  templateUrl: './checkout-order-review.component.html',
  styleUrl: './checkout-order-review.component.scss'
})
export class CheckoutOrderReviewComponent implements OnChanges {
  item = input<any>();
  isMedPax = input<boolean>(false);
  @Input() cartData: any;
  promoCodeEntries: number[] = [];

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['cartData']?.currentValue) {
      this.promoCodeEntries = [];
      if (this.cartData?.appliedProductPromotions?.length > 0) {
        this.cartData?.appliedProductPromotions?.map((promos: any) => {
          promos?.consumedEntries?.map((entry: any) => {
            this.promoCodeEntries.push(entry?.orderEntryNumber);
          });
        });
      }
    }
  }

  getFrequency() {
    const fCode = this.item()?.autoShipCode ?? 'One-Time';
    const feq = this.item()?.product?.autoShipFrequency?.find(
      (asf: any) => asf.code === fCode
    );
    return feq?.name ?? fCode;
  }

  getImgUrl() {
    return this.item()?.product?.images?.PRIMARY?.product?.url ?? '';
  }
}
