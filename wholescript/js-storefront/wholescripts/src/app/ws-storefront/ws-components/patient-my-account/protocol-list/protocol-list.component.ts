import { TemplatePortal } from '@angular/cdk/portal';
import { ChangeDetectorRef, Component, DestroyRef, EventEmitter, inject, Input, Output, TemplateRef, ViewChild, ViewContainerRef } from '@angular/core';
import { WsOverlay } from '../../../ws-services/ws-overlay.service';
import { PatientProtocolListService } from '../service/patient-protocol-listing.service';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { Router } from '@angular/router';
import { ActiveCartFacade } from '@spartacus/cart/base/root';
import { map } from 'rxjs';
import { BreakpointObserver } from '@angular/cdk/layout';
import { WsCartResponseModifier } from '../../../ws-services/ws-checkout/ws-cart-response-modifier.service';
import { RecommendationService } from '../../../ws-services/ws-recommendation.service';

declare var klaviyo: any;
@Component({
  selector: 'ws-protocol-list',
  templateUrl: './protocol-list.component.html',
  styleUrl: '../patient-my-account-protocol/patient-my-account-protocol.component.scss'
})
export class ProtocolListComponent {
  private vcr = inject(ViewContainerRef);
  private overlayService = inject(WsOverlay);
  private service = inject(PatientProtocolListService);
  private activeCartService = inject(ActiveCartFacade);
  private router = inject(Router);
  private destroyRef = inject(DestroyRef);
  private responseModifier = inject(WsCartResponseModifier);
  private recommendationService = inject(RecommendationService);
  
  @Input('protocolList') protocolList: any;
  @Output() emitAddToCartEvent = new EventEmitter<any>();
  @ViewChild('moreInfoPopUp') moreInfoPopUp: TemplateRef<any>;
  moreInfoPopUpPortal: any;
  productsData: any;
  medPaxProtocol: any;
  isMobile!: boolean;
  toastMessage: string = '';
    
  constructor(private bObserver: BreakpointObserver, private cdr: ChangeDetectorRef) {
    this.bObserver.observe('(max-width: 599px)').subscribe((result) => {
      this.isMobile = false;
      if (result.matches) {
        this.isMobile = true;
      }
    });
  }



  ngAfterViewInit() {
    this.moreInfoPopUpPortal = new TemplatePortal(this.moreInfoPopUp, this.vcr);
  }
  openPopUp(id: string) {
    this.service.currentProtocolId = id;
    let width = '55%';
    const isTablet = window.innerWidth > 767 && window.innerWidth < 1025; 
    if(isTablet) width = '80%';
    this.overlayService.openModal(this.moreInfoPopUpPortal, width, '95vh', true);
  }
  closePopup() {
    this.overlayService.closeTopModal();
  }

  showToasterMsg(msg: string) {
    this.toastMessage = msg;
    this.cdr.detectChanges();
    this.resetToastMessage();
  }

  resetToastMessage() {
    setTimeout(() => {
      this.toastMessage = '';
      this.cdr.detectChanges();
    }, 4000)
  }

  addToCart(protocol: any) {
    this.service.addToCart(protocol?.id).pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: () => {
          this.emitAddToCartEvent.emit();
          this.activeCartService.reloadActiveCart();
          protocol.protocolEntries.forEach((item: any) => {
            this.pushAddToCartToKlaviyo(item.product, item.quantity);
          });
          this.recommendationService.getMedPaxForOwner(protocol?.id)
            .pipe(takeUntilDestroyed(this.destroyRef))
            .subscribe((res: any) => {
              this.medPaxProtocol = res;
              if (this.medPaxProtocol) this.pushMedpaxAddToCartToKlaviyo();
            });
        }
      });;
  }

  pushAddToCartToKlaviyo(product: any, quantity: any) {
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
              ItemPrice: en.totalPrice?.value, 
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
              ItemPrice: en?.actualDetails?.totalPrice?.value, 
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
          AddedItemPrice: product.retailPrice?.value,
          AddedItemQuantity: quantity,
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

  pushMedpaxAddToCartToKlaviyo() {
    if (typeof klaviyo !== 'undefined' && klaviyo) {
      const baseUrl = window.location.origin;
      let cart$ = this.activeCartService.takeActive().pipe(
        map((cart: any) => this.responseModifier.mapCartEntries(cart))
      );
      cart$
        .subscribe((cart) => {

          const products = cart.entries.map((en: any) => {
            if (!en?.isMedPax) {
              return {
                ProductID: en.product?.code,
                SKU: en.product?.code,
                ProductName: en.product?.name,
                Quantity: en.quantity,
                ItemPrice: en.totalPrice?.value,
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
                ItemPrice: en?.actualDetails?.totalPrice?.value,
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
            AddedItemProductName: this.medPaxProtocol?.name,
            AddedItemProductID: '000000000500000030',
            AddedItemSKU: '000000000500000030',
            AddedItemCategories: this.medPaxProtocol?.medpaxEntry.map(
              (item: any) => item?.product?.name
            ),
            AddedItemImageURL: 'https://api.c1lud0zblk-xymogenin1-d1-public.model-t.cc.commerce.ondemand.com/medias/65x65-0008394-medpax-carton.png?context=bWFzdGVyfHdob2xlc2NyaXB0c0ltYWdlc3w1NTcyfGltYWdlL3BuZ3xhR05pTDJobE15ODRPRFE1TXpFMk5qYzFOakUwTHpZMWVEWTFYekF3TURnek9UUmZiV1ZrY0dGNExXTmhjblJ2Ymk1d2JtY3wzNTQwYzVhYTMzYmQwY2ExMGVjNjE4MzE2NzNkNDJhMDU3YWY3NGZiYjg3MmEzZGMyNTdjMTEyZGZmOGNmZDk4',
            AddedItemURL: baseUrl + '/medpax',
            AddedItemPrice: this.medPaxProtocol?.totalPrice,
            AddedItemQuantity: 1,
            ItemNames: cart.entries.map((e: any) => { //Name of the Items in the cart
              if (!e?.isMedPax) return e?.product?.name
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
}
