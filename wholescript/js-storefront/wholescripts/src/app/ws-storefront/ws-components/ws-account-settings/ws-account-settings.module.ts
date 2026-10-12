import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { WsAccountSettingsComponent } from './ws-account-settings/ws-account-settings.component';
import { CmsPageGuard, MediaModule, PageSlotModule } from '@spartacus/storefront';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RouterModule, Routes } from '@angular/router';
import { WsAsFindAPractitionerComponent } from './ws-as-find-a-practitioner/ws-as-find-a-practitioner.component';
import { AuthGuard } from '@spartacus/core';
import { WsEditReferralComponent } from './ws-edit-referral-code/ws-edit-referral-code.component';
import { WsConfirmReferralPopupComponent } from './ws-confirm-referral-popup/ws-confirm-referral-popup.component';
import { WsAsAddressesComponent } from './ws-as-addresses/ws-as-addresses.component';
import { WsAsAddressesPaginationComponent } from './ws-as-addresses/ws-as-addresses-pagination/ws-as-addresses-pagination.component';
import { WsAsAddressesNewAddressComponent } from './ws-as-addresses/ws-as-addresses-new-address/ws-as-addresses-new-address.component';
import { WsAsAddressesValidateAddressComponent } from './ws-as-addresses/ws-as-addresses-validate-address/ws-as-addresses-validate-address.component';
import { WsAsAddressesDeleteAddressComponent } from './ws-as-addresses/ws-as-addresses-delete-address/ws-as-addresses-delete-address.component';
import { WsAsNotificationsComponent } from './ws-as-notifications/ws-as-notifications.component';
import { WsAsChangePasswordComponent } from './ws-as-change-password/ws-as-change-password.component';
import { WsIdVerificationComponent } from './ws-id-verification/ws-id-verification.component';
import { WsVerificationPopupOneComponent } from './ws-id-verification/ws-verification-popup-one/ws-verification-popup-one.component';
import { WsSharedModule } from '../ws-shared/ws-shared.module';
import { WsPaymentSettingModule } from '../account-setting/ws-payment-settings.module';
import { PractititionerPaymentsTabComponent } from '../account-setting/practititioner-payments-tab/practititioner-payments-tab.component';
import { AccSettingPaymentComponent } from '../account-setting/acc-setting-payment/acc-setting-payment.component';
import { WsSpinnerModule } from '../ws-spartacus/spinner/ws-spinner.module';
import { WsAsMyPractitionerComponent } from './ws-as-my-practitioner/ws-as-my-practitioner.component';

const appRoute: Routes = [
  {
    path: 'accounts-settings',
    component: WsAccountSettingsComponent,
    canActivate: [CmsPageGuard, AuthGuard],
    children: [
      {
        path: '',
        component: WsAsAddressesComponent,
        data: {
          pageLabel: '/accounts-settings/Addresses',
          cxPath: 'accounts-settings',
        }
      },
      {
        path: 'FindAPractitioner',
        component: WsAsFindAPractitionerComponent,
        data: {
          pageLabel: '/accounts-settings/FindAPractitioner',
          cxPath: 'accounts-settings',
        }
      },
      {
        path: 'Addresses',
        component: WsAsAddressesComponent,
        data: {
          pageLabel: '/accounts-settings/Addresses',
          cxPath: 'accounts-settings',
        }
      },
      {
        path: 'IDVerification',
        component: WsIdVerificationComponent,
        data: {
          pageLabel: 'patient/accounts-settings/IDVerification',
          cxPath: 'accounts-settings',
        }
      },
      {
        path: 'Notifications',
        component: WsAsNotificationsComponent,
        data: {
          pageLabel: '/accounts-settings/Notifications',
          cxPath: 'accounts-settings',
        }
      },
      {
        path:'Payment',
        component:PractititionerPaymentsTabComponent,
        data:{
          pageLabel: '/accounts-settings/Payment',
          cxPath: 'accounts-settings',
        }
      },
      {
        path: 'Password',
        component: WsAsChangePasswordComponent,
        data: {
          pageLabel: '/accounts-settings/Password',
          cxPath: 'accounts-settings',
        }
      },
    ],
  },
  {
    path: 'patient/accounts-settings',
    component: WsAccountSettingsComponent,
    canActivate: [CmsPageGuard, AuthGuard],
    children: [
      {
        path: '',
        component: WsAsAddressesComponent,
        data: {
          pageLabel: 'patient/accounts-settings',
          cxPath: 'accounts-settings',
        }
      },
      {
        path: 'MyPractitioner',
        component: WsAsMyPractitionerComponent,
        data: {
          pageLabel: 'patient/accounts-settings/MyPractitioner',
          cxPath: 'accounts-settings',
        }
      },
      {
        path: 'Addresses',
        component: WsAsAddressesComponent,
        data: {
          pageLabel: 'patient/accounts-settings/Addresses',
          cxPath: 'accounts-settings',
        }
      },
      {
        path: 'IDVerification',
        component: WsIdVerificationComponent,
        data: {
          pageLabel: 'patient/accounts-settings/IDVerification',
          cxPath: 'accounts-settings',
        }
      },
      {
        path: 'Notifications',
        component: WsAsNotificationsComponent,
        data: {
          pageLabel: '/accounts-settings/Notifications',
          cxPath: 'accounts-settings',
        }
      },
      {
        path: 'Payment',
        component: AccSettingPaymentComponent,
        data: {
          pageLabel: 'patient/accounts-settings//Payment',
          cxPath: 'accounts-settings',
        }
      },
      
      {
        path: 'Password',
        component: WsAsChangePasswordComponent,
        data: {
          pageLabel: '/accounts-settings/Password',
          cxPath: 'accounts-settings',
        }
      },
    ],
  },
];
@NgModule({
  imports: [
    CommonModule,
    PageSlotModule,
    MediaModule,
    FormsModule,
    ReactiveFormsModule,
    WsSharedModule,
    WsPaymentSettingModule,
    WsSpinnerModule,
    RouterModule.forChild(appRoute),
  ],
  declarations: [
    WsAccountSettingsComponent,
    WsAsFindAPractitionerComponent,
    WsEditReferralComponent,
    WsConfirmReferralPopupComponent,
    WsAsNotificationsComponent,
    WsAsChangePasswordComponent,
    WsAsAddressesComponent,
    WsAsAddressesPaginationComponent,
    WsAsAddressesNewAddressComponent,
    WsAsAddressesValidateAddressComponent,
    WsAsAddressesDeleteAddressComponent,
    WsVerificationPopupOneComponent,
    WsIdVerificationComponent,
    WsAsMyPractitionerComponent
  ],
  exports: [WsAccountSettingsComponent, WsEditReferralComponent, WsAsAddressesComponent, 
    WsAsAddressesPaginationComponent, WsAsAddressesNewAddressComponent, WsAsAddressesValidateAddressComponent,
    WsAsAddressesDeleteAddressComponent]
})
export class WsAccountSettingsModule { }
