import {
   Component,
   DestroyRef,
   inject,
   signal,
   TemplateRef,
   ViewChild,
   ViewContainerRef,
   ChangeDetectorRef
} from '@angular/core';
import { ProtocolService } from '../../../ws-services/protocol/protocol.service';
import { WsResponsiveService } from '../../../ws-services/ws-responsive.service';
import { WsOverlay } from '../../../ws-services/ws-overlay.service';
import { TemplatePortal } from '@angular/cdk/portal';
import {
   createmedpax,
   WsCustomMedPaxService,
} from '../medpaxService/customMedpaxservice.service';
import { ActivatedRoute, Router } from '@angular/router';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import {
   asyncScheduler,
   combineLatest,
   debounceTime,
   delay,
   distinctUntilChanged,
   Observable,
   shareReplay,
   tap,
} from 'rxjs';
import { WsCustomMedPaxEvents } from '../medpaxService/medpaxEventservice.service';
import { FormGroup, FormArray, FormControl, Validators } from '@angular/forms';
import { CmsService } from '@spartacus/core';
import { TabsetComponent , TabDirective } from 'ngx-bootstrap/tabs';
import { WsCommonFacetService } from '../../ws-spartacus/product-facet-navigation/services/ws-common-facet.service';
import { currentState , getUserData, WsCurrentUserService } from '../../../ws-services/state-services/currentuser.service';
import { WsFacetService } from '../../ws-spartacus/product-facet-navigation/services/facet.service';
import { PaginationComponent } from '../../ws-shared/pagination/pagination.component';
const sortDatas = [
   {
      code: 'relevance',
      name: 'Relevance',
      selected: true,
   },
   {
      code: 'topRated',
      name: 'Top Rated',
      selected: false,
   },
   {
      code: 'name-asc',
      name: 'Name (ascending)',
      selected: false,
   },
   {
      code: 'name-desc',
      name: 'Name (descending)',
      selected: false,
   },
   {
      code: 'price-asc',
      name: 'Price (lowest first)',
      selected: false,
   },
   {
      code: 'price-desc',
      name: 'Price (highest first)',
      selected: false,
   },
];

const backGroundImg: any = {
   condSpecificBeautyLink: '/assets/icons/common_icons/csm-card-energy.webp',
   condSpecificEnergyLink: '/assets/icons/common_icons/csm-card-beauty.webp',
   condSpecificGutLink: '/assets/icons/common_icons/csm-card-gut.webp',
   condSpecificImmuneLink: '/assets/icons/common_icons/csm-card-immune.webp',
   condSpecificLearnLink: '/assets/icons/common_icons/csm-card-sleep.webp',
};
@Component({
   selector: 'ws-custom-medpax-landing-page',
   templateUrl: './custom-medpax-landing-page.component.html',
   styleUrl: './custom-medpax-landing-page.component.scss',
})
export class CustomMedpaxLandingPageComponent {
   private service = inject(WsCustomMedPaxService);
   public screen = inject(WsResponsiveService);
   private overlayService = inject(WsOverlay);
   private vcr = inject(ViewContainerRef);
   private destroyRef = inject(DestroyRef);
   private router = inject(Router);
   private activatedRoute = inject(ActivatedRoute);
   private protocolService = inject(ProtocolService);
   private eventService = inject(WsCustomMedPaxEvents);
   private cmsService = inject(CmsService);
   private commonFacet = inject(WsCommonFacetService);
   private cdr = inject(ChangeDetectorRef);
   private wsFacetService = inject(WsFacetService);
   curuserService = inject(WsCurrentUserService);
   currentUser = signal<string>('');
   currentUser$: Observable<any> = this.curuserService.currentState();
   currentState$ = inject(currentState).pipe(tap((user: string) => { this.currentUser.set(user) }));
   currentUserData = inject(getUserData);
   tabName: string = 'All Products';
   toastMessage :string = '';
   @ViewChild('tabset') tabset: TabsetComponent;
   @ViewChild('filterPopUp') filterProtocol: TemplateRef<any>;
   @ViewChild('filterPopUpForBuyAgain') filterProtocolForBuyAgain: TemplateRef<any>;
   @ViewChild('sortBy') sortByProtocol: TemplateRef<any>;
   @ViewChild('editDosing') editProtocol: TemplateRef<any>;
   @ViewChild('removeProduct') removeProtocol: TemplateRef<any>;
   @ViewChild('rightContainerPopup', { static: true })
   rightContainerPopup: TemplateRef<any>;
   @ViewChild('viewPopup', { static: true }) viewPopup: TemplateRef<any>;
   @ViewChild(PaginationComponent) paginationComponent!: PaginationComponent;

