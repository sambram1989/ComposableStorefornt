import { ChangeDetectorRef, Component, DestroyRef, inject, Renderer2 } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { ActivatedRoute, Router } from '@angular/router';
import { CmsService, GlobalMessageService, GlobalMessageType } from '@spartacus/core';
import { Observable } from 'rxjs';
import { WsAutoShipsService } from '../../../ws-services/autoships.service';
import { MEDPAX_CARTON_NEW, MEDPAX_HEADER_NEW } from '../../../ws-constants/ws-api-endpoint.constant';
import { DatePipe } from '@angular/common';
import { WsAddressesService } from '../../../ws-services/ws-addresses.service';
import { ActivityLogService } from '../../../ws-services/ws-activityLog.service';

@Component({
  selector: 'ws-autoships-detail-page',
  templateUrl: './ws-autoships-detail-page.component.html',
  styleUrl: './ws-autoships-detail-page.component.scss'
})
export class WsAutoshipsDetailPageComponent {
  private destroyRef = inject(DestroyRef);
  private cmsService = inject(CmsService);
  private activityLogService =  inject(ActivityLogService);
  AutoshipsDetailsHeaderParaComp$: Observable<any> = this.cmsService.getComponentData('AutoshipsDetailsHeaderParaComp');

  loader: boolean = false;
  autoShipDetails: any;
  autoShipsHeaderItems: any[] = [];
  filteredAutoShipProducts: any[] = [];
  orderNumber: any;
  defaultDate: string = '0001-01-01';
  allAddresses: any[] = [];
  autoShipsCC: any;

  constructor(private cd: ChangeDetectorRef, protected renderer: Renderer2,
    private route: ActivatedRoute, private autoshipsService: WsAutoShipsService,
    private router: Router,
    private datePipe: DatePipe,
    private addressesService: WsAddressesService,
    private globalMessageService: GlobalMessageService
  ) {
    this.route.queryParams.subscribe((params: any) => {
      if (params?.id) {
        this.orderNumber = params.id;
        this.getAutoShipDetails(params.id);
        this.getDataForAddresses();
      }
    });
  }

  getAutoShipDetails(orderNumber: any) {
    this.loader = true;
    this.autoShipsHeaderItems = [];
    this.filteredAutoShipProducts = [];
    this.autoshipsService.getAutoShipDetails(orderNumber)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe(data => {
        this.autoShipDetails = data;
        this.loader = false;
        if(!this.autoShipDetails?.autoShips?.items || this.autoShipDetails?.autoShips?.items?.length == 0) {
          this.router.navigate(['/autoships-list'])
        } else {
          this.processMedpaxDetails();
          this.processAutoShipDetails();
        }
        this.cd.detectChanges();
      }, () => {
        this.loader = false;
      })
  }

  getDataForAddresses() {
    this.addressesService
      .getListOfCommerceAddresses()
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe((data: any) => {
        this.allAddresses = data?.addresses || [];
        this.cd.detectChanges();
      })
  }

