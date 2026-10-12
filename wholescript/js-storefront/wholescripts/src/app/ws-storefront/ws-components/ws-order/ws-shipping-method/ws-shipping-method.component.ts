import { ChangeDetectorRef, Component, inject, signal, TemplateRef, ViewChild, ViewContainerRef } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Cart, ActiveCartFacade } from '@spartacus/cart/base/root';
import {
  CheckoutConfigService,
  CheckoutDeliveryModeComponent,
  CheckoutStepService,
} from '@spartacus/checkout/base/components';
import {
  CheckoutDeliveryAddressFacade,
  CheckoutDeliveryModesFacade,
} from '@spartacus/checkout/base/root';
import { AuthService, CmsService } from '@spartacus/core';
import { filter, map, Observable, tap, withLatestFrom } from 'rxjs';
import { UntypedFormBuilder } from '@angular/forms';
import { WsCheckoutDeliveryModesService } from '../../../ws-services/ws-checkout/ws-checkout-delivery-modes/ws-checkout-delivery-modes.service';
import { WsOccCheckoutDeliveryModesAdapter } from '../../../ws-services/ws-checkout/ws-checkout-delivery-modes/ws-checkout-delivery-mode.adapter';
import { WsCurrentUserService } from '../../../ws-services/state-services/currentuser.service';
import { WsOverlay } from '../../../ws-services/ws-overlay.service';
import { TemplatePortal } from '@angular/cdk/portal';
import { HttpClient } from '@angular/common/http';
import { userMap } from '../../../ws-env/environment';

@Component({
  selector: 'ws-shipping-method',
  templateUrl: './ws-shipping-method.component.html',
  styleUrl: './ws-shipping-method.component.scss',
  providers: [
    {
      provide: CheckoutDeliveryModesFacade,
      useClass: WsCheckoutDeliveryModesService,
    },
  ],
})
export class WsShippingMethodComponent extends CheckoutDeliveryModeComponent {
  private overlayService = inject(WsOverlay);
  private http = inject(HttpClient);
  selectedShippingMethod: any;
  shippingMethodPortal: any;
  screenWidth = window.innerWidth;
  modalWidth = this.screenWidth * 0.30 + 'px';
  pickupAgreement: any = {};
  allAddress: any;

  @ViewChild('shippingMethodModal') shippingMethodModal!: TemplateRef<any>;
  private vcr = inject(ViewContainerRef);
  
  private wsDeliverymodeAdapter = inject(WsOccCheckoutDeliveryModesAdapter);
  cart$: Observable<any> = this.activeCartFacade.getActive();
  deliveryAddress$ = this.checkoutDeliveryAddress.getDeliveryAddressState();
  addresses$ = this.deliveryAddress$.subscribe(resp => {
    this.allAddress = resp.data;
   });

  showAlert = signal<boolean>(true);

  authService = inject(AuthService);
  private cmsService = inject(CmsService);
  private currentUser = inject(WsCurrentUserService);
  isPatient = signal<boolean>(false);
  shippingMessage: string = '';
  PATIENT_SHIPPING_PRICE: number = 100;
  PRACTITIONER_SHIPPING_PRICE: number = 300;
  isCurrentUser$: Observable<any> = this.currentUser.isRoleExist('patient').pipe(tap((res: boolean) => this.isPatient.set(res)));
  cartTotalPrice: number;
  shippingPrice = 0;
  showTaxWarningMessage: boolean = false;

  isFriendsFamily: boolean = false;

