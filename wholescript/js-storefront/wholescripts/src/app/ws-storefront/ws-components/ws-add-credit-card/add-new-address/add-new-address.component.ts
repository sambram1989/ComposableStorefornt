import { Component, DestroyRef, inject, input, OnDestroy, signal } from '@angular/core';
import { WsAddNewCardService } from '../../ws-order/ws-addnewCard-form.service';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { map, Observable, tap } from 'rxjs';
import { AccountSettingsService } from '../../../ws-services/ws-account-settings.service';
import { WsAddCreditCardService } from '../ws-add-credit-card.service';
import { WsAddressesService } from '../../../ws-services/ws-addresses.service';


@Component({
  selector: 'ws-add-new-address',
  templateUrl: './add-new-address.component.html',
  styleUrl: './add-new-address.component.scss',
})
export class AddNewAddressComponent implements OnDestroy {
  private service = inject(WsAddCreditCardService);
  private addNewCardService = inject(WsAddNewCardService);
  private destroyRef = inject(DestroyRef);
  private accountSettingsService = inject(AccountSettingsService);
  private addressService= inject(WsAddressesService)
  //country Array
  countries = [{ code: 'US', value: 'United States' }];
  //input
  isAccountSettings = input<boolean>(false);

  addCardForm = this.addNewCardService.addNewcard;

  addNewAddressError = signal<boolean>(false);
  submitted = signal<boolean>(false);

  regions$: Observable<any> = this.accountSettingsService.getRegions('US').pipe(
    map((data: any) =>
      data?.regions?.map((item: any) => ({
        code: item.isocode,
        value: item.name,
      }))
    ),
    tap((regions) => {
      this.addCardForm.controls.country.setValue('US');
      if (regions?.length) {
        this.addCardForm.controls.state.setValue(null);
      }
    })
  );
  closeApi() {
    this.submitted.set(true);
    const payload = {
      firstName: this.addCardForm.value.firstName,
      lastName: this.addCardForm.value.lastName,
      line1: this.addCardForm.value.streetAddress,
      line2: this.addCardForm.value.apartment,
      town: this.addCardForm.value.city,
      region: {
        isocode: this.addCardForm.value.state,
      },
      postalCode: this.addCardForm.value.postalCode,
      country: {
        isocode: this.addCardForm.value.country,
        name: 'United States',
      },
    };
    this.addNewAddressError.set(false);
    if (this.addCardForm.valid) {
      this.addressService
        .validateAddress(payload)
        .pipe(takeUntilDestroyed(this.destroyRef))
        .subscribe({
          next: (data: any) => {
            if (data?.decision === 'ACCEPT') {
              this.service.emitaddAddressButton({newDeliveryAddress: this.addCardForm.value, validatedAddressResponse: data});
            } else {
              this.addNewAddressError.set(true);
            }
          },
          error: () => {
            this.addNewAddressError.set(true);
          },
        });
    } else {
      this.addCardForm.markAllAsTouched();
    }
  }

  closeMe() {
    this.service.emitcloseMeButton();
  }

  addAddress(addr: any) {
    this.service.emitaddAddressButton(addr);
  }

  ngOnDestroy() {
    this.addNewCardService.addNewcard.reset();
  }
  
}
