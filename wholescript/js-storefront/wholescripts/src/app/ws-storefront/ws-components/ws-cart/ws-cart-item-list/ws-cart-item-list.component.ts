import { ChangeDetectorRef, Component, DestroyRef, inject, Input, input } from '@angular/core';
import { UntypedFormControl, UntypedFormGroup } from '@angular/forms';
import { CartItemListComponent } from '@spartacus/cart/base/components';
import { ActiveCartFacade, SelectiveCartFacade, MultiCartFacade, OrderEntry } from '@spartacus/cart/base/root';
import { UserIdService } from '@spartacus/core';
import { WsCartEvents } from '../ws-cartEvents-Service/ws-cartevent.service';
import { Observable, startWith, tap, map, distinctUntilChanged, filter, switchMap, debounceTime, of } from 'rxjs';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { Router } from '@angular/router';

@Component({
  selector: 'ws-cart-item-list',
  templateUrl: './ws-cart-item-list.component.html',
  styleUrl: './ws-cart-item-list.component.scss',
})

export class WsCartItemListComponent extends CartItemListComponent {
  private cartEvents = inject(WsCartEvents);
  private destroyRef = inject(DestroyRef);
  custInput = input<any>();
  promoCodeEntries: number[] = [];
  cartItems: any[] = [];
  cart: any;

  counterSubscription = new Map<string, boolean>();

  constructor(activeCartService: ActiveCartFacade,
    selectiveCartService: SelectiveCartFacade,
    userIdService: UserIdService,
    multiCartService: MultiCartFacade,
    cd: ChangeDetectorRef, private router:Router) {

    super(activeCartService,
      selectiveCartService,
      userIdService,
      multiCartService,
      cd);
    this.getCartItems();
  }

  getCartItems() {
    this.activeCartService
      .getActive()
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe((cart) => {
        this.cart = cart;
        this.promoCodeEntries = [];
        if (cart?.appliedProductPromotions?.length > 0) {
          cart?.appliedProductPromotions?.map(promos => {
            promos?.consumedEntries?.map(entry => {
              this.promoCodeEntries.push(entry?.orderEntryNumber);
            })
          })
        }
      });
  }

  protected override createForm(): void {
    this._items.forEach((item) => {
      const controlName = this.getControlName(item);
      const control = this.form.get(controlName);
      const currentItem:any = item;
      const newItem = currentItem?.isMedPax ? currentItem?.actualDetails : currentItem;
      const qty =  currentItem?.isMedPax ? newItem?.abstractOrderQty : newItem.quantity ;
      
      if (control) {
        if (control.get('quantity')?.value !== qty) {
          control.patchValue({ quantity: qty }, { emitEvent: false });
        }
      } else {
        const group = new UntypedFormGroup({
          entryNumber: new UntypedFormControl(newItem.entryNumber),
          quantity: new UntypedFormControl(qty, { updateOn: 'blur' }),
        });
        this.form.addControl(controlName, group);
      }

      // If we disable form group before adding, disabled status will reset
      // Which forces us to disable control after including to form object
      if (!item.updateable || this.readonly) {
        this.form.controls[controlName].disable();
      }
    });
  }
  getQuantityControl(item: OrderEntry): UntypedFormControl {
    const controlGroup = this.form.get(this.getControlName(item)) as UntypedFormGroup;
    return controlGroup?.get('quantity') as UntypedFormControl;
  }
  openRemoveAll() {
    this.cartEvents.emitRemoveAllClick();
  }

