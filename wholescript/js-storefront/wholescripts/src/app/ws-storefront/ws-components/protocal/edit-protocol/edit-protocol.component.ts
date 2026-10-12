import { ChangeDetectorRef, Component, computed, DestroyRef, HostListener, inject, OnInit, signal, TemplateRef, ViewChild, ViewContainerRef } from '@angular/core';
import { BsModalRef, BsModalService, ModalOptions } from 'ngx-bootstrap/modal';
import { CreateNewProtocolComponent } from '../create-new-protocol/create-new-protocol.component';
import { ProtocolAdd, ProtocolService, searchProducts } from '../../../ws-services/protocol/protocol.service';
import { ActivatedRoute, Router } from '@angular/router';
import { asyncScheduler, debounceTime, delay, distinctUntilChanged, Observable, shareReplay, Subject, tap } from 'rxjs';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { TemplatePortal } from '@angular/cdk/portal';
import { WsOverlay } from '../../../ws-services/ws-overlay.service';
import { FormArray, FormControl, FormGroup, Validators } from '@angular/forms';
import { ApiCall } from '../../ws-practitioner/ws-My-patients/my-patient-landing/practitioner-my-patient-listing.service';
import { WsResponsiveService } from '../../../ws-services/ws-responsive.service';
import { WsCommonFacetService } from '../../ws-spartacus/product-facet-navigation/services/ws-common-facet.service';
import { FavoriteService } from '../../../ws-services/ws-favorite.service';
import { WsCustomMedPaxEvents } from '../../custom-medpax/medpaxService/medpaxEventservice.service';
import { createmedpax, WsCustomMedPaxService } from '../../custom-medpax/medpaxService/customMedpaxservice.service';
import { weekArray, weekMap, materialForm } from '../../ws-shared/edit-dosingdetailspopup/editDosingConstants';
import { CmsService } from '@spartacus/core';
import { currentState, WsCurrentUserService } from '../../../ws-services/state-services/currentuser.service';
import { WsProductDetailsPopupComponent } from '../../ws-pdp/ws-product-details-popup/ws-product-details-popup.component';
import { WsFacetService } from '../../ws-spartacus/product-facet-navigation/services/facet.service';

@Component({
   selector: 'ws-edit-protocol',
   templateUrl: './edit-protocol.component.html',
   styleUrl: './edit-protocol.component.scss'
})
export class EditProtocolComponent implements OnInit {
   private protocolService = inject(ProtocolService);
   private destroyRef = inject(DestroyRef);
   private overlayService = inject(WsOverlay);
   private vcr = inject(ViewContainerRef);
   private router = inject(Router);
   public screen = inject(WsResponsiveService);
   private commonFacet = inject(WsCommonFacetService);
   private eventService = inject(WsCustomMedPaxEvents);
   private customMedPaxService = inject(WsCustomMedPaxService);
   public searchSubject = new Subject<string>();
   
   currentUser = signal<string>('');
   currentState$ = inject(currentState).pipe(tap((user: string) => { this.currentUser.set(user) }));
   curuserService = inject(WsCurrentUserService);
   materialFormType = materialForm;
   alerts: any[] = [];
   alertsSignal = signal<any>(this.alerts);
   savedAsCustomSignal = signal<boolean>(false);
   prevFocusElm: HTMLElement | null = null;

   pagination = {
      pageSize: 20,
      totalResults: 0,
      currentPage: 0,
      totalPages: 0
   };

   medPaxPagination = {
      pageSize: 20,
      totalResults: 0,
      currentPage: 0,
      totalPages: 0
   }

   @ViewChild('editDosing') editDosing: TemplateRef<any>;
   @ViewChild('deleteProtocol') deleteProtocol: TemplateRef<any>;
   @ViewChild('productPopup') productProtocol: TemplateRef<any>;
   @ViewChild('filterPopUp') filterProtocol: TemplateRef<any>;
   @ViewChild('sortBy') sortByProtocol: TemplateRef<any>;
   @ViewChild('removeProductPopUp') removeProductPopUp: TemplateRef<any>;
   @ViewChild('removeMedPax') removeEntireMedPax: TemplateRef<any>;
   @ViewChild('removeMedPaxProductPopup') removeMedPaxProductPopup: TemplateRef<any>;

   editDosingPortal: any;
   deletePortal: any;
   productPortal: any;
   filterPortal: any;
   sortByPortal: any;
   removeProductPortal: any;
   removeMedPaxPortal: any;
   removeMedPaxProductPortal: any;

