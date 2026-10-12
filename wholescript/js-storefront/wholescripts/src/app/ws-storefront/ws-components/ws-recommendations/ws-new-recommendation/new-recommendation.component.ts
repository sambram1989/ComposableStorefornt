import {
  AfterViewInit,
  ChangeDetectorRef,
  Component,
  computed,
  DestroyRef,
  inject,
  OnInit,
  signal,
  TemplateRef,
  ViewChild,
  ViewContainerRef,
} from '@angular/core';
import { Location } from '@angular/common';
import { WsPage } from '../../../ws-services/ws-page.service';
import { asyncScheduler, debounceTime, delay, distinctUntilChanged, Observable, shareReplay, tap } from 'rxjs';
import { slotMarkup } from '../../../ws-models/page.model';
import { CmsBannerComponent } from '@spartacus/core';
import { ActivatedRoute, Router } from '@angular/router';
import { RecommendationService } from '../../../ws-services/ws-recommendation.service';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { BsModalRef, BsModalService, ModalOptions } from 'ngx-bootstrap/modal';
import { WsRecommendationFiltersComponent } from '../ws-recommendation-filters/ws-recommendation-filters.component';
import { FavoriteService } from '../../../ws-services/ws-favorite.service';
import { ProtocolService } from '../../../ws-services/protocol/protocol.service';
import { createmedpax, WsCustomMedPaxService } from '../../custom-medpax/medpaxService/customMedpaxservice.service';
import { WsCustomMedPaxEvents } from '../../custom-medpax/medpaxService/medpaxEventservice.service';
import { TemplatePortal } from '@angular/cdk/portal';
import { FormGroup, FormArray, FormControl, Validators } from '@angular/forms';
import { WsOverlay } from '../../../ws-services/ws-overlay.service';
import { weekMap, weekArray } from '../../ws-shared/edit-dosingdetailspopup/editDosingConstants';
import { currentState, WsCurrentUserService } from '../../../ws-services/state-services/currentuser.service';
import { WsProductDetailsPopupComponent } from '../../ws-pdp/ws-product-details-popup/ws-product-details-popup.component';
import { WsCommonFacetService } from '../../ws-spartacus/product-facet-navigation/services/ws-common-facet.service';
import { WsFacetService } from '../../ws-spartacus/product-facet-navigation/services/facet.service';

@Component({
  selector: 'ws-new-recommendation',
  templateUrl: './new-recommendation.component.html',
  styleUrl: './new-recommendation.component.scss',
})
export class NewRecommendationComponent implements OnInit, AfterViewInit {
  public location = inject(Location);
  protocolService = inject(ProtocolService);
  customMedPaxService = inject(WsCustomMedPaxService);
  private eventService = inject(WsCustomMedPaxEvents);
  private commonFacet = inject(WsCommonFacetService);
  private vcr = inject(ViewContainerRef);
  curuserService = inject(WsCurrentUserService);
  private overlayService = inject(WsOverlay);
  paginationData: any =  {};
  weekMapItems: any = weekMap;
  displayedColumns: string[] = ['Name', 'Date Created', 'Status', 'Actions'];
  dataSource: any;
  sortOptions: any;
  productsList: any;
  SearchText: string = '';
  recommendationsSlot = new Observable<slotMarkup>();
  pageContent: CmsBannerComponent;
  patientName: string = '';
  itemsInCart: any[] = [];
  recommendationId: string = '';
  entryNumber = 1;
  cartList: any[] = [];
  recommendationEntries: any;
  isMobile: boolean = false;
  payLoad = {
    currentPage: 0,
    pageSize: 10,
    discount: '',
    referralCode: '',
    status: '',
    query: '',
    sort: '',
    practitionerId: '',
    filters: ''
  };
  bsModalRef?: BsModalRef;
  filterfacets: any;
  dosingInfo: any;
  alerts: any[] = [];
  alertSignal = signal<any>(this.alerts);
  savedAsCustomSignal = signal<boolean>(false);
  favoriteList: { wishLists?: { wishListEntries: { productCode: string }[] } } =
    {};
  isMedPaxTabSelected = signal<boolean>(false);
  selectedMedPaxProductCodes = signal<any>([]);
  entryMedPaxNumberList: any;
  products = signal<any>({});
  isMedPax = computed(() => this.protocolService.isMedPaxPage());
  medPaxProducts = signal<any>({});
  createNewMedpaxPayLoad: createmedpax;
  wrapperForm = new FormGroup({
    hobbiesArray: new FormArray([this.createHobbyGroup()])
  });
  currentProduct: any;
  DisplayItemTimes: any;
  removeMedPaxProductEntryNumber: any;
  removeMedPaxProduct: any;
  isMedPaxEditDosing: any;
  total: any;
  customDosingdData: any;
  currentRecommendationProduct: any;
  currentState$ = inject(currentState);
  customDosingList = signal<any>({});
  currentMedPaxProduct = signal<any>({});
  currentBlueLabelProductRemove:any;

  @ViewChild('filterPopUp') filterProtocol: TemplateRef<any>;
  @ViewChild('editDosingPopup') editDosingPopup: TemplateRef<any>;
  @ViewChild('removeProductPopUp') removeProductPopUp: TemplateRef<any>;
  @ViewChild('removeMedPax') removeEntireMedPax: TemplateRef<any>;
  @ViewChild('removeNormalProductPopUp') removeNormalProductPopup:TemplateRef<any>;
  @ViewChild('swapProtocolPopup') swapProtocolPopup:TemplateRef<any>;
  @ViewChild('confirmSwapProtocol') confirmSwapProtocol:TemplateRef<any>;
  @ViewChild('productPopup') recommendationProduct: TemplateRef<any>;
  currentUser = signal<string>('');
  filterPortal: any;
  QueryString = '';
  activeFilters = signal<any[]>([]);
  msrpSubTotalPrice: any = {};
  removeProductPortal: any;
  editDosingPortal: any;
  removeMedPaxPortal: any;
  removeNormalProductPortal:any;
  swapProtocolPopupPortal:any;
  confirmSwapProtocolPortal:any;
  productPortal: any;
  selectedProduct = signal<any>({});
  screenWidth = window.innerWidth;
  modalWidth = this.screenWidth <= 1100 ? this.screenWidth * 0.50 + 'px' : this.screenWidth <= 1400 ? this.screenWidth * 0.40 + 'px' : this.screenWidth * 0.30 + 'px';

