import { DestroyRef, inject, Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { catchError, filter, map, switchMap, take } from 'rxjs/operators';
import { Observable, of } from 'rxjs';
import {
  EventService,
  LoginEvent,
  OccEndpointsService,
  UserIdService,
} from '@spartacus/core';
import {
  ActiveCartFacade,
  CartAddEntrySuccessEvent,
  CartRemoveEntrySuccessEvent,
} from '@spartacus/cart/base/root';
import { NavigationStart, Router } from '@angular/router';
import { OrderFacade } from '@spartacus/order/root';
import { CheckoutDeliveryModeSetEvent } from '@spartacus/checkout/base/root';
import { UserProfileFacade } from '@spartacus/user/profile/root';
import { DOCUMENT } from '@angular/common';
import { WsCartResponseModifier } from './ws-checkout/ws-cart-response-modifier.service';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { WsCurrentUserService } from './state-services/currentuser.service';

interface ActivityLog {
  event: string;
  message: string;
}

const ACTIVITY_EVENTS: ActivityLog[] = [
  {
    event: 'add_to_cart',
    message: 'Item {productName} added to cart with qty {quantity}',
  },
  {
    event: 'remove_from_cart',
    message: 'Item {productName} removed from cart',
  },
  { event: 'promo_add', message: 'Promo {promoName} applied' },
  {
    event: 'order_created',
    message:
      'Order created: order # {orderNumber}. Capture Order create response',
  },
  {
    event: 'cart_checkout_shipping',
    message: 'Shipping method selected: {shippingMethod}',
  },
  {
    event: 'customer_asm',
    message: 'Asm session started by store owner {emailId}',
  },
  { event: 'address_add', message: 'Added {address}' },
  { event: 'address_remove', message: 'Removed address {address}' },
  {
    event: 'cc_add',
    message: 'Added card {cardType} card ends with {lastFourDigits}',
  },
  {
    event: 'cc_remove',
    message: 'Removed card {cardType} card ends with {lastFourDigits}',
  },
  {
    event: 'cc_declined',
    message: 'Declined card {cardType} card ends with {lastFourDigits}',
  },
  { event: 'ws_login', message: 'Customer logged in' },
  { event: 'ws_logout', message: 'Customer logged out' },
  { event: 'medpax_build', message: 'Pills added/removed {pillDetails}' },
  {
    event: 'medpax_addtocart',
    message: 'Medpax added to cart. Include all the item details',
  },
  {
    event: 'protocol_create',
    message: 'Protocol created. Include item details',
  },
  {
    event: 'autoship_edit',
    message: 'Autoship changed for {frequency}, {date}',
  },
  { event: 'autoship_cancel', message: 'Autoship {autoshipId} canceled' },
  {
    event: 'cc_approve',
    message:
      'Amount ${amount} authorized with {cardType} card ends with {lastFourDigits}',
  },
  {
    event: 'ach_token_payment',
    message: 'ach payment tokenize failed',
  },
  {
    event: 'ach_auth_payment',
    message: 'ach payment authrization failed',
  },
];
declare var klaviyo: any;
@Injectable({
  providedIn: 'root',
})
export class ActivityLogService {
  private eventService = inject(EventService);
  private activeCartService = inject(ActiveCartFacade);
  private http = inject(HttpClient);
  private occEndpoints = inject(OccEndpointsService);
  private router = inject(Router);
  private orderFacade = inject(OrderFacade);
  private userIdService = inject(UserIdService);
  private userProfile = inject(UserProfileFacade);
  private document = inject(DOCUMENT);
  private responseModifier = inject(WsCartResponseModifier);
  private currentUser = inject(WsCurrentUserService);
  private destroyRef = inject(DestroyRef);
  currentUserRole$: Observable<any> = this.currentUser.currentState();
  role: any;

  initActivityLog() {
    this.watchEvents();
  }

  sendLog(event: string, params: Record<string, string | number> = {}) {
    const activity = ACTIVITY_EVENTS.find((e) => e.event === event);
    if (!activity) {
      console.error('Event not found:', event);
      return;
    }

    let message = activity.message;
    Object.keys(params).forEach((key) => {
      message = message.replace(`{${key}}`, String(params[key]));
    });

    const payload: ActivityLog = { event, message };
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    const apiUrl = this.occEndpoints.buildUrl('activityLog', {
      urlParams: { userId: 'current' },
    });

    this.http
      .post(apiUrl, payload, { headers })
      .pipe(
        catchError((error) => {
          console.error('Failed to send log:', error);
          return of(null);
        })
      )
      .subscribe();
  }

  watchEvents(): void {
    //add to cart
    this.eventService.get(CartAddEntrySuccessEvent).subscribe((cart) => {
      this.currentUserRole$.pipe(takeUntilDestroyed(this.destroyRef)).subscribe(role => {
        this.role =
          role.indexOf('practitioner') > -1 ? 'practitioner' : 'patient';
      })
      this.pushAddToCartToKlaviyo(cart.entry.product, cart.quantity);
      this.sendLog('add_to_cart', {
        productName: cart.entry?.product?.name,
        quantity: cart.quantity,
      });
    });
    // remove from cart
    this.eventService
      .get(CartRemoveEntrySuccessEvent)
      .subscribe((event: any) => {
        this.activeCartService
          .getEntries()
          .pipe(take(1))
          .subscribe((entries) => {
            const removedEntry = entries.find(
              (entry) => entry.entryNumber.toString() === event.entryNumber
            );
            const productName =
              removedEntry?.product?.name || 'Unknown Product';

            this.sendLog('remove_from_cart', { productName });
          });
      });
    // login
    this.eventService.get(LoginEvent).subscribe((_: any) => {
      this.sendLog('ws_login');
    });
    // logout
    this.router.events.subscribe((event) => {
      if (event instanceof NavigationStart) {
        if (event.url === '/logout') {
          this.sendLog('ws_logout');
        }
      }
    });
    //order placed
    this.orderFacade
      .getOrderDetails()
      .pipe(
        filter((order) => !!order && !!order.code), // Filter out empty or incomplete orders
        take(1) // Take only the first emission (order placed)
      )
      .subscribe((order) => {
        this.sendLog('order_created', { orderNumber: order.code });
      });
    //CheckoutDeliveryModeSetEvent
    this.eventService
      .get(CheckoutDeliveryModeSetEvent)
      .subscribe((CheckoutDeliveryMode: any) => {
        this.sendLog('cart_checkout_shipping', {
          shippingMethod: CheckoutDeliveryMode.deliveryModeCode,
        });
      });
    //asm session
    this.listenToAsmSessionStart();
  }

  private listenToAsmSessionStart() {
    this.userIdService
      .isEmulated()
      .pipe(
        filter((isEmulated) => isEmulated),
        switchMap(() => this.userProfile.get()),
        filter((res: any) => res?.uid)
      )
      .subscribe((res: any) => {
        this.addBodyClass();
        this.sendLog('customer_asm', { emailId: res.uid });
      });
  }

  addBodyClass() {
    this.document.body.classList.add(`ws-emulated`);
  }

  pushAddToCartToKlaviyo(product: any, qty: number) {
    if (typeof klaviyo !== 'undefined' && klaviyo) {
      let productImageUrl = product?.images?.find((x: any) => x?.format == 'product')?.url;
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
            AddedItemImageURL: productImageUrl ? `${baseUrl}${productImageUrl.replace('///', '/').replace('//', '/')}` : '',
            AddedItemURL: `${baseUrl}${product.url.replace('///', '/').replace('//', '/')}`,
            AddedItemPrice: product.yourPrice?.value,
            AddedItemQuantity: qty,
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