   selectedProductCodes = signal<any>([]);
   entryNumberList: any;
   protocalDetails: any;
   protocolId: string = ''
   bsModalRef?: BsModalRef;
   searchKey = '';
   QueryString = '';
   activeFilters = signal<any[]>([]);
   currentPage = 0;
   pageSizeQuery = '&currentPage=0';
   selectedMedPaxProductCodes = signal<any>([]);
   entryMedPaxNumberList: any;
   isMedPaxEditDosing = false;
   favoriteList: { wishLists?: { wishListEntries: { productCode: string }[] } } = {};
   medpaxFavorites: { wishLists?: { wishListEntries: { productCode: string }[] } } = {};
   searchProductPayLoad: searchProducts = {
      currentPage: 0,
      pageSize: 10,
      query: ''
   };
   SearchFocus: boolean = false;
   allProductsList: any = [];
   removeMedPaxProduct: any;
   removeMedPaxProductEntryNumber: any;
   products = signal<any>({});
   medPaxTableProducts = signal<any>({});
   selectedProduct = signal<any>({});
   totalPrice = signal<string>('');
   totalMsrpPrice = signal<string>('');
   medPaxProducts = signal<any>({});
   isMedPax = computed(() => this.protocolService.isMedPaxPage());
   isMedPaxTabSelected = signal<boolean>(false);
   addProductPayLoad: ProtocolAdd = {
      quantity: 1,
      notes: '',
      product: {
         code: ''
      },
      itemDuration: 'ON_GOING',
      productItemType: 'BOTTLE',
      dosing: [
         {
            quantity: 0,
            itemTimes: [
               {
                  code: '',
               }
            ]
         }
      ]
   }
   currentMedPaxProduct = signal<any>({});

   //dosingForm starts
   DisplayItemTimes: any[] = [];
   selectedTimes: string[] = [''];
   wrapperForm = new FormGroup({
      hobbiesArray: new FormArray([this.createHobbyGroup()])
   });
   editDosingField = new FormGroup({
      duration: new FormControl('ON_GOING', Validators.required),
      units: new FormControl(1, [Validators.required, Validators.min(0)]),
      notes: new FormControl('', Validators.required),
   });


   payLoad: ApiCall = {
      currentPage: 0,
      pageSize: 10,
      discount: '',
      referralCode: '',
      status: '',
      searchText: '',
      sortBy: '',
      practitionerId: '',
   };
   createNewMedpaxPayLoad: createmedpax;

   durationOptions = weekArray;
   weekMapItems: any = weekMap;
   currentEditDosingType = '';
   currentProduct: any;
   customDosingdData: any;
   currentProtocolProduct: any;
   fetchImages$: Observable<any> = this.protocolService.fetchIcons().pipe(
      tap((res: any) => {
         this.DisplayItemTimes = res.itemTimeListWsDTO.map((data: any) => {
            return {
               code: data?.code,
               src: this.protocolService.getBaseUrl() + data?.iconImage?.url,
               alt: data?.timeOfTheDay
            }
         })
      }),
      shareReplay({ bufferSize: 1, refCount: true })
   );
   customDosingList = signal<any>({});
   getQuantity = signal<number>(0);
   //dosing Form ends

   loader: boolean = false;
   screenWidth = window.innerWidth;
   modalWidth = this.screenWidth * 0.35 + 'px';
   dosingDataCache: { [productCode: string]: Observable<any> } = {};
   isMedPaxProductsClicked = signal<boolean>(false);
   editProtocolTitle$: Observable<any> = this.cmsService.getComponentData('myProtocolEditTitleComponent');

   //sortFormControl
   sortControl = new FormControl('');

   hideMedpaxPage: boolean = false;
   currentUserData$: Observable<any> = this.curuserService.getCurrentUserData();
   selectedFilters: any = [];

   constructor(private bsModalService: BsModalService,
      private activatedRoute: ActivatedRoute,
      private favoriteService: FavoriteService,
      public cmsService: CmsService,
      private cdRef: ChangeDetectorRef,
      protected facetService: WsFacetService
   ) {
      this.favoriteService
      .fetchFavorites()
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe();
      this.currentUserData$
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
         next: (data) => {
            this.hideMedpaxPage = data?.hideMedpaxPage;
         },
      });
    }

   ngOnInit(): void {
      this.loader = true;
      this.favoriteService.favoriteItems$.subscribe((favorites) => {
         favorites?.wishLists?.forEach((element: any) => {
            if (element.name.includes('plpwishlist')) {
               this.favoriteList = {
                  wishLists: {
                     wishListEntries: [...element.wishListEntries]
                  }
               }
            }
         });
      });
      
      // this.updateFilterFromQueries();

      this.facetService.isFilterSelected.pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe(res => {
         if(res) this.updateFilter();
      })

      this.searchSubject.pipe(
         debounceTime(400),
         distinctUntilChanged(),
         takeUntilDestroyed(this.destroyRef)
      ).subscribe((searchKey: string) => {
         this.updateSearch(searchKey);
      });

      this.protocolId = this.activatedRoute.snapshot.paramMap.get('id');
      if (this.protocolId) {
         this.LoadSelectedProduct();
         this.LoadSelectedMedpaxProduct();
      }
      this.customEventsHandler();
      this.updateFilter();
   }

   checkRoleExist(role: string): Observable<boolean> {
      return this.curuserService.isRoleExist(role).pipe(takeUntilDestroyed(this.destroyRef));
   }

   ngAfterViewInit() {
      this.editDosingPortal = new TemplatePortal(this.editDosing, this.vcr);
      this.deletePortal = new TemplatePortal(this.deleteProtocol, this.vcr);
      this.productPortal = new TemplatePortal(this.productProtocol, this.vcr);
      this.filterPortal = new TemplatePortal(this.filterProtocol, this.vcr);
      this.sortByPortal = new TemplatePortal(this.sortByProtocol, this.vcr);
      this.removeProductPortal = new TemplatePortal(this.removeProductPopUp, this.vcr);
      this.removeMedPaxPortal = new TemplatePortal(this.removeEntireMedPax, this.vcr);
      this.removeMedPaxProductPortal = new TemplatePortal(this.removeMedPaxProductPopup, this.vcr);
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

      this.UpdateSortCodeFromFormControl();
   }

   openProductDetailPopup(productCode: string) {
      const initialState = {
         productCode: productCode
      };
      this.bsModalRef = this.bsModalService.show(WsProductDetailsPopupComponent, {
         initialState: initialState
      });
      this.bsModalRef.content.productCode = productCode;
   }

   private getQueryString(url: string): string {
      const questionMarkIndex = url.indexOf('?');
      return questionMarkIndex !== -1 ? url.substring(questionMarkIndex) : '';
   }