  processMedpaxDetails() {
    const autoShipsWithoutCartons = this.autoShipDetails?.autoShips?.items?.filter((item: any) => {
      return !MEDPAX_CARTON_NEW.includes(item.material);
    });
    const medPaxHeaders = autoShipsWithoutCartons?.filter(
      (item: { material: string; hgLvItem: string; }) =>
        MEDPAX_HEADER_NEW.includes(item.material) && item.hgLvItem === '000000'
    );
    let autoShipsMedpax: any[] = [];
    medPaxHeaders.map((header: any) => {
      const childItems = autoShipsWithoutCartons.filter(
        (item: any) => item?.hgLvItem === header?.itmNumber && item?.orderNumber === header?.orderNumber
      );
      const dynamicEntries = childItems.reduce((acc: any[], childItem: any) => {
        const key = childItem.matDesc;
        if (!acc[key]) {
          acc[key] = {
            entries: autoShipsWithoutCartons.filter(
              (item: any) => item.hgLvItem === childItem.itmNumber && item?.orderNumber === childItem?.orderNumber
            ),
          };
        }
        return acc;
      }, {});
      let overallTotalSum = 0;
      Object.keys(dynamicEntries).forEach((key) => {
        dynamicEntries[key].totalSum = dynamicEntries[key].entries.reduce(
          (sum: number, entry: any) => {
            return sum + (Number(entry.price) * Number(entry.reqQty));
          },
          0
        );
        overallTotalSum += dynamicEntries[key].totalSum;
      });
      autoShipsMedpax.push({
        ...header,
        children: childItems,
        packetDetails: dynamicEntries,
        isMedPax: true,
        overallTotalSum,
      });
    });

    const medPaxSalesItems = autoShipsMedpax
      .map((medPax) => [
        medPax.itmNumber,
        ...medPax.children.map((child: any) => child.itmNumber),
      ])
      .flat();
    const medPaxPacketDetails = autoShipsMedpax
      .map((t) =>
        Object.keys(t.packetDetails)
          .map((key) => t.packetDetails[key].entries)
          .flat()
      )
      .flat();
    // Filter out remaining items (not MEDPAX related)
    const remainingProducts = autoShipsWithoutCartons.filter(
      (item: any) =>
        !medPaxSalesItems.includes(item.itmNumber) &&
        !medPaxPacketDetails.some(
          (packet) => packet.itmNumber === item.itmNumber
        )
    );
    this.filteredAutoShipProducts = [
      ...remainingProducts,
      ...autoShipsMedpax
    ];
  }

  processAutoShipDetails() {
    this.autoShipsCC = this.autoShipDetails?.autoShipsCC?.item?.[0] || {};
    this.autoShipDetails?.autoShipsHdr?.item?.map((item: any) => {
      this.filteredAutoShipProducts?.map((product: any) => {
        if (item.orderNumber === product.orderNumber) {
          let isSheduleDateExist: boolean = false;
          const existingAutoShipHeaderItem = this.autoShipsHeaderItems?.filter((existinAutoShip) => existinAutoShip.orderNumber === item.orderNumber);
          existingAutoShipHeaderItem?.filter(existingItem => {
            const sheduleDate = existingItem?.products?.filter((existingProducts: { schedDate: any; }) => existingProducts?.schedDate === product.schedDate)?.[0]?.schedDate;
            if (sheduleDate) {
              let existingIndex: number = this.autoShipsHeaderItems?.findIndex((existinAutoShip) => {
                if (existinAutoShip.orderNumber === item.orderNumber) {
                  const existProductSheduleDate = existinAutoShip?.products?.filter((existingProducts: { schedDate: any; }) => existingProducts?.schedDate === product.schedDate)?.[0]?.schedDate;
                  if (existProductSheduleDate) return true;
                }
                return false;
              });
              isSheduleDateExist = true;
              this.autoShipsHeaderItems[existingIndex]['products'] = [
                ...this.autoShipsHeaderItems[existingIndex]['products'],
                product
              ];
              this.autoShipsHeaderItems[existingIndex]['totalPrice'] = this.autoShipsHeaderItems[existingIndex]['products']?.reduce((total: number, item: any) => total + Number(item?.overallTotalSum || (item?.price * item.reqQty)), 0);
            }
          });
          if (!isSheduleDateExist) {
            this.autoShipsHeaderItems.push({
              ...item,
              shippingDate: product?.schedDate,
              totalPrice: product?.overallTotalSum || (product?.price * product.reqQty),
              products: [product]
            })
          }
        }
      });
    });
  }

  goToOrderList() {
    this.router.navigate(['/order/history'])
  }

