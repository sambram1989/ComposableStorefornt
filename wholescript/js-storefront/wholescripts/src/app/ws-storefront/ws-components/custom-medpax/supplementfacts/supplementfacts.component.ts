import {
  ChangeDetectorRef,
  Component,
  DestroyRef,
  effect,
  inject,
  input,
  signal,
  SimpleChanges,
} from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { WsOverlay } from '../../../ws-services/ws-overlay.service';
import { WsProductDetailsService } from '../../../ws-services/ws-product-details.service';

@Component({
  selector: 'ws-supplementfacts',
  templateUrl: './supplementfacts.component.html',
  styleUrl: './supplementfacts.component.scss',
})
export class SupplementfactsComponent {
  [x: string]: any;
  private productDetailsService = inject(WsProductDetailsService);
  private cdr = inject(ChangeDetectorRef);
  private destroyRef = inject(DestroyRef);
  supplementFacts: any[] = [];
  productIds = input<any>();
  productName = input<any>();
  selectedProductIds = signal<string[]>([]);
  loading: boolean = false;

  localSelectedIds: string[] = [];

  constructor() {
    effect(() => {
      const currentSelected = this.productIds();
      this.localSelectedIds = [...currentSelected];
      if (currentSelected.length > 0) {
        this.fetchSupplementFacts(currentSelected);
      } else {
        this.supplementFacts = [];
      }
    });
  }


  fetchSupplementFacts(productIds: string[]) {
   
    this.loading = true;
    this.productDetailsService
      .fetchSupplimentFacts(productIds)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: (data: any) => {
          this.supplementFacts = data;
          this.loading = false;
          this.cdr.detectChanges();
        },
        error: (err) => {
          this.loading = false;
          this.supplementFacts = [];
          this.cdr.detectChanges();
        },
      });
  }

  toggleProductCode(productId: string) {
    const totalProducts = this.productIds().length;
    const currentlyVisible = totalProducts - this.localSelectedIds.length;
    if (this.localSelectedIds.includes(productId)) {
      this.localSelectedIds = this.localSelectedIds.filter(id => id !== productId);
    } else {
      this.localSelectedIds.push(productId);
    }
    this.fetchSupplementFacts(this.localSelectedIds);
  }
}
