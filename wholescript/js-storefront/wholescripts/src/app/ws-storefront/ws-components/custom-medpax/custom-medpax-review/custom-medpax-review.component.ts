import { TemplatePortal } from '@angular/cdk/portal';
import { Location } from '@angular/common';
import { ChangeDetectorRef, Component, DestroyRef, inject, signal, TemplateRef, ViewChild, ViewContainerRef } from '@angular/core';
import { AbstractControl, FormControl, FormGroup, ValidationErrors, Validators } from '@angular/forms';
import { WsOverlay } from '../../../ws-services/ws-overlay.service';
import { WsCustomMedPaxEvents } from '../medpaxService/medpaxEventservice.service';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { updatePacket, WsCustomMedPaxService } from '../medpaxService/customMedpaxservice.service';
import { ActivatedRoute, Router } from '@angular/router';
import { ActiveCartFacade, MultiCartFacade } from '@spartacus/cart/base/root';
import { Pipe, PipeTransform } from '@angular/core';
import { catchError, defer, distinctUntilChanged, EMPTY, filter, map, Observable, of, switchMap } from 'rxjs';
import { RecommendationService } from '../../../ws-services/ws-recommendation.service';
import { ProtocolService } from '../../../ws-services/protocol/protocol.service';
import { ActivityLogService } from '../../../ws-services/ws-activityLog.service';
import { WsCartResponseModifier } from '../../../ws-services/ws-checkout/ws-cart-response-modifier.service';

export const medPaxPacketEnum = {
  am: ['AM', 'AM_WITH_FOOD', 'AM_WITHOUT_FOOD'],
  pm: ['PM', 'PM_WITH_FOOD', 'PM_WITHOUT_FOOD'],
  daily: ['Daily', 'DAILY_WITH_FOOD', 'DAILY_WITHOUT_FOOD'],
  bedtime: ['Bedtime', 'BEDTIME_WITH_FOOD', 'BEDTIME_WITHOUT_FOOD'],
};
declare var klaviyo: any;
@Component({
  selector: 'ws-custom-medpax-review',
  templateUrl: './custom-medpax-review.component.html',
  styleUrl: './custom-medpax-review.component.scss'
})
export class CustomMedpaxReviewComponent {
  public location = inject(Location);
  private vcr = inject(ViewContainerRef);
  private destroyRef = inject(DestroyRef);
  private activatedRoute = inject(ActivatedRoute);
  private router = inject(Router);
  private activeCartService = inject(ActiveCartFacade);
  private multiCartService = inject(MultiCartFacade);
  private overlay = inject(WsOverlay);
  private eventService = inject(WsCustomMedPaxEvents);
  private service = inject(WsCustomMedPaxService);
  private recommendationService = inject(RecommendationService);
  private protocolService = inject(ProtocolService);
  private activityLogService = inject(ActivityLogService);
  private cdr = inject(ChangeDetectorRef);
  private responseModifier = inject(WsCartResponseModifier);

  dosingDetails = new FormGroup({
    amControl: new FormControl(''),
    pmControl: new FormControl(''),
    dailyControl: new FormControl(''),
    bedControl: new FormControl('')
  });

  packetName = new FormControl('', [Validators.required, this.noWhitespaceValidator]);
  @ViewChild('EditName', { static: true }) editNamePopup: TemplateRef<any>;
  editNamePopupData: any;
  medPaxId = signal<string>('');
  getPacket = signal<any>({});
  protocolId = signal<string>('');
  medPaxProducts = signal<any>({});
  upDatePacketDataPayLoad: updatePacket;
  onPageLoad = false;
  recommendationEntries: any;

  alerts: any[] = [];
  alertsSignal = signal<any>(this.alerts);
  toastMessage: string = '';
  msrpSubTotalPrice: any;
  medPaxProtocol: any;
  cartIsLoading$: Observable<boolean> = this.activeCartService
    .isStable()
    .pipe(map((loaded) => !loaded));
  addToCartCalled: boolean = false;

  ngOnInit() {

    let url = this.location.path();
    let loadMedPax = false;
    if (url?.includes('protocol/edit/medpax/review')) {
      this.protocolId.set(this.activatedRoute.snapshot.paramMap.get('id'));
      this.loadProtocol();
      this.updateMedpaxId();
    } else {
      this.updateMedpaxId(true);
      loadMedPax = true;

    }
    if (url?.includes('recommendation/edit/medpax/review')) {
      this.protocolId.set(this.activatedRoute.snapshot.paramMap.get('id'));
      this.updateMedpaxId();
      this.loadRecommendation();
    } else {
      loadMedPax = true;
    }
    if (this.medPaxId()) {
      this.loadData();
    }
    if (loadMedPax) {
      this.LoadMedPax();
    }

    this.packetName.valueChanges.pipe(takeUntilDestroyed(this.destroyRef)).subscribe((res: any) => {
      if (res && !/^[a-zA-Z0-9\s]*$/.test(res)) {
        const sanitizedValue = res.replace(/[^a-zA-Z0-9\s]/g, '');
        this.packetName.setValue(sanitizedValue, { emitEvent: false });
      }
    });

    this.getMedPaxByCode();
  }

