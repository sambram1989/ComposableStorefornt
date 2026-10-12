import { Component, DestroyRef, ElementRef, inject, Input, input, OnChanges, signal, SimpleChanges, ViewChild } from '@angular/core';
import { AbstractControl, AsyncValidatorFn, FormControl, ValidationErrors, Validators } from '@angular/forms';
import { CartItemComponent, CartItemContextSource } from '@spartacus/cart/base/components';
import { ActiveCartFacade, CartAddEntrySuccessEvent, CartItemContext, OrderEntry } from '@spartacus/cart/base/root';
import { WsCartEvents } from '../ws-cartEvents-Service/ws-cartevent.service';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { BreakpointObserver } from '@angular/cdk/layout';

export function quantityValidator(min: number, max: number): AsyncValidatorFn {
  return (control: AbstractControl): Observable<ValidationErrors | null> => {
    const value = control.value;

    if (value < min) {
      return of({ minError: { requiredMin: min } }).pipe(delay(500));
    }
    if (value > max) {
      return of({ maxError: { requiredMax: max } }).pipe(delay(500));
    }

    return of(null); 
  };
}

interface OrderEntryMedpax extends OrderEntry {
  medpaxName?: string;
  shippingDelayFlag?: boolean
} 

@Component({
  selector: '[ws-cart-item-list-row],ws-cart-item-list-row',
  templateUrl: './cart-item-list-row.component.html',
  styleUrl: './cart-item-list-row.component.scss',
  providers: [
    CartItemContextSource,
    { provide: CartItemContext, useExisting: CartItemContextSource },
  ],
})
export class WsCartItemListRowComponent extends CartItemComponent {
  private cartEventsService = inject(WsCartEvents);
  private destroyRef = inject(DestroyRef);
  private cartService = inject(ActiveCartFacade);
  @Input() override item: any;
  isMobile = false;
  autoShipFrequency: string = 'One-Time';
  autoShipFrequencyData: any[] = [];
  name = new FormControl("asa");
  dropDown = new FormControl('newest');
  activeCartId: any;
  cartItems: any[] = [];

  isMedPax = input<boolean>(false);
  cartId = input<string>('');
  qtyUpdated = signal<boolean>(false);
  changeFrequency = signal<any>([]);
  currentValue = 0;
  frequency = new FormControl('');
  isUserLoggedIn: boolean = true;

  constructor(cartItemContextSource: CartItemContextSource,
     private eventService: EventService , 
     private bObserver: BreakpointObserver,private router:Router, protected authService: AuthService,) {
    super(cartItemContextSource)
    this.bObserver.observe('(max-width: 599px)').subscribe((result) => {
      this.isMobile = false;
      if (result.matches) {
        this.isMobile = true;
      }
    });
    this.authService.isUserLoggedIn().subscribe((data: boolean) => {
      this.isUserLoggedIn = data;
    })
  }

  removeProduct(product: any): void {
    this.cartEventsService.isRemoveCalled = true;
    this.cartEventsService.emitOpenRemoveProductPopupClick(product);
  }

  ngAfterViewInit() {
    const item: any = this.item;
    this.autoShipFrequencyData = item?.product?.autoShipFrequency || [];
    this.autoShipFrequency = item?.autoShipCode || 'One-Time';
    this.cartService.getActiveCartId().subscribe(cartId => {
      this.activeCartId = cartId;
    })
    this.cartEventsService.Remove.pipe(takeUntilDestroyed(this.destroyRef)).subscribe((res: any) => {
      if (res.entryNumber == this.item.entryNumber) {
        super.removeItem();
      }
    });
    this.cartService.getEntries().subscribe((res) =>
      res.forEach((el: any) => {
        this.cartItems.push(el.product.code);
      })
    );
    this.frequency.setValue(this.autoShipFrequency, { emitEvent: false });
    this.changeFrequency.set(this.autoShipFrequencyData);
    this.frequency.valueChanges.pipe(takeUntilDestroyed(this.destroyRef)).subscribe((res: string) => {
    this.changeAutoShipFrequency(res);
    });
  }

  changeAutoShipFrequency(event: any) {
    const value = event || '';
    let itemQty = this.quantityControl.value;
    if(this.isMedPax()) {
      itemQty = 1;
    }
    const userId = this.isUserLoggedIn ? 'current' : 'anonymous';
    this.cartEventsService.updateEnties(this.activeCartId, this.item.entryNumber, value, itemQty, userId)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe(() => {
        this.cartService.reloadActiveCart();
       })
  }
  handleEnterKey(event: Event): void {
    const keyboardEvent = event as KeyboardEvent;
    const key = keyboardEvent.key;
    if (key === ' ') {
      keyboardEvent.preventDefault();
      return;
    }
      if (key !== 'Enter') {
      return;
    }
    keyboardEvent.preventDefault();
    const target = keyboardEvent.target as HTMLElement;
    const focusType = target.getAttribute('data-cx-focus');
    if (focusType === 'increment' || focusType === 'decrement') {
      target.click();
    }
  }
  

  navigateOnEnter(event: KeyboardEvent, product:any): void {
    if (event.key === 'Enter') {
    this.router.navigate([`/product/${product?.code}/${product?.name}`])
    event.preventDefault();
  }
}
}

import { Pipe, PipeTransform } from '@angular/core';
import { AuthService, EventService } from '@spartacus/core';
import { delay, filter, Observable, of, tap } from 'rxjs';
import { WsOccCartEntryAdapter } from '../../../ws-services/ws-cart-test/ws-cart-adapter.service';
import { Router } from '@angular/router';

@Pipe({
  name: 'hasMedpax'
})
export class HasMedpaxPipe implements PipeTransform {
  transform(otherAvailableProducts: any[]): boolean {
    if (!Array.isArray(otherAvailableProducts)) {
      return false;
    }

    return otherAvailableProducts.some(
      (res: any) => res?.productDisplyName?.toLowerCase() === 'medpax'
    );
  }
}

