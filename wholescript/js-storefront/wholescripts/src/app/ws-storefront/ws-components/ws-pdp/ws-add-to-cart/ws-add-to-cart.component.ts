import {
  ChangeDetectorRef,
  Component,
  DestroyRef,
  inject,
  Input,
  Optional,
} from '@angular/core';
import { UntypedFormControl } from '@angular/forms';
import {
  ActiveCartFacade
} from '@spartacus/cart/base/root';
import { CmsAddToCartComponent, EventService } from '@spartacus/core';
import {
  CmsComponentData,
  CurrentProductService,
  ProductListItemContext,
} from '@spartacus/storefront';
import { PdpDataservice } from '../data-pdp/pdpData.service';
import { map, Observable, of, switchMap, take } from 'rxjs';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { WsCartResponseModifier } from '../../../ws-services/ws-checkout/ws-cart-response-modifier.service';
import { WsCurrentUserService } from '../../../ws-services/state-services/currentuser.service';

declare var klaviyo: any;
@Component({
  selector: 'cx-add-to-cart',
  templateUrl: './ws-add-to-cart.component.html',
  styleUrl: './ws-add-to-cart.component.scss',
})
export class WsAddToCartComponent {
  private activeCartService = inject(ActiveCartFacade);
  private destroyRef = inject(DestroyRef);
  private responseModifier = inject(WsCartResponseModifier);
  private currentUser = inject(WsCurrentUserService);
  @Input() control: UntypedFormControl;
  product$ = this.currentProductService.getProduct() as unknown as Observable<any | null>;
  cartItems: any[] = [];
  quantity: number = 1;
  alert: any = {};
  currentUserRole$: Observable<any> = this.currentUser.currentState();
  role: any;
  cartIsLoading$: Observable<boolean> = this.activeCartService
      .isStable()
      .pipe(map((loaded) => !loaded));

  constructor(
    private pdpService: PdpDataservice,
    protected currentProductService: CurrentProductService,
    protected cd: ChangeDetectorRef,
    protected component: CmsComponentData<CmsAddToCartComponent>,
    protected eventService: EventService,
    @Optional()
    protected overrideproductListItemContext?: ProductListItemContext
  ) {
    this.currentUserRole$.pipe(takeUntilDestroyed(this.destroyRef)).subscribe(role => {
      this.role =
        role.indexOf('practitioner') > -1 ? 'practitioner' : 'patient';
    })
  }

  addProductToCart(item: any) {
    this.activeCartService
      .getActiveCartId()
      .pipe(switchMap((cartId: any) => {
        const cartAction = cartId?.trim()?.length
          ? of(cartId)
          : this.pdpService.createCart();
        return cartAction;
      }))
      .pipe(switchMap((resolvedCartID: any) => 
        this.pdpService.addToCart(resolvedCartID,item.code,this.pdpService.autoShipFrequency(),this.quantity )),takeUntilDestroyed(this.destroyRef))
      .subscribe((data: any) => {
        this.activeCartService.reloadActiveCart();
        this.addCart();
        this.pushAddToCartToKlaviyo(item);
        this.cd.detectChanges();
      });
 
  }

  onClosed(): void {
    this.alert = {};
  }

  addQuantity() {
    if(Number(this.quantity) < 9999) this.quantity = Number(this.quantity) + 1;
  }
  removeQuantity() {
    if (Number(this.quantity) > 1) this.quantity = Number(this.quantity) - 1;
  }

  setQuantity(ev: any) {
    const value = ev.target.value;
  
    if (value !== '') {
      const reg = new RegExp(/^\d+$/);
      if (!reg.test(value)) {
        ev.target.value = value.replace(/\D/g, '');
      }
  
      if (value.length >= 1 && value.startsWith('0')) {
        ev.target.value = value.replace(/^0+/, '');
      }

      this.quantity = ev.target.value;
    }
  }

  addCart(): void {
    this.alert = {
      type: 'info',
      msg: `Product added to cart`,
      timeout: 3000,
    };
  }

  pushAddToCartToKlaviyo(product: any) {
    if (typeof klaviyo !== 'undefined' && klaviyo) {
      let productImageUrl = '';
      if (product?.images?.PRIMARY?.product?.url) {
        productImageUrl = product.images.PRIMARY.product.url;
      }
      const baseUrl = window.location.origin;
      let cart$ = this.activeCartService.takeActive().pipe(
        map((cart: any) => this.responseModifier.mapCartEntries(cart))
      );
      cart$
      .subscribe((cart) => {
        
        const products = cart.entries.map((en: any) => {
          if(!en?.isMedPax){
            return {
              ProductID: en.product?.code,
              SKU: en.product?.code,
              ProductName: en.product?.name,
              Quantity: en.quantity,
              ItemPrice: en.quantity, 
              RowTotal: en.totalPrice?.value,
              ProductURL: `${baseUrl}${en.product.url.replace('///', '/').replace('//', '/')}}`,
              ImageURL: en.product?.images ?
                (en.product?.images['PRIMARY'] as any)?.product?.url : '',
              ProductCategories: en.product.categories?.map(
                (category: any) => category.name
              ),
            }
          } else {
            return {
              ProductID: en?.actualDetails?.product?.code,
              SKU: en?.actualDetails?.product?.code,
              ProductName: en?.actualDetails?.product?.name,
              Quantity: en?.actualDetails?.quantity,
              ItemPrice: en?.actualDetails?.quantity, 
              RowTotal: en?.actualDetails?.totalPrice?.value,
              ProductURL: `${baseUrl}${en?.actualDetails?.product.url.replace('///', '/').replace('//', '/')}}`,
              ImageURL: en?.actualDetails?.product?.images ?
                (en?.actualDetails?.product?.images['PRIMARY'] as any)?.product?.url : '',
              ProductCategories: en?.actualDetails?.product.categories?.map(
                (category: any) => category.name
              ),
            }
          }
        });
        const klaviyoCart = {
          $value: cart?.totalPrice?.value,
          AddedItemProductName: product.name,
          AddedItemProductID: product.code,
          AddedItemSKU: product.code,
          AddedItemCategories: product.categories.map(
            (category: any) => category.name
          ),
          AddedItemImageURL: productImageUrl,
          AddedItemURL: `${baseUrl}${product.url.replace('///', '/').replace('//', '/')}`,
          AddedItemPrice: this.role == 'practitioner' ? product.wholeSalePrice?.value : product.retailPrice?.value,
          AddedItemQuantity: this.quantity,
          ItemNames: cart.entries.map((e: any) => { //Name of the Items in the cart
                      if(!e?.isMedPax) return e?.product?.name
                      else return e?.actualDetails?.product?.name
                     }),
          CheckoutURL: baseUrl + '/cart',
          Items: products, //All the Items in the cart
        };
        klaviyo.push(['track', 'Added to Cart', klaviyoCart]);
        console.log("Klaviyo 'Added to Cart' event tracked:", klaviyoCart);
      });
    }
  }

  getAddToCartDisableFlag(selectedProduct: any): boolean {
    if(selectedProduct?.isMedpax) {
      if(selectedProduct?.stock?.stockLevelStatus === 'outOfStock') return true;
    } else if(selectedProduct?.stock?.forceOutOfStock){
        return true;
    }
    return false;
  }

  setQuantityLimit(event: any){
    if(event.target.value === '') {
      event.target.value = '1';
      this.quantity = 1;
    }
  }
}