  cancelAutoShip() {
    const autoShipHeaderDetails = this.autoShipDetails?.autoShipsHdr?.item?.[0] || {};
    const autoShipCCDetails = this.autoShipDetails?.autoShipsCC?.item?.[0] || {};
    const payload = {
      orderNumber: autoShipHeaderDetails?.orderNumber || '',
      instructions: autoShipHeaderDetails?.instructions || '',
      doseNote: autoShipHeaderDetails?.doseNote || '',
      shipMethod: '',
      carrier: autoShipHeaderDetails?.carrier || '',
      shipTo: autoShipHeaderDetails?.shipTo || '',
      endDate: '',
      creditCardDetails: {
        validTo: autoShipCCDetails?.ccValidTo || '',
        cardType: autoShipCCDetails?.ccType || '',
        cardNumber: autoShipCCDetails?.ccNumber || '',
        cardHolderName: autoShipCCDetails?.ccName || ''
      },
      items: {
        item: this.autoShipDetails?.autoShips?.items?.map((item: any) => {
          return {
            entryNumber: item?.itmNumber || '',
            entryGroupNumber: item?.hgLvItem || '',
            poItemNumber: item?.poItemNumber || '',
            material: item?.material || '',
            description: '',
            requestedQty: Number(item?.reqQty) || '',
            salesUnit: item?.salesUnit || '',
            price: item?.price || '',
            startDate: this.defaultDate,
            endDate: this.defaultDate,
            frequency: item?.frequency || '',
            frequency_type: item?.frequencyType || '',
            text: item?.text || '',
            action: 'D',
            doseNote: item?.doseNote || '',
            sapPrice: item?.sapPrice || '',
            confirmedQuantity: item?.confirmedQuantity || '',
            nextDate: item?.nextDate || '',
            dateChangeType: '',
            sapDiscount: item?.sapDiscount || '',
            sapSubtotal: item?.sapSubtotal || '',
            sapTax: item?.sapTax || '',
            sapTotal: item?.sapTotal || '',
            matEntered: item?.matEntered || '',
            caExclusion: item?.caExclusion || '',
            priority: item?.priority || '',
            recommendationId: item?.recommendationId || '',
            discount: item?.discount || '',
            templateId: item?.templateId || '',
            discountCash: item?.discountCash || ''
          }
        }) || []
      }
    };
    this.loader = true;
    this.autoshipsService.updateAutoShip(payload)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe(data => {
        this.globalMessageService.add(
          'Autoship Cancelled Successfully',
          GlobalMessageType.MSG_TYPE_CONFIRMATION,
          5000
        );
        this.activityLogService.sendLog('autoship_cancel', { autoshipId: autoShipHeaderDetails?.orderNumber || ''})
        this.router.navigateByUrl('autoships-list');
        this.loader = false;
        this.cd.detectChanges();
      }, () => {
        this.loader = false;
        this.cd.detectChanges();
      })
  }

  changeShipToAddress(address: any) {
    const autoShipHeaderDetails = this.autoShipDetails?.autoShipsHdr?.item?.[0] || {};
    const autoShipCCDetails = this.autoShipDetails?.autoShipsCC?.item?.[0] || {};
    const payload = {
      orderNumber: autoShipHeaderDetails?.orderNumber || '',
      instructions: autoShipHeaderDetails?.instructions || '',
      doseNote: autoShipHeaderDetails?.doseNote || '',
      shipMethod: '',
      carrier: autoShipHeaderDetails?.carrier || '',
      shipTo: address?.addressNumber || '',
      endDate: '',
      creditCardDetails: {
        validTo: autoShipCCDetails?.ccValidTo || '',
        cardType: autoShipCCDetails?.ccType || '',
        cardNumber: autoShipCCDetails?.ccNumber || '',
        cardHolderName: autoShipCCDetails?.ccName || ''
      },
      items: {
        item: this.autoShipDetails?.autoShips?.items?.map((item: any) => {
          return {
            entryNumber: item?.itmNumber || '',
            entryGroupNumber: item?.hgLvItem || '',
            poItemNumber: item?.poItemNumber || '',
            material: item?.material || '',
            description: '',
            requestedQty: Number(item?.reqQty) || '',
            salesUnit: item?.salesUnit || '',
            price: item?.price || '',
            startDate: this.defaultDate,
            endDate: this.defaultDate,
            frequency: item?.frequency || '',
            frequency_type: item?.frequencyType || '',
            text: item?.text || '',
            action: 'U',
            doseNote: item?.doseNote || '',
            sapPrice: item?.sapPrice || '',
            confirmedQuantity: item?.confirmedQuantity || '',
            nextDate: item?.nextDate || '',
            dateChangeType: '',
            sapDiscount: item?.sapDiscount || '',
            sapSubtotal: item?.sapSubtotal || '',
            sapTax: item?.sapTax || '',
            sapTotal: item?.sapTotal || '',
            matEntered: item?.matEntered || '',
            caExclusion: item?.caExclusion || '',
            priority: item?.priority || '',
            recommendationId: item?.recommendationId || '',
            discount: item?.discount || '',
            templateId: item?.templateId || '',
            discountCash: item?.discountCash || ''
          }
        }) || []
      }
    };
    this.loader = true;
    this.autoshipsService.updateAutoShip(payload)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe(data => {
        this.getAutoShipDetails(this.orderNumber);
        this.loader = false;
        this.cd.detectChanges();
      }, (error) => {
        this.loader = false;
        this.cd.detectChanges();
      })
  }

