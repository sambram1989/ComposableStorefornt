import { TemplatePortal } from '@angular/cdk/portal';
import { ChangeDetectorRef, Component, DestroyRef, HostListener, inject, signal, TemplateRef, ViewChild, ViewContainerRef } from '@angular/core';
import { WsOverlay } from '../../../ws-services/ws-overlay.service';
import { protocolListFetch, ProtocolService } from '../../../ws-services/protocol/protocol.service';
import { BehaviorSubject, debounceTime, distinctUntilChanged, map, Observable, Subject, tap, switchMap } from 'rxjs';
import { Router } from '@angular/router';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { HttpParams } from '@angular/common/http';
import { getUserData } from '../../../ws-services/state-services/currentuser.service';
import { WsCustomMedPaxService } from '../../custom-medpax/medpaxService/customMedpaxservice.service';
import { ActiveCartFacade } from '@spartacus/cart/base/root';
import { WsCartResponseModifier } from '../../../ws-services/ws-checkout/ws-cart-response-modifier.service';
import { userMap } from '../../../ws-env/environment';
import { ApiCall, PractitionerMyPatientListingService } from '../../ws-practitioner/ws-My-patients/my-patient-landing/practitioner-my-patient-listing.service';

declare var klaviyo: any;
@Component({
  selector: 'ws-protocol-landing',
  templateUrl: './ws-protocol-landing.component.html',
  styleUrl: './ws-protocol-landing.component.scss'
})
export class WsProtocolLandingComponent {
  private _viewContainerRef = inject(ViewContainerRef);
  private overLayService = inject(WsOverlay);
  private service = inject(ProtocolService);
  private cdr = inject(ChangeDetectorRef);
  private router = inject(Router);
  private destroyRef = inject(DestroyRef);
  private currentUser= inject(getUserData);
  private customMedpaxService = inject(WsCustomMedPaxService);
  private activeCartService = inject(ActiveCartFacade);
  private responseModifier = inject(WsCartResponseModifier);
  private patientService = inject(PractitionerMyPatientListingService);

  currentUserData = signal<any>({});

  searchKey = '';
  ApiCall = signal<any>({})

  payLoad: protocolListFetch = {
    currentPage: 0,
    pageSize: 10,
    sort: '',
    searchText: ''
  };

  patientPayLoad: ApiCall = {
    currentPage: 0,
    pageSize: 10,
    discount: '',
    referralCode: '',
    status: '',
    searchText: '',
    sortBy: '',
    practitionerId: '',
  };

  pagination = signal<any>({})
  private searchSubject = new Subject<string>();
  private protocolListSubject = new BehaviorSubject<any>(null);
  service$ = this.protocolListSubject.asObservable();
  patientsData = signal<any[]>([]);
  @ViewChild('protoProduct') protoProduct: TemplateRef<unknown>;
  protoProductPortal: TemplatePortal<any>;
  currentPage = signal<number>(1);
  protocolProducts$: Observable<any>;
  medPaxProtocol: any
  
  alerts: any[] = [];
  alertsSignal = signal<any>(this.alerts);
  isPatientDirect: boolean;
  hasPatients: boolean = false;

  constructor () {
    const user = JSON.parse(sessionStorage.getItem('user_config'));
    if(user) {
        const key = Object.keys(userMap).find(key => userMap[key] === 'patientdirect');
        this.isPatientDirect = (user?.roles || []).includes(key);
    }
    this.getPatients();
  }

  ngOnInit() {
    this.service.isWsPage.set(true);
    this.setSearchStream();
    this.load();
  }
  
  @HostListener('document:keydown', ['$event'])
  handleEscapeKey(event: KeyboardEvent) {
    if (event.key === 'Escape') {
      this.closeFilterModal();
    }
  }

    ngAfterViewInit() {
      this.protoProductPortal = new TemplatePortal(
        this.protoProduct,
        this._viewContainerRef
      );
      this.loadUserData();
    }

  loadUserData(){
    this.currentUser.subscribe((response:any)=>{
     this.currentUserData = response;
    });
  }

