import { ChangeDetectorRef, Component, DestroyRef, HostListener, inject, OnInit, signal, TemplateRef, ViewChild, ViewContainerRef } from '@angular/core';
import { BehaviorSubject, debounceTime, distinctUntilChanged, map, Observable, single, Subject, tap } from 'rxjs';
import { WsOverlay } from '../../../ws-services/ws-overlay.service';
import { TemplatePortal } from '@angular/cdk/portal';
import { BsModalRef, BsModalService, ModalOptions } from 'ngx-bootstrap/modal';
import { CreateNewProtocolComponent } from '../create-new-protocol/create-new-protocol.component';
import { protocolListFetch, ProtocolService } from '../../../ws-services/protocol/protocol.service';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ActiveCartFacade } from '@spartacus/cart/base/root';
import { PractitionerMyPatientListingService } from '../../ws-practitioner/ws-My-patients/my-patient-landing/practitioner-my-patient-listing.service';
import { WsCurrentUserService } from '../../../ws-services/state-services/currentuser.service';
import { WsCustomMedPaxService } from '../../custom-medpax/medpaxService/customMedpaxservice.service';
import { ParseUrlPipe } from '../../../ws-pipes/wsParseUrl.pipe';
import { WsCartResponseModifier } from '../../../ws-services/ws-checkout/ws-cart-response-modifier.service';

declare var klaviyo: any;
@Component({
  selector: 'app-protocol-landing-page',
  templateUrl: './protocol-landing-page.component.html',
  styleUrls: ['./protocol-landing-page.component.scss', '../create-new-protocol/create-new-protocol.component.scss']
})
export class ProtocolLandingPageComponent implements OnInit {

  private overLayService = inject(WsOverlay);
  private _viewContainerRef = inject(ViewContainerRef);
  private destroyRef = inject(DestroyRef);
  private router = inject(Router);
  private PatientService = inject(PractitionerMyPatientListingService);
  private customMedpaxService = inject(WsCustomMedPaxService);
  private currentUser = inject(WsCurrentUserService);
  private parseUrlPipe: ParseUrlPipe;
  private activeCartService = inject(ActiveCartFacade);
  private responseModifier = inject(WsCartResponseModifier);

  @ViewChild('notShareOption') notShareOption: TemplateRef<unknown>;
  @ViewChild('shareOptions') shareOptions: TemplateRef<unknown>;
  @ViewChild('editProtocol') editProtocol: TemplateRef<unknown>;
  @ViewChild('duplicateProtocol') duplicateProtocol: TemplateRef<unknown>;
  @ViewChild('productProtocol') productProtocol: TemplateRef<unknown>;
  @ViewChild('ShareWithAll') ShareWithAll: TemplateRef<unknown>;
  @ViewChild('publishProtocol') publishProtocol: TemplateRef<unknown>;

  notShareOptionPortal: TemplatePortal<any>;
  shareOptionsPortal: TemplatePortal<any>;
  editProtocolPortal: TemplatePortal<any>;
  duplicateProtocolPortal: TemplatePortal<any>;
  productProtocolPortal: TemplatePortal<any>;
  ShareWithAllPortal: TemplatePortal<any>;
  publishProtocolPortal: TemplatePortal<any>;

  editPopup = false;
  duplicatePopup = false;

  currentProtocolId = '';
  currentProtocolName = '';
  currentProtocol: any;
  totalValue = signal<string>('');
  pagination = signal<any>({});
  isDoctor = signal<boolean>(false)

  private searchSubject = new Subject<string>();
  dataSource = new BehaviorSubject<any>([]);
  protocolProducts$: Observable<any>;
  searchKey = '';
  sampleText: any;
  displayedColumns: string[] = ['lastUpdated', 'protocalName', 'items', 'price', 'shareStatus', 'actions'];

  bsModalRef?: BsModalRef;
  protocolsList = new BehaviorSubject<any>([]);

  payLoad: protocolListFetch = {
    currentPage: 0,
    pageSize: 10,
    sort: 'date:desc',
    searchText: ''
  }
  createProtocol = new FormGroup({
    name: new FormControl('', Validators.required),
    notes: new FormControl(''),
    checkbox: new FormControl(false)
  });
  patientDetails$: Observable<any> = this.PatientService.getPatientList();
  currentUser$: Observable<any> = this.currentUser.currentState();
  medPaxProtocol: any;
  url: string = '';
  loader: boolean = false;
  placeholder: string = 'Search Protocols';
  patientList: any;
  cartIsLoading$: Observable<boolean> = this.activeCartService
      .isStable()
      .pipe(map((loaded) => !loaded));

