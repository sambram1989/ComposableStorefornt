import { ChangeDetectorRef, Component, DestroyRef, ElementRef, inject, signal, TemplateRef, ViewChild, ViewContainerRef } from '@angular/core';
import { wsCheckoutStepService } from '../../../ws-services/ws-checkout/ws-checkout.service';
import { ActiveCartFacade, Cart, PaymentType } from '@spartacus/cart/base/root';
import {
  HttpErrorModel,
  OccHttpErrorType,
  GlobalMessageType,
  getLastValueSync,
  GlobalMessageService,
  isNotUndefined,
  AuthService,
  CmsService,
} from '@spartacus/core';
import {
  Observable,
  tap,
  catchError,
  of,
  map,
  BehaviorSubject,
  combineLatest,
  distinctUntilChanged,
  filter,
} from 'rxjs';
import { ActivatedRoute } from '@angular/router';
import {
  B2BPaymentTypeEnum,
  CheckoutPaymentTypeFacade,
} from '@spartacus/checkout/b2b/root';
import { CheckoutStepService } from '@spartacus/checkout/base/components';
import { CheckoutDeliveryAddressFacade, CheckoutStepType } from '@spartacus/checkout/base/root';
import { TemplatePortal } from '@angular/cdk/portal';
import { WsOverlay } from '../../../ws-services/ws-overlay.service';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { WsCheckoutPaymentService } from '../../../ws-services/ws-checkout/ws-checkout-payment.service';
import { WsCurrentUserService } from '../../../ws-services/state-services/currentuser.service';
import { userMap } from '../../../ws-env/environment';

@Component({
  selector: 'ws-checkout-payment-method',
  templateUrl: './checkout-payment-method.component.html',
  styleUrl: './checkout-payment-method.component.scss',
})
export class WsCheckoutPaymentMethodComponent {
  private wsCheckoutStepService = inject(wsCheckoutStepService);
  private wsCheckoutPaymentService = inject(WsCheckoutPaymentService);
  private currentUser = inject(WsCurrentUserService);
  private vcr = inject(ViewContainerRef);
  private overLayService = inject(WsOverlay);
  private destroyRef = inject(DestroyRef);
  private newCardAdded = false;
  @ViewChild('poNumber', { static: false })
  private poNumberInputElement: ElementRef<HTMLInputElement>;

  @ViewChild('addNewCreditCard') addNewCard : TemplateRef<any>;
  addNewCardPortal:any;

  authService = inject(AuthService);
  private cmsService = inject(CmsService);
  shippingMessage: string = '';
  PATIENT_SHIPPING_PRICE: number = 100;
  PRACTITIONER_SHIPPING_PRICE: number = 300;
  cartTotalPrice: number;
  shippingPrice = 0;
  showTaxWarningMessage: boolean = false;

  protected busy$ = new BehaviorSubject<boolean>(false);
  isPatient = signal<boolean>(false);
  isCurrentUser$:Observable<any> =  this.currentUser.isRoleExist('patient').pipe(tap((res:boolean)=> this.isPatient.set(res) ));

  isFriendsFamily: boolean = false;
  
  typeSelected?: string;
  paymentTypesError = false;
  cart$: Observable<any> = this.activeCartFacade.getActive();
  allAddress: any;
  deliveryAddress$ = this.checkoutDeliveryAddress.getDeliveryAddressState();
  addresses$ = this.deliveryAddress$.subscribe(resp => {
   this.allAddress = resp.data;
  });

  isUpdating$ = combineLatest([
    this.busy$,
    this.checkoutPaymentTypeFacade
      .getSelectedPaymentTypeState()
      .pipe(map((state) => state.loading)),
  ]).pipe(
    map(([busy, loading]) => busy || loading),
    distinctUntilChanged()
  );

  paymentTypes$: Observable<PaymentType[]> = this.checkoutPaymentTypeFacade
    .getPaymentTypes()
    .pipe(
      map((pType: PaymentType[]) =>
        pType.filter(
          (ptype) => ptype.code !== 'ACCOUNT' && ptype.code !== 'CARD' && ptype.code !== 'XCRD'
        )
      ),
      tap(() => (this.paymentTypesError = false)),
      catchError((error: HttpErrorModel) => {
        if (
          error.details?.[0]?.type === OccHttpErrorType.CLASS_MISMATCH_ERROR
        ) {
          this.globalMessageService.add(
            { key: 'httpHandlers.forbidden' },
            GlobalMessageType.MSG_TYPE_ERROR
          );
          this.paymentTypesError = true;
        }
        return of([]);
      })
    );

