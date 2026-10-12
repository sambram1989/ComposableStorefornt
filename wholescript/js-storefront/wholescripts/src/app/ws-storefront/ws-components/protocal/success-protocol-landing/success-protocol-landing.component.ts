import { ChangeDetectorRef, Component, computed, DestroyRef, inject, Input, OnInit, signal, SimpleChanges } from '@angular/core';
import { ActiveCartFacade, MultiCartFacade } from '@spartacus/cart/base/root';
import { ProtocolService } from '../../../ws-services/protocol/protocol.service';
import { Router } from '@angular/router';
import { HttpParams } from '@angular/common/http';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { WsCurrentUserService } from '../../../ws-services/state-services/currentuser.service';
import { map, Observable, of } from 'rxjs';
import { PractitionerMyPatientListingService } from '../../ws-practitioner/ws-My-patients/my-patient-landing/practitioner-my-patient-listing.service';
import { WsCartResponseModifier } from '../../../ws-services/ws-checkout/ws-cart-response-modifier.service';
import { WsCustomMedPaxService } from '../../custom-medpax/medpaxService/customMedpaxservice.service';

declare var klaviyo: any;
@Component({
  selector: 'ws-success-protocol-landing',
  templateUrl: './success-protocol-landing.component.html',
  styleUrl: './success-protocol-landing.component.scss'
})
export class SuccessProtocolLandingComponent implements OnInit {
 
  private protocolService = inject(ProtocolService);
  private router = inject(Router);
  private destroyRef = inject(DestroyRef);
  private currentUser = inject(WsCurrentUserService);
  private PatientService = inject(PractitionerMyPatientListingService);
  private cd = inject(ChangeDetectorRef);
  private activeCartService = inject(ActiveCartFacade);
  private responseModifier = inject(WsCartResponseModifier);
  private customMedpaxService = inject(WsCustomMedPaxService);

  @Input() protocolId: any;
  @Input() selectedProduct: any;
  @Input() caller = false;
  @Input() isComeFromWs = false;
  isWholescriptsProtocol = false; 
  isProtocoDetailsPage: boolean = false;

  productsData: any;

  totalPrice = signal<string>('');
  totalMsrpPrice = signal<string>('');
  description = signal<string>('');

  currentUser$: Observable<any> = this.currentUser.currentState();
  patientList: any;
  medPaxProtocol: any;

  ngOnInit(): void {
   if (localStorage.getItem('navigatedFromWholeScriptsProtocol')) {
    this.isWholescriptsProtocol = true
    localStorage.removeItem('navigatedFromWholeScriptsProtocol');
   }
   this.getPatientList();
   if (this.router.url.includes('/protocol/details')) {
      this.isProtocoDetailsPage = true;
      this.cd.detectChanges(); 
    }
  }
  ngOnChanges(changes: SimpleChanges) {
    if (changes) {
      this.productsData = this.selectedProduct?.protocolEntries?.map((data: any) => {
        return {
          product: data?.product,
          quantity: data?.quantity
        }
      });
      this.totalPrice.set(this.selectedProduct?.totalPrice?.formattedValue);
      this.totalMsrpPrice.set(this.selectedProduct?.msrpSubTotalPrice?.formattedValue);
      this.description.set(this.selectedProduct?.description);
    }

  }

  addToCart() {
    this.protocolService.addToCartProtocol(this.protocolId).pipe(takeUntilDestroyed(this.destroyRef)).subscribe((res: any) =>{
      this.selectedProduct.protocolEntries.forEach((item: any) => {
        this.pushAddToCartToKlaviyo(item.product, item.quantity);
      });
      this.customMedpaxService.getMedPaxForOwner(this.protocolId)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe((res: any) => {
        this.medPaxProtocol = res;
        if(this.medPaxProtocol) this.pushMedpaxAddToCartToKlaviyo();
        this.router.navigate(['cart']);
      });
    });
  }

  recommendThisProtocol() {
    let params = new HttpParams();
    params = params.append('protocolId', this.protocolId);
    params = params.append('name', this.selectedProduct?.name);
    this.protocolService.protocoltorecommendation(params).pipe(takeUntilDestroyed(this.destroyRef)).subscribe((res: any) => {
      this.router.navigate([
        `/SearchPatientAdvanced/newrecommendation/${this.protocolId}`
      ])
    });
  }

  getPatientList(){
    this.PatientService
      .getPatientList()
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe((res: any) => {
        this.patientList = res
        this.cd.detectChanges();
      });
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
          AddedItemPrice: product.wholeSalePrice?.value,
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
