import { ChangeDetectorRef, Component, DestroyRef, ElementRef, EventEmitter, inject, Input, OnChanges, Output, Renderer2, SimpleChanges, ViewChild } from '@angular/core';
import { WsOverlay } from '../../../../ws-services/ws-overlay.service';
import { Observable } from 'rxjs';
import { CurrencyService } from '@spartacus/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { BASE_PATH } from '../../../../ws-constants/ws-api-endpoint.constant';
import { WsAutoShipsService } from '../../../../ws-services/autoships.service';

@Component({
  selector: 'ws-autoships-detail',
  templateUrl: './ws-autoships-detail.component.html',
  styleUrl: './ws-autoships-detail.component.scss'
})
export class WsAutoshipsDetailComponent implements OnChanges {
  private overlayService = inject(WsOverlay);
  private destroyRef = inject(DestroyRef);
  focusedIndex: number = -1;

  @Input() autoShipsHeaderItems: any[] = [];
  @Input() autoShipsCC: any;
  @Output() cancelAutoShip = new EventEmitter<string>();
  @Output() changeShipToAddress = new EventEmitter<string>();

  @ViewChild('cancelAutoShipModal', { static: false }) cancelAutoShipModal!: ElementRef;
  @ViewChild('changeAddressModal', { static: false }) changeAddressModal!: ElementRef;

  private lastFocusedElement: HTMLElement | null = null;

  showCancelAutoShipModal: boolean = false;
  showChangeShippingAddressModal: boolean = false;
  searchText: string = '';
  currency$: Observable<string>;

  totalPrice: number = 0;
  selectedAddress: any;
  @Input() allAddresses: any[] = [];
  addresses: any[] = [];
  searchKeys = ['formattedAddress'];
  images = [
    { name: 'Amex', src: BASE_PATH('amex') },
    { name: 'Visa', src: BASE_PATH('visa') },
    { name: 'Mastercard', src: BASE_PATH('mastercard') },
    { name: 'Discover', src: BASE_PATH('discover') },
  ];
  addressVerificationLoader: boolean = false;
  updateAddressErrorType: string = '';

  constructor(private cd: ChangeDetectorRef, protected renderer: Renderer2,
    private currencyService: CurrencyService, private autoshipsService: WsAutoShipsService
  ) {
    this.currency$ = this.currencyService.getActive();
    this.addresses = this.allAddresses ? [...this.allAddresses] : [];
  }

  ngOnChanges(changes: SimpleChanges): void {
      if (this.autoShipsHeaderItems?.length > 0) {
        this.totalPrice = this.autoShipsHeaderItems?.reduce((total: number, item: { totalPrice: any; }) => total + Number(item.totalPrice), 0) || 0;
      }
  }

  ngAfterViewInit(): void {
    if (this.showCancelAutoShipModal && this.cancelAutoShipModal) {
      this.setFocusTrap(this.cancelAutoShipModal);
    }
    if (this.showChangeShippingAddressModal && this.changeAddressModal) {
      this.setFocusTrap(this.changeAddressModal);
    }
  }

  updateSearch(searchValue: string) {
    this.searchText = searchValue;
    if (this.searchText) {
      this.addresses = this.allAddresses?.filter(item => {
        for (let key of this.searchKeys) {
          if (item.hasOwnProperty(key) && item[key] !== null && item[key].toString().toLowerCase().includes(this.searchText?.toLowerCase())) {
            return true;
          }
        };

        return false;
      });
      this.cd.detectChanges();
    }
  }

  handleKeyDown(event: KeyboardEvent) {
    if (!this.addresses?.length) return;
  
    switch (event.key) {
      case 'ArrowDown':
        event.preventDefault();
        this.focusedIndex = (this.focusedIndex + 1) % this.addresses.length;
        break;
  
      case 'ArrowUp':
        event.preventDefault();
        this.focusedIndex =
          (this.focusedIndex - 1 + this.addresses.length) % this.addresses.length;
        break;
  
      case 'Enter':
        if (this.focusedIndex >= 0 && this.addresses[this.focusedIndex]) {
          this.selectAddress(this.addresses[this.focusedIndex]);
        }
        break;
    }
  }
  