  typeSelected$: Observable<PaymentType> = combineLatest([
    this.checkoutPaymentTypeFacade.getSelectedPaymentTypeState().pipe(
      filter((state) => !state.loading),
      map((state) => state.data)
    ),
    this.paymentTypes$,
  ]).pipe(
    map(
      ([selectedPaymentType, availablePaymentTypes]: [
        PaymentType | undefined,
        PaymentType[]
      ]) => {
        if (
          selectedPaymentType &&
          availablePaymentTypes.find((availablePaymentType) => {
            return availablePaymentType.code === selectedPaymentType.code;
          })
        ) {
          return selectedPaymentType;
        }
        if (availablePaymentTypes.length) {
          this.busy$.next(true);
          this.checkoutPaymentTypeFacade
            .setPaymentType(
              availablePaymentTypes[0].code as string,
              this.poNumberInputElement?.nativeElement?.value
            )
            .subscribe({
              complete: () => this.onSuccess(false),
              error: () => this.onError(),
            });
          return availablePaymentTypes[0];
        }
        return this.isPatient() ? {} : undefined;
      }
    ),
    filter(isNotUndefined),
    distinctUntilChanged(),
    tap((selected) => {
      // this.typeSelected = selected?.code;
      this.checkoutStepService.resetSteps();
      this.checkoutStepService.disableEnableStep(
        CheckoutStepType.PAYMENT_DETAILS,
        selected?.code === B2BPaymentTypeEnum.ACCOUNT_PAYMENT
      );
    })
  );

  cartPoNumber$: Observable<string> = this.checkoutPaymentTypeFacade
    .getPurchaseOrderNumberState()
    .pipe(
      filter((state) => !state.loading),
      map((state) => state.data),
      filter(isNotUndefined),
      distinctUntilChanged()
    );

    getPaymentdetails = signal<any>({}) ;
    screenWidth = window.innerWidth;
    modalWidth = this.screenWidth * 0.30 + 'px';
    currentPaymentDetailsId='';
  constructor(
    protected checkoutPaymentTypeFacade: CheckoutPaymentTypeFacade,
    protected checkoutStepService: CheckoutStepService,
    protected activatedRoute: ActivatedRoute,
    protected globalMessageService: GlobalMessageService,
    private checkoutDeliveryAddress: CheckoutDeliveryAddressFacade,
    private activeCartFacade: ActiveCartFacade,
    private cd: ChangeDetectorRef
  ) {
    const user = JSON.parse(sessionStorage.getItem('user_config'));
    if (user) {
      const key = Object.keys(userMap).find(key => userMap[key] === 'friendsfamily');
      this.isFriendsFamily = (user?.roles || []).includes(key);
    }
   }

  ngOnInit(){
  this.getPaymentDetails();
  this.customEventListeners();
  this.getFreeshipingDetails();
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

  ngAfterViewInit(){
    this.addNewCardPortal = new TemplatePortal(this.addNewCard,this.vcr);
    if(document.getElementsByTagName('title') && !document.getElementsByTagName('title')[0].innerText) document.getElementsByTagName('title')[0].innerText = 'Payment Method';
  }

  changeType(code: string): void {
    this.busy$.next(true);
    this.typeSelected = code;

    this.checkoutPaymentTypeFacade
      .setPaymentType(code)
      .subscribe({
        next: () => this.onSuccess(),
        error: () => this.onError(),
      });
  }

  getPaymentDetails(){
    this.wsCheckoutStepService.getPaymentDetails().pipe(takeUntilDestroyed(this.destroyRef)).subscribe({
      next:(res:any)=>{
        this.getPaymentdetails.set(res);
        if(this.newCardAdded && res.cartPaymentInfo.id){
          this.newCardAdded = false;
          this.selectOption('XCRD',res.cartPaymentInfo.id)
        } 
      }
    })
  }

  protected onSuccess(canComplete=true): void {
    this.busy$.next(false);
    this.updateAddress();
    if(canComplete){
      this.goToFinalStep();
    }

  }

  protected onError(): void {
    this.busy$.next(false);
  }

  createNewCard() {
    this.overLayService.openModal(this.addNewCardPortal,'35%');
  }

  goToFinalStep(): void {
    this.wsCheckoutStepService.enableFinalStepNavigation();
    this.wsCheckoutStepService.goToFinalStep();
  }

  selectOption(code: any,paymentDetailsId:string) {
    this.currentPaymentDetailsId = paymentDetailsId;
    this.changeType(code);
  }

  customEventListeners(){
    this.wsCheckoutPaymentService.addNewCardSucceeded.pipe(takeUntilDestroyed(this.destroyRef)).subscribe({
      next:(response:any)=>{
        this.overLayService.closeTopModal();
        this.newCardAdded = true;
        this.getPaymentDetails();
      }
    });
  }

  updateAddress(){
    if(this.currentPaymentDetailsId?.trim()?.length>1){
      this.wsCheckoutPaymentService.updateNewAddress(this.currentPaymentDetailsId).pipe(takeUntilDestroyed(this.destroyRef)).subscribe();
    }
  }
  isCardExpired(card: any) {
    const month = new Date().getMonth();
    const year = new Date().getFullYear();
    const expiryMonth = parseInt(card.expiryMonth, 10);
    const expiryYear = parseInt(card.expiryYear, 10);
    return (expiryYear < year || (expiryYear === year && expiryMonth < month + 1)) ? true : false;
  }
}
