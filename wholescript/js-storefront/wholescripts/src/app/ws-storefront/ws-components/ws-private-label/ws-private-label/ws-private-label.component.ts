import {
  AfterViewInit,
  ChangeDetectorRef,
  Component,
  DestroyRef,
  OnInit
} from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { PatientDirectService } from '../../../ws-services/ws-patient-direct.service';
import { first, map, Observable } from 'rxjs';
import { CmsService, CurrencyService, EventService } from '@spartacus/core';
import { ActiveCartFacade, CartAddEntrySuccessEvent } from '@spartacus/cart/base/root';
import { WsPrivateLabelService } from '../../../ws-services/ws-private-label.service';
import { BsModalRef, BsModalService } from 'ngx-bootstrap/modal';
import { WsProductDetailsPopupComponent } from '../../ws-pdp/ws-product-details-popup/ws-product-details-popup.component';

@Component({
  selector: 'ws-private-label',
  templateUrl: './ws-private-label.component.html',
  styleUrl: './ws-private-label.component.scss',
})
export class WsPrivateLabelComponent implements OnInit,AfterViewInit {
  privateLabelProducts: any;
  sortOptions: any;
  productsList: any;
  filterfacets: any;
  modalProduct: any;
  cartItems: any[] = [];
  SearchText: string = '';
  currency$: Observable<string>;
  pagination: any;
  bsModalRef?: BsModalRef;
  heading$: Observable<any> = this.cmsService.getComponentData('privateLabelParagraphComponent');
  cartIsLoading$: Observable<boolean> = this.cartService
    .isStable()
    .pipe(map((loaded) => !loaded));

  payLoad = {
    currentPage: 0,
    pageSize: 200,
    status: '',
    query: ':bestsellers',
    sort: '',
    filters: ''
  };

  alerts: any[] = [];

  constructor(
    private privateLabelService: WsPrivateLabelService,
    private destroyRef: DestroyRef,
    private cdRef: ChangeDetectorRef,
    private patientDirectService: PatientDirectService,
    private currencyService: CurrencyService,
    private cmsService: CmsService,
    private cartService: ActiveCartFacade,
    private bsModalService: BsModalService,
    private eventService: EventService
  ) {
    this.getPageInfo();
  }

  ngOnInit(): void {
    this.currency$ = this.currencyService.getActive();
  }

  ngAfterViewInit(): void {
    if(document.getElementsByTagName('title') && !document.getElementsByTagName('title')[0].innerText) document.getElementsByTagName('title')[0].innerText = 'Private Label';
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

  showSearchResults(val: string) {
    this.SearchText = val;
    this.payLoad.query = this.SearchText;
    this.getPageInfo();
  }

  sortList(sort: string): void {
    this.payLoad.query = this.payLoad.query?.replace(this.payLoad.sort || 'bestsellers', sort);
    this.payLoad.sort = sort;
    this.getPageInfo();
  }

  setFilter(option: any) {
    const query: string = option?.query?.query?.value || '';
    this.SearchText = '';
    this.payLoad.query = query;
    this.getPageInfo();
  }

  getPaginationRange(
    currentPage: number,
    itemsPerPage: number,
    totalItems: number
  ) {
    const start = currentPage * itemsPerPage + 1;
    const end = Math.min((currentPage + 1) * itemsPerPage, totalItems);
    return { start, end };
  }

  getPageInfo() {
    this.privateLabelService
      .getAllProducts(this.payLoad)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe((res: any) => {
        this.privateLabelProducts = this.productsList = res.products;
        this.sortOptions = res.sorts;
        this.filterfacets = res.facets
        this.pagination = res.pagination;
        this.cdRef.detectChanges();
      });
  }

  resetFilter() {
    this.payLoad.query = ':bestsellers';
    this.payLoad.sort = '';
    this.SearchText = '';
    this.getPageInfo();
  }

  pageChanged(event: any) {
    this.payLoad.currentPage = event;
    this.getPageInfo();
    window.scrollTo({
      top: 0,
      behavior: 'smooth',
    });
  }

  addProductToCart(item: any) {
    this.watchAddEntrySuccessEvent();
    let index = this.cartItems.findIndex((entry: any) => entry === item.code);
    if (index !== -1) this.cartService.updateEntry(index, item.quantity);
    else this.cartService.addEntry(item.code, item.quantity);
  }

  protected watchAddEntrySuccessEvent(): void {
    this.eventService
      .get(CartAddEntrySuccessEvent)
      .pipe(first(), takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: () => {
          this.addCart();
        }
      });
  }

  addCart(): void {
    this.alerts.push({
      type: 'info',
      msg: `Product added to cart`,
      timeout: 3000,
    });
    this.cdRef.markForCheck();
  }

  onClosed(dismissedAlert: any): void {
    this.alerts = this.alerts.filter((alert) => alert !== dismissedAlert);
  }

  setQuantity(product: any, ev: any) {
    if (ev.target.value !== '') product.quantity = ev.target.value;
    else product.quantity = 1;
  }
  
  addQuantity(item: any) {
    item.quantity = Number(item.quantity) || 1;
    item.quantity += 1;
  }
  removeQuantity(item: any) {
    item.quantity = Number(item.quantity) || 1;
    if (item.quantity > 1) item.quantity -= 1;
  }

  exportCSV() {
    const csvPrivateLabelProducts = this.privateLabelProducts?.map((product: any) => {
      return {
        'Sku': product?.code?.replace(/^0+/, '') || '',
        'Private Label Name': product?.productDescription || '',
        'Blue Label Name': product?.name || '',
        'Is Wholescripts Kit': product?.isKit ? 'TRUE' : 'FALSE',
        'Pre-labeled Inventory': product?.preLabeledUnit || '',
        'Case Qty': product?.caseQty ? Number(product.caseQty) : '',
        'Case Price': product?.casePrice?.value || '',
        'Unit Price': product?.wholeSalePrice?.value || '',
        'MSRP Unit Price': product?.retailPrice?.value || ''
      }
    }) || [];
    this.patientDirectService.exportCSV(
      csvPrivateLabelProducts,
      'PrivateLabelProducts'
    );
  }

  getAddToCartDisableFlag(selectedProduct: any): boolean {
    if(selectedProduct?.forceOutOfStock) {
      return true;
    }
    return false;
  }

}