   viewPortal: any;
   sortByPortal: any;
   filterPortal: any;
   filterPortalForBuyAgain: any;
   buyAgainFacets: any;
   savedFacet: any;
   editPortal: any;
   removePortal: any;
   buyAgainloader: boolean = false;
   selectedFacetOutput: string[] = [];
   selectedActiveFacetOutput: string[] = [];
   selectedProductsAfterFacet: any = [];
   selectedProductCodesAfterFacet: any = [];
   productCodes: string[] = [];
   productName: string[] = [];

   wrapperForm = new FormGroup({
      hobbiesArray: new FormArray([this.createHobbyGroup()]),
   });

   deleteProduct: any;
   editDosingProduct: any;
   editDosingProductCode: any;

   products = signal<any>({});
   allBuyAgainProducts:any = {};
   updatedBuyAgainProducts = signal<any>({});
   bestsellersInBuyAgain = false;
   medPaxProducts = signal<any>({});
   currentMedPaxProduct = signal<any>({});
   cmsData = signal<any>({});
   isMedPaxAvailable = signal<boolean>(false);
   isSupplementVisible = signal<boolean>(false);
   getQuantity = signal<number>(0);
   pagination = signal<any>({});
   currentPage = 0;

   alerts: any[] = [];
   alertsSignal = signal<any>(this.alerts);
   savedAsCustomSignal = signal<boolean>(false);
   searchTerm: string | null = null;
   QueryString = '';
   searchKey = '';
   searchKeyBuyAgain = '';
   entryNumberList: any;
   selectedProductCodes: any;
   sorts: any[] = [];
   bannerArray: any[] = [];
   medPaxId='';
   filterTextTitle:string;

   createNewMedpaxPayLoad: createmedpax;
   sortKey: string = ''; 