  //Edit Dosing
  durationOptions = weekArray;
  selectedTimes: string[] = [''];
  editDosingField = new FormGroup({
    duration: new FormControl('ON_GOING', Validators.required),
    units: new FormControl(1, [Validators.required, Validators.min(0)]),
    notes: new FormControl('', Validators.maxLength(4000)),
  });
  currentEditDosingType = '';
  selectedProductCodes = signal<any>([]);
  entryNumberList: any;
  getQuantity = signal<number>(0);
  dosingDataCache: { [productCode: string]: Observable<any> } = {};
  isMedPaxProductsClicked = signal<boolean>(false);
  MAX_QTY = 10;

  currentSwappableProtocolId ='';
  swappedProtocolId='';
  sortControl= new FormControl('');

  hideMedpaxPage: boolean = false;
  currentUserData$: Observable<any> = this.curuserService.getCurrentUserData();

  openRemoveProductCode: any;

  constructor(
    public wsPageService: WsPage,
    private cdRef: ChangeDetectorRef,
    private recommendationService: RecommendationService,
    private favoriteService: FavoriteService,
    private router: Router,
    private activatedRoute: ActivatedRoute,
    private destroyRef: DestroyRef,
    private dialog: BsModalService,
    protected facetService: WsFacetService
  ) {

    const width = window.innerWidth;
    this.isMobile = width <= 991;
    this.activatedRoute.paramMap
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe((params: any) => {
        this.recommendationId = params.get('id');
      });
      this.protocolService.fetchIcons().
        subscribe((res: any) => {
          this.DisplayItemTimes = res.itemTimeListWsDTO.map((data: any) => {
            return {
              code: data?.code,
              src: this.protocolService.getBaseUrl() + data?.iconImage?.url,
              alt: data?.timeOfTheDay
            }
          })
          this.DisplayItemTimes.reverse();
        });
    this.currentUserData$
    .pipe(takeUntilDestroyed(this.destroyRef))
    .subscribe({
      next: (data) => {
          this.hideMedpaxPage = data?.hideMedpaxPage;
      },
    })
  }
  ngOnInit(): void {
    this.getPageInfo();
    this.recommendationService?.getRecommendation(this.recommendationId).subscribe(res => {
      this.patientName = res?.patient;
    });
    // this.patientName = localStorage.getItem('patient');
    this.loadRecommendationOnPageLoad();
    this.fetchDosingInfo();

      // this.updateFilterFromQueries();

    this.facetService.isFilterSelected.pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe(res => {
        if (res) this.updateFilter();
    })

    this.favoriteService.favoriteItems$.subscribe((favorites) => {
      favorites?.wishLists?.forEach((element: any) => {
        if(element.name.includes('plpwishlist')) {
          this.favoriteList = {
            wishLists: {
              wishListEntries: [...element.wishListEntries]
            }
          }
        }
      });
    });
    this.customEventsHandler();
  }

  checkRoleExist(role: string): Observable<boolean> {
    return this.curuserService.isRoleExist(role).pipe(takeUntilDestroyed(this.destroyRef));
  }

  ngAfterViewInit() {
    this.filterPortal = new TemplatePortal(this.filterProtocol, this.vcr);
    this.editDosingPortal = new TemplatePortal(this.editDosingPopup, this.vcr);
    this.removeProductPortal = new TemplatePortal(this.removeProductPopUp, this.vcr);
    this.removeMedPaxPortal = new TemplatePortal(this.removeEntireMedPax, this.vcr);
    this.removeNormalProductPortal = new TemplatePortal(this.removeNormalProductPopup,this.vcr);
    this.swapProtocolPopupPortal = new TemplatePortal(this.swapProtocolPopup,this.vcr);
    this.confirmSwapProtocolPortal = new TemplatePortal(this.confirmSwapProtocol,this.vcr);
    this.productPortal = new TemplatePortal(this.recommendationProduct, this.vcr);
    this.wrapperForm.valueChanges.pipe(takeUntilDestroyed(this.destroyRef)).subscribe(() => {
      const hobbiesArray = this.wrapperForm.get('hobbiesArray') as FormArray;
      let currQty = hobbiesArray.controls.reduce((total, group) => {
        let qty = group.get('qty')?.value || 0;
        qty = qty.toString().replace(/[^0-9]/g, '');
        if (!qty || qty == '0') {
          group.get('qty').patchValue(1, { emitEvent: false });
          qty = 1;
        }
        const numericQty = Number(qty);
        if (numericQty >= 10) {
            group.get('qty').patchValue(10, { emitEvent: false });
            qty = 10;
        } else {
            group.get('qty').patchValue(qty, { emitEvent: false });
        }
        
        return total + Number(qty);
      }, 0);
      this.getQuantity.set(currQty);
    });

    this.editDosingField.valueChanges.pipe(takeUntilDestroyed(this.destroyRef)).subscribe(() => {
      let qty = this.editDosingField.get('units')?.value || 0;
      qty = Number(qty.toString().replace(/[^0-9]/g, ''));
        
      if (!qty) qty = 0;
    
      const numericQty = Number(qty);
      if (numericQty >= 9999) {
        this.editDosingField.get('units').patchValue(9999, { emitEvent: false });
      } else {
        this.editDosingField.get('units').patchValue(qty, { emitEvent: false });
      }
    });
    this.updateSort();
  }

  private getQueryString(url: string): string {
    const questionMarkIndex = url.indexOf('?');
    return questionMarkIndex !== -1 ? url.substring(questionMarkIndex) : '';
 }

