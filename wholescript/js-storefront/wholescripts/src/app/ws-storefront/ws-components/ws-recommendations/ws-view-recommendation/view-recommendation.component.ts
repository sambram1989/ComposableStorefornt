import { ChangeDetectorRef, Component, DestroyRef, inject, TemplateRef, ViewChild, ViewContainerRef } from '@angular/core';
import { RecommendationService } from '../../../ws-services/ws-recommendation.service';
import { ActiveCartFacade, OrderEntry } from '@spartacus/cart/base/root';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { WsOverlay } from '../../../ws-services/ws-overlay.service';
import { TemplatePortal } from '@angular/cdk/portal';
import { Config } from '@spartacus/core';
import { map } from 'rxjs';
import { WsCartResponseModifier } from '../../../ws-services/ws-checkout/ws-cart-response-modifier.service';
import { Router } from '@angular/router';

declare var klaviyo: any;
@Component({
  selector: 'ws-view-recommendation',
  templateUrl: './view-recommendation.component.html',
  styleUrl: './view-recommendation.component.scss',
})
export class ViewRecommendationComponent {
  private destroyRef = inject(DestroyRef);
  private overlay = inject(WsOverlay);
  private vcr = inject(ViewContainerRef);
  private responseModifier = inject(WsCartResponseModifier);
  private router = inject(Router);
  patientRecommendations: any[] = [];
  showDetails: boolean = false;
  expandedItem: any;
  selected: any;
  userDetails: any;
  cartItems: OrderEntry[] = [];
  isOutOfStock: boolean = false;
  showAlert: boolean = false;
  deleteRecommendationAlert: boolean = false;
  baseURL: string = '';

  @ViewChild('deleteRecommendationModal') deleteRecommendationModal: TemplateRef<any>;
  deleteRecommendationModalPortal: any;
  currentRecommendation: any;
  medPaxProtocol: any;
  activeIndex: number | null = null;

  constructor(private recommendationService: RecommendationService, private cdRef: ChangeDetectorRef,
    private cartService: ActiveCartFacade,
    protected config: Config,
  ) {
    this.getPageInfo();
    this.baseURL = this.config.backend?.occ?.baseUrl
    ? this.config.backend?.occ?.baseUrl
    : '';
  }

  ngAfterViewInit() {
    this.deleteRecommendationModalPortal = new TemplatePortal(this.deleteRecommendationModal, this.vcr);
  }

  openDeletePopup(rec: any) {
    this.currentRecommendation = rec;
    const isMobile = window.innerWidth <= 768; 
    const height = 'auto';
    const width = isMobile ? '100%' : '50%';
    this.overlay.openModal(this.deleteRecommendationModalPortal, width, height, true);
  }

  accordianView(item: any,data:any) {
    this.activeIndex = this.activeIndex === item ? null : item;
    if(data?.status == 'SENT') this.markRecommendationViewed(item,data);
    else this.toggleAccordion(item);
  }

  markRecommendationViewed(item:any, selectedRecommendation:any){
      this.recommendationService.patientViewed(selectedRecommendation?.id, '').pipe(takeUntilDestroyed(this.destroyRef)).subscribe(res=>{
        if(res){
            selectedRecommendation.status = res?.status;
            this.toggleAccordion(item);
        }
      });
  }

  toggleAccordion(item:any){
    let outOfStockItems = this.patientRecommendations[item].recommendationEntries.filter((el: any) => el.product?.stock?.stockLevelStatus !== 'inStock');
    this.isOutOfStock = outOfStockItems.length > 0;
    if (this.expandedItem === item) {
      this.patientRecommendations[item].isSelected = false;
      this.expandedItem = null;
      return;
    }
    this.patientRecommendations.forEach((el: any, index: number) => {
      el.isSelected = index === item;
      this.expandedItem = item;
    });
    this.cdRef.detectChanges();
  }
  getMedpaxEntries() {
    this.patientRecommendations.forEach((item) => {
      this.recommendationService.getMedPaxForOwner(item.id, true).subscribe((medpax: any) => {
        if (medpax?.medpaxEntry?.length) {
          item.medpax = medpax;
          item.isMedpax = true;
        } else {
          item.isMedpax = false;
        }
        this.cdRef.detectChanges();
      })
    });
    console.log(this.patientRecommendations)
  }
  getPageInfo() {
    this.recommendationService.getUserDetails().subscribe((res) => {
      this.userDetails = res;
      this.cdRef.detectChanges();
    })
    this.recommendationService.fetchWholesaleRecommendations().subscribe((res) => {
      this.patientRecommendations = res.recommendations;
      this.patientRecommendations[0].isSelected = true;
      this.getMedpaxEntries();
    });

  }
  deleteRecommendation(id: string) {
    this.recommendationService.deleteRecommendationId(id).subscribe(() =>{
      this.closePopup();
      this.deleteRecommendationAlert = true;
       this.getPageInfo();
      });
  }

  getMedPaxCartonImageUrl(item: any): string | undefined {
    return item?.image?.url 
    ? `${this.baseURL}${item?.image?.url}` 
    : `assets/images/private-label/pl-generic-icon.png`;
  }

  onClosed(): void {
    this.showAlert = false;
    this.deleteRecommendationAlert = false;
  }
  addToCart(recommendation: any) {
    this.recommendationService.addToCartRecommendation(recommendation?.id)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: () => {
          this.cartService.reloadActiveCart();
          this.showAlert = true;
          recommendation.recommendationEntries.forEach((item: any) => {
            this.pushAddToCartToKlaviyo(item.product, item.quantity);
          });
          this.recommendationService.getMedPaxForOwner(recommendation?.id, true)
            .pipe(takeUntilDestroyed(this.destroyRef))
            .subscribe((res: any) => {
              this.medPaxProtocol = res;
              if (this.medPaxProtocol) this.pushMedpaxAddToCartToKlaviyo();
              this.router.navigate(['cart']);
            });
        }
      })
  }

  closePopup() {
    this.overlay.closeAllModals();
  }
  gotopage(url: any) {}

  pushAddToCartToKlaviyo(product: any, quantity: any) {
    if (typeof klaviyo !== 'undefined' && klaviyo) {
      let productImageUrl = '';
      if (product?.images?.PRIMARY?.product?.url) {
        productImageUrl = product.images.PRIMARY.product.url;
      }
      const baseUrl = window.location.origin;
      let cart$ = this.cartService.takeActive().pipe(
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
      let cart$ = this.cartService.takeActive().pipe(
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
