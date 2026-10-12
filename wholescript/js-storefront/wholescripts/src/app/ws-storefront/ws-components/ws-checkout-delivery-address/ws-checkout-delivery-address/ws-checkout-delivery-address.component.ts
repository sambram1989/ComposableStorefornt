import { AfterViewInit, ChangeDetectorRef, Component, DestroyRef, ElementRef, inject, Renderer2, signal, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ActiveCartFacade, Cart } from '@spartacus/cart/base/root';
import { CheckoutDeliveryAddressComponent, CheckoutStepService } from '@spartacus/checkout/base/components';
import { CheckoutDeliveryAddressFacade, CheckoutDeliveryModesFacade } from '@spartacus/checkout/base/root';
import { AuthService, CmsService, GlobalMessageService, TranslationService, UserAddressService } from '@spartacus/core';
import { debounceTime, distinctUntilChanged, Observable, Subject, tap } from 'rxjs';
import { WsCheckoutDeliveryAddressService } from '../../../ws-services/ws-checkout-delivery-address.service';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { WsOverlay } from '../../../ws-services/ws-overlay.service';
import { AccountSettingsService } from '../../../ws-services/ws-account-settings.service';
import { WsAddressesService } from '../../../ws-services/ws-addresses.service';
import { WsCurrentUserService } from '../../../ws-services/state-services/currentuser.service';
import { userMap } from '../../../ws-env/environment';

@Component({
  selector: 'ws-checkout-delivery-address',
  templateUrl: './ws-checkout-delivery-address.component.html',
  styleUrl: './ws-checkout-delivery-address.component.scss'
})
export class WsCheckoutDeliveryAddressComponent extends CheckoutDeliveryAddressComponent implements AfterViewInit {
  wsShippingPageTopParagraphComponent$: Observable<any> = this.cmsservice.getComponentData('wsShippingPageTopParagraphComponent');
  wsOrderSummaryCheckoutParagraphComponent$: Observable<any> = this.cmsservice.getComponentData('wsOrderSummaryCheckoutParagraphComponent');
  wsSecureCheckoutParagraphComponent$: Observable<any> = this.cmsservice.getComponentData('wsSecureCheckoutParagraphComponent');

  cart$: Observable<any>;
  private overlayService = inject(WsOverlay);

  authService = inject(AuthService);
  private cmsService = inject(CmsService);
  private currentUser = inject(WsCurrentUserService);
  isPatient = signal<boolean>(false);
  shippingMessage: string = '';
  PATIENT_SHIPPING_PRICE: number = 100;
  PRACTITIONER_SHIPPING_PRICE: number = 300;
  isCurrentUser$: Observable<any> = this.currentUser.isRoleExist('patient').pipe(tap((res: boolean) => this.isPatient.set(res)));
  cartTotalPrice: number = 0;
  shippingPrice = 0;

  searchText: string = '';
  currentPage: number = 0;
  pageSize: number = 10;
  addressesResponse: any;
  private searchSubject = new Subject<string>();
  activeCartId: string;
  private destroyRef = inject(DestroyRef);

  allAddresses: any[] = [];
  allOriginalAddresses: any[] = [];

  searchKeys = [
    'addressDescription',
    'firstName',
    'lastName',
    'formattedAddress'
  ];

  paginationSignal = signal<any>({});
  pagination = {
    itemPerPage: 10,
    totalItems: 0,
    currentPage: 1,
  };
  loader: boolean = false;

  showNewAddress: boolean = false;
  showValidateAddress: boolean = false;
  newAndValidateAddressDetails: any;
  usersSelectedAddress: any = null;

  showRemoveItemsModal: boolean = false
  showAgeRestrictionDetails = signal<boolean>(false); 
  removeItemModalType: 'region' | 'po' | 'restricted' = 'restricted';

  regions: any[] = [];
  notValidProducts: any[] = [];

  isFriendsFamily: boolean = false;

