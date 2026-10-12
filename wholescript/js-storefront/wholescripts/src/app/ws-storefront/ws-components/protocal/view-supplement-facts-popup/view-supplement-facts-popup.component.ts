import {
  ChangeDetectorRef,
  Component,
  DestroyRef,
  inject,
  input,
  OnInit,
  signal,
} from '@angular/core';
import { WsOverlay } from '../../../ws-services/ws-overlay.service';
import { WsProductDetailsService } from '../../../ws-services/ws-product-details.service';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';

@Component({
  selector: 'ws-view-supplement-facts-popup',
  templateUrl: './view-supplement-facts-popup.component.html',
  styleUrl: './view-supplement-facts-popup.component.scss',
})
export class ViewSupplementFactsPopupComponent implements OnInit {
  [x: string]: any;
  private overlay = inject(WsOverlay);
  private productDetailsService = inject(WsProductDetailsService);
  private cdr = inject(ChangeDetectorRef);
  private destroyRef = inject(DestroyRef);
  supplementFacts: any[] = []; 
  productIds = input<any>();
  productName = input<any>();
  selectedProductIds = signal<string[]>([]);
  loading: boolean = false;

  ngOnInit() {
    this.selectedProductIds.set(this.productIds());
    this.fetchSupplementFacts(this.productIds());
  }

  fetchSupplementFacts(productIds: string[]) {
    this.loading = true
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
  const currentSelected = this.selectedProductIds();
  if (currentSelected.includes(productId)) {
    this.selectedProductIds.set(
      currentSelected.filter((id) => id !== productId)
    );
  } else {
    this.selectedProductIds.set([...currentSelected, productId]);
  }
  this.fetchSupplementFacts(this.selectedProductIds());
}

  updateData(selectedMedicine: any): void {
    const updatedMedicines = this.productName().map((medicine: any) =>
      medicine === selectedMedicine
        ? { ...medicine, selected: !medicine.selected }
        : medicine
    );
  }

  closeModal() {
    this.overlay.closeTopModal();
  }
}
