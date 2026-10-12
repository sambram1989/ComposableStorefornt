import { ChangeDetectorRef, Component, ElementRef, EventEmitter, inject, Input, Output, Renderer2, signal, ViewChild } from '@angular/core';
import { WsOverlay } from '../../../../ws-services/ws-overlay.service';
import { Observable } from 'rxjs';
import { CurrencyService } from '@spartacus/core';
import { DaysOfWeek } from '@spartacus/order/root';
import { WsLeftFilterFacet } from '../../../../ws-services/ws-left-filter-facet.service';

@Component({
  selector: 'ws-autoships-detail-products',
  templateUrl: './ws-autoships-detail-products.component.html',
  styleUrl: './ws-autoships-detail-products.component.scss',
})
export class WsAutoshipsDetailProductsComponent {
  private overlayService = inject(WsOverlay);
  public Number = Number;
  currency$: Observable<string>;
  objectKeys = Object.keys;
  shipMentDate = new Date();
  selectedDate: any;

  @Input() autoShipsHeaderItems: any;
  @Output() changeShipmentDate = new EventEmitter<any>();
  @Output() skipInstead = new EventEmitter<any>();
  @Output() remove = new EventEmitter<any>();
  @Output() saveChanges = new EventEmitter<any>();

  @ViewChild('datePickerWrapper') datePickerWrapper!: ElementRef;
  @ViewChild('removeProductModal') removeProductModal!: ElementRef;
  @ViewChild('changeDateModal') changeDateModal!: ElementRef;
  @ViewChild('editProductModal') editProductModal!: ElementRef;
  private lastFocusedElement: HTMLElement | null = null;
  private firstFocusableElement: HTMLElement | null = null;
  private lastFocusableElement: HTMLElement | null = null;

  todayDate = "";

  showRemoveProductModal: boolean = false;
  showChangeDateModal: boolean = false;
  showEditProductModal: boolean = false;
  sliderState = signal<boolean>(false);
  changeDateDetails: any;
  selectedProductModal: any;
  autoShipEditModalFrequency: any;
  totalMedpaxCount: number = 0;
  @ViewChild('myDate',{ static: false }) dateInputRef!: ElementRef<HTMLInputElement>;

  constructor(
    private cd: ChangeDetectorRef,
    protected renderer: Renderer2,
    private currencyService: CurrencyService
  ) {
    this.currency$ = this.currencyService.getActive();
    this.todayDate = this.getShipmentDate();
  }

  openRemoveProductModal(product: any) {
    this.showRemoveProductModal = true;
    this.selectedProductModal = { ...product };
    this.cd.detectChanges();
    setTimeout(() => this.setFocusTrap(this.removeProductModal), 0);
  }

  getShipmentDate() {
    let date = new Date();
    let dayOfWeek = date.getDay();
    if(dayOfWeek === 0 || dayOfWeek === 1) date.setDate(date.getDate() + 3);
    if(dayOfWeek >= 2 && dayOfWeek < 6) date.setDate(date.getDate() + 5);
    if(dayOfWeek === 6) date.setDate(date.getDate() + 4);
    return date.toISOString().split('T')[0];
  }
  openEditProductModal(product: any) {
    this.showEditProductModal = true;
    this.selectedProductModal = { ...product };
    this.autoShipEditModalFrequency = Number(
      this.selectedProductModal.frequency
    );
    this.totalMedpaxCount = 0;
    const existingPills = new Set<string>();
    if (product?.packetDetails && typeof product.packetDetails === 'object') {
      Object.values(product.packetDetails).forEach((packet: any) => {
        if (Array.isArray(packet.entries)) {
          packet.entries.forEach((entry: any) => {
            if (entry.material) {
              existingPills.add(entry.material);
            }
          });
        }
      });
    }
    this.totalMedpaxCount = existingPills.size;
    this.cd.detectChanges();
    setTimeout(() => this.setFocusTrap(this.editProductModal), 0);
  }

  openChangeDateModal(detail: any) {
    this.showChangeDateModal = true;
    this.changeDateDetails = detail;
    this.shipMentDate = new Date(this.changeDateDetails?.shippingDate);
    this.cd.detectChanges();
    setTimeout(() => {
      const input = this.dateInputRef?.nativeElement;
      if (input?.showPicker) {
        input.showPicker(); 
      }
    }, 100); 
    setTimeout(() => this.setFocusTrap(this.changeDateModal), 0);
  }

  closePopup() {
    this.overlayService.closeAllModals();
    this.renderer.removeClass(document.body, 'modal-open');
    const backdrop = document.querySelector('.modal-backdrop');
    if (backdrop) {
      this.renderer.removeClass(backdrop, 'fade');
      this.renderer.removeClass(backdrop, 'in');
      this.renderer.removeClass(backdrop, 'modal-backdrop');
    }
    if (this.lastFocusedElement) {
      setTimeout(() => this.lastFocusedElement?.focus(), 0);
      this.lastFocusedElement = null;
    }
    this.removeFocusTrap();
  }

  closeModal() {
    this.showRemoveProductModal = false;
    this.showChangeDateModal = false;
    this.showEditProductModal = false;
    this.closePopup();
    this.cd.detectChanges();
    this.removeFocusTrap();
  }

  save() {
    this.changeShipmentDate.emit({
      details: this.changeDateDetails,
      shipmentDate: this.shipMentDate,
    });
  }

  shipmentDateChange(date: any) {
    const dateObj = new Date(date);
    this.shipMentDate = dateObj;
  }

  skipInsteadClick() {
    this.skipInstead.emit(this.selectedProductModal);
  }

