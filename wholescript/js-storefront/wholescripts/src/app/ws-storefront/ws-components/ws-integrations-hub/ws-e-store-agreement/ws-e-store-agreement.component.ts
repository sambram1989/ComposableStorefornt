import { AfterViewInit, ChangeDetectorRef, Component, inject, OnInit } from '@angular/core';
import { PaymentService } from '../../account-setting/payment-service/payment.service';
import { Router } from '@angular/router';
import { CmsService, Config } from '@spartacus/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { DatePipe } from '@angular/common';
import { Observable, shareReplay } from 'rxjs';

@Component({
  selector: 'ws-e-store-agreement',
  templateUrl: './ws-e-store-agreement.component.html',
  styleUrl: './ws-e-store-agreement.component.scss',
  providers: [DatePipe],
})
export class WsEStoreAgreementComponent implements OnInit, AfterViewInit{
  cards$: any[] = [];
  creditCardHeader: any = this.cmsService.getComponentData(
    'SelectCreditCardHeaderComp'
  );
  creditCardContent: any = this.cmsService.getComponentData(
    'SelectCreditCardParaComp'
  );
  today = new Date();
  signAgreementForm = new FormGroup({
    sign: new FormControl('', Validators.required),
    date: new FormControl(
      this.datePipe.transform(this.today, 'MM/dd/yyyy'),
      Validators.required
    ),
    radio: new FormControl('', Validators.required),
  });
  cardActive: any;
  selectedCard: any;
  baseURL: any;
  loader: boolean = false;
  integrationAgreementHeaderBannerComp$: Observable<any> = this.cmsService.getComponentData('IntegrationAgreementHeaderBannerComp');
  submitted: boolean = false;
  constructor(
    private paymentService: PaymentService,
    private cdRef: ChangeDetectorRef,
    private router: Router,
    private cmsService: CmsService,
    private datePipe: DatePipe,
    protected config: Config,
  ) {
    this.paymentService.previousRoutePath.next(window.location.pathname);
    this.getPaymentCardsList();
  }

  ngOnInit(): void {
  this.baseURL = this.config?.backend?.occ?.baseUrl
  ? this.config.backend?.occ?.baseUrl
  : '';
  this.signAgreementForm.get('date')?.disable();
}
  ngAfterViewInit(): void {
    if(document.getElementsByTagName('title') && !document.getElementsByTagName('title')[0].innerText) document.getElementsByTagName('title')[0].innerText = 'E Store Agreement';
  }

getImageUrl(image: any) {
  return image?.url ? `${this.baseURL}${image.url}` : undefined;
}

  getPaymentCardsList() {
    this.loader = true;
    this.paymentService.getPayments().subscribe((res: any) => {
      this.loader = false;
      this.cards$ = res?.cartData?.item;
      this.cdRef.detectChanges();
    },() => {
      this.loader = false;
      this.cdRef.detectChanges();
    });
  }
  pageChanged(ev: any) {}

  signAgreement() {
    this.submitted = true;
    if(this.signAgreementForm.invalid || !this.selectedCard?.billingAddress) return;
    const signDate = this.datePipe.transform(
      this.signAgreementForm.controls.date.value,
      'yyyy-MM-dd'
    );
    // Type value is static as per API specs
    const request = {
      ccLastFour: this.signAgreementForm.controls.radio.value.slice(-4),
      signedName: this.signAgreementForm.controls.sign.value,
      type: 'A_APITerms',
      signedDate: signDate,
    };
    const card: any = this.cards$.find(
      (item: any) =>
        item.cardNumber === this.signAgreementForm.controls.radio.value
    );
    const mt = this.datePipe.transform(card.validTo, 'MM');
    const yr = this.datePipe.transform(card.validTo, 'yyyy');
    this.paymentService
      .updateActivePaymentSettings(card, mt, yr)
      .pipe(shareReplay(1))
      .subscribe(() => {});
    this.paymentService.signedAgreementStatus(request).subscribe((res) => {
      this.router.navigate(['/integrations-hub/estore-settings']);
    });
  }
  addNewCard() {
    this.router.navigateByUrl('/accounts-settings/Payment');
  }
  getCardIcon(type: string) {
    const cardType = type.toLocaleLowerCase() === 'disc' 
    ? 'discover' 
    :  type.toLocaleLowerCase() === 'mc'
    ? 'mastercard'
    : type;
    return `/assets/icons/payment_method_icons/${cardType.toLocaleLowerCase()}-lg.svg`;
  }
  getSelectedCardData(item: any){
    this.selectedCard = item;
  }
}
