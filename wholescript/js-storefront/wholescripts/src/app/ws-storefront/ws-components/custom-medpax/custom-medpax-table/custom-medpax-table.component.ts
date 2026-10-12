import { Component, computed, DestroyRef, inject, input, signal } from '@angular/core';
import { FavoriteService } from '../../../ws-services/ws-favorite.service';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { WsCustomMedPaxEvents } from '../medpaxService/medpaxEventservice.service';
import { ProtocolService } from '../../../ws-services/protocol/protocol.service';
import { asyncScheduler, delay, Observable, shareReplay, tap } from 'rxjs';
import { BsModalService, BsModalRef } from 'ngx-bootstrap/modal';
import { WsProductDetailsPopupComponent } from '../../ws-pdp/ws-product-details-popup/ws-product-details-popup.component';
import { ActivityLogService } from '../../../ws-services/ws-activityLog.service';

@Component({
  selector: 'ws-custom-medpax-table',
  templateUrl: './custom-medpax-table.component.html',
  styleUrl: './custom-medpax-table.component.scss'
})
export class CustomMedpaxTableComponent {
  private favoriteService = inject(FavoriteService);
  private destroyRef = inject(DestroyRef);
  private eventService = inject(WsCustomMedPaxEvents);
  private protocolService = inject(ProtocolService);
  private bsModalService = inject(BsModalService);
  private activityLogService =  inject(ActivityLogService);

savedAsCustomProductCode = computed(()=> this.eventService.forceRefresh());

  selectedProductCodes = input<any>({});
  products = input<any>({});
  entryNumberList = input<any>({});
  medPaxData = input<any>({});

  alerts: any[] = [];
  alertsSignal = signal<any>({});
  bsModalRef?: BsModalRef;
  isBtnDisabled : boolean = false;

  favoriteList: { wishLists?: { wishListEntries: { productCode: string }[] } } = {};
  favoriteListSignal = signal<any>({});
  dosingDataCache: { [productCode: string]: Observable<any> } = {};

  ngOnInit() {
    this.favoriteService.favoriteItems$.pipe(takeUntilDestroyed(this.destroyRef)).subscribe((favorites: any) => {
      favorites?.wishLists?.forEach((element: any) => {
        if(element.name.includes('plpwishlist')) {
          this.favoriteList = {
            wishLists: {
               wishListEntries: [...element.wishListEntries]
            }
         }
         this.favoriteListSignal.set(this.favoriteList);
        }
      });
    });

    this.eventService.enableButton.pipe(takeUntilDestroyed(this.destroyRef)).subscribe((res: any) => {
      this.isBtnDisabled = res;
    });
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
    this.eventService.emitDeleteClick({ EntryNumber: entryNo, product: product })
  }

  AddProduct(prod: any) {
    this.activityLogService.sendLog('medpax_build', {pillDetails : JSON.stringify(prod)});
    this.eventService.emitAddClick(prod);
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

  updateFavorites(item: any, isFav: boolean) {
    if (isFav) {
      this.favoriteService
        .removeFromFavorites(item.code)
        .pipe(takeUntilDestroyed(this.destroyRef))
        .subscribe((res) => {
          item.isFavorite = !item.isFavorite;
          this.add(!isFav);
        });
    } else {
      this.favoriteService
        .addToFavorites(item.code)
        .pipe(takeUntilDestroyed(this.destroyRef))
        .subscribe((res) => {
          item.isFavorite = !item.isFavorite;
          this.add(!isFav);
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
    let product = this.favoriteList?.wishLists?.wishListEntries?.findIndex((product: any) => product.productCode === item.code);
    return product > -1 ? true : false;
  }

  getCustomDosingOnProductLevel(productCode: string, forceRefresh: boolean = false): Observable<any> {
    if (forceRefresh) {
      const freshCall$ = this.protocolService
        .getCustomDosingEntries(productCode)
        .pipe(delay(300, asyncScheduler), shareReplay(1));
  
      this.dosingDataCache[productCode] = freshCall$;
      
      return this.dosingDataCache[productCode].pipe(tap((res:any)=>{
        setTimeout(()=>{
          this.eventService.forceRefresh.set('');
        })
      }));
    }
  
    if (!this.dosingDataCache[productCode]) {
      this.dosingDataCache[productCode] = this.protocolService
        .getCustomDosingEntries(productCode)
        .pipe(delay(300, asyncScheduler), shareReplay(1));
    }
  
    return this.dosingDataCache[productCode];
  }
  
  getMedpaxPriceOnProductLevel(productCode:string){
    const ItemData = this.medPaxData().medpaxEntry.find((item:any)=>item.product.code == productCode);
    return ItemData ? ItemData?.price : false;
  }

  getAddToCartDisableFlag(selectedProduct: any): boolean {
    if(selectedProduct?.isMedpax) {
      if(selectedProduct?.forceOutOfStock) {
        return true
      } else if(selectedProduct?.stock?.stockLevelStatus === 'outOfStock') return true;
    }
    return false;
  }
  
  getProductMaterialFromName(name: any){
    if(name == 'Capsules') return 'capsule';
    if(name == 'Tablets') return 'tablet';
    if(name == 'Softgels') return 'softgel';
    if(name == 'Caplets') return 'caplet';
    if(name == 'Lozenges') return 'lozenge';
    if(name == 'Packets') return 'packet';
    return 'capsule';
  }
}

import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'medpaxPrice'
})
export class MedpaxPricePipe implements PipeTransform {
  transform(productCode: string, medPaxData: any): number {
    const itemData = medPaxData?.medpaxEntry?.find(
      (item: any) => item?.product?.code === productCode
    );
    if(location.pathname.toLowerCase().includes('recommendation')) {
      return itemData ? itemData?.msrpPrice : 0;
    }
    return itemData ? itemData?.price : 0;
  }
}

