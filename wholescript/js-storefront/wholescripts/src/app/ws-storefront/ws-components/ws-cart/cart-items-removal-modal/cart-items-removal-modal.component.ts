import { ChangeDetectorRef, Component, DestroyRef, EventEmitter, inject, Input, Output } from '@angular/core';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { WsCheckoutDeliveryAddressService } from '../../../ws-services/ws-checkout-delivery-address.service';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { ActiveCartFacade } from '@spartacus/cart/base/root';
import { RoutingService } from '@spartacus/core';
import { WsCartEvents } from '../ws-cartEvents-Service/ws-cartevent.service';
import { medPaxPacketEnum } from '../../custom-medpax/medpaxService/customMedpaxservice.service';
import { concatMap, forkJoin, from, map, Observable, of } from 'rxjs';

const medPaxEnum: Record<string, string> = medPaxPacketEnum.reduce((acc, entry) => {
  const key = Object.keys(entry)[0] as keyof typeof entry;
  acc[key] = entry[key] as string;
  return acc;
}, {} as Record<string, string>);

@Component({
  selector: 'ws-cart-items-removal-modal',
  templateUrl: './cart-items-removal-modal.component.html',
  styleUrl: './cart-items-removal-modal.component.scss'
})
export class CartItemsRemovalModalComponent {
  @Input() normalProductsWithOOS: any[] = [];
  @Input() medPaxProdcuts: any[] = [];
  @Input() medPaxFormulas: any[] = [];
  @Input() allUnavailableProducts: any[] = [];


  @Input() cartData: any = {};
  @Output() hideRemoveItemsModal = new EventEmitter();
  @Output() verifyBrand = new EventEmitter();


  private destroyRef = inject(DestroyRef);
  private routingService = inject(RoutingService);
  public wsCartEvents = inject(WsCartEvents);

  loading: boolean = false;
  medPaxEnumValue = medPaxEnum;

  constructor(private wsCheckoutDeliveryAddressService: WsCheckoutDeliveryAddressService,
    private cd: ChangeDetectorRef, private activeCartService: ActiveCartFacade
  ) { }

  closeModal() {
    this.hideRemoveItemsModal.emit();
  }

  getItemTimesCode(itemCode: any) {
    return this.medPaxEnumValue[itemCode.toLowerCase()];
  }

  removeItemClick() {
    this.loading = true;
    let entryNumbers = [...this.normalProductsWithOOS, ...this.medPaxProdcuts]?.map(product => product?.entryNumber)?.join(',') || '';
    if (entryNumbers) {
      this.wsCheckoutDeliveryAddressService.removeAllItems(this.cartData?.code, entryNumbers)
        .pipe(takeUntilDestroyed(this.destroyRef))
        .subscribe(() => {
          if (this.medPaxFormulas?.length > 0) {
            this.removeMedpaxFormulas();
          } else {
            this.activeCartService.reloadActiveCart();
            this.verifyBrand.emit();
            this.closeModal();
            this.cd.detectChanges();
          }
          this.cd.detectChanges();
        }, () => {
          this.loading = false;
          this.closeModal();
          this.activeCartService.reloadActiveCart();
          this.cd.detectChanges();
        })
    } else if(this.medPaxFormulas?.length > 0) {
      this.removeMedpaxFormulas();
    }

  }

  removeMedpaxFormulas() {

    const medPaxFormulaDelete = this.medPaxFormulas?.map((formula: any) => {
      return this.wsCartEvents.removeMedpaxFormulaEntries(this.cartData?.code,
        formula.medPaxEntryNumber, formula.medPaxID, this.getItemTimesCode(formula.label)
      );
    });

    this.callApisSequentially(medPaxFormulaDelete).subscribe(
      (allData) => {
        this.activeCartService.reloadActiveCart();
        this.verifyBrand.emit();
        this.closeModal();
        this.cd.detectChanges();
      },
      (error) => {
        this.activeCartService.reloadActiveCart();
        console.error('Error occurred in medPaxFormulaDelete:', error);
        this.closeModal();
        this.cd.detectChanges();
      }
    );
  }

  callApisSequentially(apiUrls: any[]): Observable<any[]> {
    return apiUrls.reduce((prev$, apiUrl) => {
      return prev$.pipe(
        concatMap((accumulatedResults: any) => {
          return apiUrl.pipe(
            map((data) => {
              if (data !== null) {
                accumulatedResults.push(data);
              }
              return accumulatedResults;
            })
          );
        })
      );
    }, of([]));
  }

}
