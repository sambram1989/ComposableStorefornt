import { ChangeDetectorRef, Component, inject } from '@angular/core';
import { BsModalRef, BsModalService, ModalOptions } from 'ngx-bootstrap/modal';
import { WsEditCredentialsComponent } from '../ws-e-store/ws-edit-credentials/ws-edit-credentials.component';
import { WsUpdatePaymentMethodComponent } from '../ws-e-store/ws-update-payment-method/ws-update-payment-method.component';
import { PaymentService } from '../../account-setting/payment-service/payment.service';
import { map, shareReplay, switchMap, tap } from 'rxjs';
import { getUserData } from '../../../ws-services/state-services/currentuser.service';
import { DatePipe } from '@angular/common';
import { Router } from '@angular/router';
import { WsWebKitDetailsComponent } from '../ws-e-store/ws-webkit-details/ws-webkit-details.component';
import { DomSanitizer } from '@angular/platform-browser';

@Component({
  selector: 'ws-e-store-integration',
  templateUrl: './ws-e-store-integration.component.html',
  styleUrl: './ws-e-store-integration.component.scss',
  providers: [DatePipe]
})

export class WsEStoreIntegrationComponent {
  currentUserData: any = inject(getUserData);
  bsModalRef: BsModalRef;
  credentialSelected: number = -1;
  externalStores: any[] = [];
  hasCredentialsList: any[] = [];
  sapCustomer: any;
  shippingConfirmations: boolean;
  cards$: any[] = [];
  defaultPayment: any;
  pendingOrders: any[] = [];
  alerts: any[] = [];
  initialOrders: any[] = [];
  searchKey: any;
  webCartons: any;
  webKits: any;
  loader: boolean = false;

  constructor(private dialog: BsModalService, public paymentService: PaymentService, private cdRef: ChangeDetectorRef,
              private datePipe: DatePipe, private router: Router, private sanitizer: DomSanitizer
  ) {
    this.currentUserData.subscribe((res: any) => this.sapCustomer = res.orgUnit?.uid);
    this.getExternalStores();
    this.getPendingOrders();
    this.getPaymentCardsList();
    this.getCartonsList();
    this.getWebKits();
  }
  openPaymentModal() {
    const initialState: ModalOptions = {
      class: 'ws-right-modal',
      initialState: {
        edit: false,
        cards$: this.cards$,
        defaultCard: this.defaultPayment
      },
    };
    this.bsModalRef = this.dialog.show(
      WsUpdatePaymentMethodComponent,
      initialState
    );
    this.paymentService.setPayment.pipe(
      shareReplay(1)).subscribe(() => {
        this.getPaymentCardsList();
        this.add('Payment method updated');
      })
  }
  openDetailsModal(order: any) {
    const initialState: ModalOptions = {
      class: 'ws-right-modal',
      initialState: {
        name: order.bluelabelName,
        kit: order
      },
    };
    this.bsModalRef = this.dialog.show(
      WsWebKitDetailsComponent,
      initialState
    );
  }
openImage(url: string) {
  if (!url) {
    return '';
  }
  const sanitizedUrl = this.paymentService.imgUrl(url);
  return this.sanitizer.bypassSecurityTrustUrl(sanitizedUrl);
}

  openPasswordModal(client: any, edit: boolean) {
    const initialState: ModalOptions = {
      class: 'ws-right-modal',
      initialState: {
        edit: edit,
        credentials: client,
        changeCred: edit ? client : ''
      },
    };
    this.bsModalRef = this.dialog.show(
      WsEditCredentialsComponent,
      initialState
    );
  }
  
  getExternalStores() {
    this.hasCredentialsList = [];
    this.paymentService.getExternalClientStores().pipe(
      shareReplay(1)).subscribe((res: any) => {
      this.shippingConfirmations = res.shippingConfirmations === 'true';
      this.externalStores = res.externalClientStores.filter((item: any) => item.allowGenerateCredentials === 'true');
      res.externalClients.forEach((client: any) => {
        let i = this.externalStores.findIndex((item) => item.externalClientStoreId === client.externalClientStoreId);
        if(i !== -1) {
          this.externalStores[i] = {...this.externalStores[i], client};
          this.hasCredentialsList.push(this.externalStores[i]);
          this.externalStores.splice(i, 1);
        } 
      });
      this.cdRef.detectChanges();
    })
  }
  getCredentials() {
    if(this.credentialSelected === -1) return;
    const payload = {
      "sapCustomerId": this.sapCustomer,
      "externalClientStoreId": this.externalStores[this.credentialSelected]?.externalClientStoreId
  }
  this.paymentService.addNewCredential(payload).subscribe((res) => {
    this.openPasswordModal(res, true);
    this.getExternalStores();
    this.credentialSelected = -1;
    this.add('Store is created')
  })
  }
  sendConfimation(ev: any) { 
      const req = {
        "sapCustomerId": this.sapCustomer,
        "shippingConfirmations": ev.target.checked
    } 
      this.paymentService.sendEmailConfirmation(req).subscribe();
      this.add('Success');        
  }
  getPendingOrders() {
    this.paymentService.getPendingOrders().pipe(
      shareReplay(1)).subscribe((res: any) => {
        this.pendingOrders = this.initialOrders = res.orders;
        this.cdRef.detectChanges();
    })
  }
  getCartonsList() {
    this.paymentService.getPrivateLabelCartons().pipe(
      shareReplay(1)).subscribe((res: any) => {
        this.webCartons = res.medpaxCartonList.filter((item: any) => !item.defaultCarton);
        this.cdRef.detectChanges();
    })
  }
  getWebKits() {
    this.paymentService.getPrivateLabelWebkits().pipe(
      shareReplay(1)).subscribe((res: any) => {
        this.webKits = res.medpaxKitList;
        this.cdRef.detectChanges();
    })
  }
  getPaymentCardsList() {
    this.loader = true;
    this.paymentService.getActivePaymentSettings().pipe(
      tap(payment => this.defaultPayment = payment),
      switchMap(() => this.paymentService.getPayments()),
      map((res:any) => res?.cartData?.item),
      shareReplay(1)
    ).subscribe(cards => {
      this.cards$ = cards;
      this.loader = false;
      this.cdRef.detectChanges();
    });
  }
  add(message:any): void {
    this.alerts.push({
      type: 'success',
      msg: message,
      timeout: 2000,
    });
  }

  onClosed(dismissedAlert: any): void {
    this.alerts = this.alerts.filter((alert) => alert !== dismissedAlert);
  }
  searchOrder(ev: any) {
    if(ev.target.value) {
      this.pendingOrders = this.initialOrders.filter((item) => item.orderNumber.toLowerCase().includes(ev.target.value.toLowerCase()));
    } else {
      this.pendingOrders = this.initialOrders;
    }
    this.cdRef.detectChanges();
  }
  goToPage(url: string) {
    window.open(url, "_blank");
  }
}