  override getControl(item: OrderEntry): Observable<UntypedFormGroup> | undefined {
    const controlName = this.getControlName(item);
     const control = this.form.get(controlName);
     if(!control) return undefined
   
    if(!this.counterSubscription.has(controlName)) {
        this.counterSubscription.set(controlName, true);
        control?.valueChanges.pipe(
          debounceTime(500),
          distinctUntilChanged(),
          takeUntilDestroyed(this.destroyRef),
          filter((value: any) => !!value),
          startWith(null),
          tap((value) => {
            let currentItemIsMedPax = false;
            const foundItem: any = this.custInput()?.find((res: any) => 
              item.entryNumber == (res?.entryNumber || res?.actualDetails?.entryNumber)
            );
            currentItemIsMedPax = foundItem?.isMedPax;
            const currentItem: any = foundItem?.isMedPax ? foundItem?.actualDetails : foundItem;
            if (item?.updateable && value && !this.readonly) {
              if (this.options.isSaveForLater) {
                this.selectiveCartService.updateEntry(
                  value.entryNumber,
                  value.quantity
                );
              } else if (this.cartId && this.userId ) {
                if (currentItemIsMedPax && (value?.quantity !==0)) {
                  this.activeCartService.takeActiveCartId().pipe(
                    switchMap((cartId:any)=> this.cartEvents
                    .updateMedpaxproductQty(cartId, currentItem.entryNumber, currentItem.medpaxId, value?.quantity))
                  ).pipe(takeUntilDestroyed(this.destroyRef))
                    .subscribe(()=>this.activeCartService.reloadActiveCart())
                } else {
                  this.multiCartService.updateEntry(
                    this.userId,
                    this.cartId,
                    value.entryNumber,
                    value.quantity
                  );
                }

              } else {
                if (currentItemIsMedPax && (value?.quantity !==0)) {
                  this.activeCartService.takeActiveCartId().pipe(
                    filter(()=> foundItem?.isMedPax && !!value?.quantity && (value.quantity !== currentItem?.abstractOrderQty)),
                    switchMap((cartId:any)=> this.cartEvents
                    .updateMedpaxproductQty(cartId, currentItem.entryNumber, currentItem.medpaxId, value?.quantity))
                  ).pipe(takeUntilDestroyed(this.destroyRef))
                    .subscribe(()=>this.activeCartService.reloadActiveCart())
                } else {
                  let quantity = value.quantity >= 9999 ? 9999 : value.quantity;
                  if(!this.cartEvents.isRemoveCalled && quantity === 0) {
                    quantity = 1;
                  };
                  this.cartEvents.isRemoveCalled = false;
                  this.activeCartService.updateEntry(
                    value.entryNumber,
                    quantity
                  );
                }
              }
            }
          })
        ).subscribe();
      }
    return of(control as UntypedFormGroup);
  }

  override rerenderChangedItems(
    items: OrderEntry[],
    options?: { forceRerender?: boolean }
  ) {
    let offset = 0;
    for (
      let i = 0;
      i - offset < Math.max(items.length, this._items.length);
      i++
    ) {
      const index = i - offset;
      if (
        options?.forceRerender ||
        JSON.stringify(this._items?.[index]) !== JSON.stringify(items[index])
      ) {
        if (this._items[index]) {
          this.form?.removeControl(this.getControlName(this._items[index]));
          // is removed , delete subscription
          this.counterSubscription.delete(this.getControlName(this._items[index]));
        }
        if (!items[index]) {
          this._items.splice(index, 1);
          offset++;
        } else {
          this._items[index] = items[index];
        }
      }
    }
  }

  handleKeyDown(event: KeyboardEvent, elementType: string, index?: number): void {
    const focusableElements = Array.from(document.querySelectorAll('[tabindex="0"]')) as HTMLElement[];

    switch (event.key) {
      case 'ArrowDown': // Move to next item
        event.preventDefault();
        if (index !== undefined && index < this.cartItems.length - 1) {
          focusableElements[index + 1]?.focus();
        }
        break;

      case 'ArrowUp': // Move to previous item
        event.preventDefault();
        if (index !== undefined && index > 0) {
          focusableElements[index - 1]?.focus();
        }
        break;

      case 'Enter': // Perform action based on element type
        event.preventDefault();
        if (elementType === 'continueShopping') {
          this.navigateToCatalog();
        } else if (elementType === 'removeAll') {
          this.openRemoveAll();
        } else if (elementType === 'checkout') {
          this.proceedToCheckout();
        }
        break;
    }
  }
  navigateToCatalog() {
    this.router.navigate(['/products']);
  }
  proceedToCheckout() {
    this.router.navigate(['/checkout']);
  }
}