  constructor(private bsModalService: BsModalService,
    private protocolService: ProtocolService,
    private cd: ChangeDetectorRef
  ) { }

  ngOnInit(): void {
    this.load();
    this.patientDetails$.subscribe((res: any) => this.patientList = res)
    this.url = window.location.origin;
  }
  onSubmit() {
    let payLoad = {
      name: this.createProtocol.controls.name.value,
      description: this.createProtocol.controls.notes.value
    }

    this.duplicateProtocolApi(this.currentProtocolId, payLoad);

  }
  onKeyDown(event: KeyboardEvent, publish?: any, id?: any, name?: string, protocol?: any) {
    if (event.key === 'Enter' || event.key === ' ') {
      event.preventDefault();
      if (publish === 'stopShare') {
        this.stopShare();
      }  else if(publish === 'protocolShareSelectPatient') {
        this.navigateToProtocolShareSelectPatient(id);
      } else if(publish === 'editAndDeletePopup') {
        this.editAndDeletePopup(id);
      } else {
        this.openPopup(publish, id, name, protocol)
      }
    }
  }
  navigateToProtocolShareSelectPatient(currentProtocolId:any) {
    this.router.navigate([this.parseUrlPipe.transform('/protocol/share/select-patient', [currentProtocolId, 'false'])]);
  } 
  @HostListener('document:keydown', ['$event'])
  handleEscapeKey(event: KeyboardEvent) {
    if (event.key === 'Escape') {
      this.closePopup();
    }
  }
  ngAfterViewInit() {
    const portals: {
      ShareWithAll: TemplateRef<unknown>;
      notShareOption: TemplateRef<unknown>;
      shareOptions: TemplateRef<unknown>;
      editProtocol: TemplateRef<unknown>;
      duplicateProtocol: TemplateRef<unknown>;
      productProtocol: TemplateRef<unknown>;
      publishProtocol: TemplateRef<unknown>;
      [key: string]: TemplateRef<unknown>;
    } = {
      ShareWithAll: this.ShareWithAll,
      notShareOption: this.notShareOption,
      shareOptions: this.shareOptions,
      editProtocol: this.editProtocol,
      duplicateProtocol: this.duplicateProtocol,
      productProtocol: this.productProtocol,
      publishProtocol: this.publishProtocol
    };

    Object.keys(portals).forEach(key => {
      (this as { [key: string]: any })[`${key}Portal`] = new TemplatePortal(portals[key], this._viewContainerRef);
    });
    this.searchSubject.pipe(
      debounceTime(300),
      distinctUntilChanged()
    ).subscribe((data: string) => {
      this.payLoad.searchText = data;
      this.load();
    });
    if(document.getElementsByTagName('title') && !document.getElementsByTagName('title')[0].innerText) document.getElementsByTagName('title')[0].innerText = 'Protocol';
  }

  load() {
    this.loader = true;
    this.protocolService.fetchProtocolList(this.payLoad).subscribe(
      {
       next: (response) => {
          this.protocolsList.next(response?.protocolsListWsDto);
          this.pagination.set(response?.pagination);
          this.loader = false;
        },
        error: () => {
          this.loader = false;
        }
      }
      
  );
  }

  updateSearch(key: string) {
    this.searchSubject.next(key);
  }
  productDetails(id: string) {
    this.protocolProducts$ = this.protocolService.fetchEditableProducts(id).pipe(tap((item: any) => {
      const reduceValue = item.protocolEntries.reduce((acu: any, cur: any) => {
        return acu + cur?.basePrice?.value
      }, 0);
      this.totalValue.set(reduceValue);
    }));
    this.customMedpaxService.getMedPaxForOwner(id)
    .pipe(takeUntilDestroyed(this.destroyRef))
    .subscribe((res: any) => {
      this.medPaxProtocol = res;
      this.cd.markForCheck();
    });    
  }
  openPopup(type: string, protocolId: string, protocolName: string, protocol: any) {
    this.currentProtocolId = protocolId;
    this.currentProtocolName = protocolName;
    this.currentProtocol = protocol;
    this.productDetails(protocolId);
    const portalMap: { [key: string]: any } = {
      notshare: this.notShareOptionPortal,
      share: this.shareOptionsPortal,
      edit: this.editProtocolPortal,
      delete: this.editProtocolPortal,
      duplicate: this.duplicateProtocolPortal,
      create: this.duplicateProtocolPortal,
      product: this.productProtocolPortal,
      shareall: this.ShareWithAllPortal,
      publish: this.publishProtocolPortal
    };

    const popupFlags: { [key: string]: () => void } = {
      edit: () => this.editPopup = true,
      delete: () => this.editPopup = false,
      duplicate: () => this.duplicatePopup = true,
      create: () => this.duplicatePopup = false,
    };

    if (portalMap[type]) {
      popupFlags[type]?.();
      if (type == 'duplicate') {
        this.createProtocol.controls.name.setValue('Copy of' + protocolName);
        this.createProtocol.controls.notes.setValue(protocolName);
      }
      this.overLayService.openModal(portalMap[type], '35%');
    }
  }