  processProductsToDatePayload(item: any, startDate: string) {
    if(item.frequency && !isNaN(Number(item.frequency))) {
      let tempDate = new Date(startDate);
      tempDate.setDate(tempDate.getDate() - Number(item.frequency));
      startDate = this.datePipe.transform(new Date(tempDate), 'yyyy-MM-dd');
    }
    return {
      entryNumber: item?.itmNumber || '',
      entryGroupNumber: item?.hgLvItem || '',
      poItemNumber: item?.poItemNumber || '',
      material: item?.material || '',
      description: '',
      requestedQty: Number(item?.reqQty) || '',
      salesUnit: item?.salesUnit || '',
      price: item?.price || '',
      startDate: startDate,
      endDate: this.defaultDate,
      frequency: item?.frequency || '',
      frequency_type: item?.frequencyType || '',
      text: item?.text || '',
      action: 'U',
      doseNote: item?.doseNote || '',
      sapPrice: item?.sapPrice || '',
      confirmedQuantity: item?.confirmedQuantity || '',
      nextDate: item?.nextDate || '',
      dateChangeType: 'X',
      sapDiscount: item?.sapDiscount || '',
      sapSubtotal: item?.sapSubtotal || '',
      sapTax: item?.sapTax || '',
      sapTotal: item?.sapTotal || '',
      matEntered: item?.matEntered || '',
      caExclusion: item?.caExclusion || '',
      priority: item?.priority || '',
      recommendationId: item?.recommendationId || '',
      discount: item?.discount || '',
      templateId: item?.templateId || '',
      discountCash: item?.discountCash || ''
    }
  }