   screenWidth = window.innerWidth;
   modalWidth = this.screenWidth * 0.3 + 'px';
   deleteEntryNumber: any;
   dosingDataCache: { [productCode: string]: Observable<any> } = {};
   selectedFilters: any = [];

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
      this.savedAsCustomSignal.set(true);
   }

   checkRoleExist(role: string): Observable<boolean> {
      return this.curuserService.isRoleExist(role).pipe(takeUntilDestroyed(this.destroyRef));
   } 
   
   ngOnInit(){   
      this.medPaxId = this.activatedRoute.snapshot.paramMap.get('ShoppingCartId');
      this.searchKey = this.service.currentSearchQuery();
      if(this.searchKey){
         this.updateSearch(this.searchKey);
      }
      this.searchTerm = this.activatedRoute.snapshot.queryParamMap.get('query');
      this.updateQueriesFromRouter();
      this.LoadSelectedProduct(true);
      this.customEventListeners();
      this.loadCmsPageData();
   }

   ngAfterViewInit() {
      this.filterPortal = new TemplatePortal(this.filterProtocol, this.vcr);
      this.filterPortalForBuyAgain = new TemplatePortal(this.filterProtocolForBuyAgain, this.vcr);
      this.sortByPortal = new TemplatePortal(this.sortByProtocol, this.vcr);
      this.editPortal = new TemplatePortal(this.editProtocol, this.vcr);
      this.removePortal = new TemplatePortal(this.removeProtocol, this.vcr);
      this.wrapperForm.valueChanges
         .pipe(takeUntilDestroyed(this.destroyRef))
         .subscribe(() => {
            const hobbiesArray = this.wrapperForm.get('hobbiesArray') as FormArray;
            let currQty = hobbiesArray.controls.reduce((total, group) => {
               let qty = group.get('qty')?.value || 0;
               qty = qty.toString().replace(/[^0-9]/g, '');
               if (!qty || qty == '0') {
                  group.get('qty').patchValue(1, { emitEvent: false });
                  qty = 1;
               }
               const numericQty = Number(qty);
               if (numericQty >= 99) {
                  group.get('qty').patchValue(99, { emitEvent: false });
                  qty = 99;
               } else {
                  group.get('qty').patchValue(qty, { emitEvent: false });
               }
               
               return total + Number(qty);
           }, 0);
            this.getQuantity.set(currQty);
         });
         
         let inputText = document.querySelector('.input-wrapper > input') as HTMLInputElement;
         if(inputText) {
            inputText.value = inputText?.value?.replace(/^"(.*)"$/, '$1');
         }
    if(document.getElementsByTagName('title') && !document.getElementsByTagName('title')[0].innerText) document.getElementsByTagName('title')[0].innerText = 'Custom Medpax';
   }

   //Updating the search and filters
   sortList(sortCode: string): void {
      this.sortKey = sortCode;
      this.LoadProduct();
    }

   sortListBuyAgain(sortCode: string): void {
      let holdTheSortedArray = [];
      if(sortCode.toLowerCase() == 'bestsellers') {
         this.bestsellersInBuyAgain = true;
         this.buyAgainloader = true;
         this.getBuyAgainProducts();      // get latest results
      }  else if(sortCode.toLowerCase() == 'name-asc') {
         holdTheSortedArray = this.allBuyAgainProducts?.products?.sort((a: any, b: any) =>
            a?.name.localeCompare(b?.name)
         );
      } else if(sortCode.toLowerCase() == 'name-desc') {
         holdTheSortedArray = this.allBuyAgainProducts?.products?.sort((a: any, b: any) =>
            b?.name.localeCompare(a?.name)
         );
      } else if(sortCode.toLowerCase() == 'price-asc') {
         holdTheSortedArray = this.allBuyAgainProducts?.products?.sort((a: any, b: any) =>
            a?.wholeSalePrice?.value - b?.wholeSalePrice?.value
         );
      } else if(sortCode.toLowerCase() == 'price-desc') {
         holdTheSortedArray = this.allBuyAgainProducts?.products?.sort((a: any, b: any) =>
            b?.wholeSalePrice?.value - a?.wholeSalePrice?.value
         );
      }
      this.updatedBuyAgainProducts.set({ products : holdTheSortedArray });
   }

   updateSearch(key: string) {
      if (key) {
         this.searchKey = key?.trim();
         this.currentPage = 0;
         const encodedSearch = this.searchKey ? encodeURIComponent(this.searchKey) : '';
         this.QueryString = encodedSearch ? `&query=${encodedSearch}` : '';
         this.LoadProduct();
      }
    }
    
   updateBuyAgainSearch(event: any) {
      this.searchKeyBuyAgain = event.toLowerCase();
      this.updatedBuyAgainProducts.set({
         products : this.allBuyAgainProducts?.products.filter((e: { name: string; }) => e.name.toLowerCase().includes(this.searchKeyBuyAgain)) 
      });
      this.paginationComponent.repositionPaginationLinks();
   }

   pageChanged(event: any) {
    this.currentPage = event;
    this.LoadProduct();
   }

   getPaginationMessage(pagination: any): string {
      if (pagination.totalResults === 0) {
        return "No MedPaxes available";
      }
   
      const start = pagination.currentPage * pagination.pageSize + 1;
      const end = Math.min(start + pagination.pageSize - 1, pagination.totalResults);
    
      return `Showing ${start} to ${end} of ${pagination.totalResults} MedPaxes`;
   }

   updateQueriesFromRouter() {
      combineLatest([
        this.activatedRoute.queryParams.pipe(
          debounceTime(300),
          distinctUntilChanged()
        ),
        this.wsFacetService.facetFilterList$
         .pipe(
            debounceTime(300),
            distinctUntilChanged()
         )
         ])
         .pipe(takeUntilDestroyed(this.destroyRef))
         .subscribe(([queryParams, facetFilters]) => {

         const queryStringFromRoute = this.getQueryString(this.router.url) || '';
         const qs = queryStringFromRoute.replace('?', '&');
         const facetQuery = this.buildFacetQuery(facetFilters , queryParams);
         this.QueryString = this.buildQueryString(facetQuery, qs.split('=')[1]);
         this.LoadProduct();
         });

   }

   buildQueryString(facetQuery: string, baseQuery: string): string {
      const queryPart = facetQuery || baseQuery || '';
      return queryPart ? `&query=${queryPart}` : '';
    }
    
   buildFacetQuery(queries: any[] , queryParams?: any) {
      if(queries.length == 0) {
         return '';
      }
      // Remove the leading ":bestSellers:" from each query
      const facetQuery = queries
        .map(item => item.query.replace(/^:?bestSellers:/, ''))
        .join(':');
      const plpquery = this.searchTerm ? this.searchTerm+':' : ':';
      return (queryParams?.query) ? (`${facetQuery}`) : (`${(facetQuery == ':bestSellers') ? ':bestSellers' : (':bestSellers:'+facetQuery)}`);
    }

   private getQueryString(url: string): string {
      const questionMarkIndex = url.indexOf('?');
      return (questionMarkIndex !== -1 && url.indexOf('from=cart') < 0) ? url.substring(questionMarkIndex) : '';
   }
   //Updating the search and filters

   //OOTB SERACH API CALL
   LoadProduct() {
      const sortParam = this.sortKey !== 'relevance' ? this.sortKey : null;
      this.service
         .fetchMedPaxProducts(this.currentPage, 20, this.QueryString, sortParam)
         .subscribe((response: any) => {
            this.selectedFilters = response.breadcrumbs;
            this.commonFacet.currentSearchList$.next(response);
            this.sorts = response?.sorts;
            this.products.set(response);
            this.pagination.set(response.pagination);
            this.cdr.detectChanges();
         });
   }

   trackSort(index: number, item: any): any {
      return item?.code;
   }

   getBuyAgainProducts() {
         this.LoadBuyAgainProducts();
      
   }

   //OOTB SERACH API CALL
   LoadBuyAgainProducts() {
      this.service
      .fetchBuyAgainProducts()
      .subscribe((response: any) => {
         this.allBuyAgainProducts = response;     // to hold the array so as to work on search and sorting and then assign to updatedBuyAgainProducts
         //Any Facets applied filter in this function
         this.filterSelectedValues();
         
         if(this.bestsellersInBuyAgain) {
            this.allBuyAgainProducts?.products?.sort((a: any, b: any) =>
               a?.isFeatured === b?.isFeatured ? 0 : a?.isFeatured ? -1 : 1
            );
         }
         this.buyAgainFacets = response?.facets;
         this.updatedBuyAgainProducts.set(this.allBuyAgainProducts);
         this.buyAgainloader = false; //Reset loader after getting response
         this.bestsellersInBuyAgain = false; //Reset flags after getting response
         this.cdr.detectChanges();
      });
   }

   LoadSelectedProduct(canShow = false) {
      // fetchMedPaxDetails
      const medpax$ = this.medPaxId ?  this.service.fetchMedPaxById(this.medPaxId): this.service.fetchMedPaxDetails();
         medpax$
         .pipe(takeUntilDestroyed(this.destroyRef))
         .subscribe({
            next: (item: any) => {
               this.updateMedpaxdetailsOnApiCall(item);
               if (item.medpaxEntry?.length) {
                  this.isMedPaxAvailable.set(true);
                  if (canShow) {
                     this.openRightContainerOnLoad();
                  }
               } else {
                  this.isMedPaxAvailable.set(false);
               }
               this.cdr.detectChanges();
            },
            error: (err: any) => {
               this.noMedPax();
            },
         });
      
     
   }
   

   noMedPax() {
      this.selectedProductCodes = [];
      this.entryNumberList = [];
      this.isMedPaxAvailable.set(false);
      this.medPaxProducts.set({});  
      this.cdr.detectChanges(); 
   }

   //update local via APi
   updateMedpaxdetailsOnApiCall(item: any) {
      this.medPaxProducts.set(item);
      this.selectedProductCodes = item.medpaxEntry?.map((prod: any) => {
         return prod.product.code;
      });
      this.entryNumberList = item.medpaxEntry?.map((prod: any) => {
         return {
            entryNumber: prod?.entryNumber || 0,
            code: prod?.product?.code || '',
         };
      });
      this.eventService.emitButtonEnable(false);
   }

   postCustomMedPax(product: any) {
      this.protocolService
         .getCustomDosingEntries(product?.code)
         .pipe(takeUntilDestroyed(this.destroyRef))
         .subscribe((apiRes: any) => {
            this.showToasterMsg('Pill added to MedPax');   
            const medpaxEntryPayload = this.createMedpaxEntryPayload(
               apiRes,
               product
            );
            this.createNewMedpaxPayLoad = {
               name: '',
               medpaxEntry: [medpaxEntryPayload],
            };
            if (this.selectedProductCodes?.length) {
               const medPaxId = this.medPaxProducts().code;
               this.updateMedPax(medpaxEntryPayload, medPaxId);
            } else {
               this.createNewMedPax();
            }
         });
   }

   // Helper function to create medpax entry payload
   private createMedpaxEntryPayload( customDosing: any, product: any, hobbiesArray: any[] = [] ): any {
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

      const { inAM = 0, inPM = 0, inDaily = 0, inBedtime = 0 } = product;

      const totalQuantity =
         dosingEntries?.reduce(
            (sum: any, entry: any) => +sum + +entry.quantity,
            0
         ) ||
         customDosingEntries?.reduce(
            (sum: any, entry: any) => +sum + +entry.quantity,
            0
         ) ||
         [inAM, inPM, inDaily, inBedtime]?.reduce(
            (sum: any, val: any) => +sum + +val,
            0
         ) ||
         1;

      const labelDosing = [
         { qty: inAM, value: 'AM' },
         { qty: inPM, value: 'PM' },
         { qty: inDaily, value: 'Daily' },
         { qty: inBedtime, value: 'Bedtime' },
      ]
         .filter((response: any) => response.qty > 0)
         .map((item: any) => ({
            quantity: item.qty,
            itemTimes: [{ code: item.value }],
            product: { code: product?.code },
         }));

      let currentDosing = hobbiesArray?.length
         ? dosingEntries
         : customDosing?.dosingList?.length
            ? customDosingEntries.slice(0, 4)
            : labelDosing;

      return {
         product: { code: product?.code },
         price: product?.wholeSalePrice?.value?.toString() || '0',
         quantity: totalQuantity,
         dosing: currentDosing,
      };
   }

   // Create new medpax
   private createNewMedPax() {
      this.service
         .createCustomMedPax(this.createNewMedpaxPayLoad)
         .pipe(takeUntilDestroyed(this.destroyRef))
         .subscribe(() => {
            this.LoadSelectedProduct();
         });
   }

   // Update existing medpax  ADD BUTTON
   private updateMedPax(payload: any, medPaxId: string) {
      this.service
         .updateMedpax(payload, medPaxId)
         .pipe(takeUntilDestroyed(this.destroyRef))
         .subscribe(() => {
            this.LoadSelectedProduct();
            this.closePopup();
         });
   }

   // Update medpax entry  SAVE BUTTON
   private updateMedPaxEntry(payload: any, entryNumber: string) {
      this.service
         .updateMedpaxEntry(payload, entryNumber)
         .pipe(takeUntilDestroyed(this.destroyRef))
         .subscribe(() => {
            this.overlayService.closeAllModals();
            this.LoadSelectedProduct();
         });
   }

   saveMedpaxDetails(element: HTMLElement) {
      const currentProduct = this.editDosingProduct.code ? this.editDosingProduct : this.editDosingProduct.prod;
      const payload = this.createMedpaxEntryPayload(
         [],
         currentProduct,
         this.wrapperForm.value?.hobbiesArray
      );
      const medPaxId = this.medPaxProducts().code;
      if (element.innerText.toLowerCase() == 'save') {
         const entryNo = this.getEntryNumber(currentProduct.code);
         this.updateMedPaxEntry(payload, entryNo);
         this.showToasterMsg('Pill added to MedPax');
      } else {
         if(!medPaxId) this.postCustomMedPax(currentProduct);     // On first time adding a product from edit popup ,medpax has to create
         this.updateMedPax(payload, medPaxId);
         this.showToasterMsg('Pill added to MedPax');
      }
      this.closePopup();
   }

   saveAsCustom() {
      let currentProduct = this.editDosingProduct.code ? this.editDosingProduct : this.editDosingProduct.prod;
      this.protocolService
         .postCustomDosingEntries(
            { isSavedAsCustom: true, productId:currentProduct?.code },
            this.saveAsCustomPayLoad()
         )
         .pipe(takeUntilDestroyed(this.destroyRef))
         .subscribe({
            next: () => {
               this.eventService.emitcustomDoesSaved(currentProduct?.code);
               this.showToasterMsg('Custom dose saved.');            },
         });
   }

   private saveAsCustomPayLoad() {
      const customPayload: { [key: string]: number } = {};
      const hobbiesArray = (this.wrapperForm.get('hobbiesArray') as FormArray)
         .controls;
      hobbiesArray.forEach((hobby) => {
         const radioValue = hobby.get('radio')?.value;
         const qtyValue = hobby.get('qty')?.value;
         if (radioValue) {
            customPayload[radioValue] = qtyValue;
         }
      });

      return customPayload;
   }

   private getEntryNumber(productCode: string) {
      const entry = this.entryNumberList?.find(
         (item: any) => item.code === productCode
      );
      return entry ? entry.entryNumber : null;
   }

   createHobbyGroup(hobby?: {
      qty: number;
      radio: string;
      id: string;
   }): FormGroup {
      return new FormGroup({
         qty: new FormControl(hobby?.qty || 1, [
            Validators.required,
            Validators.min(0),
         ]),
         radio: new FormControl(hobby?.radio || '', Validators.required),
         id: new FormControl(hobby?.id || ''),
      });
   }

   loadCmsPageData() {
      let data: { [key: string]: any } = {};
      this.cmsService
         .getContentSlot('Section1')
         .pipe(takeUntilDestroyed(this.destroyRef))
         .subscribe((res: any) => {
            res.components.forEach((item: any) => {
               this.cmsService
                  .getComponentData(item.uid)
                  .pipe(takeUntilDestroyed(this.destroyRef))
                  .subscribe((componentResponse: any) => {
                     switch (componentResponse.typeCode) {
                        case 'CMSParagraphComponent':
                           data[componentResponse.uid] = componentResponse.content;
                           break;

                        case 'CMSLinkComponent':
                           data[componentResponse.uid] = {
                              linkName: componentResponse.linkName,
                              link: componentResponse.url,
                           };
                           break;

                        case 'WholescriptsBannerComponent':
                           this.getBannerData(componentResponse);
                           break;

                        default:
                     }
                  });
            });
            this.cmsData.set({ paraData: data, banner: this.bannerArray });
         });
   }

   getBannerData(item: any) {
      this.cmsService
         .getComponentData(item.item)
         .pipe(takeUntilDestroyed(this.destroyRef))
         .subscribe((componentData: any) => {
            this.bannerArray.push({
               name: componentData.linkName,
               url: componentData.url,
               src: this.protocolService.getBaseUrl() + item.media.url,
               backgroundSrc: backGroundImg[componentData.uid],
            });
         });
   }

   //POPUP
   openViewPopup() {
      this.extractProductDetails(); 
      this.viewPortal = new TemplatePortal(this.viewPopup, this.vcr);
      this.overlayService.openModal(this.viewPortal, this.modalWidth);
   }

   closePopup() {
      this.overlayService.closeAllModals();
      this.isSupplementVisible.set(false);
      if(this.tabName.toLowerCase().includes('buy')) {
         this.selectedProductsAfterFacet = [];
      }
   }

   onTabSelect(tab?: TabDirective): void {
      this.tabName = tab?.heading;

      if(this.tabName.toLowerCase().includes('buy')) {
         this.buyAgainloader = true;
         this.getBuyAgainProducts();
      }
      this.updateQueriesFromRouter();
   }

   filterPopup(filterText?:string) {
      this.filterTextTitle = filterText;
      this.overlayService.openModal(this.filterPortal, this.modalWidth);
   }

   filterPopupForBuyAgain() {
      this.savedFacet = this.selectedFacetOutput;
      this.overlayService.openModal(this.filterPortalForBuyAgain, this.modalWidth);
   }

   clearAllFilters() {
      this.selectedActiveFacetOutput = [];
      this.selectedFacetOutput = [];
      this.getBuyAgainProducts()
   }

   receiveSelectedValues(selected: any) {
      this.selectedFacetOutput = selected;
      if(this.selectedFacetOutput.length == 0) { 
         this.selectedProductsAfterFacet = [];
         this.selectedProductCodesAfterFacet = [];
      }
      this.filterSelectedValues();
   }

   receiveActiveSelectedValues(selected: any) {
      this.selectedActiveFacetOutput = selected;
   }

   filterSelectedValues() {
      if(this.selectedFacetOutput.length) {
         this.selectedFacetOutput.map((facet:any , facetIndex:number) => {
            this.allBuyAgainProducts?.products.map((prod:any , prodIndex:number) => {
               if(facet?.name?.toLowerCase() == 'categories' && prod?.categories?.length > 0) {
                  
                  let isCategoryProduct = this.callCategoryFilters(facet , prod);
                  if (isCategoryProduct && !this.selectedProductCodesAfterFacet.includes(prod?.code)) {
                     this.selectedProductsAfterFacet.push(prod); 
                     this.selectedProductCodesAfterFacet.push(prod?.code);
                  }

               } else if(facet?.name.toLowerCase() == 'brand') {
                  let isBrandProduct = this.callBrandFilters(facet , prod);
                  if (isBrandProduct && !this.selectedProductCodesAfterFacet.includes(prod?.code)) {
                     this.selectedProductsAfterFacet.push(prod); 
                     this.selectedProductCodesAfterFacet.push(prod?.code);
                  }
               } else if(facet?.name.toLowerCase() == 'deliveryform') {
                  let isdeliveryFormProduct = this.calldeliveryFormFilters(facet , prod);
                  if (isdeliveryFormProduct && !this.selectedProductCodesAfterFacet.includes(prod?.code)) {
                     this.selectedProductsAfterFacet.push(prod); 
                     this.selectedProductCodesAfterFacet.push(prod?.code);
                  }
               }
            });
         });
         this.updatedBuyAgainProducts.set({ products : (this.selectedProductsAfterFacet.length > 0) ? this.selectedProductsAfterFacet : this.allBuyAgainProducts?.products });
      }
   }

   callCategoryFilters(facet:any , prod:any) {
      let facetCategorySelectedCodes = facet?.values;
      let proCategoryCodes = prod?.categories.map((cat:any , catIndex:number) => {
         return cat.code
      });
      return facetCategorySelectedCodes.some((item:any) => proCategoryCodes.includes(item));
   }

   callBrandFilters(facet:any , prod:any) {
      let facetBrandSelectedCodes = facet?.values;
      let prodBrandCode = prod?.brandCode;
      return facetBrandSelectedCodes.includes(prodBrandCode);
   }

   calldeliveryFormFilters(facet:any , prod:any) {
      let facetdeliveryFormSelectedCodes = facet?.values;
      let proddeliveryFormCode = prod?.deliveryForm;
      return facetdeliveryFormSelectedCodes.includes(proddeliveryFormCode);
   }

   sortByPopup() {
      this.overlayService.openModal(this.sortByPortal);
   }
   editDosingPopup() {
      this.overlayService.openModal(this.editPortal, this.modalWidth);
   }
   removePopup() {
      this.overlayService.openModal(this.removePortal, '33%');
   }
   openRightContainerOnLoad() {
      const rightContainerPortal = new TemplatePortal(
         this.rightContainerPopup,
         this.vcr
      );
      this.overlayService.openModal(rightContainerPortal, this.modalWidth);
   }
   //POPUP ENDS

   //CUSTOM EVENTLISTENERS
   customEventListeners() {
      this.eventService.editButton
         .pipe(takeUntilDestroyed(this.destroyRef))
         .subscribe((res: any) => {
            if (res?.isMedPaxEntry) {
               this.currentMedPaxProduct.set(this.medPaxProducts()?.medpaxEntry?.find((data: any) => data?.product?.code == res?.code));
               const product = {prod: this.currentMedPaxProduct()?.product || this.products().products.find((item: any) => item.code == res.code)};
               this.editDosingProduct = product;
               this.editDosingProductCode = res.code;
            } else {
               this.currentMedPaxProduct.set(
                  this.medPaxProducts()?.medpaxEntry?.find(
                     (data: any) => data?.product?.code == res?.code
                  )
               );
               this.editDosingProduct = res;
               this.editDosingProductCode = res.code;
            }
            this.editDosingPopup();
         });
         
      this.eventService.deleteButton
         .pipe(takeUntilDestroyed(this.destroyRef))
         .subscribe((res: any) => {
            const product = this.products().products.find((item: any) =>item?.code == (res?.product?.code || res?.product?.product?.code)) || res?.product?.product ;
            this.deleteProduct = product;
            this.deleteEntryNumber = res.EntryNumber;
            this.removePopup();
         });

      this.eventService.addButton
         .pipe(takeUntilDestroyed(this.destroyRef))
         .subscribe(res => this.postCustomMedPax(res));

      this.eventService.saveAsCustom
         .pipe(takeUntilDestroyed(this.destroyRef))
         .subscribe(() => this.saveAsCustom());
   }

   deleteMedPax() {
      this.deleteTheMedPaxEntry(
         this.deleteEntryNumber
      );
   }

   toggleSupplementFacts() {
      this.isSupplementVisible.set(!this.isSupplementVisible());
   }

   extractProductDetails() {
      const medpaxEntries = this.medPaxProducts()?.medpaxEntry || [];
      if (medpaxEntries.length === 0) {
        this.productCodes = [];
        this.productName = [];
        return;
      }
      this.productCodes = medpaxEntries.map((entry: any) => entry.product?.code || '');
      this.productName = medpaxEntries.map((entry: any) => entry.product?.name || '');
   }

   showToasterMsg(msg: string) {
      this.toastMessage = msg;
      this.cdr.detectChanges();
      this.resetToastMessage();
   }
   resetToastMessage() {
      setTimeout(() => {
        this.toastMessage = '';
        this.cdr.detectChanges();
      }, 2000)
    }

   deleteExistingMedPax() {
      this.service.deleteAllMedPaxProducts(this.medPaxProducts().code).subscribe(() => {
         this.noMedPax();
         this.closePopup();
      });
   }

   deleteTheMedPaxEntry(entryNumber: any) {
      this.service
         .deleteMedPaxForOwner(this.medPaxProducts().code, entryNumber)
         .pipe(takeUntilDestroyed(this.destroyRef))
         .subscribe({
            next: () => {
               this.closePopup();
               this.LoadSelectedProduct();
               this.showToasterMsg('Pill removed from MedPax');
            },
         });
   }

   getCustomDosingOnProductLevel(productCode: string): Observable<any> {
      if (!this.dosingDataCache[productCode]) {
         this.dosingDataCache[productCode] = this.protocolService
            .getCustomDosingEntries(productCode)
            .pipe(delay(300, asyncScheduler), shareReplay(1));
      }
      return this.dosingDataCache[productCode];
   }


   getProductMaterialFromName(){
      let name = this.editDosingProduct?.prod?.materialProductForm;
      if(name == 'Capsules') return 'capsule';
      if(name == 'Tablets') return 'tablet';
      if(name == 'Softgels') return 'softgel';
      if(name == 'Caplets') return 'caplet';
      if(name == 'Lozenges') return 'lozenge';
      if(name == 'Packets') return 'packet';
      return 'capsule';
    }
}