  addToCartFunctionality(Protocol: any) {
    this.service.addToCartProtocol(Protocol?.id,false).pipe(takeUntilDestroyed(this.destroyRef)).subscribe(
      {
        next:()=>{ 
        let alertMessage = `Protocol ${Protocol?.name} added to cart`;
        this.pushAlert(alertMessage,'info');
        Protocol.protocolEntries.forEach((item: any) => {
          this.pushAddToCartToKlaviyo(item.product, item.quantity);
        });
        this.customMedpaxService.getWholeScriptsProtocolMedPaxForOwner(Protocol?.id)
          .pipe(takeUntilDestroyed(this.destroyRef))
          .subscribe((res: any) => {
            this.medPaxProtocol = res;
            if (this.medPaxProtocol) this.pushMedpaxAddToCartToKlaviyo();
            this.router.navigate(['cart']);
          });
      }
      }
    );
  }

 
  load() {
    this.service.fetchProtocolList(this.payLoad).pipe(
      tap((res: any) => {
        this.pagination.set(res.pagination);
        this.protocolListSubject.next(res); 
      }),
      takeUntilDestroyed(this.destroyRef)
    ).subscribe();
  }

  openFilterModal(prod: any) {
    this.protocolProducts$ = this.service.fetchEditableProducts(prod?.id);
    this.customMedpaxService.getWholeScriptsProtocolMedPaxForOwner(prod?.id)
    .pipe(takeUntilDestroyed(this.destroyRef))
    .subscribe((res: any) => {
      this.medPaxProtocol = res;
      this.cdr.detectChanges()
    });
    this.ApiCall.set(prod);
    this.overLayService.openModal(this.protoProductPortal, "40%");
  }

  setSearchStream() {
    this.searchSubject.pipe(
      debounceTime(300),
      distinctUntilChanged(),
      tap((searchText: string) => {
        this.payLoad.searchText = searchText;
      }),
      switchMap(() => this.service.fetchProtocolList(this.payLoad)),
      tap((res: any) => {
        this.pagination.set(res.pagination);
        this.protocolListSubject.next(res); 
      }),
      takeUntilDestroyed(this.destroyRef)
    ).subscribe();
  }

  updateSearch(key: string) {
    this.searchSubject.next(key);
  }

  closeFilterModal() {
    this.overLayService.closeTopModal();
    this.overLayService.closeAllModals();
  }

  pageChanged(event: any) {
    this.payLoad.currentPage = event;
    this.currentPage.set(event);
    this.load();
  }

  viewMore(id: any) {
    localStorage.setItem('navigatedFromWholeScriptsProtocol', 'true'); 
    this.service.isComeFromWs.set(true);
    this.router.navigate([`/protocol/details/${id}`]);
  }

  copyToProtoCols(wsuser: any) {
    const payLoad = {
      name: wsuser?.name,
      description: wsuser?.description,
      notes: wsuser?.notes || '',
      protocolStatus: wsuser?.protocolStatus
    }
    this.service.duplicateAProtocol(wsuser?.id, payLoad).subscribe(
      {
        next:()=>{
            let alertMessage = `${wsuser?.name} has been added successfully`;
            this.pushAlert(alertMessage,'info');
        }
      });
  }

  getPatients() {
    this.patientService.getPatientList(this.patientPayLoad)
    .pipe(takeUntilDestroyed(this.destroyRef))
    .subscribe((res: any) => {
      if(res?.patients.length) {
        this.hasPatients = true;
        this.cdr.detectChanges();
      }
    });
  }

  recommendThisProtocol(currentProtocol: any) {
    localStorage.setItem('fromWholeScriptsProtocols', 'true');
    this.router.navigate([
      `/SearchPatientAdvanced/newrecommendation`
    ], {
      queryParams: {
        protocolId: currentProtocol.id
      }
    });
  }

  pushAlert(message: string, type: 'success' | 'info' | 'danger' | 'warning') {
    this.alerts.push({
       type: type,
       msg: message,
       timeout: 2000,
    });
    this.alertsSignal.set(this.alerts);
    this.cdr.detectChanges();
 }

 onClosed(dismissedAlert: any): void {
  this.alerts = this.alerts.filter((alert) => alert !== dismissedAlert);
  this.alertsSignal.set(this.alerts);
}

ngOnDestroy() {
  this.service.isWsPage.set(false);
}

pushAddToCartToKlaviyo(product: any, quantity: any) {
  if (typeof klaviyo !== 'undefined' && klaviyo) {
    let productImageUrl = '';
    if (product?.images?.PRIMARY?.product?.url) {
      productImageUrl = product.images.PRIMARY.product.url;
    }
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
        AddedItemImageURL: productImageUrl,
        AddedItemURL: `${baseUrl}${product.url.replace('///', '/').replace('//', '/')}`,
        AddedItemPrice: product.wholeSalePrice?.value,
        AddedItemQuantity: quantity,
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