  loadRecommendationOnPageLoad(){
    this.recommendationService
      .getRecommendationEntries(this.recommendationId)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe((res: any) => {
        this.cartList = [];
        this.patientName = res?.patient;
        this.swappedProtocolId = res?.recommendationEntries?.find((entry:any) => entry?.templateId)?.templateId;
        if (res.recommendationEntries && res.recommendationEntries.length > 0) {
          this.itemsInCart = res.recommendationEntries;
          this.recommendationEntries = res;
          this.currentBlueLabelProductRemove = this.itemsInCart?.find((item:any)=> item?.product.code == this.openRemoveProductCode);
          this.updaterecommendationNormalProductdata(res);
          this.itemsInCart.forEach((element: any) => {
            this.cartList.push(element.product.code);
          });
        } else {
          this.itemsInCart = [];
        }
        this.msrpSubTotalPrice = res?.msrpSubTotalPrice;
        this.LoadSelectedMedpaxProduct();
        this.cdRef.detectChanges();
      });
  }

  openProductDetailPopup(productCode: string) {
    const initialState = {
      productCode: productCode
    };
    this.bsModalRef = this.dialog.show(WsProductDetailsPopupComponent, {
      initialState: initialState
    });
    this.bsModalRef.content.productCode = productCode;
 }

  showSearchResults(key: string) {
    this.SearchText = key;
    //Need to search for any search key
    this.payLoad.query = this?.SearchText;
    this.recommendationService
      .getAllProducts(this.payLoad)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe((res) => {
        this.products.set(res);
        this.dataSource = res.products;
        this.updatePagination(res);
        this.cdRef.detectChanges();
      });
  }

private updateSort(){
  this.sortControl.valueChanges.pipe(takeUntilDestroyed(this.destroyRef))
  .subscribe((sort:string)=> this.sortList(sort));
}

  sortList(sortCode: string): void {
    this.payLoad.sort = sortCode;
    this.recommendationService
      .getAllProducts(this.payLoad)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe((res: any) => {
        this.products.set(res);
        this.updatePagination(res);
        this.dataSource = this.productsList = res.products;
        const currentSortingCode = res?.sorts?.find((item:any)=> item?.selected)?.code;
        this.sortControl.setValue(currentSortingCode,{emitEvent:false});
        this.cdRef.detectChanges();
      });
  }

  getCartItems(closePopup = false) {
    this.recommendationService
      .getRecommendationEntries(this.recommendationId)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe((res) => {
        this.recommendationEntries = res;
        if (res.recommendationEntries && res.recommendationEntries.length > 0) {
          this.itemsInCart = res.recommendationEntries;
          this.currentBlueLabelProductRemove = this.itemsInCart?.find((item:any)=> item?.product.code == this.openRemoveProductCode);
          this.updaterecommendationNormalProductdata(res);
          this.cdRef.detectChanges();
        } else {
          this.itemsInCart = [];
          this.cdRef.detectChanges();
        }
        closePopup && this.closePopup();
      });
  }

  updaterecommendationNormalProductdata(res: any) {
    this.selectedProductCodes.set(res.recommendationEntries.map((prod: any) => { return prod.product.code }));
    this.entryNumberList = res.recommendationEntries.map((prod: any) => {
      return {
        entryNumber: prod?.entryNumber || 0,
        code: prod?.product?.code || ''
      };
    });
  }

  filterPopup() {
    this.overlayService.openModal(this.filterPortal, this.modalWidth);
 }

  showFilters() {
    const initialState: ModalOptions = {
      class: 'ws-right-modal',
      initialState: {
        facets: this.filterfacets,
      },
    };
    this.bsModalRef = this.dialog.show(
      WsRecommendationFiltersComponent,
      initialState
    );
  }

  editDosing(currentType: string, item: any, isFromRecommendationEntry = false) {
    this.wrapperForm.reset();
    this.savedAsCustomSignal.set(false);
    this.isMedPaxProductsClicked.set(false);
    this.currentProduct = this.products()?.products?.find((res: any) => res.code == (item?.product?.code || item?.code)) || (item?.product|| item) ;
    this.currentRecommendationProduct =
    isFromRecommendationEntry ?
    item :
    this.recommendationEntries?.recommendationEntries?.find((res: any) => res?.product?.code == item?.code);
    this.getCustomDosing();
    this.updateUnitWithDurationAndNotes();
    this.overlayService.openModal(this.editDosingPortal, this.isMobile ? '65%' : this.modalWidth);
    this.isMedPaxEditDosing = false;
    this.currentEditDosingType = currentType;
  }

  updateUnitWithDurationAndNotes(){
    this.editDosingField.patchValue({
      duration:this.currentRecommendationProduct?.itemDuration || 'ON_GOING',
      notes:this.currentRecommendationProduct?.notes,
      units:this.currentRecommendationProduct?.quantity || 1
    });
  }
  addOrRemove(productCode: string): boolean {
    return this.selectedProductCodes()?.includes(productCode);
  }

  fetchDosingInfo() {
    this.recommendationService
      .getCustomDosing()
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe((res: any) => {
        this.dosingInfo = res.itemTimeListWsDTO;
        this.cdRef.detectChanges();
      });
  }

  getPageInfo() {
    this.payLoad.filters = `isMedpax:${this.isMedPaxTabSelected()}`;
    this.recommendationService
      .getAllProducts(this.payLoad)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe((res: any) => {
        res?.products.forEach((element: any) => {
          let product =
            this.favoriteList?.wishLists?.wishListEntries?.findIndex(
              (product: any) => product.productCode === element.code
            );
          element.isFavorite = product > -1 ? true : false;
        });
        this.commonFacet.currentSearchList$.next(res);
        this.products.set(res);
        this.dataSource = this.productsList = res.products;
        this.sortOptions = res.sorts;
        this.filterfacets = res.facets;
        this.updatePagination(res);
        const currentSortingCode = res?.sorts?.find((item:any)=> item?.selected)?.code;
        this.sortControl.setValue(currentSortingCode,{emitEvent:false});
        this.cdRef.detectChanges();
      });
  }