  changeShipmentDate(data: any) {
    const { details, shipmentDate } = data;
    const startDate = this.datePipe.transform(new Date(shipmentDate), 'yyyy-MM-dd');
    const autoShipHeaderDetails = this.autoShipDetails?.autoShipsHdr?.item?.[0] || {};
    const autoShipCCDetails = this.autoShipDetails?.autoShipsCC?.item?.[0] || {};
    const products: any[] = [];
    details?.products?.map((product: any) => {
      products.push(this.processProductsToDatePayload(product, startDate));
      product?.children?.map((childItem: any) => {
        products.push(this.processProductsToDatePayload(childItem, startDate));
      })
      if(product.packetDetails) {
        Object.keys(product.packetDetails)?.map((key: string) => {
          product.packetDetails?.[key]?.entries?.map((entry: any) => {
            products.push(this.processProductsToDatePayload(entry, startDate));
          })
        })
      }
      
    })
    const autoShipsWithCartons = this.autoShipDetails?.autoShips?.items?.filter((item: any) => {
      return MEDPAX_CARTON_NEW.includes(item.material);
    });
    if(autoShipsWithCartons?.length > 0) {
      autoShipsWithCartons?.map((cartoon: any) => {
        products.push(this.processProductsToDatePayload(cartoon, startDate));
      });
    }
    const payload = {
      orderNumber: autoShipHeaderDetails?.orderNumber || '',
      instructions: autoShipHeaderDetails?.instructions || '',
      doseNote: autoShipHeaderDetails?.doseNote || '',
      shipMethod: '',
      carrier: autoShipHeaderDetails?.carrier || '',
      shipTo: autoShipHeaderDetails?.shipTo || '',
      endDate: '',
      creditCardDetails: {
        validTo: autoShipCCDetails?.ccValidTo || '',
        cardType: autoShipCCDetails?.ccType || '',
        cardNumber: autoShipCCDetails?.ccNumber || '',
        cardHolderName: autoShipCCDetails?.ccName || ''
      },
      items: {
        item: products || []
      }
    };
    this.loader = true;
    this.autoshipsService.updateAutoShip(payload)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe(data => {
        this.getAutoShipDetails(this.orderNumber);
        this.loader = false;
        this.cd.detectChanges();
      }, (error) => {
        this.loader = false;
        this.cd.detectChanges();
      })
  }

  processProductsToSkipPayload(item: any) {
    return {
      entryNumber: item?.itmNumber || '',
      entryGroupNumber: item?.hgLvItem || '',
      poItemNumber: item?.poItemNumber || '',
      material: item?.material || '',
      description: '',
      requestedQty: Number(item?.reqQty) || '',
      salesUnit: item?.salesUnit || '',
      price: item?.price || '',
      startDate: item?.schedDate || this.defaultDate,
      endDate: this.defaultDate,
      frequency: item?.frequency || '',
      frequency_type: item?.frequencyType || '',
      text: item?.text || '',
      action: 'U',
      doseNote: item?.doseNote || '',
      sapPrice: item?.sapPrice || '',
      confirmedQuantity: item?.confirmedQuantity || '',
      nextDate: item?.nextDate || '',
      dateChangeType: 'X',
      sapDiscount: item?.sapDiscount || '',
      sapSubtotal: item?.sapSubtotal || '',
      sapTax: item?.sapTax || '',
      sapTotal: item?.sapTotal || '',
      matEntered: item?.matEntered || '',
      caExclusion: item?.caExclusion || '',
      priority: item?.priority || '',
      recommendationId: item?.recommendationId || '',
      discount: item?.discount || '',
      templateId: item?.templateId || '',
      discountCash: item?.discountCash || ''
    }
  }

  skipInstead(product: any) {
    const autoShipHeaderDetails = this.autoShipDetails?.autoShipsHdr?.item?.[0] || {};
    const autoShipCCDetails = this.autoShipDetails?.autoShipsCC?.item?.[0] || {};
    const products: any[] = [];
    products.push(this.processProductsToSkipPayload(product));
    product?.children?.map((childItem: any) => {
      products.push(this.processProductsToSkipPayload(childItem));
    })
    if(product.packetDetails) {
      Object.keys(product.packetDetails)?.map((key: string) => {
        product.packetDetails?.[key]?.entries?.map((entry: any) => {
          products.push(this.processProductsToSkipPayload(entry));
        })
      })
    }
    const autoShipsWithCartons = this.autoShipDetails?.autoShips?.items?.filter((item: any) => {
      return MEDPAX_CARTON_NEW.includes(item.material);
    });
    if(autoShipsWithCartons?.length > 0) {
      autoShipsWithCartons?.map((cartoon: any) => {
        products.push(this.processProductsToSkipPayload(cartoon));
      });
    }
    const payload = {
      orderNumber: autoShipHeaderDetails?.orderNumber || '',
      instructions: autoShipHeaderDetails?.instructions || '',
      doseNote: autoShipHeaderDetails?.doseNote || '',
      shipMethod: '',
      carrier: autoShipHeaderDetails?.carrier || '',
      shipTo: autoShipHeaderDetails?.shipTo || '',
      endDate: '',
      creditCardDetails: {
        validTo: autoShipCCDetails?.ccValidTo || '',
        cardType: autoShipCCDetails?.ccType || '',
        cardNumber: autoShipCCDetails?.ccNumber || '',
        cardHolderName: autoShipCCDetails?.ccName || ''
      },
      items: {
        item: products || []
      }
    };
    this.loader = true;
    this.autoshipsService.updateAutoShip(payload)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe(data => {
        this.getAutoShipDetails(this.orderNumber);
        this.loader = false;
        this.cd.detectChanges();
      }, (error) => {
        this.loader = false;
        this.cd.detectChanges();
      })
  }

