import { AfterViewInit, ChangeDetectorRef, Component, DestroyRef, ElementRef, inject, input, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { DomSanitizer } from '@angular/platform-browser';
import { delay, filter, Observable } from 'rxjs';
import { WsCheckoutPaymentService } from '../../../ws-services/ws-checkout/ws-checkout-payment.service';
import { WsAddCreditCardService } from '../../ws-add-credit-card/ws-add-credit-card.service';
import { FormControl } from '@angular/forms';
import { GlobalMessageService, GlobalMessageType } from '@spartacus/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';

declare let $XIFrame: any;

@Component({
  selector: 'ws-new-creditcard-modal',
  exportAs: '',
  templateUrl: './ws-new-creditcard-modal.component.html',
  styleUrls: ['./ws-new-creditcard-modal.component.scss'],
})
export class WsNewCreditCardComponent implements AfterViewInit, OnInit, OnDestroy {
  private service = inject(WsAddCreditCardService);
  //input
  isAccountSettings = input<boolean>(false);
  currentAddress = input<any>({});
  @ViewChild('iframeContainer', { static: false }) iframeContainer!: ElementRef;
  saveCreditCard = new FormControl(true);

  errorMessage: string = 'Payment failed for the provided credit card. Please review associated billing address for errors and try again. Contact customer service at (800) 647-6100 for further assistence.';
  private destroyRef = inject(DestroyRef);
  showLoading = false;
  isSubmitCardError: boolean = false;
  iframeUrl: any;
  paymetricsDetail$: Observable<any> =
    this.wsCheckoutPaymentService.loadPaymetricsDetail$();
  constructor(
    protected wsCheckoutPaymentService: WsCheckoutPaymentService,
    protected sanitizer: DomSanitizer,
    protected globalMessageService: GlobalMessageService,
    private cd: ChangeDetectorRef
  ) { }

  ngOnInit() {
    this.showLoading = true;
    this.wsCheckoutPaymentService.getAccessToken();
  }

  ngAfterViewInit() {
    this.wsCheckoutPaymentService.paymetricsDetail$.pipe(
      filter((detail: any) => !!detail?.paymetricsIframeAccessTokenUrl),
      delay(0), // Delay to ensure the iframe is created after the view is initialized
      takeUntilDestroyed(this.destroyRef),
    ).subscribe((detail: any) => {
      this.showLoading = false;
      const iframeUrl = detail.paymetricsIframeAccessTokenUrl;
      this.iframeUrl = iframeUrl;
      const element = document.getElementById("iframewrapper");
      if (element?.firstChild) {
        while (element.firstChild) {
          element.removeChild(element.firstChild);
        }
      }
      const existingIframe = this.iframeContainer?.nativeElement?.querySelector('#paymetricIFrame');
      if (existingIframe) {
        return;
      }

      const iframe = document.createElement('iframe');
      iframe.src = iframeUrl;
      iframe.id = 'paymetricIFrame';
      iframe.name = 'dieCommFrame';
      iframe.width = '100%';
      iframe.height = '600';
      iframe.className = 'iframeStyle';
      iframe.referrerPolicy = 'no-referrer';
      iframe.style.position = 'relative';
      iframe.style.zIndex = '1';
      iframe.onload = () => this.iFrame_OnLoad();

      this.iframeContainer.nativeElement.appendChild(iframe);
      this.cd.detectChanges();
     
    });
  }

  cleanURL(oldURL: string) {
    return this.sanitizer.bypassSecurityTrustResourceUrl(oldURL);
  }

  iFrame_OnLoad() {
    this.showLoading = false;
    const iframe = document.getElementsByName('dieCommFrame');
    if (iframe) {
      $XIFrame.onload({
        iFrameId: 'dieCommFrame',
        targetUrl: iframe[0].getAttribute('src'),
        autosizewidth: false,
        autosizeheight: false,
        onSuccess(msg: any) {
          console.log(
            'A form for the merchant guid and access token combination is loading in the iFrame successfully.'
          );
        },
        onError(msg: any) {
          console.log(
            'A form for the merchant guid and access token combination has FAILED to load.'
          );
        },
      });
    }
    this.cd.markForCheck();
  }
  submitform(accessToken: string) {
    this.showLoading = true;
    this.isSubmitCardError = false;
    const iframe = document.getElementsByName('dieCommFrame');
    const self = this;
    if (iframe) {
      $XIFrame.submit({
        iFrameId: 'dieCommFrame',
        targetUrl: iframe[0].getAttribute('src'),
        onSuccess(msg: any) {
          self.showLoading = false;
          const message = JSON.parse(msg);
          if (message?.data?.HasPassed) {
            self.callApiToAddCardDetails(accessToken);
          } else {
            self.reloadIFrame();
          }
          self.cd.detectChanges();
          console.debug('message', message);
          console.debug('msg', msg);
          console.debug('message?.data?.HasPassed', message?.data?.HasPassed);
        },
        onError(msg: any) {
          self.showLoading = false;
          this.reloadIFrame();
          self.cd.detectChanges();
          const message = JSON.parse(msg);
          console.error('message', message);
        },
        onInvalidHandler() {
          self.showLoading = false;
          self.cd.detectChanges();
        }
      });
    }
    return false;
  }

  closeMe() {
    this.service.emitcloseMeButton();
  }

  callApiToAddCardDetails(accessToken: string) {
    this.showLoading = true;
    this.cd.detectChanges();
    this.wsCheckoutPaymentService
      .addCardDetailsForBilling(this.currentAddress(), accessToken, this.isAccountSettings() ? true : this.saveCreditCard.value)
      .subscribe(() => {
        this.showLoading = false;
        this.isSubmitCardError = false;
        const closeElement = document.getElementById('add-credit-card-close');
        if(closeElement) {
          closeElement.click();
        }
        this.globalMessageService.add(
          'Card saved successfully!',
          GlobalMessageType.MSG_TYPE_CONFIRMATION,
          10000
        );
        this.wsCheckoutPaymentService.addNewCardSucceededClicked();
        this.cd.detectChanges();
      }, (error) => {
        this.reloadIFrame();
        this.showLoading = false;
        this.cd.detectChanges();
      });
  }

  reloadIFrame() {
    this.isSubmitCardError = true;
    this.wsCheckoutPaymentService.getAccessToken();
    this.cd.detectChanges();
  }

  ngOnDestroy() {
    this.wsCheckoutPaymentService.paymetricsDetail$.next(null);
  }


}


