import {
  Component,
  OnInit,
  Input,
  DestroyRef,
  ChangeDetectorRef,
  ViewChild,
  ElementRef,
} from '@angular/core';
import { WsProductDetailsService } from '../../../ws-services/ws-product-details.service';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { Config } from '@spartacus/core';
import { BsModalRef } from 'ngx-bootstrap/modal';

@Component({
  selector: 'ws-product-details-popup',
  templateUrl: './ws-product-details-popup.component.html',
  styleUrl: './ws-product-details-popup.component.scss',
})
export class WsProductDetailsPopupComponent implements OnInit {
  @Input() productCode: string;
  productData: any[] = [];
  clinicalApplications: any[] = [];
  longDescription: any[] = [];
  supplementFactsUrl: string = '';
  directions: any[] = [];
  otheringredients: any[] = [];
  loading: boolean = false;

  @ViewChild('firstFocusable', { static: false }) firstFocusable: ElementRef;

ngAfterViewInit() {
  setTimeout(() => {
    this.firstFocusable?.nativeElement?.focus();
  });
}

  constructor(
    private productDetail: WsProductDetailsService,
    private cdr: ChangeDetectorRef,
    protected config: Config,
    private destroyRef: DestroyRef,
    public bsModalRef: BsModalRef
  ) {}

  ngOnInit(): void {
    this.loadProductDetails(this.productCode);
  }

  loadProductDetails(productCode: string) {
    this.loading = true;
    this.productDetail
      .fetchProductDetail(productCode)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: (res) => {
          this.productData = res || '';
          const baseUrl = this.config.backend?.occ?.baseUrl
            ? this.config.backend?.occ?.baseUrl
            : '';
          this.supplementFactsUrl = `${baseUrl}${res?.supplementFacts?.url}`;
          const relevantClassification = res.classifications?.find(
            (classification: any) =>
              classification.code === 'pimClassificationDataClass'
          ) || {};
          this.longDescription = relevantClassification?.features?.filter(
            (feature: any) => feature.code.includes('longdescription')
          ) || [];
          this.directions = relevantClassification?.features?.filter(
            (feature: any) => feature.code.includes('directions')
          ) || [];
          this.otheringredients = relevantClassification?.features?.filter(
            (feature: any) => feature.code.includes('otheringredients')
          ) || [];
          const clinicalApplications = relevantClassification?.features?.find(
            (feature: any) => feature.name === 'Clinical Applications'
          ) || [];
          if (clinicalApplications && clinicalApplications.featureValues) {
            const value = clinicalApplications.featureValues[0]?.value;
            this.clinicalApplications = value
              ?.split('*')
              ?.map((item: any) => item.trim());
          }
          this.loading = false;
          this.cdr.detectChanges();
        },
        error: () => {
          this.loading = false;
          this.cdr.detectChanges();
        },
        complete: () => {
          this.loading = false;
          this.cdr.detectChanges();
        },
      });
  }
}