  addItems(item: any) {
    this.cartList.push(item.code);
    this.editDosingField.patchValue({
      duration:'ON_GOING',
      units:1,
      notes:''
    });
    this.protocolService.getCustomDosingEntries(item.code)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe((apiRes: any) => {
        this.recommendationService
          .addProductsToRecommendation(this.recommendationId, this.createPayLoad(item.code, apiRes))
          .pipe(takeUntilDestroyed(this.destroyRef))
          .subscribe((res) => {
            this.getCartItems();
            this.addProductAlert(item?.name); 
          });
      });

  }

  addProductAlert(productName: string): void {
    this.alerts.push({
      type: 'info',
      msg: `"${productName}" added to recommendation`, 
      timeout: 4000,
    });
    this.alertSignal.set(this.alerts);
    this.cdRef.markForCheck();
  }

  removeProductAlert(productName: string): void {
    this.alerts.push({
      type: 'info',
      msg: `"${productName}" deleted`, 
      timeout: 4000,
    });
    this.alertSignal.set(this.alerts);
    this.cdRef.markForCheck();
  }

  getName(msg: string) {
    return msg?.replace(/<[^>]*>/g, '') || '';
  }

  removeFromCart(item: any) {
    this.recommendationService
      .removeProductsFromRecommendation(this.recommendationId, item.entryNumber)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe(() => {
        this.cartList = this.cartList.filter((e) => e !== item.product.code);
        this.getCartItems();
        this.closePopup();
      });
  }
  openRemoveProduct(code:string){
    this.openRemoveProductCode = code;
    this.currentBlueLabelProductRemove = this.itemsInCart?.find((item:any)=> item?.product.code == code);
    const product = this.products()?.products?.find((item:any)=> item.code == code) || this.currentBlueLabelProductRemove?.product;
    this.currentProduct = product;
    this.overlayService.openModal(this.removeNormalProductPortal,this.modalWidth);
  }
  removeProduct(item: any) {
    let index = this.itemsInCart?.findIndex((i) => i.product.code === item.code);
    this.recommendationService
      .removeProductsFromRecommendation(
        this.recommendationId,
        this.itemsInCart[index].entryNumber
      )
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe(() => {
        this.cartList = this.cartList.filter((e) => e !== item.code);
        this.getCartItems(true);
        this.removeProductAlert(item?.name);
      });
  }

  goToReviewRecommendation() {
    this.router.navigate([
      '/Recommendation/RecommendationReview',
      this.recommendationId,
    ]);
  }

  pageChanged(event: any) {
    this.payLoad.currentPage = event - 1 ;
    this.getPageInfo();
  }

  updateFavorites(item: any, isFav: boolean) {
    if (isFav) {
      this.favoriteService
        .removeFromFavorites(item.code)
        .pipe(takeUntilDestroyed(this.destroyRef))
        .subscribe((res) => {
          item.isFavorite = !item.isFavorite;
          this.add(item.isFavorite);
          this.cdRef.markForCheck();
        });
    } else {
      this.favoriteService
        .addToFavorites(item.code)
        .pipe(takeUntilDestroyed(this.destroyRef))
        .subscribe((res) => {
          item.isFavorite = !item.isFavorite;
          this.add(item.isFavorite);
          this.cdRef.markForCheck();
        });
    }
  }

  add(isFavorite: boolean): void {
    this.alerts.push({
      type: 'info',
      msg: isFavorite
        ? `Product added to favorites.`
        : `Product removed from favorites.`,
      timeout: 2000,
    });
    this.alertSignal.set(this.alerts);
  }

  isFavorite(item: any): boolean {
    let product = this.favoriteList?.wishLists?.wishListEntries?.findIndex((product: any) => product.productCode === item.code);
    return product > -1 ? true : false;
  }

  onClosed(dismissedAlert: any): void {
    this.alerts = this.alerts.filter((alert) => alert !== dismissedAlert);
    this.alertSignal.set(this.alerts);
  }

  productPopUp() {
    this.overlayService.openModal(this.productPortal);
  }

  openEditDosingPopup(currentType: string, currProd: any, isFromRecommendationEntry = false) {
    this.wrapperForm.reset();
    this.savedAsCustomSignal.set(false);
    this.isMedPaxEditDosing = false;
    this.isMedPaxProductsClicked.set(false);
    this.selectedProduct.set(this.recommendationEntries);
    if (isFromRecommendationEntry) {
      this.currentRecommendationProduct = this.selectedProduct()?.recommendationEntries?.find((item: any) => item?.product?.code == currProd?.product?.code);
      const product = this.products()?.products?.find((item: any) => item.code === currProd?.product?.code) || this.currentRecommendationProduct?.product;
      this.currentProduct = product;
    } else {
      this.currentProduct = currProd;
      this.currentRecommendationProduct = this.selectedProduct()?.recommendationEntries?.find((item: any) => item?.product?.code == this.currentProduct?.code);
    }
    this.updateUnitWithDurationAndNotes();
    this.currentEditDosingType = currentType;
    this.getCustomDosing();
    this.overlayService.openModal(this.editDosingPortal, this.modalWidth);
  }

