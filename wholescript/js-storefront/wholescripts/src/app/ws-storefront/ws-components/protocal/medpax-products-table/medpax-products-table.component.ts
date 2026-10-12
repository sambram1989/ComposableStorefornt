import { ChangeDetectorRef, Component, DestroyRef, inject, Input, input, signal } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { FavoriteService } from '../../../ws-services/ws-favorite.service';
import { WsCustomMedPaxEvents } from '../../custom-medpax/medpaxService/medpaxEventservice.service';
import { Location } from '@angular/common';
import { Observable, delay, asyncScheduler, shareReplay } from 'rxjs';
import { ProtocolService } from '../../../ws-services/protocol/protocol.service';
import { WsProductDetailsPopupComponent } from '../../ws-pdp/ws-product-details-popup/ws-product-details-popup.component';
import { BsModalRef, BsModalService } from 'ngx-bootstrap/modal';
@Component({
  selector: 'ws-medpax-products-table',
  templateUrl: './medpax-products-table.component.html',
  styleUrl: './medpax-products-table.component.scss'
})
export class MedpaxProductsTableComponent {
  private favoriteService = inject(FavoriteService);
  private destroyRef = inject(DestroyRef);
  private eventService = inject(WsCustomMedPaxEvents);
  public location = inject(Location);
  private protocolService = inject(ProtocolService);
  private bsModalService = inject(BsModalService);
  private cdRef = inject(ChangeDetectorRef);

  selectedProductCodes= input<any>([]);
  products = input<any>({});
  entryNumberList = input<any>({});
  medPaxData = input<any>({});
  bsModalRef?: BsModalRef;
  alerts: any[] = [];
  alertsSignal = signal<any>({});
  favoriteList: { wishLists?: { wishListEntries: { productCode: string }[] } } = {};
  dosingDataCache: { [productCode: string]: Observable<any> } = {};
  isBtnDisabled: boolean = false;

  ngOnInit() {
    this.favoriteService.favoriteItems$.pipe(takeUntilDestroyed(this.destroyRef)).subscribe((favorites: any) => {
      favorites?.wishLists?.forEach((element: any) => {
        if(element.name.includes('plpwishlist')) {
          this.favoriteList = {
            wishLists: {
              wishListEntries: element.wishListEntries.filter((entry: any) => entry.isMedpax).map((entry: any) => {
                return {productCode: entry.productCode}
              })
            }
          }
        }
      });
    });

    this.eventService.enableButton.pipe(takeUntilDestroyed(this.destroyRef)).subscribe((res: any) => {
      this.isBtnDisabled = res;
    });

    this.favoriteService.addMedpax.pipe(takeUntilDestroyed(this.destroyRef)).subscribe(res => {
      if(res) this.addAndRemove(this.addOrRemove(res?.code),this.getEntryNumber(res?.code),res);
    })
  }
  
  openProductDetailPopup(productCode: string) {
    const initialState = {
      productCode: productCode
    };
    this.bsModalRef = this.bsModalService.show(WsProductDetailsPopupComponent, {
      initialState: initialState
    });
    this.bsModalRef.content.productCode = productCode;
 }

  openEditDosingPopup(currentType: string, currProd: any) {
    this.eventService.emitEditClick({ type: currentType, prod: currProd, code: currProd?.code });
  }

  addOrRemove(productCode: string): boolean {
    return this.selectedProductCodes()?.includes(productCode);
  }

  addAndRemove(isRemove: boolean, entryNo: any, product?: any) {
    if (isRemove) {
      this.RemoveProduct(entryNo, product);
    } else {
      this.isBtnDisabled = true;
      this.AddProduct(product);
    }
  }

  getEntryNumber(productCode: string) {
    const entry = this.entryNumberList()?.find((item: any) => item.code === productCode);
    return entry ? entry.entryNumber : null;
  }

  RemoveProduct(entryNo: any, product: any) {
    this.eventService.emitDeleteClick({ EntryNumber: entryNo, product: product });
    const productName = product?.name || 'Unknown Product';
    this.removeMedPaxProductAlert(productName );
  }

  AddProduct(prod: any) {
    this.eventService.emitAddClick(prod);
    const productName = prod?.name || 'Unknown Product';
    this.addToMedPaxAlert(productName );
  }

  getAlertMessage(msg: string) {
    return msg?.replace(/<[^>]*>/g, '') || '';
  }

  addToMedPaxAlert(productName: string): void {
    this.alerts.push({
      type: 'info',
      msg: `"${productName}" added to MedPax`, 
      timeout: 4000,
    });
    this.alertsSignal.set(this.alerts);
    this.cdRef.markForCheck();
  }

  removeMedPaxProductAlert(productName: string): void {
    this.alerts.push({
      type: 'info',
      msg: `"${productName}" deleted from MedPax`, 
      timeout: 4000,
    });
    this.alertsSignal.set(this.alerts);
    this.cdRef.markForCheck();
  }


  updateFavorites(item: any, isFav: boolean) {
    if (isFav) {
      this.favoriteService
        .removeFromFavorites(item.code)
        .pipe(takeUntilDestroyed(this.destroyRef))
        .subscribe(() => {
          item.isFavorite = !item.isFavorite;
          this.add(item.isFavorite);
          this.cdRef.detectChanges();
        });
    } else {
      this.favoriteService
        .addToFavorites(item.code)
        .pipe(takeUntilDestroyed(this.destroyRef))
        .subscribe(() => {
          item.isFavorite = !item.isFavorite;
          this.add(item.isFavorite);
          this.cdRef.detectChanges();
        });
    }
  }

  add(isFavorite: boolean): void {
    this.alerts.push({
      type: 'info',
      msg: isFavorite ? `Product added to favorites.` : `Product removed from favorites.`,
      timeout: 2000
    });
    this.alertsSignal.set(this.alerts);
  }

  onClosed(dismissedAlert: any): void {
    this.alerts = this.alerts.filter((alert) => alert !== dismissedAlert);
    this.alertsSignal.set(this.alerts);
  }

  isFavorite(item: any): boolean {
    const productIndex = this.favoriteList?.wishLists?.wishListEntries?.findIndex((product: any) => product.productCode === item.code);
    return productIndex > -1 ? true : false;
  }

  getCustomDosingOnProductLevel(productCode: string): Observable<any> {
    if (!this.dosingDataCache[productCode]) {
      this.dosingDataCache[productCode] = this.protocolService
        .getCustomDosingEntries(productCode)
        .pipe(delay(300, asyncScheduler), shareReplay(1));
    }
    return this.dosingDataCache[productCode];
  }

  getAddToCartDisableFlag(selectedProduct: any): boolean {
    if(selectedProduct?.isMedpax) {
      if(selectedProduct?.forceOutOfStock) {
        return true;
      } else if(selectedProduct?.stock?.stockLevelStatus === 'outOfStock') {
        return true;
      }
    }
    return false;
  }

}
