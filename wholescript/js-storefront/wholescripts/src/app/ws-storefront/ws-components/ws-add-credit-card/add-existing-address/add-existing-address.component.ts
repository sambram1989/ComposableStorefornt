import {
  Component,
  computed,
  DestroyRef,
  inject,
  input,
  signal,
} from '@angular/core';
import { WsAddCreditCardService } from '../ws-add-credit-card.service';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { CheckoutDeliveryAddressFacade } from '@spartacus/checkout/base/root';
import { BASE_PATH } from '../../../ws-constants/ws-api-endpoint.constant';
import { getUserData } from '../../../ws-services/state-services/currentuser.service';

@Component({
  selector: 'ws-add-existing-address',
  templateUrl: './add-existing-address.component.html',
  styleUrl: './add-existing-address.component.scss',
})
export class AddExistingAddressComponent {
  images = [
    { name: 'American Express', src: BASE_PATH('amex') },
    { name: 'Visa', src: BASE_PATH('visa') },
    { name: 'Mastercard', src: BASE_PATH('mastercard') },
    { name: 'Discover', src: BASE_PATH('discover') },
  ];
  //Injections
  private service = inject(WsAddCreditCardService);
  private checkoutDeliveryAddress = inject(CheckoutDeliveryAddressFacade);
  private destroyRef = inject(DestroyRef);
  private currentUserData: any = inject(getUserData);

  mainCard = signal<any>({});
  AddressList = signal<any>({});
  //input
  isAccountSettings = input<boolean>(false);
  newAddressAdded = input<any>({});
  //computed
  selectedCard = computed(() => this.service.userSelectedAddress());

  loader = signal<boolean>(false);
  isMainCardSelected = signal<boolean>(true);

  searchText = '';
  orgUnitId: string;

  ngOnInit() {
    this.currentUserData.subscribe((res: any) => {
      this.orgUnitId = res.orgUnit?.uid?.replace(/^0+/, '');
    });
    this.checkoutDeliveryAddress
      .getDeliveryAddressState()
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe((response: any) => {
        this.mainCard.set(response?.data);
        this.getAllAddresses();
      });
      
  }

  updateCurrentCard(type: 'Main' | 'Ship') {
    if (type == 'Main') return;
    this.isMainCardSelected.set(false);
  }

  updateSelectedCard(addr: any) {
    this.searchText = '';
    this.service.userSelectedAddress.set(addr);
  }


  updateSearch(key: string) {
   this.searchText = key
  }

  getAllAddresses() {
    this.service.getListOfAddresses()
    .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: (res: any) => {
          this.loader.set(true);
          this.AddressList.set(res?.addresses || []);
          let mainAddress = this.AddressList().find(
            (data: any) => data?.addressNumber == this.orgUnitId
          )
          if(this.service.userSelectedAddress()?.addressNumber) {
            this.updateSelectedCard(this.service.userSelectedAddress());
          } else if(this.newAddressAdded()?.id && res?.addresses?.length > 0) {
            let addedAddress = this.AddressList().find(
              (data: any) => data?.id == this.newAddressAdded()?.id
            )
            this.updateSelectedCard(addedAddress);
          } else if(!mainAddress?.addressNumber && res?.addresses?.length > 0){
            this.updateSelectedCard(res?.addresses?.[res?.addresses.length - 1]);
          } else {
            this.mainCard.set(mainAddress);
            this.updateSelectedCard(mainAddress);
          }
        },
      });
  }



}