  processProductsToRemovePayload(item: any) {
    return {
      entryNumber: item?.itmNumber || '',
      entryGroupNumber: item?.hgLvItem || '',
      poItemNumber: item?.poItemNumber || '',
      material: item?.material || '',
      description: '',
      requestedQty: Number(item?.reqQty) || '',
      salesUnit: item?.salesUnit || '',
      price: item?.price || '',
      startDate: this.defaultDate,
      endDate: this.defaultDate,
      frequency: item?.frequency || '',
      frequency_type: item?.frequencyType || '',
      text: item?.text || '',
      action: 'D',
      doseNote: item?.doseNote || '',
      sapPrice: item?.sapPrice || '',
      confirmedQuantity: item?.confirmedQuantity || '',
      nextDate: item?.nextDate || '',
      dateChangeType: '',
      sapDiscount: item?.sapDiscount || '',
      sapSubtotal: item?.sapSubtotal || '',
      sapTax: item?.sapTax || '',
      sapTotal: item?.sapTotal || '',
      matEntered: item?.matEntered || '',
      caExclusion: item?.caExclusion || '',
      priority: item?.priority || '',
      recommendationId: item?.recommendationId || '',
      discount: item?.discount || '',
      templateId: item?.templateId || '',
      discountCash: item?.discountCash || ''
    }
  }

  removeProduct(product: any) {
    const autoShipHeaderDetails = this.autoShipDetails?.autoShipsHdr?.item?.[0] || {};
    const autoShipCCDetails = this.autoShipDetails?.autoShipsCC?.item?.[0] || {};
    const products: any[] = [];
    products.push(this.processProductsToRemovePayload(product));
    product?.children?.map((childItem: any) => {
      products.push(this.processProductsToRemovePayload(childItem));
    })
    if(product.packetDetails) {
      Object.keys(product.packetDetails)?.map((key: string) => {
        product.packetDetails?.[key]?.entries?.map((entry: any) => {
          products.push(this.processProductsToRemovePayload(entry));
        })
      })
    }
    const autoShipsWithCartons = this.autoShipDetails?.autoShips?.items?.filter((item: any) => {
      return MEDPAX_CARTON_NEW.includes(item.material);
    });
    if(autoShipsWithCartons?.length > 0) {
      autoShipsWithCartons?.map((cartoon: any) => {
        products.push(this.processProductsToRemovePayload(cartoon));
      });
    }
    const payload = {
      orderNumber: autoShipHeaderDetails?.orderNumber || '',
      instructions: autoShipHeaderDetails?.instructions || '',
      doseNote: autoShipHeaderDetails?.doseNote || '',
      shipMethod: '',
      carrier: autoShipHeaderDetails?.carrier || '',
      shipTo: autoShipHeaderDetails?.shipTo || '',
      endDate: '',
      creditCardDetails: {
        validTo: autoShipCCDetails?.ccValidTo || '',
        cardType: autoShipCCDetails?.ccType || '',
        cardNumber: autoShipCCDetails?.ccNumber || '',
        cardHolderName: autoShipCCDetails?.ccName || ''
      },
      items: {
        item: products || []
      }
    };
    this.loader = true;
    this.autoshipsService.updateAutoShip(payload)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe(data => {
        this.getAutoShipDetails(this.orderNumber);
        this.loader = false;
        this.cd.detectChanges();
      }, () => {
        this.loader = false;
        this.cd.detectChanges();
      })
  }

