import {
  ChangeDetectorRef,
  Component,
  DestroyRef,
  ElementRef,
  inject,
  OnInit,
  signal,
  TemplateRef,
  ViewChild,
  ViewContainerRef,
} from '@angular/core';
import { WsOverlay } from '../../../ws-services/ws-overlay.service';
import { TemplatePortal } from '@angular/cdk/portal';
import { WsOrderHistoryService } from '../../../ws-services/order-history/ws-order-history.service';
import { ActivatedRoute, Router } from '@angular/router';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { CurrencyService, Config } from '@spartacus/core';
import { BehaviorSubject, combineLatest, map, Observable, take } from 'rxjs';
import {
  BASE_PATH,
  MEDPAX_CARTON_NEW,
  MEDPAX_HEADER_NEW,
} from '../../../ws-constants/ws-api-endpoint.constant';
import { getUserData } from '../../../ws-services/state-services/currentuser.service';
import { OrderItemsRemovalModalComponent } from '../order-items-removal-modal/order-items-removal-modal.component';
import { BsModalRef, BsModalService } from 'ngx-bootstrap/modal';
import { ActiveCartFacade } from '@spartacus/cart/base/root';
import { WsCartResponseModifier } from '../../../ws-services/ws-checkout/ws-cart-response-modifier.service';

declare var klaviyo: any;
@Component({
  selector: 'ws-ws-order-details',
  templateUrl: './ws-order-details.component.html',
  styleUrl: './ws-order-details.component.scss',
})
export class WsOrderDetailsComponent implements OnInit {
  images = [
    { name: 'American Express', src: BASE_PATH('amex') },
    { name: 'Visa', src: BASE_PATH('visa') },
    { name: 'Mc', src: BASE_PATH('mastercard') },
    { name: 'Discover', src: BASE_PATH('discover') },
  ];

  public currentUser$ = inject(getUserData);

  orderHeader: any[] = [];
  shippingAddres: any[] = [];
  ccDetails: any[] = [];
  invalidDeliveryItemsFromShipped: any[] = [];
  toastMessage :any;
  baseURL: string;

  private router = inject(Router);
  notShippedMedPax$ = new BehaviorSubject<any[]>([]);
  cancelledMedPax$ = new BehaviorSubject<any[]>([]);
  

  shippedBlocks$ = new BehaviorSubject<any[]>([]);
  canceledItems$ = new BehaviorSubject<any[]>([]);
  notShippedItems$ = new BehaviorSubject<any[]>([]);
  isPrivateLabelProduct$ = new BehaviorSubject<boolean>(false); 
  
  objectKeys = Object.keys;
  cancelItemPortal: any;
  cancelEntireOrderPortal: any;
  salesOrder: string = '';
  currency$: Observable<string>;
  loading = true;
  pdfLoading = false;
  private overlayService = inject(WsOverlay);
  private vcr = inject(ViewContainerRef);
  private destroyRef = inject(DestroyRef);
  private activeCartService = inject(ActiveCartFacade);
  private responseModifier = inject(WsCartResponseModifier);
  @ViewChild('cancelItem') cancelItem: TemplateRef<any>;
  @ViewChild('cancelEntireOrder') cancelEntireOrder: TemplateRef<any>;
  @ViewChild('firstFocusElement') firstFocusElement: ElementRef;


  selectedOrderItem = signal<any>({});
  selectedMedPaxOrderItem = signal<any>({});
  selectedItemIsMedpax = signal<boolean>(false);
  actualApiResponse = signal<any>({});
  hideCancelButton = false; 
  hasDiscounts: boolean = false;
  promoCodeEntries: string[] = []; 
  bsModalRef?: BsModalRef

  constructor(
    private route: ActivatedRoute,
    private orderHistoryService: WsOrderHistoryService,
    private cd: ChangeDetectorRef,
    protected config: Config,
    protected currencyService: CurrencyService,
    private dialog: BsModalService,
  ) {}

  ngOnInit(): void {
    if (localStorage.getItem('navigatedFromPatientProfile')) {
      this.hideCancelButton = true;
      localStorage.removeItem('navigatedFromPatientProfile');
    }
    this.salesOrder = this.route.snapshot.paramMap.get('salesOrder');

    if (this.salesOrder) {
      this.fetchOrderDetails(this.salesOrder);
    }
    this.currency$ = this.currencyService.getActive();
    this.baseURL = this.config.backend?.occ?.baseUrl
    ? this.config.backend?.occ?.baseUrl
    : '';
  }

