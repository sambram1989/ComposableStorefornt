import {
  ChangeDetectorRef,
  Component,
  computed,
  DestroyRef,
  ElementRef,
  inject,
  signal,
  TemplateRef,
  ViewChild,
  ViewContainerRef,
} from '@angular/core';
import { CartDetailsComponent } from '@spartacus/cart/base/components';
import { CartConfigService } from '@spartacus/cart/base/core';
import {
  ActiveCartFacade,
  SelectiveCartFacade,
} from '@spartacus/cart/base/root';
import { AuthService, CmsService, RoutingService } from '@spartacus/core';
import { WsCartEvents } from '../ws-cartEvents-Service/ws-cartevent.service';
import { TemplatePortal } from '@angular/cdk/portal';
import { WsOverlay } from '../../../ws-services/ws-overlay.service';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { catchError, Observable, of, take, tap } from 'rxjs';
import { getUserData } from '../../../ws-services/state-services/currentuser.service';

declare var klaviyo: any;
const remove = 'remove';

@Component({
  selector: 'ws-cart',
  templateUrl: './cart.component.html',
  styleUrl: './cart.component.scss'
})
export class CartComponent extends CartDetailsComponent {
  private cmsService = inject(CmsService);
  private cartEventsService = inject(WsCartEvents);
  private destroyRef = inject(DestroyRef);
  private overlayService = inject(WsOverlay);
  private vcr = inject(ViewContainerRef);
  @ViewChild('firstElement') firstElement?: ElementRef;
  private lastFocusedElement: HTMLElement | null = null;

  @ViewChild('remove') remove: TemplateRef<any>;
  @ViewChild('removeAll') removeAll: TemplateRef<any>;

  removePortal: any;
  removeAllPortal: any;

  currentProduct = signal<any>({});
  cartData$ = this.cmsService.getComponentData(
    'wsCartTopHeadingParagraphComponent'
  );

  isUserLoggedIn = signal<boolean>(false);
  isUserLoggedIn$:Observable<any>= this.authService.isUserLoggedIn().pipe(tap((res:boolean)=> this.isUserLoggedIn.set(res) ));

  screenWidth = window.innerWidth;
  modalWidth = this.screenWidth * 0.2 + 'px';

  isLoading = computed(()=> this.cartEventsService.isLoading());

  userDetails: any;
  public currentUser$ = inject(getUserData);
  
  constructor(
    activeCartService: ActiveCartFacade,
    selectiveCartService: SelectiveCartFacade,
    authService: AuthService,
    routingService: RoutingService,
    cartConfig: CartConfigService,
    private cd: ChangeDetectorRef
  ) {
    super(
      activeCartService,
      selectiveCartService,
      authService,
      routingService,
      cartConfig
    );
    this.activeCartService.reloadActiveCart();
  }

  ngAfterViewInit() {
    this.removePortal = new TemplatePortal(this.remove, this.vcr);
    this.removeAllPortal = new TemplatePortal(this.removeAll, this.vcr);
    this.activeCartService.getLoading().pipe(takeUntilDestroyed(this.destroyRef),catchError(()=>{this.cartEventsService.isLoading.set(true); return of({})})).subscribe((loading:any)=>{
      this.cartEventsService.isLoading.set(loading);
    })
    this.customEventsHandler();
    this.pushAddToCartToKlaviyo();
    this.currentUser$.subscribe((data: any) => {
      this.userDetails = data;
      this.cd.detectChanges();
    });
    setTimeout(() => {
      this.firstElement?.nativeElement?.focus();
    }, 0);
    if(document.getElementsByTagName('title') && !document.getElementsByTagName('title')[0].innerText) document.getElementsByTagName('title')[0].innerText = 'Cart';
  }

  customEventsHandler() {
    this.cartEventsService.OpenRemoveProductPopup.pipe(
      takeUntilDestroyed(this.destroyRef)
    ).subscribe((res: any) => {
      this.removeProduct(res);
    });
    this.cartEventsService.RemoveAll.pipe(
      takeUntilDestroyed(this.destroyRef)
    ).subscribe(() => {
      this.removeAllProduct();
    });
  }

  removeProduct(product: any) {
    this.lastFocusedElement = document.activeElement as HTMLElement;
    this.currentProduct.set(product);
    this.overlayService.openModal(this.removePortal, this.modalWidth);
  }

  removeAllProduct() {
    this.lastFocusedElement = document.activeElement as HTMLElement;
    this.overlayService.openModal(this.removeAllPortal, this.modalWidth);
  }

  closePopup() {
    this.overlayService.closeTopModal();
    setTimeout(() => {
      this.lastFocusedElement?.focus();
    }, 0);
  }

  Delete(type: string) {
    if (type.toLowerCase().trim() == remove) {
      this.cartEventsService.emitRemoveClick(this.currentProduct());
      this.cartEventsService.emitRemove();
    } else {
      this.cartEventsService.deleteActiveCart()
    }
    this.closePopup();
  }

  pushAddToCartToKlaviyo() {
    if (typeof klaviyo !== 'undefined' && klaviyo) {
      let productImageUrl = '';

      const baseUrl = window.location.origin;
      this.activeCartService
        .getActive()
        .pipe(take(1))
        .subscribe((cart) => {
          const totalValue = cart.totalPriceWithTax?.value;
          const itemNames = cart.entries.map((e) => e.product.name);
          const categories = cart.entries
            .map((e) => e.product.categories.map((c) => c.name))
            .flat();
          // Generate the event ID
          const eventId = `${27832374}_${Date.now()}`;

          // Map the items to the required format
          const items = cart.entries.map((item: any) => ({
            ProductID: item.product?.code?.toString(),
            SKU: item.product?.code?.toString(),
            ProductName: item.product?.name,
            Quantity: item.quantity,
            ItemPrice: item.product?.yourPrice?.value,
            RowTotal: item.totalPrice.value,
            ProductURL: `${baseUrl}${item.product.url.replace('///', '/')}`,
            ImageURL: item.product?.images?.PRIMARY?.product?.url || '',
            ProductCategories: item.product?.categories?.map(
              (category: any) => category.name
            ),
          }));

          const klaviyoCart = {
            $event_id: eventId,
            $value: totalValue,
            ItemNames: itemNames,
            CheckoutURL: window.location.href,
            Categories: categories,
            Items: items,
          };

          klaviyo.push(['track', 'Started Checkout', klaviyoCart]);
          console.log("Klaviyo 'Started Checkout' event tracked:", klaviyoCart);
        });
    }
  }
}
