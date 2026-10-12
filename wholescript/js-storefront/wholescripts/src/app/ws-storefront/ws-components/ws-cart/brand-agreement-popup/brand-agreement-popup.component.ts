import { AfterViewInit, Component, Input } from '@angular/core';
import { OccConfig, RoutingService } from '@spartacus/core';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { WsCheckoutDeliveryAddressService } from '../../../ws-services/ws-checkout-delivery-address.service';
import * as pdfjsLib from 'pdfjs-dist/legacy/build/pdf';
pdfjsLib.GlobalWorkerOptions.workerSrc = '//cdnjs.cloudflare.com/ajax/libs/pdf.js/3.4.120/pdf.worker.min.js';


@Component({
  selector: 'ws-brand-agreement-popup',
  templateUrl: './brand-agreement-popup.component.html',
  styleUrls: ['./brand-agreement-popup.component.scss']
})
export class BrandAgreementPopupComponent implements AfterViewInit {
  @Input() brandContent: any;
  isAgreed: boolean = false;
  iframeError = false;
  loader: boolean = false;
  constructor(public bsModalRef: BsModalRef, public occ: OccConfig, public wsCheckoutDeliveryAddressService: WsCheckoutDeliveryAddressService, private routingService: RoutingService) { }

  ngAfterViewInit(): void {
    setTimeout(() => {
      this.brandContent.forEach((item: any, index: string) => {
        this.renderPdf(this.getPdfUrl(item), 'pdfViewer' + index);
      });
    },0);
  }
  
  toggleContinueButton(item: any): void {
    item.isAgreed = !item.isAgreed;
    let data = this.brandContent?.find((x: any) => !x.isAgreed);
    this.isAgreed = data ? false : true;
  }

  closeModal(): void {
    this.bsModalRef?.hide();
  }

  continueToCheckout(): void {
    let payload = {
      brands: this.brandContent.map((x: any) => {
        return {
          brandName: x.name,
          isConsentAccepted: true,
          pdfPath: x?.brandAgreementPdf ? x?.brandAgreementPdf?.url : x?.brandAgreementPdfUrl
        }
      })
    }
    this.wsCheckoutDeliveryAddressService.getBrandConsetUpdate(payload).subscribe((res: any) => {
    }, (error) => {
      if (error?.status === 200) {
        this.bsModalRef?.hide();
        this.routingService.go({
          cxRoute: 'checkout'
        })
      }
    })
  }

  renderPdf(url: string, containerId: string) {
    this.loader = true;
    const container = document.getElementById(containerId);
    if (!container) {
      this.loader = false;
      return;
    }
    const loadingTask = pdfjsLib.getDocument({
      url: url,
      disableWorker: true
    } as any);
    
    loadingTask.promise.then((pdf: any) => {
      const totalPages = pdf.numPages;
      for (let pageNum = 1; pageNum <= totalPages; pageNum++) {
        pdf.getPage(pageNum).then((page: any) => {
          const viewport = page.getViewport({ scale: 1.5 });
          const canvas = document.createElement('canvas');
          const context = canvas.getContext('2d')!;
          canvas.height = viewport.height;
          canvas.width = viewport.width;

          container.appendChild(canvas);

          page.render({ canvasContext: context, viewport: viewport });
        });
      }
      this.loader = false;
    });
  }

  getPdfUrl(item: any) {
    return item?.brandAgreementPdf ? this.occ.backend.occ.baseUrl + item?.brandAgreementPdf?.url : item?.brandAgreementPdfUrl;
  }
}
