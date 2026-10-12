import { Component, inject, input, SimpleChanges, DestroyRef, ChangeDetectorRef, OnInit } from '@angular/core';
import { Location } from '@angular/common';
import { WsCustomMedPaxService } from '../../custom-medpax/medpaxService/customMedpaxservice.service';
import { map, Observable, of } from 'rxjs';
import { PatientProtocolListService } from '../../patient-my-account/service/patient-protocol-listing.service';
import { ActiveCartFacade } from '@spartacus/cart/base/root';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { WsCartResponseModifier } from '../../../ws-services/ws-checkout/ws-cart-response-modifier.service';
import { Config } from '@spartacus/core';

declare var klaviyo: any;
@Component({
  selector: 'ws-medpax-protocol-review-card',
  templateUrl: './medpax-protocol-review-card.component.html',
  styleUrl: './medpax-protocol-review-card.component.scss',
})
export class MedpaxProtocolReviewCardComponent implements OnInit {
  protocolId = input<string>();
  data = input<any>(null);
  isProtocol = input<boolean>(false);
  isWSProtocol = input<boolean>(false);
  private customMedPaxService = inject(WsCustomMedPaxService);
  private location = inject(Location);
  private service = inject(PatientProtocolListService);
  private activeCartService = inject(ActiveCartFacade);
  private destroyRef = inject(DestroyRef);
  private cdr = inject(ChangeDetectorRef);
  private responseModifier = inject(WsCartResponseModifier);
  protected config =  inject(Config);

  isRecommendation = this.location.path().toLowerCase()?.includes('recommendation');
  isPatientProtocol = this.location.path().toLowerCase()?.includes('customer/protocol');

  apiData$: Observable<any>;
  alert: any = {};
  baseURL: string;

  ngOnInit(): void {
    this.baseURL = this.config.backend?.occ?.baseUrl
  }

  ngOnChanges(changes: SimpleChanges) {
    if (changes) {
      if (this.data() !== null) {
        this.apiData$ = of(this.data());
      } else {
        this.isRecommendation = this.isProtocol() ? false : this.isRecommendation;
        if(this.isWSProtocol()){
          this.apiData$ = this.customMedPaxService.getWholeScriptsProtocolMedPaxForOwner(
            this.protocolId()
          );
        } else {
          this.apiData$ = this.customMedPaxService.getMedPaxForOwner(
            this.protocolId(),(this.isProtocol() ? false : this.isRecommendation ? true : false)
          );
        }
      }
    }
  }

  addToCart(medpax: any) {    
    this.service.addToCart(this.protocolId()).pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: () => {
          this.activeCartService.reloadActiveCart();
          this.showMsg();
          this.pushMedpaxAddToCartToKlaviyo(medpax);
        }
    });
  }

  showMsg(): void {
    this.alert = {
      type: 'info',
      msg: `Product added to cart`,
      timeout: 2000,
    };
    this.cdr.markForCheck();
  }

  onClosed(): void {
    this.alert = {};
  }

  getProductImageUrl(image: any): string | undefined {
    return image?.url ? `${this.baseURL}${image.url}` : '../../../../../assets/images/private-label/pl-generic-icon.png';
  }

  pushMedpaxAddToCartToKlaviyo(medpax: any) {
    if (typeof klaviyo !== 'undefined' && klaviyo) {
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
          AddedItemProductName: medpax?.name,
          AddedItemProductID: '000000000500000030',
          AddedItemSKU: '000000000500000030',
          AddedItemCategories: medpax?.medpaxEntry.map(
            (item: any) => item?.product?.name
          ),
          AddedItemImageURL: 'https://api.c1lud0zblk-xymogenin1-d1-public.model-t.cc.commerce.ondemand.com/medias/65x65-0008394-medpax-carton.png?context=bWFzdGVyfHdob2xlc2NyaXB0c0ltYWdlc3w1NTcyfGltYWdlL3BuZ3xhR05pTDJobE15ODRPRFE1TXpFMk5qYzFOakUwTHpZMWVEWTFYekF3TURnek9UUmZiV1ZrY0dGNExXTmhjblJ2Ymk1d2JtY3wzNTQwYzVhYTMzYmQwY2ExMGVjNjE4MzE2NzNkNDJhMDU3YWY3NGZiYjg3MmEzZGMyNTdjMTEyZGZmOGNmZDk4',
          AddedItemURL: baseUrl + '/medpax',
          AddedItemPrice: medpax?.totalPrice,
          AddedItemQuantity: 1,
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
}
