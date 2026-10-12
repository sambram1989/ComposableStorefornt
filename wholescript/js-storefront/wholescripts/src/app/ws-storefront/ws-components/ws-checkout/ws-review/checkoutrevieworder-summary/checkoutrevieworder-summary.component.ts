import { ChangeDetectorRef, Component, inject, signal } from '@angular/core';
import { ActiveCartFacade, Cart } from '@spartacus/cart/base/root';
import { Observable, tap } from 'rxjs';
import { WsPurchaseOrderService } from '../../../../ws-services/ws-checkout/ws-checkout-po.service';
import { AuthService, CmsService } from '@spartacus/core';
import { WsCurrentUserService } from '../../../../ws-services/state-services/currentuser.service';
import { WsACHPaymentService } from '../../../../ws-services/ws-checkout/ws-ach-payment.service';
import { userMap } from '../../../../ws-env/environment';

@Component({
  selector: 'ws-checkoutrevieworder-summary',
  templateUrl: './checkoutrevieworder-summary.component.html',
  styleUrl: './checkoutrevieworder-summary.component.scss'
})
export class CheckoutrevieworderSummaryComponent {
  private activeCartFacade = inject(ActiveCartFacade);
  get cart$(): Observable<any> {
    return this.activeCartFacade.getActive();
  }

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
  cardNumber: string;
  paymentCode: string = '';
  achCCName: string = '';
  achCCNumber: string = '';
  showTaxWarningMessage: boolean = false;

  purchaseOrderNumber: string;

  isACHPaymentMethod: boolean = false;
  isFriendsFamily: boolean = false;

  constructor(private purchaseOrderService: WsPurchaseOrderService, private cd: ChangeDetectorRef,
    private achPaymentService: WsACHPaymentService) {
    this.getFreeshipingDetails();
    this.achPaymentService.isACHPaymentMethod$.subscribe(data => {
      this.isACHPaymentMethod = data;
      if(this.paymentCode == 'ZACH' && !this.isACHPaymentMethod) {
        const achInfo = this.achPaymentService.achResponseForPlaceOrder;
        this.achCCName = achInfo?.achCCName;
        this.achCCNumber = achInfo?.achCCNumber?.split('-')?.[1];
      }
      this.cd.markForCheck();
    })
    this.cart$.subscribe((cart) => {
      this.cardNumber = cart.paymentInfo?.cardNumber?.replace(/\*/g, ''); 
      this.paymentCode = cart?.paymentType?.code;
    });
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
    this.activeCartFacade.getActive().subscribe((data:any) => {
      this.cartTotalPrice = data?.subTotal?.value || 0;
      this.showTaxWarningMessage = data?.deliveryAddress?.region?.isShippingTAxWarningEnabled;
      this.cd.markForCheck();
    })
  }

  onPurchaseOrderNumberChange(value: string): void {
    this.purchaseOrderNumber = value;
    this.purchaseOrderService.setPurchaseOrderNumber(this.purchaseOrderNumber);
  }

  showPO() {
    const user = JSON.parse(sessionStorage.getItem('user_config'));
  
    // If user exists, check if they have the "patient" role
    return user ? !(user?.roles || []).includes('patientcustomergroup') : true;
  }
}
