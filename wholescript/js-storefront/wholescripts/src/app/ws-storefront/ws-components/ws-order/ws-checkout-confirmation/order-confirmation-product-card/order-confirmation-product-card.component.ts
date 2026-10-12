import {
  Component,
  Input,
  input,
  OnChanges,
  SimpleChanges,
} from '@angular/core';
import { Config } from '@spartacus/core';
@Component({
  selector: 'ws-order-confirmation-product-card',
  templateUrl: './order-confirmation-product-card.component.html',
  styleUrl: '../ws-checkout-confirmation.component.scss',
})
export class OrderConfirmationProductCardComponent implements OnChanges {
  entry = input<any>({});
  isMedPax = input<boolean>(false);
  @Input() cartData: any;
  promoCodeEntries: number[] = [];

  constructor(protected config: Config) {}

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

  getImgUrl() {
    const images = this.entry()?.product?.images;
    if (!images) {
      return '';
    }
    const appendBaseUrl = (url: string) =>
      url.startsWith('http://') || url.startsWith('https://')
        ? url
        : this.getBaseUrl() + url;

    if (images?.PRIMARY?.product?.url) {
      return appendBaseUrl(images?.PRIMARY?.product?.url);
    }

    if (Array.isArray(images)) {
      const productImage = images.find((img: any) => img.format === 'product');
      if (productImage) {
        return appendBaseUrl(productImage.url);
      }
    }
    return images[0]?.url ? appendBaseUrl(images[0]?.url) : '';
  }

  getBaseUrl(): string {
    return (
      this.config.backend?.media?.baseUrl ??
      this.config.backend?.occ?.baseUrl ??
      ''
    );
  }
}