  @ViewChild('newAddressBtn', {static: false}) newAddressBtn?: ElementRef<HTMLButtonElement>;
  constructor(public cmsservice: CmsService,
    userAddressService: UserAddressService,
    checkoutDeliveryAddressFacade: CheckoutDeliveryAddressFacade,
    activatedRoute: ActivatedRoute,
    translationService: TranslationService,
    activeCartFacade: ActiveCartFacade,
    checkoutStepService: CheckoutStepService,
    checkoutDeliveryModesFacade: CheckoutDeliveryModesFacade,
    globalMessageService: GlobalMessageService,
    private wsCheckoutDeliveryAddressService: WsCheckoutDeliveryAddressService,
    private cd: ChangeDetectorRef,
    protected renderer: Renderer2,
    private accountSettingsService: AccountSettingsService,
    private router: Router,
    private activeCartService: ActiveCartFacade,
    private addressesService: WsAddressesService
  ) {
    super(
      userAddressService,
    checkoutDeliveryAddressFacade,
    activatedRoute,
    translationService,
    activeCartFacade,
    checkoutStepService,
    checkoutDeliveryModesFacade,
    globalMessageService
    )
    this.cart$ = this.activeCartFacade.getActive();
    this.activeCartFacade.getActiveCartId().subscribe(cartId => {
      this.activeCartId = cartId;
      this.searchText = '';
      this.getDataForAddresses('');
    })
    this.subscribeSearch();
    this.getRegions();
    this.getFreeshipingDetails();
    const user = JSON.parse(sessionStorage.getItem('user_config'));
    if (user) {
      const key = Object.keys(userMap).find(key => userMap[key] === 'friendsfamily');
      this.isFriendsFamily = (user?.roles || []).includes(key);
    }
  }

  getFreeshipingDetails() {
    this.cmsService.getComponentData('PromotionsOrderParaComp').subscribe((data: any) => {

      this.shippingMessage = data?.content;
      this.shippingMessage = this.shippingMessage;
      this.cd.markForCheck();
    });
    this.activeCartFacade.getActive().subscribe(data => {
      this.cartTotalPrice = data?.subTotal?.value || 0;
      this.cd.markForCheck();
    })
  }

  getDataForAddresses(searchText: string) {
    this.loader = true;
    this.addressesService
      .getListOfCommerceAddresses()
    .pipe(takeUntilDestroyed(this.destroyRef))
    .subscribe((data: any) => {
      this.allOriginalAddresses = data?.addresses || [];
      this.allAddresses = [...this.allOriginalAddresses];
      this.pagination = {
        itemPerPage: 10,
        totalItems: this.allAddresses?.length,
        currentPage: 1,
      };
      this.paginationSignal.set(this.pagination);
      this.loader = false;
      this.cd.detectChanges();
    }, () => {
      this.loader = false;
    })
  }

  getRegions() {
    this.accountSettingsService.getRegions('US').pipe(takeUntilDestroyed(this.destroyRef)).subscribe(data => {
      this.regions = data?.regions || [];
      this.cd.detectChanges();
    })
  }

  subscribeSearch() {
    this.searchSubject.pipe(
      debounceTime(300),
      distinctUntilChanged()
    ).subscribe((searchText) => {
      searchText = searchText.toLowerCase();
      this.allAddresses = this.allOriginalAddresses?.filter(item => {
        for(let key of this.searchKeys) {
          if (item.hasOwnProperty(key) && item[key] !== null && item[key].toString().toLowerCase().includes(searchText)) {
            return true;
          }
        };

        return false;
      });
      this.pagination = {
        itemPerPage: 10,
        totalItems: this.allAddresses.length,
        currentPage: 1,
      };
      this.paginationSignal.set(this.pagination);
      this.cd.detectChanges();
    });
  }

  updateSearch(searchText: string) {
    this.searchText = searchText;
    this.searchSubject.next(searchText);
  }

  clearSearch() {
    this.searchText = '';
    this.searchSubject.next(this.searchText);
  }

  verifyAddress(address: any) {
    this.loader = true;
    const filteredRegion = this.regions?.filter(region => region?.isocode == address?.region?.isocode)?.[0];
    const payload = {
      address: {
        firstName: address.firstName,
        lastName: address.lastName,
        companyName: address.companyName,
        line1: address.line1,
        line2: address.line2,
        town: address.town,
        region: {
          isocode: filteredRegion?.isocode || address?.region,
          isocodeShort: filteredRegion?.isocodeShort,
          countryIso: filteredRegion?.countryIso,
          name: filteredRegion?.name
        },
        postalCode: address.postalCode,
        phone: address.phone,
        cellphone: "",
        email: address.email,
        country: {
          isocode: "US",
          name: "United States"
        }
      }
    };
    this.wsCheckoutDeliveryAddressService.verifyAddress(this.activeCartId, payload)
    .pipe(takeUntilDestroyed(this.destroyRef))
    .subscribe(data => {
      this.loader = false;
      this.selectCurrentAddress(address);
      this.cd.detectChanges();
    }, (error) => {
      this.loader = false;
      if(error?.status === 200) {
        this.selectCurrentAddress(address);
      } else if(error?.status === 422) {
        if(error?.error?.type) {
          const type = error.error.type;
          this.usersSelectedAddress = address; 
          if(type === 'RegionExclusionException') {
            this.removeItemModalType = 'region';
            this.showRemoveItemsModal = true;
            this.notValidProducts = error.error?.entries || [];
          } else if(type === 'POBoxAgeRestrictionException') {
            this.removeItemModalType = 'po';
            this.showRemoveItemsModal = true;
            this.notValidProducts = error.error?.entries || [];
          } else if(type === 'AgeRestrictionException') {
            this.showAgeRestrictionDetails.set(true);
            this.notValidProducts = error.error?.entries || [];
          }
        }
      }
      this.cd.detectChanges();
    })
  }