  //MEDPAX UPDATES
  customEventsHandler() {
    this.eventService.deleteButton.pipe(takeUntilDestroyed(this.destroyRef)).subscribe((res: any) => {
      this.isMedPaxProductsClicked.set(true);
      const product = this.products()?.products?.find((item:any)=> item?.code  ==(res?.product?.code  || res?.product?.product?.code)) || res?.product?.product ;
      this.removeMedPaxProduct = product;
      this.removeMedPaxProductEntryNumber = res?.EntryNumber;
      this.overlayService.openModal(this.removeProductPortal, this.modalWidth);
    });

    this.eventService.editButton.pipe(takeUntilDestroyed(this.destroyRef)).subscribe((res: any) => {
      this.wrapperForm.reset();
      this.savedAsCustomSignal.set(false);
      this.isMedPaxProductsClicked.set(true);
      if (res?.isMedPaxEntry) {
        this.currentMedPaxProduct.set(this.medPaxProducts()?.medpaxEntry?.find((data: any) => data?.product?.code == res?.code));
        let currProd   = {prod: this.products()?.products?.find((item: any) => item.code == res.code) || this.currentMedPaxProduct()?.product };
        this.currentProduct = currProd ;

      } else {
        this.currentMedPaxProduct.set(this.medPaxProducts()?.medpaxEntry?.find((data: any) => data?.product?.code == res?.code));
        this.currentProduct = res;
      }
      this.isMedPaxEditDosing = true;
      // this.getCustomDosing();
      this.overlayService.openModal(this.editDosingPortal, this.modalWidth);
    });

    this.eventService.addButton.pipe(takeUntilDestroyed(this.destroyRef)).subscribe((res: any) => {
      this.currentProduct = res.prod;
      this.postCustomMedPax(res);
    });

    this.eventService.saveAsCustom.pipe(takeUntilDestroyed(this.destroyRef)).subscribe(() => {
      this.saveAsCustom();
    });

    this.eventService.deleteEntireMedPax.pipe(takeUntilDestroyed(this.destroyRef)).subscribe(()=>{
      this.overlayService.openModal(this.removeMedPaxPortal, this.modalWidth);
   });

  }

  createHobbyGroup(hobby?: { qty: number; radio: string, id: string }): FormGroup {
    return new FormGroup({
      qty: new FormControl(hobby?.qty || 1, [Validators.required, Validators.min(1)]),
      radio: new FormControl(hobby?.radio || '', Validators.required),
      id: new FormControl(hobby?.id || '')
    });
  }

  get hobbies(): FormArray {
    return this.wrapperForm.get('hobbiesArray') as FormArray;
  }

  callLoadProductOnMedPaxTabSelection(selected: boolean) {
    this.isMedPaxTabSelected.set(selected);
    this.payLoad.filters =`isMedpax:${this.isMedPaxTabSelected()}`;
    this.getPageInfo();
  }

  //customMedpAX CODE
  postCustomMedPax(product: any,fromPopup=false,payload={}) {
    this.protocolService.getCustomDosingEntries(product?.code)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe((apiRes: any) => {
        const medpaxEntryPayload = this.createMedpaxEntryPayload(apiRes, product);
        this.createNewMedpaxPayLoad = {
          name: '',
          medpaxEntry: [medpaxEntryPayload],
          recommendationId: this.recommendationId
        };
        if (this.medPaxProducts()?.code) {
          const medPaxId = this.medPaxProducts().code;
          if(fromPopup){
            this.updateMedPax(payload, medPaxId);
          }else{
            this.updateMedPax(medpaxEntryPayload, medPaxId);
          }
        } else {
          this.createNewMedPax();
        }
      });
  }

  // Helper function to create medpax entry payload
  private createMedpaxEntryPayload(customDosing: any, product: any, hobbiesArray: any[] = []): any {
    const customDosingEntries = customDosing?.dosingList?.map((item: any) => ({
      quantity: item.quantity,
      itemTimes: [{ code: item?.itemTimes?.code }],
      product: { code: item?.product?.code },
    }));

    const dosingEntries = hobbiesArray?.map((hobby) => ({
      quantity: hobby.qty,
      itemTimes: [{ code: hobby.radio }],
      product: { code: product?.code },
    }));

    const { inAM = 0, inPM = 0, inDaily = 0, inBedtime = 0 } = product ?? {};

    const totalQuantity =
      dosingEntries?.reduce((sum: any, entry: any) => +sum + +entry.quantity, 0) ||
      customDosingEntries?.reduce((sum: any, entry: any) => +sum + +entry.quantity, 0) ||
      [inAM, inPM, inDaily, inBedtime]?.reduce((sum: any, val: any) => +sum + +val, 0) ||
      1;

    const labelDosing = [{ qty: inAM, value: 'AM' }, { qty: inPM, value: 'PM' }, { qty: inDaily, value: 'Daily' }, { qty: inBedtime, value: 'Bedtime' }]
    .filter((response: any) => response.qty > 0).map((item: any) => ({
      quantity: item.qty,
      itemTimes: [{ code: item.value }],
      product: { code: product?.code },
    }));

    let currentDosing = (hobbiesArray?.length) ? dosingEntries : (customDosing?.dosingList?.length) ? customDosingEntries.slice(0, 4) : labelDosing;

    return {
      product: { code: product?.code },
      price: product?.wholeSalePrice?.value?.toString() || '0',
      quantity: totalQuantity,
      dosing: currentDosing,
    };

  }

  // Create new medpax
  private createNewMedPax() {
    this.customMedPaxService.createCustomMedPax(this.createNewMedpaxPayLoad)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe(() => {
        this.LoadSelectedMedpaxProduct(true);
        this.closePopup();
      });
  }

  // Update existing medpax  ADD BUTTON
  private updateMedPax(payload: any, medPaxId: string) {
    this.customMedPaxService.updateMedpax(payload, medPaxId)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe(() => {
        this.LoadSelectedMedpaxProduct(true);
        this.closePopup();
      });
  }