  updateMedpaxId(isurl = false) {
    this.medPaxId.set(isurl ? this.activatedRoute.snapshot.paramMap.get('id') : this.service.localCurrentmedPaxId());
  }

  loadData() {
    this.service.fetchMedPaxPackets(this.medPaxId())
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe((res: any) => {
        this.getPacket.set(res);
        const isNameThere = (this.getPacket()?.name?.trim()?.length > 0);
        if (!isNameThere) {
          this.openPopup();
        } else {
          this.packetName.setValue(this.getPacket()?.name?.trim());
        }
        this.initiateFormState();
      });
  }
  LoadMedPax() {
    const isRecommendationOrProtocol = () => {
      const path = this.location.path().toLowerCase();
      return path.includes('recommendation') || path.includes('protocol');
    };
    const fetchOwnerDetails$ = defer(() =>
      isRecommendationOrProtocol()
        ? this.service.getMedPaxForOwner(
          this.protocolId(),
          this.location.path().toLowerCase().includes('recommendation')
        )
        : EMPTY
    );

    fetchOwnerDetails$
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: (item: any) => {
          this.medPaxProducts.set(item);
          if (!this.medPaxId()) {
            this.medPaxId.set(item?.code);
            this.loadData();
          }


        },
      });
  }
  initiateFormState() {
    const controlMapping = {
      amControl: medPaxPacketEnum.am,
      pmControl: medPaxPacketEnum.pm,
      dailyControl: medPaxPacketEnum.daily,
      bedControl: medPaxPacketEnum.bedtime,
    };

    this.getPacket().packets.forEach((packet: any) => {
      const packetCode = packet.packetCode;

      Object.entries(controlMapping).forEach(([controlName, packetCodes]) => {
        if (packetCodes.includes(packetCode)) {
          this.dosingDetails.get(controlName)?.setValue(packetCode);
        }
      });
    });

    this.onPageLoad = true;
  }


  ngAfterViewInit() {
    this.customEventsHandler();

    const controlsMapping = [
      { control: this.dosingDetails.controls.amControl, type: 'AM' },
      { control: this.dosingDetails.controls.pmControl, type: 'PM' },
      { control: this.dosingDetails.controls.dailyControl, type: 'Daily' },
      { control: this.dosingDetails.controls.bedControl, type: 'Bedtime' },
    ];

    controlsMapping.forEach(({ control, type }) => {
      control.valueChanges
        .pipe(takeUntilDestroyed(this.destroyRef), distinctUntilChanged(), filter(() => this.onPageLoad))
        .subscribe((res: any) => {
          const payload = this.createPayload(type, res);
          this.updatePacketData(payload);
        });
    });
  }


  createPayload(packetCode: string, changeItemTimeTo: string) {
    const packet = this.getPacket().packets.find((p: any) => p.packetCode?.includes(packetCode));

    if (!packet) {
      console.warn(`No packet found for code: ${packetCode}`);
      return null;
    }

    return {
      medPaxId: this.getPacket().medPaxCode,
      changeItemTimeTo: changeItemTimeTo,
      changeItemTimeFrom: packet?.packetCode,
    };
  }

  openPopup() {
    this.editNamePopupData = new TemplatePortal<any>(this.editNamePopup, this.vcr);
    this.overlay.openModal(this.editNamePopupData, '35%',null , null , true);
  }

  goBack() {
    this.location.back();
  }

  closePopup() {
    this.overlay.closeTopModal();
  }

  customEventsHandler() {
    this.eventService.editNameButton.pipe(takeUntilDestroyed(this.destroyRef)).subscribe((response: any) => {
      this.openPopup();
    });
  }

  addToCart() {
    this.addToCartCalled = true;
    this.activeCartService.getActiveCartId().pipe(
      switchMap((cartId: any) => {
        const cartAction = cartId?.trim()?.length
          ? of(cartId)
          : this.multiCartService.createCart({ userId: 'current', extraData: { active: true } });
        return cartAction;
      })
    ).pipe(switchMap((resolvedCartId: any) => this.service.addMedPaxToCart(this.medPaxId(), resolvedCartId))
      , catchError((error) => {
        this.addToCartCalled = false;
        this.pushAlert('Failed to Add Items to Cart', 'danger')
        return of({});
      }), takeUntilDestroyed(this.destroyRef)).subscribe({
        next: () => {
          this.addToCartCalled = false;
          this.pushAlert('Product added to cart.', 'success');
          this.activityLogService.sendLog('medpax_addtocart');
          this.activeCartService.reloadActiveCart();
          this.pushMedpaxAddToCartToKlaviyo();
          this.router.navigate(['cart']);
        }
      });

  }

  noWhitespaceValidator(control: AbstractControl): ValidationErrors | null {
    const isWhitespace = (control.value || '').trim().length === 0;
    return isWhitespace ? { whitespace: true } : null;
  }

  updatePacketName() {
    this.service.updateMedpaxName({ name: this.packetName.value, code: this.medPaxId() }).pipe(takeUntilDestroyed(this.destroyRef)).subscribe(() => {
      this.loadData();
      this.closePopup();
      this.showToasterMsg('Packet Name changed successfully');
     });
  }

  updatePacketData(payLoad: any) {
    this.upDatePacketDataPayLoad = {
      medPaxId: payLoad?.medPaxId,
      changeItemTimeTo: payLoad?.changeItemTimeTo,
      changeItemTimeFrom: payLoad?.changeItemTimeFrom
    };
    this.service.postMedPaxPackets(this.upDatePacketDataPayLoad).pipe(takeUntilDestroyed(this.destroyRef)).subscribe();
  }

  //Recommendation
  goToReviewRecommendation() {
    this.router.navigate([
      '/Recommendation/RecommendationReview',
      this.protocolId(),
    ]);
  }

  loadRecommendation() {
    this.recommendationService
      .getRecommendationEntries(this.protocolId())
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe((res: any) => {
        this.msrpSubTotalPrice = res.msrpSubTotalPrice;
        if (res.recommendationEntries && res.recommendationEntries.length > 0) {
          this.recommendationEntries = res; //why we dis this why total variable required we can use existing one 
        }
      });
  }
  loadProtocol() {
    this.protocolService.fetchEditableProducts(this.protocolId()).pipe(takeUntilDestroyed(this.destroyRef)).subscribe((res: any) => {
      this.recommendationEntries = res;
    })
  }

  onClosed(dismissedAlert: any): void {
    this.alerts = this.alerts.filter((alert) => alert !== dismissedAlert);
    this.alertsSignal.set(this.alerts);
  }

  pushAlert(message: string, type: 'success' | 'info' | 'danger' | 'warning') {
    this.alerts.push({
      type: type,
      msg: message,
      timeout: 2000,
    });
    this.alertsSignal.set(this.alerts);
  }

  showToasterMsg(msg: string) {
    this.toastMessage = msg;
    this.resetToastMessage();
 }
 resetToastMessage() {
    setTimeout(() => {
      this.toastMessage = '';
      this.cdr.detectChanges();
    }, 2000)
  }

 getMedPaxByCode(){
  this.service.fetchMedPaxById(this.medPaxId()).pipe(takeUntilDestroyed(this.destroyRef))
    .subscribe((res: any) => this.medPaxProtocol = res);
 }

  pushMedpaxAddToCartToKlaviyo() {
    if (typeof klaviyo !== 'undefined' && klaviyo) {
      const baseUrl = window.location.origin;
      let cart$ = this.activeCartService.takeActive().pipe(
        map((cart: any) => this.responseModifier.mapCartEntries(cart))
      );
      cart$
        .subscribe((cart) => {

          const products = cart.entries.map((en: any) => {
            if (!en?.isMedPax) {
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
            AddedItemProductName: this.medPaxProtocol?.name,
            AddedItemProductID: '000000000500000030',
            AddedItemSKU: '000000000500000030',
            AddedItemCategories: this.medPaxProtocol?.medpaxEntry.map(
              (item: any) => item?.product?.name
            ),
            AddedItemImageURL: 'https://api.c1lud0zblk-xymogenin1-d1-public.model-t.cc.commerce.ondemand.com/medias/65x65-0008394-medpax-carton.png?context=bWFzdGVyfHdob2xlc2NyaXB0c0ltYWdlc3w1NTcyfGltYWdlL3BuZ3xhR05pTDJobE15ODRPRFE1TXpFMk5qYzFOakUwTHpZMWVEWTFYekF3TURnek9UUmZiV1ZrY0dGNExXTmhjblJ2Ymk1d2JtY3wzNTQwYzVhYTMzYmQwY2ExMGVjNjE4MzE2NzNkNDJhMDU3YWY3NGZiYjg3MmEzZGMyNTdjMTEyZGZmOGNmZDk4',
            AddedItemURL: baseUrl + '/medpax',
            AddedItemPrice: this.medPaxProtocol?.totalPrice,
            AddedItemQuantity: 1,
            ItemNames: cart.entries.map((e: any) => { //Name of the Items in the cart
              if (!e?.isMedPax) return e?.product?.name
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

@Pipe({
  name: 'controlName'
})
export class ControlNamePipe implements PipeTransform {
  transform(form: FormGroup, packetCode: string): any {
    const controlMapping: any = {
      AM: 'amControl',
      PM: 'pmControl',
      Daily: 'dailyControl',
      Bedtime: 'bedControl'
    };

    const controlName = controlMapping[packetCode] || 'amControl';
    return form.get(controlName);
  }
}