  ngAfterViewInit(): void {
    if(document.getElementsByTagName('title') && !document.getElementsByTagName('title')[0].innerText) document.getElementsByTagName('title')[0].innerText = 'Shipping Address';
  }

  selectCurrentAddress(address: any) {
    this.loader = true;
    const filteredRegion = this.regions?.filter(region => region?.isocode == address?.region?.isocode)?.[0]?.isocode || address?.region?.isocode;
    const payload = {
      addressDescription: address.addressDescription,
      addressNumber: address.addressNumber,
      country: {
        isocode: address.country?.isocode,
      },
      firstName: address.firstName,
      lastName: address.lastName,
      line1: address.line1,
      line2: address.line2,
      town: address.town,
      region: {
        isocode: filteredRegion,
      },
      postalCode: address.postalCode,
      phone: address.phone,
      cellphone: "",
      defaultAddress: false,
      email: address.email,
      companyName: address.companyName
    }
    this.wsCheckoutDeliveryAddressService.createDeliveryAddress(this.activeCartId, address.addressNumber, payload)
    .pipe(takeUntilDestroyed(this.destroyRef))
    .subscribe(() => {
      this.loader = false;
      this.router.navigateByUrl('checkout/ShippingMethods');
      this.cd.detectChanges();
    }, () => {
      this.loader = false;
      this.cd.detectChanges();
    })
  }

  onAgeVerificationSuccess() {
    if (this.usersSelectedAddress) {
      this.selectCurrentAddress(this.usersSelectedAddress); 
      this.showAgeRestrictionDetails.set(false); 
    }
  }

  closePopup() {
    this.overlayService.closeAllModals();
    this.renderer.removeClass(document.body, 'modal-open');
    const backdrop = document.querySelector('.modal-backdrop');
    if (backdrop) {
      this.renderer.removeClass(backdrop, 'fade');
      this.renderer.removeClass(backdrop, 'in');
      this.renderer.removeClass(backdrop, 'modal-backdrop');
    }
    setTimeout(() => {
      this.newAddressBtn?.nativeElement.focus();  
    }, 50);
  }

  hideNewAddressModal() {
    this.showNewAddress = false;
    this.closePopup();
  }

  hideRemoveItemsModal() {
    this.showRemoveItemsModal = false;
    this.closePopup();
  }

  showValidateAddressModal(addressDetails: any) {
    this.showNewAddress = false;
    this.showValidateAddress = true;
    this.newAndValidateAddressDetails = addressDetails;
  }

  hideValidateAddressModal() {
    this.showValidateAddress = false;
    this.closePopup();
    this.cd.detectChanges();
  }

  setCurrentPage(page: number) {
    this.currentPage = page;
    this.getDataForAddresses(this.searchText);
  }

  refreshDeliveryAddresses() {
    this.getDataForAddresses('');
  }

  pageChanged(event: any) {
    this.pagination.currentPage = event;

    this.pagination = {
      itemPerPage: 10,
      totalItems: this.allAddresses.length,
      currentPage: event,
    };
    this.paginationSignal.set(this.pagination);
  }

  showDeliveryAddress() {
    this.showAgeRestrictionDetails.set(false);
  }

  removeItems() {
    this.loader = true;
    let entryNumbers = this.notValidProducts?.map(product => product?.entryNumber)?.join(',') || '';
    this.wsCheckoutDeliveryAddressService.removeAllItems(this.activeCartId, entryNumbers)
    .pipe(takeUntilDestroyed(this.destroyRef))
    .subscribe(() => {
      this.loader = false;
      this.showAgeRestrictionDetails.set(false);
      this.showRemoveItemsModal = false;
      this.activeCartService.reloadActiveCart();
      this.router.navigate(['/cart']);
      this.cd.detectChanges();
    }, () => {
      this.loader = false;
      this.cd.detectChanges();
    })
  }
}