  LoadSelectedMedpaxProduct(callLoadRecommendation = false) {
    // fetchMedPaxDetails
    this.customMedPaxService.getMedPaxForOwner(this.recommendationId, true)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: (item: any) => {
          this.updateMedpaxdetailsOnApiCall(item);
          if (callLoadRecommendation) {
            this.getCartItems();
          }
          this.cdRef.detectChanges();
        },
        error: (err: any) => {
        this.noItemsInMedPax();
        }
      });
  }

  noItemsInMedPax(){
    this.selectedMedPaxProductCodes.set([]);
    this.entryMedPaxNumberList = [];
    this.updateMedpaxState(false);
 }

  updateMedpaxdetailsOnApiCall(item: any) {
    this.medPaxProducts.set(item);
    this.updateMedpaxState(item?.medpaxEntry?.length > 0);
    this.customMedPaxService.currentMedPaxID.set(item?.code);
    this.selectedMedPaxProductCodes.set(item?.medpaxEntry?.map((prod: any) => { return prod?.product?.code }));
    this.entryMedPaxNumberList = item?.medpaxEntry?.map((prod: any) => {
      return {
        entryNumber: prod?.entryNumber || 0,
        code: prod?.product?.code || ''
      };
    });
    this.eventService.emitButtonEnable(false);
  }

  updateMedpaxState(state: boolean) {
    this.protocolService.isMedPaxPage.set(state);
  }

  // Update medpax entry  SAVE BUTTON
  private updateMedPaxEntry(payload: any, entryNumber: string) {
    this.customMedPaxService.updateMedpaxEntry(payload, entryNumber)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe(() => { this.closePopup(); this.LoadSelectedMedpaxProduct(true); });
  }


  saveMedpaxDetails(element: HTMLElement) {
    let currentProduct = this.currentProduct.code ? this.currentProduct : this.currentProduct.prod;
    const payload = this.createMedpaxEntryPayload([], currentProduct, this.wrapperForm.value?.hobbiesArray);
    if (element.innerText.toLowerCase() == 'save') {
      const entryNo = this.getMedPaxEntryNumber(currentProduct.code);
      this.updateMedPaxEntry(payload, entryNo);
    } else {
      this.postCustomMedPax(currentProduct,true,payload);
    }
  }

  getMedPaxEntryNumber(productCode: string) {
    const entry = this.entryMedPaxNumberList?.find((item: any) => item.code === productCode);
    return entry ? entry.entryNumber : null;
  }

  saveAsCustom() {
    const currentProduct = this.currentProduct.code ? this.currentProduct : this.currentProduct.prod;
    this.protocolService.postCustomDosingEntries({ isSavedAsCustom: true, productId:currentProduct?.code }, this.saveAsCustomPayLoad())
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next:() => {
           this.eventService.emitcustomDoesSaved(currentProduct?.code);
           this.pushAlert(`Custom dose saved.`, 'success');
           }
     });
  }

  pushAlert(message:string,type:'success'|'info'|'danger'|'warning'){
    this.alerts.push({
      type: type,
      msg:  message,
      timeout: 2000,
    });
    this.alertSignal.set(this.alerts);
    this.savedAsCustomSignal.set(true);
  }

  saveAsCustomPayLoad() {
    const customPayload: { [key: string]: number } = {};
    const hobbiesArray = (this.wrapperForm.get('hobbiesArray') as FormArray).controls;

    hobbiesArray.forEach(hobby => {
      const radioValue = hobby.get('radio')?.value;
      const qtyValue = hobby.get('qty')?.value;

      if (radioValue) {
        customPayload[radioValue] = qtyValue;
      }
    });

    return customPayload;
  }

  getCustomDosing(onSaveAsCustom=false) {
     this.protocolService.getCustomDosingEntries(this.currentProduct.code)
    .pipe(takeUntilDestroyed(this.destroyRef)).subscribe({
      next:(res:any)=> {
        this.customDosingdData = res;
        if(onSaveAsCustom){
          this.updateFormWithCustomDosing();
        }else{
          this.resetToCustomDosing();
        }
        this.customDosingList.set(res);
      }
     })
  }

  resetToCustomDosing() {
    const hobbiesArray = this.wrapperForm.get('hobbiesArray') as FormArray;
    let isLabelDosing = false;
    this.removeAllindexesInFormArray();
    if (this.customDosingdData.dosingList?.length) {
      this.removeInputControl(0);
    }
    //Updating the values Of the form Array itemTimes
    if (this.currentRecommendationProduct && this.currentRecommendationProduct?.dosing?.length) {
      this.removeInputControl(0);
      this.currentRecommendationProduct?.dosing?.forEach((doseData: any, index: number) => {
        if (index <= 3) {
          hobbiesArray.push(this.createHobbyGroup({ qty: doseData?.quantity || 1, radio: doseData?.itemTimes?.code, id: doseData?.id }));
          this.onRadioChange(index);
        }
      });//forEach
    } else {
      if (this.customDosingdData?.dosingList?.length) {
        this.updateFormWithCustomDosing();
      } else {
        isLabelDosing = true;
      }

    }
    if (isLabelDosing) {
      this.updateLabelDosing(this.currentProduct);
    }
    this.customDosingList.set(this.customDosingdData);
  }

  updateFormWithCustomDosing() {
    const hobbiesArray = this.wrapperForm.get('hobbiesArray') as FormArray;
    hobbiesArray.clear();
    this.customDosingdData?.dosingList?.forEach((doseData: any, index: number) => {
      if (index <= 3) {
        hobbiesArray.push(this.createHobbyGroup({ qty: doseData?.quantity || 1, radio: doseData?.itemTimes?.code, id: doseData?.id }));
        this.onRadioChange(index);
      }
    });//forEach
  }

  updateLabelDosing(product: any) {
    const labelDosing = [
      ...((+product?.inAM || 0) > 0 ? [{ key: 'inAm', qty: product.inAm, code: 'AM' }] : []),
      ...((+product?.inPM || 0) > 0 ? [{ key: 'inPm', qty: product.inPm, code: 'PM' }] : []),
      ...((+product?.inDaily || 0) > 0 ? [{ key: 'inDaily', qty: product.inDaily, code: 'Daily' }] : []),
      ...((+product?.inBedtime || 0) > 0 ? [{ key: 'inBedtime', qty: product.inBedtime, code: 'Bedtime' }] : []),
    ];

    const hobbiesArray = this.wrapperForm.get('hobbiesArray') as FormArray;
    hobbiesArray.clear();
    labelDosing.forEach((doseData: any, index: number) => {
      hobbiesArray.push(this.createHobbyGroup({
        qty: doseData.qty || 1,
        radio: doseData.code,
        id: product.id
      }));

      this.onRadioChange(index);
    });
    if (!hobbiesArray.length) {
      this.addInputControl();
    }
  }

  removeAllindexesInFormArray() {
    const hobbiesArray = this.wrapperForm.get('hobbiesArray') as FormArray;
    let length = hobbiesArray.length;
    for (let i = length; i > 0; i--) {
      this.removeInputControl(i);
    }
  }

  closePopup() {
    this.overlayService.closeTopModal();
  }

  resetToCustom() {
    this.updateFormWithCustomDosing();
  }

  //-------EDIT DOSING ----------
  postEditDosingData() {
    const type = this.currentEditDosingType.toLowerCase() == 'add' ? 'post' : 'patch';
    const code = this.currentProduct?.code || this.currentProduct?.product?.code;
    const hobbiesArray = this.wrapperForm.get('hobbiesArray') as FormArray;
    this.protocolService.getCustomDosingEntries(code)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe((apiRes: any) => {
        this.recommendationService
          .addProductTotheRecommendationwithDosingEtries(
            this.recommendationId,
            this.createPayLoad(code, apiRes, hobbiesArray.value),
            type,
            this.getEntryNumber(this.currentProduct?.code || this.currentProduct?.product?.code),
          )
          .pipe(takeUntilDestroyed(this.destroyRef))
          .subscribe(() => {
            this.cartList.push(this.currentProduct.code);
            this.closePopup();
            this.getCartItems();
          });
      })

  }

  createPayLoad(code: string, customDosing: any, hobbiesArray: any[] = []) {

    const product = this.products()?.products?.find((item: any) => item.code === code);

    const dosing = (this.wrapperForm.get('hobbiesArray') as FormArray).controls.map(hobby => ({
      id: hobby.get('id')?.value,
      quantity: hobby.get('qty')?.value,
      itemTimes: [
        { code: hobby.get('radio')?.value }
      ]
    }));

    const customDosingEntries = customDosing?.dosingList?.map((item: any) => ({
      quantity: item.quantity,
      itemTimes: [{ code: item?.itemTimes?.code }],
      product: { code: item?.product?.code },
    }));

    const { inAM = 0, inPM = 0, inDaily = 0, inBedtime = 0 } = product ?? {};

    const labelDosing = [{ qty: inAM, value: 'AM' }, { qty: inPM, value: 'PM' }, { qty: inDaily, value: 'Daily' }, { qty: inBedtime, value: 'Bedtime' }]
    .filter((response: any) => response.qty > 0)
    .map((item: any) => ({
      quantity: item.qty,
      itemTimes: [{ code: item.value }],
      product: { code: product?.code },
    }));

    let currentDosing = (hobbiesArray?.length) ? dosing : (customDosing?.dosingList?.length) ? customDosingEntries.slice(0, 4) : labelDosing;

    let PayLoad: any = {
      quantity: this.editDosingField.controls.units.value,
      notes: this.editDosingField.controls.notes.value,
      product: {
        code: code
      },
      itemDuration: this.editDosingField.controls.duration.value,
      //  productItemType:product?.materialProductForm || "BOTTLE",
      dosing: currentDosing
    };
    return PayLoad;
  }

  getEntryNumber(productCode: string) {
    const entry = this.entryNumberList?.find((item: any) => item.code === productCode);
    return entry ? entry.entryNumber : null;
  }

  //-------Dosing Form functionality starts----------

  addInputControl() {
    this.hobbies.push(this.createHobbyGroup());
    this.selectedTimes.push('');
  }

  removeInputControl(idx: number) {
    this.hobbies.removeAt(idx);
    this.selectedTimes.splice(idx, 1);
  }

  onRadioChange(idx: number, time?: any) {
    const selectedValue = time? time.code : this.hobbies.at(idx).get('radio')?.value;
    this.selectedTimes[idx] = selectedValue;
    this.hobbies.at(idx).get('radio')?.setValue(selectedValue)
    this.hobbies.controls?.forEach((control, index) => {
      if (index !== idx) {
        this.updateAvailableTimes(index);
      }
    });
  }

  updateAvailableTimes(index: number) {
    const availableTimes = this.DisplayItemTimes?.map((item: any) => item.code);
    this.selectedTimes?.forEach((time, idx) => {
      if (idx !== index && time) {
        const disableIndex = availableTimes?.indexOf(time);
        if (disableIndex !== -1) {
          availableTimes?.splice(disableIndex, 1);
        }
      }
    });
    const radioControl = this.hobbies.at(index).get('radio');
    radioControl.updateValueAndValidity();
  }

  isRadioEnabled(index: number, time: string): boolean {
    return !this.selectedTimes.some((selected, idx) => selected === time && idx !== index);
  }

  incrementQty(index: number) {
    const qtyControl = this.hobbies.at(index).get('qty');
    const currentValue = Number(qtyControl?.value || 0);
    if (currentValue < 10) {
      qtyControl?.setValue(currentValue + 1);
    }
  }
  
  decrementQty(index: number) {
    const qtyControl = this.hobbies.at(index).get('qty');
    if (qtyControl.value > 1) {
      qtyControl.setValue(Number(qtyControl.value) - 1);
    }
  }

  allowOnly1to9(event: KeyboardEvent) {
    const allowedKeys = ['Backspace', 'Tab', 'ArrowLeft', 'ArrowRight', 'Delete'];
    const isAllowedKey = /^[0-9]$/.test(event.key) || allowedKeys.includes(event.key);

    if (!isAllowedKey) {
      event.preventDefault();
    }
  }

  getSelectedRadio(idx: number): string {
    return this.selectedTimes[idx] || '';
  }

  saveEditDosing() {
    this.protocolService.postCustomDosingEntries({
      isSavedAsCustom: true,
      productId: this.currentProduct?.code || this.currentProduct?.product?.code
    }, this.saveAsCustomPayLoad())
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe(() => {
        this.getCustomDosing(true);
        this.pushAlert(`Custom dose saved.`,'success');
        this.LoadSelectedMedpaxProduct(true);
      });
  }

  //EDit DOSING SECOND FORM SECTION
  increment() {
    let value = +this.editDosingField.controls.units.value;
    if(value < 9999) this.editDosingField.controls.units.setValue(++value);
  }

  decrement() {
    let value = +this.editDosingField.controls.units.value;
    if (value > 0)
      this.editDosingField.controls.units.setValue(--value);
  }
  //EDIT DOSING ENDS HERE

  //---------dosing form fundtionality Ends-----------
  getCustomDosingOnProductLevel(productCode: string): Observable<any> {
    if (!this.dosingDataCache[productCode]) {
      this.dosingDataCache[productCode] = this.protocolService
        .getCustomDosingEntries(productCode)
        .pipe(delay(300, asyncScheduler), shareReplay(1));
    }
    return this.dosingDataCache[productCode];
  }

  deleteMedPax() {
    const productEntry = this.medPaxProducts()?.medpaxEntry?.find(
      (entry: any) => entry.entryNumber === this.removeMedPaxProductEntryNumber
    );
    const productName = productEntry ? productEntry.product?.name : 'Product';
    this.deleteTheMedPaxEntry(this.removeMedPaxProductEntryNumber, productName);
  }

 deleteExistingMedPax() {
    this.customMedPaxService.deleteAllMedPaxProducts(this.medPaxProducts().code).subscribe(() => {
       this.closePopup();
       this.noItemsInMedPax();
       this.LoadSelectedMedpaxProduct(true);
    });
 }

  deleteTheMedPaxEntry(entryNumber: any, productName: string) {
    this.customMedPaxService
    .deleteMedPaxForOwner(this.medPaxProducts().code, entryNumber)
    .pipe(takeUntilDestroyed(this.destroyRef))
    .subscribe({
      next: () => {
        this.closePopup();
        this.LoadSelectedMedpaxProduct(true);
        this.removeMedPaxProductAlert(productName); 
      },
    });
  }

  removeMedPaxProductAlert(productName: string): void {
    this.alerts.push({
      type: 'info',
      msg: `"${productName}" deleted from MedPax`, 
      timeout: 4000,
    });
    this.alertSignal.set(this.alerts);
    this.cdRef.markForCheck();
  }

 //Swap Protocol Functionality
 openSwapProtocolPopup(){
  this.overlayService.openModal(this.swapProtocolPopupPortal);
 }

 openConfirmSwapProtocol(protocolId:any){
  this.overlayService.closeTopModal();
  this.currentSwappableProtocolId = protocolId;
  this.overlayService.openModal(this.confirmSwapProtocolPortal,this.modalWidth);
 }

 swapProtocol(protocolId:any){
   this.protocolService.swapProtocol(this.recommendationId,protocolId)
   .pipe(takeUntilDestroyed(this.destroyRef)).subscribe({
    next:()=>{
      this.loadRecommendationOnPageLoad();
      this.closePopup();
    }
   })
 }

 updatePagination(res: any) {
    this.paginationData =  res.pagination;
 }

 getPaginationMessage(pagination: { currentPage: number, pageSize: number, totalResults: number }): string {
  if (pagination.totalResults === 0) {
    return "No entries available";
  }

  const start = pagination.currentPage * pagination.pageSize + 1;
  const end = Math.min(start + pagination.pageSize - 1, pagination.totalResults);

  return `Showing ${start} to ${end} of ${pagination.totalResults} entries`;
}