  ngAfterViewInit() {
    this.cancelItemPortal = new TemplatePortal(this.cancelItem, this.vcr);
    this.cancelEntireOrderPortal = new TemplatePortal(
      this.cancelEntireOrder,
      this.vcr
    );
    setTimeout(() => {
      this.firstFocusElement?.nativeElement?.focus();
    }, 0);
    if(document.getElementsByTagName('title') && !document.getElementsByTagName('title')[0].innerText) document.getElementsByTagName('title')[0].innerText = 'Order Details';
  }

  fetchOrderDetails(salesOrder: string) {
    this.loading = true;
    this.orderHistoryService
      .getOrderDetails(salesOrder)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: (response: any) => {
          this.actualApiResponse.set(response);
          if (response) {
            this.orderHeader = response?.orderHeader?.items;
            // check if headerDiscount or itemDiscount is available
            this.hasDiscounts =
            (response?.headerDiscount?.items?.length ?? 0) > 0 ||
            (response?.itemDiscount?.items?.length ?? 0) > 0;
            this.promoCodeEntries = response?.itemDiscount?.items?.map(
              (discountItem: any) => discountItem.itemNumber
            ) ?? [];
            this.findShippinggAddress(response);
            this.getMatchingCreditCardItems(response);
            this.filterCanceledItems(response);
            this.filterShippedNotShippedItems(response);
          }
          this.loading = false;
          this.cd.detectChanges();
        },
        error: (err) => {
          console.error('Eroor Fetching Order Details', err)
          this.loading = false;
          this.cd.detectChanges();
        },
        complete: ()=> {
          this.loading = false;
          this.cd.detectChanges();
        }
      }
    )
  }

  getProductImageUrl(item: any): string | undefined {
    return item?.image?.url 
    ? `${this.baseURL}${item?.image?.url}` 
    : item?.no_image?.url
    ? `${this.baseURL}${item?.no_image?.url}`
    : item?.isPrivateLabel
    ? `assets/images/recomendation/default-image.png` 
    : `assets/images/recomendation/default-image.png`;
  }

  getMedPaxCartonImageUrl(item: any): string | undefined {
    return item?.image?.url 
    ? `${this.baseURL}${item?.image?.url}` 
    : `assets/images/private-label/pl-generic-icon.png`;
  }


  getApiStoreBadge(orderOrigin: string): string {
    return this.orderHistoryService.getApiBadge(orderOrigin);
  }

  getCardImage(ccType: string): string | undefined {
    if (!ccType) return undefined;
    return this.images.find(img => img.name.toLowerCase() === ccType.toLowerCase())?.src;
  }

  extractLastFourDigits(response: string): string | null {
    const match = response.match(/(\d{4})(?=-[A-Z0-9]+$)/);
    return match ? match[1] : null;
  }

  getMatchingCreditCardItems(response: any): void {
    const totalStr = response?.orderHeader?.items?.total;
    const [totalWhole, totalDecimal] = totalStr?.split('.') || [];
    const totalFirstDecimal = totalDecimal?.[0];
  
    const ccDetail = response?.orderCcard?.items?.filter((item: any) => {
      const authAmountStr = item?.authAmount;
      const [authWhole, authDecimal] = authAmountStr?.split('.') || [];
      const authFirstDecimal = authDecimal?.[0];
  
      return totalWhole === authWhole && totalFirstDecimal === authFirstDecimal;
    });
  
    this.ccDetails = ccDetail || [];
  }
  
  downloadPdf(invoiceNumber: string) {
    this.pdfLoading = true
    this.orderHistoryService.pdf(invoiceNumber).subscribe({
      next: (data: any) => {
        const byteString = window.atob(data.o_XSTRING);
        const arrayBuffer = new ArrayBuffer(byteString.length);
        const int8Array = new Uint8Array(arrayBuffer);
        for (let i = 0; i < byteString.length; i++) {
          int8Array[i] = byteString.charCodeAt(i);
        }
        const blob = new Blob([int8Array], { type: 'application/pdf' });
        const url = URL.createObjectURL(blob);
        window.open(url, '_blank');
        this.pdfLoading = false;
        this.cd.detectChanges();
      },
      error: (error) => {
        this.pdfLoading = false;
        console.log('Error downloading PDF:', error);
        this.cd.detectChanges();
      },
      complete: () => {
        this.pdfLoading = false;
        this.cd.detectChanges();
      },
    });
  }

  findShippinggAddress(response: any) {
    const shipTo = response?.orderHeader?.items?.shipTo;
    if (shipTo && response?.orderAddress?.items) {
      const matchedAddress = response.orderAddress.items.find(
        (address: any) => address.partner === shipTo
      );
      if (matchedAddress) {
        this.shippingAddres = matchedAddress;
      }
    }
  }

  get canCancelEntireOrder(): Observable<boolean> {
    return this.isPrivateLabelProduct$.pipe(
      map((isPrivateLabelProduct: any) => !isPrivateLabelProduct)
    );
  }

  filterCanceledItems(response: any) {
    const normalizeArray = (data: any): any[] =>
      Array.isArray(data) ? data : data ? [data] : [];
    const orderItemArray = normalizeArray(response.orderItem?.items);
    const cancelledItems = orderItemArray.filter(
      (item: any) => item.canceled === 'C'
    );
    const filteredCancelledItems = cancelledItems.filter(
      (item) =>   !MEDPAX_CARTON_NEW.includes(item.material)
    );
    const medPaxHeaders = filteredCancelledItems.filter(
      (item) =>
        MEDPAX_HEADER_NEW.includes(item.material) && item.salesItemHigh === '000000'
    );
  
    let cancelledMedpax: any[] = [];
    medPaxHeaders.map((header: any) => {
      const childItems = filteredCancelledItems.filter(
        (item: any) => item?.salesItemHigh === header?.salesItem
      );
      const dynamicEntries = childItems.reduce((acc: any[], childItem) => {
        const key = childItem.description;
        if (!acc[key]) {
          acc[key] = {
            entries: filteredCancelledItems.filter(
              (item) => item.salesItemHigh === childItem.salesItem
            ),
          };
        }
        return acc;
      }, {});
      let overallTotalSum = 0;
      Object.keys(dynamicEntries).forEach((key) => {
        dynamicEntries[key].totalSum = dynamicEntries[key].entries.reduce(
          (sum: number, entry: any) => {
            return sum + parseFloat(entry.extPrice);
          },
          0
        );
        overallTotalSum += dynamicEntries[key].totalSum;
      });

      const allPillsEntries = Object.values(dynamicEntries)
      .flatMap((entry: any) => entry.entries);
      const inStockCount = allPillsEntries.filter(item => !this.isOutOfStock(item)).length;
      const isOutOfStock = inStockCount < 3;

      cancelledMedpax.push({
        medpPaxDetails: {
          ...header,
          isOutOfStock
        },
        children: childItems,
        packetDetails: dynamicEntries,
        overallTotalSum,
      });
    });
    const medPaxSalesItems = cancelledMedpax
      .map((medPax) => [
        medPax.medpPaxDetails.salesItem,
        ...medPax.children.map((child: any) => child.salesItem),
      ])
      .flat();
    const medPaxPacketDetails = cancelledMedpax
      .map((t) =>
        Object.keys(t.packetDetails)
          .map((key) => t.packetDetails[key].entries)
          .flat()
      )
      .flat();
    const remainingCancelledItems = filteredCancelledItems.filter(
      (item) =>
        !medPaxSalesItems.includes(item.salesItem) &&
        !medPaxPacketDetails.some(
          (packet) => packet.salesItem === item.salesItem
        )
    );
    
    const remainingCancelledItemsWithStockFlag = remainingCancelledItems.map(item => {
      return {
        ...item,
        isForcedOutOfStock: this.isForcedOutOfStock(item),
      };
    });
  
    this.canceledItems$.next(remainingCancelledItemsWithStockFlag);
    this.cancelledMedPax$.next(cancelledMedpax);
  }

  filterShippedNotShippedItems(response: any): void {
    const normalizeArray = (data: any): any[] =>
      Array.isArray(data) ? data : data ? [data] : [];
    const orderHdrDlvArray = normalizeArray(response.orderHdrDlv?.items);
    const orderItemDlvArray = normalizeArray(response.orderItmDlv?.items);
    const orderItemArray = normalizeArray(response.orderItem?.items);
    const orderHdrBilArray = normalizeArray(response?.orderHdrBil?.items);
    const orderItmBilArray = normalizeArray(response?.orderItmBil?.items)
    this.processShippedItems(
      orderHdrDlvArray,
      orderItemDlvArray,
      orderItemArray,
      orderHdrBilArray,
      orderItmBilArray
    );
    this.processNotShippedItems(orderItemArray, orderItemDlvArray);
    this.cd.detectChanges();
  }

  showCreditMemoButton(
    orderHdrBil: any[],
    orderItmBil: any[],
    itmNumbersInBlock: string[]
  ): string[] {
    const relevantBilNumbers = orderHdrBil
    .filter(header => header.doctype === 'O')
    .map(header => header.bilNumber);

  if (!relevantBilNumbers.length) return [];

  const associatedItmNumbers = orderItmBil
    .filter(itemBil => relevantBilNumbers.includes(itemBil.bilNumber))
    .map(itemBil => itemBil.itmNumber);

  return itmNumbersInBlock
    .filter(itm => associatedItmNumbers.includes(itm))
    .map(itm => {
      const itemBil = orderItmBil.find(item => item.itmNumber === itm);
      return itemBil ? itemBil.bilNumber : null;
    })
    .filter(bilNumber => bilNumber !== null);
  }

  processShippedItems(
    orderHdrDlvArray: any[],
    orderItemDlvArray: any[],
    orderItemArray: any[],
    orderHdrBilArray: any[],
    orderItmBilArray: any[]
  ) {
    let shippedBlocks: any[] = [];
    
    const invalidDeliveries = orderHdrDlvArray.filter(
      (delivery) => delivery.shipDate === "0000-00-00"
    );
    this.invalidDeliveryItemsFromShipped = []; 
    invalidDeliveries.forEach((invalidDelivery) => {
      const { delNumber } = invalidDelivery;
      const relatedItems = orderItemDlvArray.filter(
        (item) => item.delNumber === delNumber
      );
      relatedItems.forEach((item) => {
        const matchedOrderItem = orderItemArray.find(
          (orderItem) => orderItem.salesItem === item.itmNumber
        );
        if (matchedOrderItem) {
          const total = item.refQty * matchedOrderItem.unitPrice;
          this.invalidDeliveryItemsFromShipped.push({
            salesItem: matchedOrderItem.salesItem,
            description: matchedOrderItem.description,
            quantity: item.refQty,
            unitPrice: matchedOrderItem.unitPrice,
            total,
            image: matchedOrderItem.image,
            productUrl: matchedOrderItem.productUrl,
          });
        }
      });
    });
    const validDeliveries = orderHdrDlvArray.filter(
      (delivery) => delivery.shipDate !== "0000-00-00"
    );
    validDeliveries.forEach((delivery) => {
      const { delNumber, shipDate, bilNumber, trackingNo } = delivery;
      const relatedItems = orderItemDlvArray.filter(
        (item) => item.delNumber === delNumber
      );
      const itmNumbers = relatedItems.map((item) => item?.itmNumber);

      const relevantBilNumbers = this.showCreditMemoButton(
        orderHdrBilArray,
        orderItmBilArray,
        itmNumbers
      );

      const hasCreditMemo = relevantBilNumbers.length > 0;
      
      let creditMemoBilNumber = null;
      if (relevantBilNumbers.length > 0) {
        creditMemoBilNumber = relevantBilNumbers; 
      }

      const filterShippedItems = orderItemArray.filter(
        (orderItem) => !MEDPAX_CARTON_NEW.includes(orderItem.material)
      );
      const medPaxHeaders = filterShippedItems.filter(
        (orderItem) =>
          MEDPAX_HEADER_NEW.includes(orderItem.material) &&
          orderItem.salesItemHigh === '000000'
      );
      const hasMatchingSalesItem = medPaxHeaders.some((header: any) =>
        itmNumbers.includes(header?.salesItem)
      );
  
      let shippedMedPax: any[] = [];
      let shippedItems: any[] = [];
      if (hasMatchingSalesItem) {
        medPaxHeaders.forEach((header: any) => {
          const childItems = filterShippedItems.filter(
            (item: any) => item?.salesItemHigh === header?.salesItem
          );
          const dynamicEntries = childItems.reduce((acc: any, childItem) => {
            const key = childItem.description;
            if (!acc[key]) {
              acc[key] = {
                entries: filterShippedItems.filter(
                  (item) => item.salesItemHigh === childItem.salesItem
                ),
              };
            }
            return acc;
          }, {});
  
          let overallTotalSum = 0;
          Object.keys(dynamicEntries).forEach((key) => {
            dynamicEntries[key].totalSum = dynamicEntries[key].entries.reduce(
              (sum: number, entry: any) => {
                return sum + parseFloat(entry.extPrice);
              },
              0
            );
            overallTotalSum += dynamicEntries[key].totalSum;
          });

          const allPillsEntries = Object.values(dynamicEntries)
          .flatMap((entry: any) => entry.entries);
          const inStockCount = allPillsEntries.filter(item => !this.isOutOfStock(item)).length;
          const isOutOfStock = inStockCount < 3;
  
          shippedMedPax.push({
            medpPaxDetails: {
              ...header,
              isOutOfStock 
            },
            children: childItems,
            packetDetails: dynamicEntries,
            overallTotalSum,
          });
        });
      }
      const medPaxSalesItems = shippedMedPax
        .map((medPax) => [
          medPax.medpPaxDetails.salesItem,
          ...medPax.children.map((child: any) => child.salesItem),
        ])
        .flat();
      const medPaxPacketDetails = shippedMedPax
        .map((t) =>
          Object.keys(t.packetDetails)
            .map((key) => t.packetDetails[key].entries)
            .flat()
        )
        .flat();
      const remainingItems = filterShippedItems.filter(
        (item) =>
          !medPaxSalesItems.includes(item.salesItem) &&
          !medPaxPacketDetails.some(
            (packet) => packet.salesItem === item.salesItem
          )
      );
      relatedItems.forEach((item) => {
        const matchedOrderItem = remainingItems.find(
          (orderItem) => orderItem?.salesItem === item?.itmNumber
        );
        if (matchedOrderItem) {
          const total = item.refQty * matchedOrderItem.unitPrice;
          shippedItems.push({
            salesItem: matchedOrderItem?.salesItem,
            description: matchedOrderItem.description,
            quantity: item.refQty,
            unitPrice: matchedOrderItem.unitPrice,
            total,
            image: matchedOrderItem.image,
            productUrl: matchedOrderItem.productUrl,
            isOutOfStock: this.isOutOfStock(matchedOrderItem),
            isForcedOutfStock: this.isForcedOutOfStock(matchedOrderItem),      
            material: matchedOrderItem.material
          });
        }
      });
      
      shippedBlocks.push({
        shipDate,
        bilNumber,
        trackingNo,
        shippedMedPax,
        shippedItems,
        hasCreditMemo,
        creditMemoBilNumber
      });
    });
    this.shippedBlocks$.next(shippedBlocks);
    this.cd.detectChanges();
  }

  processNotShippedItems(orderItemArray: any[], orderItemDlvArray: any[]) {
    // Check if all items are fully shipped
    const allShipped = orderItemArray.every((orderItem) =>
      orderItemDlvArray.some((dlvItem) => dlvItem.itmNumber === orderItem.salesItem && Number(dlvItem.refQty) >= Number(orderItem.quantity))
    );
  
    if (allShipped) {
      this.notShippedMedPax$.next([]);
      this.notShippedItems$.next([]);
      this.isPrivateLabelProduct$.next(false);
      this.cd.detectChanges();
      return;
    }
    const filteredOrderItems = orderItemArray.filter(
      (item) => item.canceled !== 'C' && !MEDPAX_CARTON_NEW.includes(item.material)
    );  
    const notShipped = filteredOrderItems.filter(
      (item) => {
      const remainingUnshipped = orderItemDlvArray.reduce((total, dlvItem) => {
        if(item.salesItem == dlvItem.itmNumber) {
            const qty = Number(dlvItem?.refQty);
            return total + qty;
          } else return total + 0;
      }, 0);
        return Number(item.quantity) >= Number(remainingUnshipped)
      }
    );
    const medPaxHeaders = notShipped.filter(
      (item) =>
        MEDPAX_HEADER_NEW.includes(item.material) && item.salesItemHigh === '000000'
    );
    let notShippedMedPax: any[] = [];
    medPaxHeaders.map((header: any) => {
      const childItems = notShipped.filter(
        (item: any) => item?.salesItemHigh === header?.salesItem
      );
      const dynamicEntries = childItems.reduce((acc: any[], childItem) => {
        const key = childItem.description;
        if (!acc[key]) {
          acc[key] = {
            entries: notShipped.filter(
              (item) => item.salesItemHigh === childItem.salesItem
            ),
          };
        }
        return acc;
      }, {});
      let overallTotalSum = 0;
      Object.keys(dynamicEntries).forEach((key) => {
        dynamicEntries[key].totalSum = dynamicEntries[key].entries.reduce(
          (sum: number, entry: any) => {
            return sum + parseFloat(entry.extPrice);
          },
          0
        );
        overallTotalSum += dynamicEntries[key].totalSum;
      });
  
      const allPillsEntries = Object.values(dynamicEntries)
      .flatMap((entry: any) => entry.entries);
      const inStockCount = allPillsEntries.filter(item => !this.isOutOfStock(item)).length;
      const isOutOfStock = inStockCount < 3;
  
      notShippedMedPax.push({
        medpPaxDetails: {
          ...header,
          isOutOfStock 
        },
        children: childItems,
        packetDetails: dynamicEntries,
        overallTotalSum,
      });
    });
    this.notShippedMedPax$.next(notShippedMedPax);
    const medPaxSalesItems = notShippedMedPax
      .map((medPax) => [
        medPax.medpPaxDetails.salesItem,
        ...medPax.children.map((child: any) => child.salesItem),
      ])
      .flat();  
    const medPaxPacketDetails = notShippedMedPax
      .map((t) =>
        Object.keys(t.packetDetails)
          .map((key) => t.packetDetails[key].entries)
          .flat()
      )
      .flat();
    const remainingNotShippedItems = notShipped.filter(
      (item) =>
        !medPaxSalesItems.includes(item.salesItem) &&
        !medPaxPacketDetails.some(
          (packet) => packet.salesItem === item.salesItem
        )
    );
    let notShippedItems: any[] = [];
    remainingNotShippedItems.forEach((item: any) => {
      const { salesItem, description, quantity, unitPrice, custMaterial, image, productUrl, material, isPrivateLabel } = item;
      const relatedDeliveryQuantity = orderItemDlvArray.reduce((total, dlvItem) => {
        if(dlvItem?.itmNumber === salesItem) {
          const qty = Number(dlvItem?.refQty || 0);
          return total + qty;
        } else return total + 0
     }, 0)
      const canCancel = !custMaterial;
      const isOutOfStock = this.isOutOfStock(item);
      const isForcedOutOfStock = this.isForcedOutOfStock(item);
      if (relatedDeliveryQuantity > 0) {
        const remainingQty = Number(quantity) - Number(relatedDeliveryQuantity);
        if (remainingQty > 0) {
          notShippedItems.push({
            salesItem,
            description,
            quantity: remainingQty,
            image,
            productUrl,
            unitPrice,
            total: remainingQty * unitPrice,
            refItm: salesItem,
            canCancel,
            isOutOfStock,
            isForcedOutOfStock,
            material,
            isPrivateLabel
          });
        }
      } else {
        notShippedItems.push({
          salesItem,
          description,
          quantity,
          image,
          productUrl,
          unitPrice,
          total: quantity * unitPrice,
          refItm: salesItem,
          canCancel,
          isOutOfStock,
          isForcedOutOfStock,
          isPrivateLabel
        });
      }
    });
   // items where shipDate starts from 0000 displaying in not shipped
    if (this.invalidDeliveryItemsFromShipped?.length) {
      this.invalidDeliveryItemsFromShipped.forEach((item) => {
        notShippedItems.push({
          ...item,
          isOutOfStock: this.isOutOfStock(item),
        });
      });
    }
    this.notShippedItems$.next(notShippedItems);
    const isPrivateLabelProduct = remainingNotShippedItems.some((item) => item.custMaterial);
    this.isPrivateLabelProduct$.next(isPrivateLabelProduct);
    this.cd.detectChanges();
  }

  opencancelEntireOrderPopUp() {
    this.overlayService.openModal(this.cancelEntireOrderPortal, '40%');
  }

  openCancelItemPopup(item:any,isMedPax=false) {
    if(isMedPax){
      this.selectedMedPaxOrderItem.set(item);
      this.selectedItemIsMedpax.set(true);
      this.updateCurrentItemForCancelItem(item?.medpPaxDetails);
    }
    else{
      this.updateCurrentItemForCancelItem(item);
      this.selectedItemIsMedpax.set(false);
    }
    console.debug(item);
    this.overlayService.openModal(this.cancelItemPortal, '40%');
  }
  navigateToOrders() {
    this.router.navigate(['/order/history/all-orders']);
  }

  closePopup() {
    this.overlayService.closeTopModal();
  }

  updateCurrentItemForCancelItem(currentItem:any){
   this.selectedOrderItem.set(currentItem); 
  }

  cancelEntireOrderApiCallPayLoadCreator(){
   this.cancelOrderApiCall('');
  }

  cancelSingleItem(itemId: string): void {
    const { medpPaxDetails } = this.selectedMedPaxOrderItem();
    const orderItems = this.selectedItemIsMedpax()
      ? medpPaxDetails.salesItem
      : itemId;
    this.cancelOrderApiCall(orderItems);
  }
  
  cancelOrderApiCall(orderItems:string){
    this.orderHistoryService.cancelOrder(this.salesOrder,orderItems).pipe(takeUntilDestroyed(this.destroyRef)).subscribe({
    next:()=>{ this.toastMessage = 'Item cancelled from order'; this.resetToastMessage();this.fetchOrderDetails(this.salesOrder);this.closePopup(); this.cd.detectChanges();}
    })
  }

  resetToastMessage() {
    setTimeout(() => {
      this.toastMessage = '';
      this.cd.detectChanges();
    }, 2000);
  }

  private isOutOfStock(item: any): boolean {
    return item?.stock?.stockLevelStatus === 'outOfStock' 
  }

  private isForcedOutOfStock(item: any): boolean {
    return item?.stock?.forceOutOfStock;
  }

  private removeOutOfStockItems(item: any): boolean {
    return item?.stock?.stockLevelStatus === 'outOfStock' || item?.stock?.forceOutOfStock;
  }

  private outOfStockItems(normalItems$: Observable<any[]>): Observable<any[]> {
    return normalItems$.pipe(
      take(1),
      map((items: any[]) => {
        return items.filter(item => item.isForcedOutOfStock);
      })
    );
  }

  private outOfStockIShippedtems(shippedItems$: Observable<any[]>): Observable<any[]> {
    return shippedItems$.pipe(
      take(1),
      map((response: any[]) => {
        const allShippedItems = response.flatMap(item => item.shippedItems); 
        return allShippedItems.filter(item => item.isForcedOutOfStock); 
      })
    );
  }
  
  private outOfStockMedpaxtems(source$: Observable<any[]>): Observable<any[]> {
    return source$.pipe(
      take(1),
      map(medpaxList => {
        const outOfStockItems: any[] = [];
        medpaxList.forEach((medpax: any) => {
          const { medpPaxDetails, children, packetDetails } = medpax;
  
          if (this.removeOutOfStockItems(medpPaxDetails)) {
            outOfStockItems.push(medpPaxDetails);
          }
  
          if (packetDetails) {
            Object.values(packetDetails).forEach((packet: any) => {
              packet.entries.forEach((entry: any) => {
                if (this.removeOutOfStockItems(entry)) {
                  outOfStockItems.push(entry);
                }
              });
            });
          }
        });
  
        return outOfStockItems;
      })
    );
  }

  private outOfStockMedpaxtemsShippedBlocks(source$: Observable<any[]>): Observable<any[]> {
    return source$.pipe(
      take(1),
      map(shippedBlocks => {
        const outOfStockItems: any[] = [];
    
        shippedBlocks.forEach((block: any) => {
          const { shippedMedPax } = block;
          if (shippedMedPax) {
            shippedMedPax.forEach((medPax: any) => {
              if (this.removeOutOfStockItems(medPax.medpPaxDetails)) {
                outOfStockItems.push(medPax.medpPaxDetails);
              }
  
              if (medPax.packetDetails) {
                Object.values(medPax.packetDetails).forEach((packet: any) => {
                  packet.entries.forEach((entry: any) => {
                    if (this.removeOutOfStockItems(entry)) {
                      outOfStockItems.push(entry);
                    }
                  });
                });
              }
            });
          }
        });
    
        return outOfStockItems;
      })
    );
  }

  addOrderToCart() {
    combineLatest([
      this.outOfStockMedpaxtems(this.notShippedMedPax$),
      this.outOfStockMedpaxtems(this.cancelledMedPax$),
      this.outOfStockMedpaxtemsShippedBlocks(this.shippedBlocks$),
      this.outOfStockItems(this.canceledItems$),
      this.outOfStockItems(this.notShippedItems$),
      this.outOfStockIShippedtems(this.shippedBlocks$)
    ])
    .pipe(takeUntilDestroyed(this.destroyRef))
    .subscribe(([notShippedMedPax, cancelledMedPax, shippedMedPax, cancelledItems, notShippedItems, shippedItems]) => {
      const allOutOfStockItems = [
        ...notShippedMedPax || [],
        ...cancelledMedPax || [],
        ...shippedMedPax || [],
        ...cancelledItems || [],
        ...notShippedItems || [],
        ...shippedItems || []
      ];
  
      const orderItems = this.actualApiResponse()?.orderItem?.items || [];
  
      const outOfStockItems = orderItems.filter((orderItem: any) => {
        return allOutOfStockItems.some(stockItem => stockItem.salesItem === orderItem.salesItem);
      });
  
      const inStockItems = orderItems.filter((orderItem: any) => {
        return !allOutOfStockItems.some(stockItem => stockItem.salesItem === orderItem.salesItem);
      });
  
      if (outOfStockItems.length > 0) {
        this.openOutOfStockModal(outOfStockItems, inStockItems); 
        return;
      }
  
      // If no out-of-stock items, directly add to cart
      this.addItemsToCart(inStockItems);
    });
  }
  
  addItemsToCart(items: any[]) {
    if (items.length === 0) return;
    this.loading = true;
    const request = {
      orderItem: {
        ...this.actualApiResponse()?.orderItem,
        items: items
      }
    };
  
    this.orderHistoryService.addProductToCart(request)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe(res => {
        if (res) {
          this.toastMessage = 'Product Added to Cart';
          this.resetToastMessage();
          this.loading = false;
          this.cd.detectChanges();
          this.klaviyoRequest();
          this.router.navigate(['cart']);
        }
      }, () => {
        this.loading = false;
        this.cd.detectChanges();
      });
  }

  private openOutOfStockModal(outOfStockItems: any[], inStockItems: any[]) {
    this.bsModalRef = this.dialog.show(OrderItemsRemovalModalComponent, {
      initialState: {
        outOfStockItems: outOfStockItems 
      },
      class: 'modal-dialog-centered modal-lg custom-modal-width' 
    });
  
    this.bsModalRef.content.confirmAddToCart
      .pipe(take(1)) 
      .subscribe(() => {
        this.addItemsToCart(inStockItems); 
    });
  }

  klaviyoRequest(){
    this.cancelledMedPax$.subscribe(res => {
      res.forEach(item => {
        this.pushMedpaxAddToCartToKlaviyo(item);
      })
    })
    this.canceledItems$.subscribe(res => {
      res.forEach(item => {
        this.pushAddToCartToKlaviyo(item);
      })
    })
    this.notShippedMedPax$.subscribe(res => {
      res.forEach(item => {
        this.pushMedpaxAddToCartToKlaviyo(item);
      })
    })
    this.notShippedItems$.subscribe(res => {
      res.forEach(item => {
        this.pushAddToCartToKlaviyo(item);
      })
    })
    this.shippedBlocks$.subscribe(res => {
      res.forEach((item: any) => {
        item.shippedItems.forEach((x: any) => {
          this.pushAddToCartToKlaviyo(x);
        })
        item.shippedMedPax.forEach((x: any) => {
          this.pushMedpaxAddToCartToKlaviyo(x);
        })
      })
    })
  }

  getTotalPrice(medpaxItem: any) {
    return Number(Number(medpaxItem?.overallTotalSum).toFixed(2)) * Number(medpaxItem?.medpPaxDetails?.quantity)
  }

  handleKeydown(event: KeyboardEvent, item: any, action: string) {
    if (event.key === 'Enter') {
      event.preventDefault();
      switch (action) {
        case 'cancel-entire-order':
          this.opencancelEntireOrderPopUp();
          break;
        case 'cancel-medpax':
          this.openCancelItemPopup(item, true);
          break;
        case 'cancel-item':
          this.openCancelItemPopup(item);
          break;
        case 'add-to-cart':
          this.addOrderToCart();
          break;
      }
    }
    else if (event.key === ' ') {
      event.preventDefault();
    }
  }

  handleSpecialKeydown(event: KeyboardEvent, action: string, item?: any) {
    if (event.key === 'Enter') {
    switch (action) {
      case 'close-popup':
        this.closePopup();
        break;
      case 'cancel-all-entire-order':
        this.cancelEntireOrderApiCallPayLoadCreator();
        break;
      case 'cancel-single-item':
        this.cancelSingleItem(item);
        break;
      }  
    }
    else if (event.key === ' ') {
      event.preventDefault();
    }
  }
  
  pushAddToCartToKlaviyo(product: any) {
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
            AddedItemProductName: product?.description,
            AddedItemProductID: product?.material,
            AddedItemSKU: product?.material,
            AddedItemCategories: product?.categories ? product?.categories.map(
              (category: any) => category.name
            ) : [],
            AddedItemImageURL: productImageUrl,
            AddedItemURL: product?.productUrl ? `${baseUrl}${product?.productUrl.replace('///', '/').replace('//', '/')}` : '',
            AddedItemPrice: product?.unitPrice,
            AddedItemQuantity: product.quantity,
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

  pushMedpaxAddToCartToKlaviyo(medpax: any) {
    if (typeof klaviyo !== 'undefined' && klaviyo) {
      const baseUrl = window.location.origin;
      let data: any = [];
      Object.keys(medpax?.packetDetails).forEach(keys => {
        data.push(medpax?.packetDetails[keys]?.entries?.map((x: any) => x?.productName))
      });
      data = data.flat();

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
          AddedItemProductName: medpax?.medpPaxDetails?.description,
          AddedItemProductID: '000000000500000030',
          AddedItemSKU: '000000000500000030',
          AddedItemCategories: data,
          AddedItemImageURL: 'https://api.c1lud0zblk-xymogenin1-d1-public.model-t.cc.commerce.ondemand.com/medias/65x65-0008394-medpax-carton.png?context=bWFzdGVyfHdob2xlc2NyaXB0c0ltYWdlc3w1NTcyfGltYWdlL3BuZ3xhR05pTDJobE15ODRPRFE1TXpFMk5qYzFOakUwTHpZMWVEWTFYekF3TURnek9UUmZiV1ZrY0dGNExXTmhjblJ2Ymk1d2JtY3wzNTQwYzVhYTMzYmQwY2ExMGVjNjE4MzE2NzNkNDJhMDU3YWY3NGZiYjg3MmEzZGMyNTdjMTEyZGZmOGNmZDk4',
          AddedItemURL: baseUrl + '/medpax',
          AddedItemPrice: medpax?.overallTotalSum,
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
