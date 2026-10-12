import { ChangeDetectorRef, Component, DestroyRef, Inject, inject, OnInit, Renderer2 } from '@angular/core';
import { AuthService } from '@spartacus/core';
import { CurrentProductService, ProductSummaryComponent } from '@spartacus/storefront';
import { forkJoin, Observable, tap } from 'rxjs';
import { RecommendationService } from '../../../ws-services/ws-recommendation.service';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { PdpDataservice } from '../data-pdp/pdpData.service';
import { userMap } from '../../../ws-env/environment';
import { WsPromotionsService } from '../../../ws-services/promotions.service';

@Component({
  selector: 'cx-product-summary',
  templateUrl: './ws-product-summaary.component.html',
  styleUrl: './ws-product-summaary.component.scss'
})
export class WsProductSummaaryComponent extends ProductSummaryComponent implements OnInit {
  isLoggedIn: boolean = false;
  curUser$: Observable<any>;
  isPractioner: boolean;
  autoShipFrequency: string = 'One-Time';
  showPromoCode: boolean = true;
  alert: any = {};
  showPromoDetailsModal: boolean = false;
  selectedPromotion: any = {};
  allPromotionsWithDescription: any[] = [];
  prevFocusElm: HTMLElement | null = null;
  
  private destroyRef = inject(DestroyRef);
  constructor(
    protected override  currentProductService: CurrentProductService,
    private authService: AuthService,
    private recommendationService: RecommendationService,
    private pdpService: PdpDataservice,
    private cd: ChangeDetectorRef,
    private promotionService: WsPromotionsService
  ) {
    super(currentProductService);

    this.authService.isUserLoggedIn().pipe(takeUntilDestroyed(this.destroyRef))
    .subscribe((res: any) => (this.isLoggedIn = res));
  }

  ngOnInit(): void {
    this.curUser$ = this.recommendationService.getUserDetails().pipe(
      tap((res:any) => {
        const key = Object.keys(userMap).find(key => userMap[key] === 'practitioner');
        this.isPractioner = (res?.roles || []).includes(key);        
      })
    );
    this.product$.subscribe(data => {
      this.allPromotionsWithDescription = [];
      if(data?.potentialPromotions?.length > 0) {
        const promotionApi = data.potentialPromotions.reduce((acc: any, promotion) => {
          acc[promotion.code] = this.promotionService.getPromotionByCode(promotion.code);
          return acc;
        }, {});
        forkJoin(promotionApi).subscribe({
          next: (response) => {
            Object.values(response)?.map(promotion => {
              if(promotion?.code) {
                this.allPromotionsWithDescription.push(promotion);
              }
            })
            this.cd.detectChanges();
          },
          error: (err) => {
            console.error('Error occurred in getPromotionByCode:', err);
          },
        });
      }
    })
  }

  changeAutoShipFrequency(event: any) {
    const value = event?.target?.value || '';
    this.pdpService.autoShipFrequency.set(value);
  }

  seeDetails(promotion: any) {
    this.selectedPromotion = promotion;
    this.showPromoDetailsModal = true;
    this.prevFocusElm = document.activeElement as HTMLElement;
  }

  closeModal() {
    this.showPromoDetailsModal = false;
    this.cd.detectChanges();
    setTimeout(() => {
      if (this.prevFocusElm) {
        this.prevFocusElm.focus();
        this.prevFocusElm = null;
      }
    }, 0);
  }

  copyPromoCode(text: any) {
    this.add();
    navigator.clipboard.writeText(text).then(() => {
      console.log('Text copied to clipboard successfully!');
    }).catch(err => {
    });
  }

  add(): void {
    this.alert = {
      type: 'success',
      msg: `Code copied to clipboard.`,
      timeout: 2000
    };
  }

  onClosed(): void {
    this.alert = {};
  }
}