  getCardImage(ccType: string): string | undefined {
    if (!ccType) return undefined;
    return this.images.find(img => img.name.toLowerCase() === ccType.toLowerCase())?.src;
  }

  extractLastFourDigits(response: string): string | null {
    const match = response.match(/(\d{4})(?=-[A-Z0-9]+$)/);
    return match ? match[1] : null;
  }

  openCancelAutoShipModal() {
    this.showCancelAutoShipModal = true;
    this.cd.detectChanges();
    setTimeout(() => this.setFocusTrap(this.cancelAutoShipModal), 0);
  }

  openChangeShippingAddress() {
    this.showChangeShippingAddressModal = true;
    this.cd.detectChanges();
    setTimeout(() => this.setFocusTrap(this.changeAddressModal), 0);
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
    this.showCancelAutoShipModal = false;
    this.showChangeShippingAddressModal = false;
    this.closePopup();
    this.cd.detectChanges();
    this.removeFocusTrap();
  }

  cancelAutoShipClick() {
    this.cancelAutoShip.emit();
  }

  selectAddress(address: any) {
    this.selectedAddress = {};
    this.updateAddressErrorType = '';
    const productCodes: string[] = [];
    this.autoShipsHeaderItems?.map(header => {
      header?.products?.map((item: any) => {
        productCodes.push(item.material)
      })
    })
    const payload = {
      address: address,
      productCodes: productCodes
    }
    this.addressVerificationLoader = true;
    this.autoshipsService.verifyAddress(payload)
    .pipe(takeUntilDestroyed(this.destroyRef))
    .subscribe(data => {
      this.selectedAddress = address;
      this.searchText = '';
      this.addressVerificationLoader = false;
      this.cd.detectChanges();
    }, (error) => {
      if(error?.status === 200) {
        this.selectedAddress = address;
      } else if(error?.status === 422) {
        if(error?.error?.errors?.length > 0) {
          error?.error?.errors?.map((e: any) => {
            const type = e.type;
            if(type === 'AgeRestrictionError' ||  type === 'POBoxAgeRestrictionError' || type === 'RegionExclusionError') {
              this.updateAddressErrorType = type;
            }
          })
        }
      }
      this.searchText = '';
      this.addressVerificationLoader = false;
      this.cd.detectChanges();
    });
  }

  changeShippingAddressClick() {
    this.changeShipToAddress.emit(this.selectedAddress);
    setTimeout(() => this.setFocusTrap(this.changeAddressModal), 0);

  }

  private setFocusTrap(modalElement: ElementRef): void {
    if (!modalElement || !modalElement.nativeElement) return;

    const getFocusableElements = (): HTMLElement[] => {
      const selectors = `
        a[href],
        button:not([disabled]),
        textarea:not([disabled]),
        input:not([disabled]),
        select:not([disabled]),
        [tabindex]:not([tabindex="-1"]):not([disabled])
      `;
      return Array.from(modalElement.nativeElement.querySelectorAll(selectors)) as HTMLElement[];
    };

    let focusable = getFocusableElements();
    let firstElement = focusable[0];
    let lastElement = focusable[focusable.length - 1];

    if (firstElement) firstElement.focus();

    const updateFocusableElements = () => {
      focusable = getFocusableElements();
      firstElement = focusable[0];
      lastElement = focusable[focusable.length - 1];
    };

    const observer = new MutationObserver(() => {
      updateFocusableElements();
    });

    observer.observe(modalElement.nativeElement, {
      childList: true,
      subtree: true,
      attributes: true,
      attributeFilter: ['disabled', 'tabindex']
    });

    modalElement.nativeElement.addEventListener('keydown', (event: KeyboardEvent) => {
      if (event.key === 'Tab' && focusable.length > 0) {
        const activeElement = document.activeElement;
        if (event.shiftKey) {
          if (activeElement === firstElement) {
            event.preventDefault();
            lastElement.focus();
            observer.disconnect();
          }
        } else {
          if (activeElement === lastElement) {
            event.preventDefault();
            firstElement.focus();
            observer.disconnect();
          }
        }
      }
    });
  }

  private removeFocusTrap() {
    document.removeEventListener('keydown', this.handleKeyDown);
  }
}