  //Overriden to stop initial API CALL
  override supportedDeliveryModes$ = this.checkoutDeliveryModesFacade
  .getSupportedDeliveryModes()
  .pipe(
    filter((deliveryModes) => !!deliveryModes?.length),
    withLatestFrom(this.selectedDeliveryModeCode$),
    tap(([deliveryModes, code]) => {
      if (
        !code ||
        !deliveryModes.find((deliveryMode) => deliveryMode.code === code)
      ) {
        code =
          this.checkoutConfigService.getPreferredDeliveryMode(deliveryModes);
      }
      if (code) {
        this.mode.controls['deliveryModeId'].setValue(code);
        // this.changeMode(code);
      }
    }),
    map(([deliveryModes]) =>
      deliveryModes.filter((mode) => mode.code !== 'pickup')
    )
  );
  ngAfterViewInit() {
    this.shippingMethodPortal = new TemplatePortal(this.shippingMethodModal, this.vcr);
    if(document.getElementsByTagName('title') && !document.getElementsByTagName('title')[0].innerText) document.getElementsByTagName('title')[0].innerText = 'Shipping Methods';
  }

  constructor(
    public cmsservice: CmsService,
    private checkoutDeliveryAddress: CheckoutDeliveryAddressFacade,
    fb: UntypedFormBuilder,
    checkoutConfigService: CheckoutConfigService,
    activatedRoute: ActivatedRoute,
    checkoutStepService: CheckoutStepService,
    checkoutDeliveryModesFacade: CheckoutDeliveryModesFacade,
    activeCartFacade: ActiveCartFacade,
    private cd: ChangeDetectorRef
  ) {
    super(
      fb,
      checkoutConfigService,
      activatedRoute,
      checkoutStepService,
      checkoutDeliveryModesFacade,
      activeCartFacade
    );
    this.activeCartFacade.reloadActiveCart();
    this.getFreeshipingDetails();
    this.fetchPickupAgreement();
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
    this.activeCartFacade.getActive().subscribe((data: any) => {
      this.cartTotalPrice = data?.subTotal?.value || 0;
      this.showTaxWarningMessage = data?.deliveryAddress?.region?.isShippingTAxWarningEnabled;
      this.cd.markForCheck();
    })
  }

  goToPayments(code: string, cost: any) {
    if (
      this.selectedShippingMethod?.name === 'Pick Up At Wholescripts Pharmacy' &&
      this.selectedShippingMethod?.description ===
        'Orders not picked up after 5 business days will be canceled, except for MedPax orders. MedPax orders not picked up will be shipped to the address on the order, and any applicable shipping charges will apply.'
    ) {
      this.overlayService.openModal(this.shippingMethodPortal, this.modalWidth);
    } else {
      this.wsDeliverymodeAdapter.deliveryCost.set(cost);
      this.changeMode(code);
    }
  }

  override changeMode(code: string | undefined, event?: Event): void {
    if (!code) {
      return;
    }
    const lastFocusedId = (<HTMLElement>event?.target)?.id;
    this.busy$.next(true);
    this.checkoutDeliveryModesFacade.setDeliveryMode(code).subscribe({
    complete: () =>{this.onSuccess();this.checkoutStepService.next(this.activatedRoute); },
      error: () => this.onError(),
    });

    if (this.featureConfigService?.isEnabled('a11yCheckoutDeliveryFocus')) {
      const isTriggeredByKeyboard = (<MouseEvent>event)?.screenX === 0;
      if (isTriggeredByKeyboard) {
        this.restoreFocus(lastFocusedId, code);
        return;
      }
      this.mode.setValue({ deliveryModeId: code });
    }
  }

  fetchPickupAgreement() {
    this.http.get('/assets/pickup-agreement.json').subscribe((data) => {
      this.pickupAgreement = data;
    });
  }

  openShippingMethodModal(mode: any) {
    this.selectedShippingMethod = mode;
    this.overlayService.openModal(this.shippingMethodPortal, this.modalWidth);
  }

  confirmShippingMethod() {
    setTimeout(() => {
      this.wsDeliverymodeAdapter.deliveryCost.set(this.selectedShippingMethod?.deliveryCost?.value);
      this.changeMode(this.selectedShippingMethod?.code);
    }, 300);
    this.overlayService.closeTopModal();
  }

  closePopup() {
    this.overlayService.closeTopModal();
  }

}