private UpdateSortCodeFromFormControl(){
   this.sortControl.valueChanges.pipe(takeUntilDestroyed(this.destroyRef)).subscribe((res:string)=> this.sortList(res))
}
   sortList(sortCode: string): void {
      this.protocolService.sort(sortCode);
   }

   sortByPopup() {
      this.overlayService.openModal(this.sortByPortal);
   }
   filterPopup() {
      this.overlayService.openModal(this.filterPortal);
      this.prevFocusElm = document.activeElement as HTMLElement;
   }
   productPopUp() {
      this.overlayService.openModal(this.productPortal);
   }
   LoadProduct(pageSize?: string) {
      let apiUrl = this.currentPage ? this.QueryString + pageSize : this.QueryString;
       apiUrl = apiUrl?.replace('sortCode','sort');
      this.protocolService
         .fetchProducts(
            this.searchProductPayLoad,
            apiUrl + `&filters=isMedpax:false`
         )
         .subscribe({
            next: (response: any) => {
               this.commonFacet.currentSearchList$.next(response);
               this.loader = false;
               this.selectedFilters = response.breadcrumbs;
               response?.products.forEach((element: any) => {
                  let product =
                     this.favoriteList?.wishLists?.wishListEntries?.findIndex(
                        (product: any) => product.productCode === element.code
                     );
                  element.isFavorite = product > -1 ? true : false;
               });
               const currentSortingCode = response?.sorts?.find((item:any)=> item?.selected).code;
                this.sortControl.setValue(currentSortingCode,{emitEvent:false});
               this.products.set(response);
               this.pagination = {
                  ...response.pagination,
                  currentPage: response?.pagination?.currentPage + 1
               };
               this.cdRef.detectChanges();
            },
            error: () => {
               this.loader = false;
            },
         });
   }

   LoadMedPaxListProducts(pageSize?: string) {
      let apiUrl = this.currentPage ? this.QueryString + pageSize : this.QueryString;
      apiUrl = apiUrl?.replace('sortCode','sort');
      this.protocolService
         .fetchProducts(
            this.searchProductPayLoad,
            apiUrl + `&filters=isMedpax:true`
         )
         .subscribe({
            next: (response: any) => {
               this.commonFacet.currentSearchList$.next(response);
               this.medPaxTableProducts.set(response);
               console.log( this.medPaxTableProducts(), ' this.medPaxTableProducts');
               this.loader = false;
               this.products.set(response);
               response?.products.forEach((element: any) => {
                  let product =
                     this.favoriteList?.wishLists?.wishListEntries?.findIndex(
                        (product: any) => product.productCode === element.code
                     );
                  element.isFavorite = product !== -1 ? true : false;
               });
               const currentSortingCode = response?.sorts?.find((item:any)=> item?.selected).code;
               this.sortControl.setValue(currentSortingCode,{emitEvent:false});
               this.medPaxPagination = {
                  ...response.pagination,
                  currentPage: response?.pagination?.currentPage + 1
               };
            },
            error: () => {
               this.loader = false;
            },
         });
   }

   pageChanged(event: any) {
      this.currentPage = event;
      this.pageSizeQuery = this.currentPage ? '&currentPage=' + (this.currentPage - 1) : '';
      if (this.isMedPaxTabSelected()) {
         this.LoadMedPaxListProducts(this.pageSizeQuery);
      } else {
         this.LoadProduct(this.pageSizeQuery);
      }

   }

   LoadSelectedProduct() {
      this.protocolService.fetchEditableProducts(this.protocolId).pipe(takeUntilDestroyed(this.destroyRef)).subscribe((item: any) => {
         this.selectedProductCodes.set(item.protocolEntries.map((prod: any) => { return prod.product.code }))
         this.entryNumberList = item.protocolEntries.map((prod: any) => {
            return {
               entryNumber: prod?.entryNumber || 0,
               code: prod?.product?.code || ''
            };
         });
         this.totalPrice.set(item?.totalPrice?.formattedValue);
         this.totalMsrpPrice.set(item?.msrpSubTotalPrice?.formattedValue);
         this.selectedProduct.set(item);
         this.loader = false;
         this.cdRef.markForCheck();
      }, () => {
         this.loader = false;
      });
   }

   deleteProtocolApi() {
      this.loader = true;
      this.protocolService.deleteAProtocol(this.protocolId).
         pipe(takeUntilDestroyed(this.destroyRef)).subscribe({
            next: () => {
               this.overlayService.closeAllModals();
               this.router.navigate([`protocol/list`]);
               this.loader = false;
            }, error: () => {
               this.loader = false;
            }
         }
         );
   }

   closePopup() {
      this.editDosingField.reset();
      this.overlayService.closeTopModal();
      if (this.prevFocusElm) {
         setTimeout(() => {
           this.prevFocusElm?.focus();
           this.prevFocusElm = null;
         }, 0);
       }
   }

   AddProduct(data: any) {
      const { code, name } = data; 
      this.addProductPayLoad.product.code = code;
      this.loader = true;
      this.protocolService.getCustomDosingEntries(code)
         .pipe(takeUntilDestroyed(this.destroyRef))
         .subscribe((apiRes: any) => {
            this.protocolService
               .addProductsToProtocol(this.protocolId, this.createPayLoad(code, apiRes))
               .pipe(takeUntilDestroyed(this.destroyRef))
               .subscribe(() => {
                  this.LoadProduct();
                  this.LoadSelectedProduct();
                  this.addCart(name); 
                  this.loader = false;
               }, () => {
                  this.loader = false;
               });
         })

   }

   addCart(productName: string): void {
      this.alerts.push({
          type: 'info',
          msg: `"${productName}" added`, 
          timeout: 5000,
      });
      this.alertsSignal.set(this.alerts);
      this.cdRef.markForCheck();
  }

   getAlertMessage(msg: string) {
      return msg?.replace(/<[^>]*>/g, '') || '';
   }

   RemoveProduct(entryNo: any, productName: string) {
      let PayLoad = {
         protocolId: this.protocolId,
         entryNumber: entryNo
      };
      this.protocolService
         .removeProductsFromProtocol(PayLoad)
         .subscribe(() => {
            this.LoadProduct();
            this.LoadSelectedProduct();
            this.closePopup();
            this.removeProductAlert(productName); 
      });
   }
 
   removeProductAlert(productName: string): void {
      this.alerts.push({
        type: 'info',
        msg: `"${productName}" removed`, 
        timeout: 4000,
      });
      this.alertsSignal.set(this.alerts);
      this.cdRef.markForCheck();
    }

   addOrRemove(productCode: string): boolean {
      return this.selectedProductCodes()?.includes(productCode);
   }

   updateSearch(searchKey: string): void {
      if (!this.searchKey) {
         this.searchProductPayLoad.query = '';
      }
      this.searchKey = searchKey?.trim() ?? '';
      this.searchProductPayLoad = {
         ...this.searchProductPayLoad,
         currentPage: 0
      };
      this.currentPage = 0;
      this.pageSizeQuery = '&currentPage=0';
         const currentQuery = this.activatedRoute.snapshot.queryParamMap.get('query') || '';
         const [searchPart, ...rest] = currentQuery.split(':');
     
         const filters = currentQuery.includes(':category:')
           ? currentQuery.split(':').slice(1).join(':')
           : '';
     
         let newQuery = '';
     
         if (searchKey && filters) {
           newQuery = `${searchKey}:${filters}`;
         } else if (searchKey) {
           newQuery = searchKey;
         } else if (filters) {
           newQuery = `:${filters}`;
         }
     
         this.router.navigate([], {
           relativeTo: this.activatedRoute,
           queryParams: { query: newQuery },
           queryParamsHandling: 'merge',
         });
   }


   
   addAndRemove(isRemove: boolean, entryNo: any, product?: any) {
      this.isMedPaxProductsClicked.set(false);
      if (isRemove) {
         this.currentProduct = this.selectedProduct()?.protocolEntries?.find((item: any) => item?.product?.code == product?.code);
         this.removeProduct(this.currentProduct);
      } else {
         this.AddProduct(product);
      }
   }

   removeProtocolItem(product: any) {
      const entryNo = this.getEntryNumber(product?.product?.code);
      if (entryNo !== null) {
        this.RemoveProduct(entryNo, product?.product?.name);
      }
   }

   getEntryNumber(productCode: string) {
      const entry = this.entryNumberList?.find((item: any) => item.code === productCode);
      return entry ? entry.entryNumber : null;
   }

   deletePopup() {
      this.overlayService.openModal(this.deletePortal);
   }


   getBaseUrl() {
      return this.protocolService.getBaseUrl();
   }

   openEditDosingPopup(currentType: string, currProd: any, isFromProtocolEntry = false) {
      this.wrapperForm.reset();
      this.savedAsCustomSignal.set(false);
      this.isMedPaxEditDosing = false;
      this.isMedPaxProductsClicked.set(false);
      if (isFromProtocolEntry) {
         this.currentProtocolProduct = this.selectedProduct()?.protocolEntries?.find((item: any) => item?.product?.code == currProd?.product?.code);
         const product = this.products()?.products?.find((item: any) => item.code === currProd?.product?.code) || this.currentProtocolProduct?.product;
         this.currentProduct = product;
      } else {
         this.currentProduct = currProd;
         this.currentProtocolProduct = this.selectedProduct()?.protocolEntries?.find((item: any) => item?.product?.code == this.currentProduct?.code);
      }
      this.updateUnitWithDurationAndNotes();
      this.currentEditDosingType = currentType;
      this.getCustomDosing();
      this.prevFocusElm = document.activeElement as HTMLElement;
      setTimeout(() => {
         this.overlayService.openModal(this.editDosingPortal, this.modalWidth);
      },200)
   }

   editProtocolName(typeOfProtocol: string, protocolName: string, protocolDesc: string) {
      const initialState: ModalOptions = {
         initialState: {
            typeOfProtocol: typeOfProtocol,
            protocolName: protocolName,
            protocolId: this.protocolId,
            protocolDesc: protocolDesc,
         }
      };
      this.bsModalRef = this.bsModalService.show(CreateNewProtocolComponent, initialState);
      this.bsModalRef.onHide.subscribe((data: any) => {
         this.LoadSelectedProduct();
      })
   }

   updateUnitWithDurationAndNotes() {
      this.editDosingField.patchValue({
         duration: this.currentProtocolProduct?.itemDuration || 'ON_GOING',
         notes: this.currentProtocolProduct?.notes,
         units: this.currentProtocolProduct?.quantity || 1
      });
   }

   //-------Dosing Form functionality starts----------
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
      this.hobbies.at(idx).get('radio')?.setValue(selectedValue);
      this.hobbies.controls?.forEach((control, index) => {
         if (index !== idx) {
           this.updateAvailableTimes(index);
         }
      });
   }

   updateAvailableTimes(index: number) {
      const availableTimes = this.DisplayItemTimes.map(item => item.code);
      this.selectedTimes.forEach((time, idx) => {
         if (idx !== index && time) {
            const disableIndex = availableTimes.indexOf(time);
            if (disableIndex !== -1) {
               availableTimes.splice(disableIndex, 1);
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
      if(Number(qtyControl.value) < 9999) qtyControl.setValue(Number(qtyControl.value) + 1);
   }

   decrementQty(index: number) {
      const qtyControl = this.hobbies.at(index).get('qty');
      if (qtyControl.value > 1) {
         qtyControl.setValue(Number(qtyControl.value) - 1);
      }
   }

   allowOnly1to9(event: KeyboardEvent) {
      console.log("allowOnly1to9");
      const allowedKeys = ['Backspace', 'Tab', 'ArrowLeft', 'ArrowRight', 'Delete'];
      const isAllowedKey = /^[1-9]$/.test(event.key) || allowedKeys.includes(event.key);
   
      if (!isAllowedKey) {
      event.preventDefault(); // Block anything that's not 1-9 or a control key
      }
   }

   getSelectedRadio(idx: number): string {
      return this.selectedTimes[idx] || '';
   }

   resetToCustom() {
      this.updateFormWithCustomDosing();
   }

   //---------dosing form fundtionality Ends-----------
   createPayLoad(code: string, customDosing: any, hobbiesArray: any[] = []) {

      const product = this.products()?.products?.find((item: any) => item.code === code) || null;

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
      })) || [];

      const inAM = product?.inAM ?? 0;
      const inPM = product?.inPM ?? 0;
      const inDaily = product?.inDaily ?? 0;
      const inBedtime = product?.inBedtime ?? 0;

      const labelDosing = [
         { qty: inAM, value: 'AM' },
         { qty: inPM, value: 'PM' },
         { qty: inDaily, value: 'Daily' },
         { qty: inBedtime, value: 'Bedtime' }
      ]
         .filter(item => item.qty > 0)
         .map(item => ({
            quantity: item.qty,
            itemTimes: [{ code: item.value }],
            product: { code: product?.code ?? '' },
         }));

      let currentDosing = hobbiesArray?.length
         ? dosing
         : customDosingEntries.length
            ? customDosingEntries.slice(0, 4)
            : labelDosing;

      let PayLoad: ProtocolAdd = {
         quantity: this.editDosingField.controls.units.value || 1,
         notes: this.editDosingField.controls.notes.value || '',
         product: {
            code: code
         },
         itemDuration: this.editDosingField.controls.duration.value || 'ON_GOING',
         productItemType: this.materialFormType.get(product?.materialProductForm) || "BOTTLE",
         dosing: currentDosing
      };

      return PayLoad;
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

   getCustomDosing(onSaveAsCustom = false) {
      this.protocolService.getCustomDosingEntries(this.currentProduct.code || this.currentProduct?.prod?.code)
         .pipe(takeUntilDestroyed(this.destroyRef)).subscribe({
            next: (res: any) => {
               this.customDosingdData = res;
               this.customDosingList.set(this.customDosingdData);
               if (onSaveAsCustom) { this.updateFormWithCustomDosing() }
               else { this.resetToCustomDosing() }
            }
         });
   }

   resetToCustomDosing() {
      const hobbiesArray = this.wrapperForm.get('hobbiesArray') as FormArray;
      let isLabelDosing = false;
      this.removeAllindexesInFormArray();
      if (this.customDosingdData.dosingList?.length) {
         this.removeInputControl(0);
      }
      //Updating the values Of the form Array itemTimes
      if (this.currentProtocolProduct && this.currentProtocolProduct?.dosing?.length) {
         this.removeInputControl(0);
         this.currentProtocolProduct?.dosing?.forEach((doseData: any, index: number) => {
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

   postEditDosingData() {
      const type = this.currentEditDosingType.toLowerCase() == 'add' ? 'post' : 'patch';
      const code = this.currentProduct?.code || this.currentProduct?.product?.code;
      const hobbiesArray = this.wrapperForm.get('hobbiesArray') as FormArray;
      this.protocolService.getCustomDosingEntries(code)
         .pipe(takeUntilDestroyed(this.destroyRef))
         .subscribe((apiRes: any) => {
            this.protocolService
               .addProductTotheProtocolwithDosingEtries(
                  this.protocolId,
                  this.createPayLoad(code, apiRes, hobbiesArray.value),
                  type,
                  this.getEntryNumber(code),
               )
               .pipe(takeUntilDestroyed(this.destroyRef))
               .subscribe(() => {
                  this.loader = false;
                  this.closePopup();
                  this.LoadSelectedProduct();
               }, () => {
                  this.loader = false;
               });
         }, () => {
            this.loader = false;
         })
         if (this.prevFocusElm) {
            setTimeout(() => {
              this.prevFocusElm?.focus();
              this.prevFocusElm = null;
            }, 0);
          }

   }

   @HostListener('document:keydown', ['$event'])
   handleEscapeKey(event: KeyboardEvent): void {
     if (event.key === 'Escape') {
       this.closePopup();
     }
   }
 
   onKeyDown(event: KeyboardEvent) {
      if (event.key === 'Enter' || event.key === ' ') {
        event.preventDefault();
        this.closePopup()
      }
    }

   saveEditDosing() {
      this.protocolService.postCustomDosingEntries({
         isSavedAsCustom: true,
         productId: this.currentProduct?.code || this.currentProduct?.product?.code
      }, this.saveAsCustomPayLoad())
         .pipe(takeUntilDestroyed(this.destroyRef))
         .subscribe(() => {
            this.getCustomDosing(true);
            this.pushAlert(`Custom dose saved.`, 'success');
            this.LoadSelectedProduct();
         });
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

   //EDit DOSING SECOND FORM SECTION
   increment() {
      let value = +this.editDosingField.controls.units.value;
      if(value < 9999) this.editDosingField.controls.units.setValue(++value)
   }

   decrement() {
      let value = +this.editDosingField.controls.units.value;
      if (value > 0)
         this.editDosingField.controls.units.setValue(--value)
   }
   //EDIT DOSING ENDS HERE
   // remove product popup
   removeProduct(product: any) {
      this.currentProduct = product;
      this.overlayService.openModal(this.removeProductPortal, "35%");
      this.prevFocusElm = document.activeElement as HTMLElement;
   }

   updateFavorites(item: any, isFav: boolean) {
      if (isFav) {
         this.favoriteService
            .removeFromFavorites(item.code)
            .pipe(shareReplay(1))
            .subscribe((res) => {
               item.isFavorite = !item.isFavorite;
               this.add(item.isFavorite);
               this.cdRef.detectChanges();
            });
      } else {
         this.favoriteService
            .addToFavorites(item.code)
            .pipe(shareReplay(1))
            .subscribe(() => {
               item.isFavorite = !item.isFavorite;
               this.add(item.isFavorite);
               this.cdRef.detectChanges();
            });
      }
   }

   add(isFavorite: boolean): void {
      this.alerts.push({
         type: 'info',
         msg: isFavorite ? `Product added to favorites.` : `Product removed from favorites.`,
         timeout: 2000
      });
      this.alertsSignal.set(this.alerts);
   }

   onClosed(dismissedAlert: any): void {
      this.alerts = this.alerts.filter((alert) => alert !== dismissedAlert);
      this.alertsSignal.set(this.alerts);
   }

   isFavorite(item: any): boolean {
      let product = this.favoriteList?.wishLists?.wishListEntries?.findIndex((product: any) => product.productCode === item.code);
      return product > -1 ? true : false;
   }

   customEventsHandler() {
      this.eventService.deleteButton.pipe(takeUntilDestroyed(this.destroyRef)).subscribe((res: any) => {
         this.isMedPaxProductsClicked.set(true);
         const product = this.medPaxTableProducts()?.products?.find((item: any) => item?.code == (res?.product?.code || res?.product?.product?.code)) || res?.product?.product;
         this.removeMedPaxProduct = product;
         console.log(this.removeProduct, "this-product-to-be-romoved");
         this.removeMedPaxProductEntryNumber = res.EntryNumber;
         this.overlayService.openModal(this.removeMedPaxProductPortal, this.modalWidth);
      });

      this.eventService.editButton.pipe(takeUntilDestroyed(this.destroyRef)).subscribe((res: any) => {
         this.wrapperForm.reset();
         this.savedAsCustomSignal.set(false);
         this.isMedPaxProductsClicked.set(true);
         this.medPaxEditbuttonUpdates(res);
      });

      this.eventService.addButton.pipe(takeUntilDestroyed(this.destroyRef)).subscribe((res: any) => {
         this.currentProduct = res.prod;
         this.postCustomMedPax(res);
      });

      this.eventService.saveAsCustom.pipe(takeUntilDestroyed(this.destroyRef)).subscribe(() => {
         this.saveAsCustom();
      });

      this.eventService.deleteEntireMedPax.pipe(takeUntilDestroyed(this.destroyRef)).subscribe(() => {
         this.overlayService.openModal(this.removeMedPaxPortal, this.modalWidth);
      });

   }

   medPaxEditbuttonUpdates(res: any) {
      if (res?.isMedPaxEntry) {
         this.currentMedPaxProduct.set(this.medPaxProducts()?.medpaxEntry?.find((data: any) => data?.product?.code == res?.code));
         const prod = { prod: this.medPaxTableProducts()?.products?.find((item: any) => item?.code == res?.code) || this.currentMedPaxProduct()?.product };
         this.currentProduct = prod;
      } else {
         this.currentMedPaxProduct.set(this.medPaxProducts()?.medpaxEntry?.find((data: any) => data?.product?.code == res?.code));
         let prod = { prod: res.prod };
         this.currentProduct = prod;
      }
      this.isMedPaxEditDosing = true;
      this.getCustomDosing();
      this.overlayService.openModal(this.editDosingPortal, this.modalWidth);
   }
   //customMedpAX CODE
   postCustomMedPax(product: any, fromPopup = false, payload = {}) {
      this.protocolService.getCustomDosingEntries(product?.code)
         .pipe(takeUntilDestroyed(this.destroyRef))
         .subscribe((apiRes: any) => {
            const medpaxEntryPayload = this.createMedpaxEntryPayload(apiRes, product);
            this.createNewMedpaxPayLoad = {
               name: '',
               medpaxEntry: [medpaxEntryPayload],
               protocolId: this.protocolId
            };
            if (this.medPaxProducts()?.code) {
               const medPaxId = this.medPaxProducts().code;
               if (fromPopup) {
                  this.updateMedPax(payload, medPaxId);
               } else {
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

      const { inAM = 0, inPM = 0, inDaily = 0, inBedtime = 0 } = product;

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
         });
   }

   // Update existing medpax  ADD BUTTON
   private updateMedPax(payload: any, medPaxId: string) {
      this.customMedPaxService.updateMedpax(payload, medPaxId)
         .pipe(takeUntilDestroyed(this.destroyRef))
         .subscribe(() => {
            this.LoadSelectedMedpaxProduct(true);
         });
   }

   LoadSelectedMedpaxProduct(callProtocol = false) {
      // fetchMedPaxDetails
      this.customMedPaxService.getMedPaxForOwner(this.protocolId)
         .pipe(takeUntilDestroyed(this.destroyRef))
         .subscribe({
            next: (item: any) => {
               this.updateMedpaxdetailsOnApiCall(item);
               if (callProtocol) {
                  this.LoadSelectedProduct();
               }
            },
            error: (err: any) => {
               this.noItemsInMedPax();
            }
         });
   }

   noItemsInMedPax() {
      this.selectedMedPaxProductCodes.set([]);
      this.entryMedPaxNumberList = [];
      this.updateMedpaxState(false);
   }

   //update local via APi
   updateMedpaxdetailsOnApiCall(item: any) {
      this.medPaxProducts.set(item);
      this.updateMedpaxState(item?.medpaxEntry?.length > 0);
      this.customMedPaxService.currentMedPaxID.set(item?.code);
      this.selectedMedPaxProductCodes.set(item?.medpaxEntry?.map((prod: any) => { return prod.product.code }));
      this.eventService.emitButtonEnable(false);

      this.entryMedPaxNumberList = item?.medpaxEntry?.map((prod: any) => {
         return {
            entryNumber: prod?.entryNumber || 0,
            code: prod?.product?.code || ''
         };
      });
   }

   // Update medpax entry  SAVE BUTTON
   private updateMedPaxEntry(payload: any, entryNumber: string) {
      this.customMedPaxService.updateMedpaxEntry(payload, entryNumber)
         .pipe(takeUntilDestroyed(this.destroyRef))
         .subscribe(() => {
            this.LoadSelectedMedpaxProduct(true);
         });
   }


   saveMedpaxDetails(element: HTMLElement) {
      let currentProduct = this.currentProduct.code ? this.currentProduct : this.currentProduct.prod;
      const payload = this.createMedpaxEntryPayload([], currentProduct, this.wrapperForm.value?.hobbiesArray);
      if (element.innerText.toLowerCase() == 'save') {
         const entryNo = this.getMedPaxEntryNumber(currentProduct.code);
         this.updateMedPaxEntry(payload, entryNo);
      } else {
         this.postCustomMedPax(currentProduct, true, payload);
      }
      this.closePopup();
   }

   getMedPaxEntryNumber(productCode: string) {
      const entry = this.entryMedPaxNumberList?.find((item: any) => item.code === productCode);
      return entry ? entry.entryNumber : null;
   }

   saveAsCustom() {
      const currentProduct = this.currentProduct.code ? this.currentProduct : this.currentProduct.prod;
      this.protocolService.postCustomDosingEntries({ isSavedAsCustom: true, productId: currentProduct?.code }, this.saveAsCustomPayLoad())
         .pipe(takeUntilDestroyed(this.destroyRef))
         .subscribe(
            {
               next: () => {
                  this.eventService.emitcustomDoesSaved(currentProduct?.code);
                  this.pushAlert(`Custom dose saved.`, 'success');
               }
            });
   }

   updateMedpaxState(state: boolean) {
      this.protocolService.isMedPaxPage.set(state);
   }

   callLoadProductOnMedPaxTabSelection(selected: boolean) {
      this.isMedPaxTabSelected.set(selected);
      this.currentPage = 0;
      this.pagination = {
         pageSize: 20,
         totalResults: 0,
         currentPage: 0,
         totalPages: 0
      };
   
      this.medPaxPagination = {
         pageSize: 20,
         totalResults: 0,
         currentPage: 0,
         totalPages: 0
      }
      if (this.isMedPaxTabSelected()) {
         this.LoadMedPaxListProducts('&currentPage=0');
      } else {
         this.LoadProduct('&currentPage=0');
      }
   }

   getCustomDosingOnProductLevel(productCode: string): Observable<any> {
      if (!this.dosingDataCache[productCode]) {
         this.dosingDataCache[productCode] = this.protocolService
            .getCustomDosingEntries(productCode)
            .pipe(delay(300, asyncScheduler), shareReplay(1));
      }
      return this.dosingDataCache[productCode];
   }

   deleteMedPax() {
      this.deleteTheMedPaxEntry(
         this.removeMedPaxProductEntryNumber
      );
   }

   deleteExistingMedPax() {
      this.customMedPaxService.deleteAllMedPaxProducts(this.medPaxProducts().code).subscribe(() => {
         this.closePopup();
         this.noItemsInMedPax();
         this.LoadSelectedMedpaxProduct(true);
      });
   }

   deleteTheMedPaxEntry(entryNumber: any) {
      this.customMedPaxService
         .deleteMedPaxForOwner(this.medPaxProducts().code, entryNumber)
         .pipe(takeUntilDestroyed(this.destroyRef))
         .subscribe({
            next: () => {
               this.closePopup();
               this.LoadSelectedMedpaxProduct(true);
            },
         });
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

   updateFilterFromQueries() {
      this.activatedRoute.queryParams.pipe(
         debounceTime(300),
         distinctUntilChanged(),
         takeUntilDestroyed(this.destroyRef)
      ).subscribe(() => {
         this.QueryString = this.getQueryString(this.router.url) || '';
         this.QueryString = this.QueryString.replace('?', '&');
         if (this.isMedPaxTabSelected()) {
            this.LoadMedPaxListProducts(this.pageSizeQuery);
         } else {
            this.LoadProduct(this.pageSizeQuery);
         }
      });
   }

   ngOnDestroy() {
      sessionStorage.removeItem('selectedFilter');
   }

   updateFilter(){
      let data = sessionStorage.getItem('selectedFilter')!;
      if(data) this.QueryString = data;
      else this.QueryString = '';
      
      if (this.isMedPaxTabSelected()) {
         this.LoadMedPaxListProducts(this.pageSizeQuery);
      } else {
         this.LoadProduct(this.pageSizeQuery);
      }
   }

}