  removeClick() {
    this.remove.emit(this.selectedProductModal);
  }

  decreseQty() {
    if (Number(this.selectedProductModal?.reqQty) > 1) {
      const eachPrice = Number(this.selectedProductModal?.overallTotalSum / Number(this.selectedProductModal?.reqQty))
      this.selectedProductModal.reqQty =
        Number(this.selectedProductModal?.reqQty) - 1;
      this.selectedProductModal.overallTotalSum = eachPrice * this.selectedProductModal.reqQty;
    }
  }

  onInputChange(event: Event): void {
    const input = event.target as HTMLInputElement;
    let value = Number(input.value);
    if(input.value == '') {
      return;
    }
    if (value < 1) {
      value = 1;
    } else if (value > 9999) {
      value = 9999;
    }
    const eachPrice = Number(this.selectedProductModal?.overallTotalSum / Number(this.selectedProductModal?.reqQty))
    this.selectedProductModal.reqQty = value;
    this.selectedProductModal.overallTotalSum = eachPrice * this.selectedProductModal.reqQty;
    input.value = value.toString();
    this.cd.markForCheck();
  }

  increaseQty() {
    const eachPrice = Number(this.selectedProductModal?.overallTotalSum / Number(this.selectedProductModal?.reqQty))
    this.selectedProductModal.reqQty =
      Number(this.selectedProductModal?.reqQty) + 1;
    this.selectedProductModal.overallTotalSum = eachPrice * this.selectedProductModal.reqQty;
  }

  saveChangesClick() {
    const productToEmit = { ...this.selectedProductModal };
    productToEmit.frequency =
      this.autoShipEditModalFrequency === 'One_Time'
        ? ''
        : this.autoShipEditModalFrequency;
    if (this.sliderState()) {
      productToEmit.nextShipmentSliderState = this.sliderState();
    }
    if (productToEmit?.isMedPax && productToEmit.packetDetails) {
      const deletedEntries: { [key: string]: { entries: any[] } } = {};

      Object.entries(productToEmit.packetDetails).forEach(
        ([key, packet]: [string, any]) => {
          const removed = packet.entries?.filter((entry: any) => entry.isRemoved);
          if (removed?.length) {
            deletedEntries[key] = { entries: removed };
          }
        }
      );
  
      productToEmit.packetDetails = deletedEntries;
    }
    this.saveChanges.emit(productToEmit);
  }

  private updateMedpaxPillsCount(): void {
    const existingPills = new Set<string>();
  
    if (this.selectedProductModal?.packetDetails) {
      Object.values(this.selectedProductModal.packetDetails).forEach((packet: any) => {
        if (Array.isArray(packet.entries)) {
          packet.entries
            .filter((entry: any) => !entry.isRemoved && entry.material)
            .forEach((entry: any) => existingPills.add(entry.material));
        }
      });
    }
    this.totalMedpaxCount = existingPills.size;
  }

  deleteMedpaxEntry(key: string, selectedEntry: any) {
    this.selectedProductModal.packetDetails[key].entries =
      this.selectedProductModal.packetDetails[key]?.entries?.map(
        (entry: any) => {
          return {
            ...entry,
            isRemoved:
              selectedEntry?.material === entry.material
                ? true
                : entry?.isRemoved,
          };
        }
      );
      this.updateMedpaxPillsCount();
  }

  undoMedpaxEntry(key: string, selectedEntry: any) {
    this.selectedProductModal.packetDetails[key].entries =
      this.selectedProductModal.packetDetails[key]?.entries?.map(
        (entry: any) => {
          return {
            ...entry,
            isRemoved:
              selectedEntry?.material === entry.material
                ? false
                : entry?.isRemoved,
          };
        }
      );
      this.updateMedpaxPillsCount();
  }

  getTotalPrice(product: any) {
    if(product?.overallTotalSum) {
      return Number(product?.overallTotalSum);
    }
    const price = product?.price;
    return Number(Number(price).toFixed(2)) * Number(product?.reqQty);
  }

  setFocusTrap(modalElementRef: ElementRef) {
    const modal: HTMLElement = modalElementRef.nativeElement;
    const focusableSelectors =
      'button, [href], input, select, textarea, [tabindex]:not([tabindex="-1"])';
    const focusableElements = Array.from(
      modal.querySelectorAll(focusableSelectors)
    ).filter((el: any) => !el.hasAttribute('disabled'));
  
    if (focusableElements.length) {
      (focusableElements[0] as HTMLElement).focus();
  
      const first = focusableElements[0] as HTMLElement;
      const last = focusableElements[focusableElements.length - 1] as HTMLElement;
  
      modal.addEventListener('keydown', (e: KeyboardEvent) => {
        if (e.key === 'Tab') {
          if (e.shiftKey) {
            if (document.activeElement === first) {
              e.preventDefault();
              last.focus();
            }
          } else {
            if (document.activeElement === last) {
              e.preventDefault();
              first.focus();
            }
          }
        }
      });
    }
  }

  private removeFocusTrap() {
    document.removeEventListener('keydown', this.handleKeyDown);
  }

  private handleKeyDown = (event: KeyboardEvent) => {
    if (event.key === 'Tab') {
      if (!this.firstFocusableElement || !this.lastFocusableElement) return;

      if (event.shiftKey) {
        if (document.activeElement === this.firstFocusableElement) {
          event.preventDefault();
          this.lastFocusableElement?.focus();
        }
      } else {
        if (document.activeElement === this.lastFocusableElement) {
          event.preventDefault();
          this.firstFocusableElement?.focus();
        }
      }
    }
  };
}
