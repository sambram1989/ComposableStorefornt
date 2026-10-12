import { AfterViewInit, ChangeDetectorRef, Component, DestroyRef, inject, OnInit, signal } from '@angular/core';
import { AuthService, CmsService } from '@spartacus/core';
import { Observable, tap } from 'rxjs';
import { WsAutoShipsService } from '../../../ws-services/autoships.service';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { MEDPAX_CARTON_NEW, MEDPAX_HEADER_NEW } from '../../../ws-constants/ws-api-endpoint.constant';
import { getUserData, WsCurrentUserService } from '../../../ws-services/state-services/currentuser.service';
import { Router } from '@angular/router';
import { userMap } from '../../../ws-env/environment';
import { FAQPageService } from '../../../ws-services/ws-faq-page.service';
import { FaqQuestionsAndAnswers, FaqTopicList } from '../../../ws-models/ws-faq-page.model';

@Component({
  selector: 'ws-autoships-list-page',
  templateUrl: './ws-autoships-list-page.component.html',
  styleUrl: './ws-autoships-list-page.component.scss'
})
export class WsAutoshipsListPageComponent implements OnInit, AfterViewInit {
  private cmsService = inject(CmsService);
  CustomizeParaComp$: Observable<any> = this.cmsService.getComponentData('CustomizeParaComp');
  AutoshipsListHeaderComp$: Observable<any> = this.cmsService.getComponentData('AutoshipsListHeaderComp');
  private destroyRef = inject(DestroyRef);
  nextShipmentDate: any;
  shipmentDetails: any;
  loader: boolean = false;
  currentUserData: any = inject(getUserData);
  nextShipmendDate: any;
  nextShipmentProducts: any[] = [];
  nextShipmentTotalPrice: number = 0;
  isPractioner: boolean;
  autoShipsHeaderItems: any[] = [];
  filteredAutoShipProducts: any[] = [];
  faqQuestionsAndAnswers: FaqQuestionsAndAnswers = {
    title: '',
    uid: '',
    faqTopicList: []
  };
  toggleAnswer: any = {};

  private currentUser = inject(WsCurrentUserService);
  isPatient = signal<boolean>(false);
  shippingMessage: string = '';
  PATIENT_SHIPPING_PRICE: number = 100;
  PRACTITIONER_SHIPPING_PRICE: number = 300;
  isCurrentUser$: Observable<any> = this.currentUser.isRoleExist('patient').pipe(tap((res: boolean) => this.isPatient.set(res)));

  constructor(private autoshipsService: WsAutoShipsService, private cd: ChangeDetectorRef, private router: Router,
    private faqPageService: FAQPageService
  ) {
    this.getAutoshipStatus();
    this.getFreeshipingDetails();
    this.getAutoshipFAQs();
  }

   ngOnInit(): void {
      const user = JSON.parse(sessionStorage.getItem('user_config'));
      if(user) {
          const key = Object.keys(userMap).find(key => userMap[key] === 'practitioner');
          this.isPractioner = (user?.roles || []).includes(key);
        }
    }

    ngAfterViewInit(): void {
    if(document.getElementsByTagName('title') && !document.getElementsByTagName('title')[0].innerText) document.getElementsByTagName('title')[0].innerText = 'Autoships List';
    }

    getAutoshipFAQs() {
      this.faqPageService.getFaqQusAnsById('autoShipping')
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe(data => {
        this.faqQuestionsAndAnswers = data;
        this.cd.markForCheck();
      })
    }

    toggleQuestion(topic: FaqTopicList) {
      this.toggleAnswer[topic.uid] = !this.toggleAnswer[topic.uid];
      this.cd.detectChanges();
    }

    getFreeshipingDetails() {
      this.cmsService.getComponentData('AutoshipsListHeaderComp').subscribe((data: any) => {
        this.shippingMessage = data?.content;
        this.cd.markForCheck();
      });
    }

  getAutoshipStatus() {
    this.loader = true;
    this.currentUserData
    .pipe(takeUntilDestroyed(this.destroyRef))
    .subscribe((account: any) => {
      if(!account?.autoShips) {
        this.loader = false;
        this.router.navigateByUrl('autoships');
      } 
      else this.getAutoShips();
    })
  }

  getAutoShips() {
    this.loader = true;
    this.autoshipsService.getAutoShips()
    .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: (data) => {
          this.shipmentDetails = data;
          if(!(this.shipmentDetails?.autoShips?.items || this.shipmentDetails?.autoShips?.items?.length > 0)) {
            this.router.navigateByUrl('autoships');
          }
          if (this.shipmentDetails?.autoShipsHdr?.item?.length > 0) {
            this.processMedpaxDetails();
            this.processNextShipmentDetails();
            this.processShipmentListDetails();
          }
          this.loader = false;
          this.cd.detectChanges();
        },
        error: (err) => {
          this.loader = false;
          if(err?.error?.errors?.length > 0) {
            err?.error?.errors?.map((eachError: { message: string }) => {
              if(eachError?.message?.includes('No Autoship orders for account')) {
                this.router.navigateByUrl('autoships');
              }
            })
          }
          this.cd.detectChanges();
        },
        complete: () => {
          this.loader = false;
        },
      });
  }

  processMedpaxDetails() {
    const autoShipsWithoutCartons = this.shipmentDetails?.autoShips?.items?.filter((item: any) => {
      return !MEDPAX_CARTON_NEW.includes(item.material);
    });
    const medPaxHeaders = autoShipsWithoutCartons.filter(
      (item: { material: string; hgLvItem: string; }) =>
        item.hgLvItem === '000000'
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


    const medPaxItems = autoShipsMedpax
      .map((medPax) => [medPax.orderNumber])
      .flat();
    const remainingProducts = autoShipsWithoutCartons.filter(
      (item: any) =>
        !medPaxItems.includes(item.orderNumber)
    );
    this.filteredAutoShipProducts = [
      ...remainingProducts,
      ...autoShipsMedpax
    ];
  }


  processNextShipmentDetails() {
    this.nextShipmentTotalPrice = 0;
    this.nextShipmendDate = '';
    const nextShipmendDateProduct = this.filteredAutoShipProducts?.reduce((latest: { schedDate: string | number | Date; }, current: { schedDate: string | number | Date; }) =>
      new Date(current.schedDate) < new Date(latest.schedDate) ? current : latest
    );
    this.nextShipmendDate = nextShipmendDateProduct?.schedDate;
    this.nextShipmentProducts = this.filteredAutoShipProducts?.filter((item: { schedDate: any; }) => {
      return item.schedDate == this.nextShipmendDate;
    }).map((item: any) => {
      this.nextShipmentTotalPrice += Number(item?.overallTotalSum || (item?.price * item.reqQty) || 0);
      return item;
    });
    
    if(this.nextShipmentProducts?.length === 0) {
      this.router.navigateByUrl('autoships');
    }
  }

  processShipmentListDetails() {
    const filteredAutoShipsWithoutNextShipment = this.filteredAutoShipProducts?.filter((product: { schedDate: any; }) => product.schedDate != this.nextShipmendDate) || [];
    this.shipmentDetails?.autoShipsHdr?.item?.map((item: any) => {
      filteredAutoShipsWithoutNextShipment?.map((product: any) => {
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

}