  closePopup() {
    this.overLayService.closeTopModal();
  }
  duplicateProtocolApi(protocolId: string, payLoad: any) {
    this.protocolService.duplicateAProtocol(protocolId, payLoad).
      pipe(takeUntilDestroyed(this.destroyRef)).subscribe(
        (results: any) => {
          this.closePopup();
          this.router.navigate([`protocol/edit/${results.id}`]);
        }
      );
  }
  deleteProtocolApi(protocolId: string) {
    this.protocolService.deleteAProtocol(protocolId).
      pipe(takeUntilDestroyed(this.destroyRef)).subscribe(
        () => {
          this.overLayService.closeAllModals();
          this.load();
        }
      );
  }

  editAndDeletePopup(isEdit: boolean) {
    if (isEdit) {
      this.router.navigate([`protocol/edit/${this.currentProtocolId}`]);
    } else {
      this.deleteProtocolApi(this.currentProtocolId);
      this.overLayService.closeTopModal();
    }
  }
  goToPage() {
    this.router.navigate(['wsprotocols/list'])
  }

  pageChanged(event: any) {
    this.payLoad.currentPage = event;
    this.load();
  }


  createNewProtocol(typeOfProtocol: string, protocolName: string) {
    const initialState: ModalOptions = {
      initialState: {
        typeOfProtocol: typeOfProtocol,
        protocolName: protocolName
      }
    };
    this.bsModalRef = this.bsModalService.show(CreateNewProtocolComponent, initialState);
    this.bsModalRef.onHide.pipe(takeUntilDestroyed(this.destroyRef)).subscribe(() => {
      this.load();
    })
  }

  stopShare() {
    this.protocolService.stopSharingProtocol(this.currentProtocolId).pipe(takeUntilDestroyed(this.destroyRef)).subscribe(
      () => {
        this.closePopup();
        this.load();
      }
    );
  }
  addToCartFunctionality(Protocol: any) {
    this.protocolService.addToCartProtocol(Protocol?.id).pipe(takeUntilDestroyed(this.destroyRef)).subscribe((res: any) =>{
      Protocol.protocolEntries.forEach((item: any) => {
        this.pushAddToCartToKlaviyo(item.product, item.quantity);
      });
      this.customMedpaxService.getMedPaxForOwner(Protocol?.id)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe((res: any) => {
        this.medPaxProtocol = res;
        if(this.medPaxProtocol) this.pushMedpaxAddToCartToKlaviyo();
        this.router.navigate(['cart']);
      });
    });
  }

  recommendThisProtocol(protocolId: string) {
    this.router.navigate([
      `/SearchPatientAdvanced/newrecommendation`
    ], {
      queryParams: {
        protocolId: protocolId
      }
    });
  }

  //Doctor Functionality
  publishApi(data: 'PUBLISHED' | 'UNPUBLISHED') {
    let PayLoad = {
      protocolStatus: data
    }
    this.protocolService.sendProtocol(this.currentProtocolId, PayLoad).subscribe((res: any) => {
      this.load();
      this.closePopup();
    });
  }

  getPaginationMessage(pagination: any): string {
    if (pagination.totalCount === 0) {
      return "No entries available";
    }
 
    const start = pagination.page * pagination.count + 1;
    const end = Math.min(start + pagination.count - 1, pagination.totalCount);
  
    return `Showing ${start} to ${end} of ${pagination.totalCount} entries`;
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

  checkRoleExist(role: string): Observable<boolean> {
    return this.currentUser.isRoleExist(role).pipe(takeUntilDestroyed(this.destroyRef));
 } 

}