  processProductsToSavePayload(item: any, quantity: any, frequency: any, startDate: any) {
    return {
      entryNumber: item?.itmNumber || '',
      entryGroupNumber: item?.hgLvItem || '',
      poItemNumber: item?.poItemNumber || '',
      material: item?.material || '',
      description: '',
      requestedQty: Number(quantity) || '',
      salesUnit: item?.salesUnit || '',
      price: item?.price || '',
      startDate: startDate || this.defaultDate,
      endDate: this.defaultDate,
      frequency: frequency || '',
      frequency_type: item?.frequencyType || '',
      text: item?.text || '',
      action: item?.isRemoved ? 'D' : 'U',
      doseNote: item?.doseNote || '',
      sapPrice: item?.sapPrice || '',
      confirmedQuantity: item?.confirmedQuantity || '',
      nextDate: item?.nextDate || '',
      dateChangeType: startDate ? 'X' : '',
      sapDiscount: item?.sapDiscount || '',
      sapSubtotal: item?.sapSubtotal || '',
      sapTax: item?.sapTax || '',
      sapTotal: item?.sapTotal || '',
      matEntered: item?.matEntered || '',
      caExclusion: item?.caExclusion || '',
      priority: item?.priority || '',
      recommendationId: item?.recommendationId || '',
      discount: item?.discount || '',
      templateId: item?.templateId || '',
      discountCash: item?.discountCash || ''
    }
  }

  saveChanges(product: any) {
    const autoShipHeaderDetails = this.autoShipDetails?.autoShipsHdr?.item?.[0] || {};
    const autoShipCCDetails = this.autoShipDetails?.autoShipsCC?.item?.[0] || {};
    const products: any[] = [];
    let startDate = product?.nextShipmentSliderState ? product?.schedDate : '';
    products.push(this.processProductsToSavePayload(product, product.reqQty, product.frequency, startDate));
    product?.children?.map((childItem: any) => {
      products.push(this.processProductsToSavePayload(childItem, product.reqQty, product.frequency, startDate));
    })
    if(product.packetDetails) {
      Object.keys(product.packetDetails)?.map((key: string) => {
        product.packetDetails?.[key]?.entries?.map((entry: any) => {
          products.push(this.processProductsToSavePayload(entry, product.reqQty, product.frequency, startDate));
        })
      })
    }
    const autoShipsWithCartons = this.autoShipDetails?.autoShips?.items?.filter((item: any) => {
      return MEDPAX_CARTON_NEW.includes(item.material);
    });
    if(autoShipsWithCartons?.length > 0) {
      autoShipsWithCartons?.map((cartoon: any) => {
        products.push(this.processProductsToSavePayload(cartoon, product.reqQty, product.frequency, startDate));
      });
    }
    const payload = {
      orderNumber: autoShipHeaderDetails?.orderNumber || '',
      instructions: autoShipHeaderDetails?.instructions || '',
      doseNote: autoShipHeaderDetails?.doseNote || '',
      shipMethod: '',
      carrier: autoShipHeaderDetails?.carrier || '',
      shipTo: autoShipHeaderDetails?.shipTo || '',
      endDate: '',
      creditCardDetails: {
        validTo: autoShipCCDetails?.ccValidTo || '',
        cardType: autoShipCCDetails?.ccType || '',
        cardNumber: autoShipCCDetails?.ccNumber || '',
        cardHolderName: autoShipCCDetails?.ccName || ''
      },
      items: {
        item: products || []
      }
    };
    this.loader = true;
    this.autoshipsService.updateAutoShip(payload)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe(data => {
        this.activityLogService.sendLog('autoship_edit', { frequency: product.frequency, date: startDate})
        this.getAutoShipDetails(this.orderNumber);
        this.loader = false;
        this.cd.detectChanges();
      }, () => {
        this.loader = false;
        this.cd.detectChanges();
      })
  }

}
