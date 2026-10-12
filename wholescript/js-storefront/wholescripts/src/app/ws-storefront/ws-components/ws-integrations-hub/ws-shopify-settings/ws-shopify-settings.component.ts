import { AfterViewInit, ChangeDetectorRef, Component, DestroyRef, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { CmsService, OccConfig } from '@spartacus/core';
import { Router } from '@angular/router';
import { PaymentService } from '../../account-setting/payment-service/payment.service';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { WsShopifyService } from '../../../ws-services/ws-shopify.service';
@Component({
  selector: 'ws-shopify-settings',
  templateUrl: './ws-shopify-settings.component.html',
  styleUrl: './ws-shopify-settings.component.scss',
})
export class WsShopifySettingsComponent implements AfterViewInit {
  shopifyHeading$: Observable<any> = this.cmsservice.getComponentData('ShopifySettingsParaComp');
  streamlineFulfillment$: Observable<any> = this.cmsservice.getComponentData('StreamlineFulfillmentShopifyParaComp');
  faqHeading$: Observable<any> = this.cmsservice.getComponentData('FaqHeaderComp');
  faqQue1$: Observable<any> = this.cmsservice.getComponentData('FaqQue1Comp');
  faqAns1$: Observable<any> = this.cmsservice.getComponentData('FaqAns1Comp');
  faqQue2$: Observable<any> = this.cmsservice.getComponentData('FaqQue2Comp');
  faqAns2$: Observable<any> = this.cmsservice.getComponentData('FaqAns2Comp');
  shopifyDownloadGuidePdf$: Observable<any> = this.cmsservice.getComponentData(
    'wholescriptShopifyDownloadMediaLink'
  );
  public occ = inject(OccConfig);
  showAgreement = false;
  loading: boolean = false; // Loading state
  shopifyInstalled: boolean = false;
  shopifyStatusDetails: any;

  constructor(
    public cmsservice: CmsService,
    private router: Router,
    private paymentService: PaymentService,
    private cdr: ChangeDetectorRef,
    private destroyRef: DestroyRef,
    private shopifyService: WsShopifyService
  ) {
    this.getSignedAmendment();
    this.shopifyStatus();
  }

  ngAfterViewInit(): void {
    if(document.getElementsByTagName('title') && !document.getElementsByTagName('title')[0].innerText) document.getElementsByTagName('title')[0].innerText = 'Shopify';
  }

  getSignedAmendment() {
    this.loading = true;
    this.paymentService
      .getAmendment()
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: (res: any) => {
          this.showAgreement = res?.success;
          this.loading = false;
          this.cdr.detectChanges();
        },
        error: (err) => {
          console.error('Error fetching amendment:', err);
          this.loading = false;
        },
      });
  }

  shopifyStatus() {
    this.shopifyService.getShopifyStatus().subscribe({
      next: (res: any) => {
        this.shopifyStatusDetails = res
        this.shopifyInstalled = res?.success;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Error fetching shopify-status:', err);
      }
    })
  }

  startIntegration() {
    this.router.navigateByUrl('/integration-agreement');
  }

  importShopifyProducts() {
    this.router.navigateByUrl('/shopify/catalog')
  }

  downloadPdfNavigation(downloadPdf: any){
    const pdfUrl = this.occ?.backend?.occ?.baseUrl + downloadPdf?.pdfFile?.url;
    if (pdfUrl) {
      window.open(pdfUrl, '_blank');
    }
  }
}
