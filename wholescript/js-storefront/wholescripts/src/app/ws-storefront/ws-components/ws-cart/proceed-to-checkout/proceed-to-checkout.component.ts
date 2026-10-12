import { ChangeDetectorRef, Component, DestroyRef, inject, Renderer2 } from '@angular/core';
import { CartProceedToCheckoutComponent } from '@spartacus/cart/base/components';
import { AuthService, RoutingService } from '@spartacus/core';
import { BsModalService } from 'ngx-bootstrap/modal';
import { BrandAgreementPopupComponent } from '../brand-agreement-popup/brand-agreement-popup.component';
import { WsCheckoutDeliveryAddressService } from '../../../ws-services/ws-checkout-delivery-address.service';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { Router } from '@angular/router';
import { WsOverlay } from '../../../ws-services/ws-overlay.service';
import { WsCartResponseModifier } from '../../../ws-services/ws-checkout/ws-cart-response-modifier.service';
import { ActiveCartFacade } from '@spartacus/cart/base/root';

@Component({
  selector: 'ws-proceed-to-checkout',
  templateUrl: './proceed-to-checkout.component.html',
})
export class ProceedToCheckoutComponent extends CartProceedToCheckoutComponent {
 authService = inject(AuthService);
 private bsModalService = inject(BsModalService);
 private wsCheckoutDeliveryAddressService = inject(WsCheckoutDeliveryAddressService)
 private destroyRef = inject(DestroyRef);
 private routingService = inject(RoutingService);
 private overlayService = inject(WsOverlay);
 private responseModifier = inject(WsCartResponseModifier);
 showCartItemsRemovalProducts: boolean = false;
 cartData: any;

 normalProductsWithOOS: any[] = [];
 medPaxProdcuts: any[] = [];
 medPaxFormulas: any[] = [];

 allUnavailableProducts: any[] = [];

 constructor(
  protected override router: Router,
  protected override cd?: ChangeDetectorRef,
  private renderer?: Renderer2,
  private activeCartService?: ActiveCartFacade
) {
   super(router, cd);
   this.activeCartService.getActive().subscribe((cart: any) =>{
    this.cartData = this.responseModifier.mapCartEntries(cart);
    this.normalProductsWithOOS = [];
    this.medPaxProdcuts = [];
    this.medPaxFormulas = [];
    this.allUnavailableProducts = [];
    this.cartData?.entries?.map((entry: any) => {
      if(entry?.isMedPax) {
        if(entry?.actualDetails?.product?.stock?.forceOutOfStock || entry?.actualDetails?.product?.stock?.stockLevelStatus === 'outOfStock') {
          this.medPaxProdcuts.push(entry?.actualDetails);
          this.allUnavailableProducts.push('Medpax: ' + entry?.actualDetails?.medpaxName);
        } else if(entry?.subEntries?.length > 0) {
          let inStockFormulasCount: number = 0;
          const outOfStockFormulas: any[] = [];
          entry?.subEntries?.map((subEntry: any) => {
            subEntry?.entries?.map((medPaxFormula: any) => {
              if(medPaxFormula?.product?.stock?.forceOutOfStock || medPaxFormula?.product?.stock?.stockLevelStatus === 'outOfStock') {
                outOfStockFormulas.push({...medPaxFormula, label: subEntry?.label, medPaxID: entry?.actualDetails?.medpaxId});
              } else {
                inStockFormulasCount += 1;
              }
            })
          })
          if(inStockFormulasCount < 3) {
            this.medPaxProdcuts.push(entry?.actualDetails);
            this.allUnavailableProducts.push('Medpax: ' + entry?.actualDetails?.medpaxName);
            this.allUnavailableProducts = this.allUnavailableProducts.concat(...outOfStockFormulas?.map(oosf => oosf?.product?.name));
          } else {
            this.medPaxFormulas = this.medPaxFormulas.concat(...outOfStockFormulas);
            this.allUnavailableProducts.push('Medpax: ' + entry?.actualDetails?.medpaxName);
            this.allUnavailableProducts = this.allUnavailableProducts.concat(...outOfStockFormulas?.map(oosf => oosf?.product?.name));
          }
        }
      } else {
        if(entry?.product?.stock?.forceOutOfStock) {
          this.normalProductsWithOOS.push(entry);
          this.allUnavailableProducts.push(entry?.product?.name);
        }
      }
    });

  });
 }

 override disableButtonWhileNavigation(): void {
  // Logic for popup
  this.cartValidationInProgress = true;
   if (this.normalProductsWithOOS?.length > 0 || this.medPaxProdcuts?.length > 0 || this.medPaxFormulas?.length > 0) {
     this.checkForRemovableProducts();
   } else {
     this.brandVerification();
   }
  }

  brandVerification(){
    let payLoad = {
      "brandValidation": true
    }

    this.wsCheckoutDeliveryAddressService.verifyAddress('current', payLoad)
    .pipe(takeUntilDestroyed(this.destroyRef))
    .subscribe(() => {},
    (error)=>{
      if(error?.status === 200) {
        this.routingService.go({
          cxRoute: 'checkoutDeliveryAddress'
        })
      } else if(error?.status === 422) {
        const type = error.error.type;
        console.log(type);
        if(type == 'BrandAgreementException'){
          this.openPopup(error?.error?.brandEntries)
        }
        this.cartValidationInProgress = false;
      } else {
        this.cartValidationInProgress = false;
      }
      this.cd.detectChanges();
    })
  }

  openPopup(res: any){
    let modalRef = this.bsModalService.show(BrandAgreementPopupComponent)
    modalRef.content.brandContent = res;
  }

  checkForRemovableProducts() {
    this.showCartItemsRemovalProducts = true;
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
  }

  hideRemoveItemsModal() {
    this.showCartItemsRemovalProducts = false;
    this.cartValidationInProgress = false;
    this.closePopup();
  }

  verifyBrand() {
    this.brandVerification();
  }
}