getAddToCartDisableFlag(selectedProduct: any): boolean {
  if(selectedProduct?.forceOutOfStock) {
    return true;
  }
  return false;
}

getProductMaterialFromName(){
  let name = this.currentProduct?.prod?.materialProductForm;
  if(name == 'Capsules') return 'capsule';
  if(name == 'Tablets') return 'tablet';
  if(name == 'Softgels') return 'softgel';
  if(name == 'Caplets') return 'caplet';
  if(name == 'Lozenges') return 'lozenge';
  if(name == 'Packets') return 'packet';
  return 'capsule';
}

sortProducts(event: any) {
  this.payLoad.sort = event.target.value;
  this.getPageInfo();
}

  updateFilterFromQueries() {
    this.activatedRoute.queryParams.pipe(
      debounceTime(300),
      distinctUntilChanged(),
      takeUntilDestroyed(this.destroyRef)
    ).subscribe(() => {
      this.QueryString = this.getQueryString(this.router.url) || '';
      this.QueryString = this.QueryString.replace('?', '&').replace('&query=', '');
      this.QueryString = this.QueryString?.replace(/%C2%AE/g, '®');
      this.QueryString = this.QueryString?.replace(/%E2%84%A2/g, '™');
      this.QueryString = this.QueryString?.replace(/%E2%80%99/g, '’');
      this.QueryString = this.QueryString?.replace(/%20%26%20/g, ' & ');
      this.QueryString = this.QueryString?.replace(/%20%2F%20/g, ' / ');
      this.payLoad.query = this.QueryString?.replace(/%20/g, ' ');
      this.getPageInfo();

    });
  }

ngOnDestroy() {
  sessionStorage.removeItem('selectedFilter');
}

updateFilter(){
  let data = sessionStorage.getItem('selectedFilter')!;
  if(data) {
    this.QueryString = data;
    this.QueryString = this.QueryString.replace('&query=', '');
    this.QueryString = this.QueryString?.replace(/%C2%AE/g, '®');
    this.QueryString = this.QueryString?.replace(/%E2%84%A2/g, '™');
    this.QueryString = this.QueryString?.replace(/%E2%80%99/g, '’');
    this.QueryString = this.QueryString?.replace(/%20%26%20/g, ' & ');
    this.QueryString = this.QueryString?.replace(/%20%2F%20/g, ' / ');
    this.payLoad.query = this.QueryString?.replace(/%20/g, ' ');
  }
  else {
    this.QueryString = '';
    this.payLoad.query = '';
  }
  
  this.getPageInfo();
}

addMedpax(product: any) {
    this.favoriteService.addMedpax.next(product);
}
}

