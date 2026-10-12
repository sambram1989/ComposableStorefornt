import {
  HTTP_INTERCEPTORS,
  provideHttpClient,
  withFetch,
  withInterceptorsFromDi,
} from '@angular/common/http';
import { inject, NgModule } from '@angular/core';
import { A11yModule } from '@angular/cdk/a11y';
import { BrowserModule } from '@angular/platform-browser';
import { EffectsModule } from '@ngrx/effects';
import { StoreModule } from '@ngrx/store';
import {
  AppRoutingModule,
  OutletRefModule,
  PageLayoutModule,
  SeoMetaService,
} from '@spartacus/storefront';
import { AppComponent } from './app.component';
import { SpartacusModule } from './spartacus/spartacus.module';
import { WsPractitionerMyPatientsModule } from './ws-storefront/ws-components/ws-practitioner/ws-My-patients/my-patients.module';
import { WsSharedModule } from './ws-storefront/ws-components/ws-shared/ws-shared.module';
import { WsBreadcrumbModule } from './ws-storefront/ws-components/ws-spartacus/ws-breadcrumb/breadcrumb/ws-breadcrumb.module';
import { ProtocolModule } from './ws-storefront/ws-components/protocal/protocol.module';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { HomePageModule } from './ws-storefront/ws-components/ws-homepage-guest/home-page.module';
import { BsDropdownModule } from 'ngx-bootstrap/dropdown';
import { ModalModule } from "ngx-bootstrap/modal";
import { WsStorefrontModule } from "./ws-storefront/ws-storefront/ws-storefront.module";
import { WsNavigationModule } from "./ws-storefront/ws-components/ws-navigation/ws-navigation.module";
import { RecommendationModule } from "./ws-storefront/ws-components/ws-recommendations/recommendation.module";
import { WsOccModule } from "./ws-storefront/ws-occ/ws-occ.module";
import { AuthRedirectService, AuthRedirectStorageService, BadRequestHandler, I18nConfig, provideConfig, RoutingConfig } from '@spartacus/core';
import { translations } from '../assets/translations/translations';
import { translationChunksConfig } from '../assets/translations/translation-chunks-config';
import { FAQModule } from './ws-storefront/ws-components/ws-faq-page/faq.module';
import { ProductUpdatesModule } from './ws-storefront/ws-components/ws-product-updates-page/prduct-updates.module';
import { ContactUsModule } from './ws-storefront/ws-components/ws-contact-us-page/ws-contact-us.module';
import { WsAdvertisingModule } from './ws-storefront/ws-components/ws-advertising/ws-advertising.module';
import { PatientmyaccountModule } from './ws-storefront/ws-components/patient-my-account/patientmyaccount.module';
import { WhyMedPaxPageModule } from './ws-storefront/ws-components/ws-why-med-pax-page/ws-why-med-pax.module';
import { WsProtocolModule } from './ws-storefront/ws-components/ws-protocols/ws-protocol.module';
import { WsAccountSettingsModule } from './ws-storefront/ws-components/ws-account-settings/ws-account-settings.module';
import { WsPatientDirectHubModule } from './ws-storefront/ws-components/patient-direct-hub/patient-direct-hub.module';
import { WsFavoritesModule } from './ws-storefront/ws-components/ws-favorites/ws-favorites.module';
import { provideToastr, ToastrModule } from 'ngx-toastr';
import { WsPatientFindPractitionerModule } from './ws-storefront/ws-components/ws-patient-find-practitioner/ws-patient-find-practitioner.module';
import { WsWebinarsModule } from './ws-storefront/ws-components/ws-webinars/wswebinar.module';
import { WsGigyaRaasModule } from './ws-storefront/ws-gigya/ws-gigya.module';
import { WsSearchModule } from './ws-storefront/ws-components/ws-search/ws-search.module';
import { WsPdpModule } from './ws-storefront/ws-components/ws-pdp/ws-pdp.module';
import { CustomMedpaxModule } from './ws-storefront/ws-components/custom-medpax/custom-medpax.module';
import { WsPlpModule } from './ws-storefront/ws-components/ws-plp/ws-plp.module';
import { WsOrdersModule } from './ws-storefront/ws-components/ws-order/ws-orders.module';
import { WsCartModule } from './ws-storefront/ws-components/ws-cart/wsCart.module';
import { WsOrderHistoryModule } from './ws-storefront/ws-components/ws-order-history/ws-order-history.module';
import { WsCheckoutDeliveryAddressModule } from './ws-storefront/ws-components/ws-checkout-delivery-address/ws-checkout-delivery-address.module';
import { WsOrderDetailsModule } from './ws-storefront/ws-components/ws-order-details/ws-order-details.module';
import { WsCheckoutPaymentMethod } from './ws-storefront/ws-components/ws-checkout/ws-checkout.module';
import { CartNotEmptyGuard, CheckoutStepService } from '@spartacus/checkout/base/components';
import { wsCheckoutStepService } from './ws-storefront/ws-services/ws-checkout/ws-checkout.service';
import { WsCheckoutStepsSetGuard } from './ws-storefront/ws-services/ws-checkout/ws-checkout.guard';
import { CheckoutB2BStepsSetGuard } from '@spartacus/checkout/b2b/components';
import { WsPractitionerPatientProfileModule } from './ws-storefront/ws-components/ws-practitioner-patient-profile/ws-practitioner-patient-profile.module';
import { WsPractitionerDashBoard } from './ws-storefront/ws-components/ws-practitioner-dashboard/ws-practitioner-dashboard.module';
import { WsAuthRedirectService } from './ws-storefront/ws-services/ws-login-redirection-auth-redirect/ws-auth-redirect.service';
import { WsAutoshipsModule } from './ws-storefront/ws-components/ws-autoships/ws-autoships.module';
import { WsMyAccountModule } from './ws-storefront/ws-components/ws-my-account/ws-my-account.module';
import { WsFooterModule } from './ws-storefront/ws-components/ws-footer/ws-footer.module';

import { WsPrivateLabelModule } from './ws-storefront/ws-components/ws-private-label/ws-private-label.module';
import { WsIntegrationsHubModule } from './ws-storefront/ws-components/ws-integrations-hub/ws-integrations-hub.module';
import { WsOccCheckoutDeliveryModesAdapter } from './ws-storefront/ws-services/ws-checkout/ws-checkout-delivery-modes/ws-checkout-delivery-mode.adapter';
import { CheckoutDeliveryModesAdapter } from '@spartacus/checkout/base/core';
import { WsSeoMetaService } from './ws-storefront/ws-services/seo/ws-seo.service';
import { WsSpecialOffersModule } from './ws-storefront/ws-components/ws-special-offers/ws-special-offers.module';
import { UserInterceptor } from './ws-storefront/ws-interceptors/userId.interceptor';
import { WsAccertifyVerifyUserModule } from './ws-storefront/ws-components/ws-accertify-verify-landing-page/ws-accertify-verify-landing.module';
import { WsAuthRedirectStorageService } from './ws-storefront/ws-services/ws-login-redirect/ws-login-redirect.service';
import { OrderConfirmationGuard } from '@spartacus/order/components';
import { WsOrderConfirmationGuard } from './ws-storefront/ws-services/ws-order/ws-order-confirmation.guard';
import { WsAsmModule } from './ws-storefront/ws-asm/ws-asm.module';
import { WsErrorPageModule } from './ws-storefront/ws-components/ws-error-page/ws-error-page.module';
import { authConfig } from './ws-storefront/ws-config/config/auth.config';
import { WsClientTokenInterceptor } from './ws-storefront/ws-interceptors/client-token.interceptor';
import { WsAboutUsModule } from './ws-storefront/ws-components/ws-about-us/ws-about-us.module';
import { AsmAutoLoginService } from './ws-storefront/ws-asm/asm-auto-login.service';
import { ErrorInterceptor } from './ws-storefront/ws-interceptors/error.interceptor';
import { IntegrationCardBadgeModule } from './ws-storefront/ws-components/integration-card-badge/integration-card-badge.module';
import { IntegrationCardContentModule } from './ws-storefront/ws-components/integration-card-content/integration-card-content.module';
import { WsCartNotEmptyGuard } from './ws-storefront/ws-services/ws-checkout/ws-cart-not-empty.guard';
import { CartPageLayoutHandler } from '@spartacus/cart/base/components';
import { CustomCartPageLayoutHandler } from './ws-storefront/ws-components/ws-cart/custom-cart-page-layout-handler';
import { CustomBadRequestHandler } from './ws-storefront/ws-interceptors/bad-request.handler';


const CustomModules = [
  WsStorefrontModule,
  WsSharedModule,
  HomePageModule,
  WsPractitionerMyPatientsModule,
  WsBreadcrumbModule,
  ProtocolModule,
  RecommendationModule,
  ModalModule.forRoot(),
  BsDropdownModule.forRoot(),
  WsNavigationModule,
  WsOccModule,
  FAQModule,
  ProductUpdatesModule,
  ContactUsModule,
  WsAdvertisingModule,
  WsAccountSettingsModule,
  PatientmyaccountModule,
  WhyMedPaxPageModule,
  WsProtocolModule,
  WsPatientDirectHubModule,
  WsFavoritesModule,
  WsPatientFindPractitionerModule,
  WsWebinarsModule,
  WsGigyaRaasModule,
  WsSearchModule,
  WsPdpModule,
  CustomMedpaxModule,
  WsPlpModule,
  WsCheckoutDeliveryAddressModule,
  WsOrdersModule,
  WsCartModule,
  WsOrderHistoryModule,
  WsOrderDetailsModule,
  WsCheckoutPaymentMethod,
  WsPractitionerPatientProfileModule,
  WsAutoshipsModule,
  WsPrivateLabelModule,
  WsIntegrationsHubModule,
  IntegrationCardBadgeModule,
  IntegrationCardContentModule,
  WsSpecialOffersModule,
  WsFooterModule,
  ToastrModule.forRoot({
    timeOut: 8000,
  }),
  WsPractitionerDashBoard,
  WsMyAccountModule,
  WsAccertifyVerifyUserModule,
  WsErrorPageModule,
  WsAsmModule,
  WsAboutUsModule
];
const sapModules = [PageLayoutModule, OutletRefModule];

@NgModule({
  declarations: [AppComponent],
  imports: [
    BrowserModule,
    A11yModule,
    StoreModule.forRoot({}),
    AppRoutingModule,
    EffectsModule.forRoot([]),
    SpartacusModule,
    BrowserAnimationsModule,
    ...CustomModules,
    ...sapModules,
  ],
  providers: [
    provideHttpClient(withFetch(), withInterceptorsFromDi()),
    provideAnimationsAsync(),
    provideConfig(<I18nConfig>{
      i18n: {
        resources: translations,
        chunks: translationChunksConfig,
        fallbackLang: 'en',
      },
    }),
    provideConfig(<RoutingConfig>{
      routing: {
        routes: {
          register: { paths: ['sign-up'] },
          checkoutDeliveryAddress: { paths: ['checkout/ShippingAddresses'] },
          checkoutDeliveryMode: { paths: ['checkout/ShippingMethods'] },
          checkoutPaymentDetails: { paths: ['checkout/PaymentMethods'] },
          checkoutReviewOrder: { paths: ['checkout/ReviewOrder'] },
          orderConfirmation: { paths: ['checkout/completed'] },
        }
      }
    }),

    provideToastr(),
    {
      provide: CheckoutB2BStepsSetGuard,
      useClass: WsCheckoutStepsSetGuard,
    },
    {
      provide: CartNotEmptyGuard,
      useClass: WsCartNotEmptyGuard,
    },
    {
      provide: CheckoutStepService,
      useClass: wsCheckoutStepService,
    },
    {
      provide: AuthRedirectService,
      useClass: WsAuthRedirectService
    },
    {
      provide: CheckoutDeliveryModesAdapter,
      useClass: WsOccCheckoutDeliveryModesAdapter
    },
    {
      provide: SeoMetaService,
      useClass: WsSeoMetaService
    },{
      provide:AuthRedirectStorageService,
      useClass:WsAuthRedirectStorageService
    },
    {
      provide: HTTP_INTERCEPTORS,
      useClass: UserInterceptor,
      multi: true,
    },
    { 
      provide: HTTP_INTERCEPTORS, 
      useClass: ErrorInterceptor, 
      multi: true 
    },
    {
      provide: HTTP_INTERCEPTORS,
      useExisting: WsClientTokenInterceptor,
      multi: true,
    },
    {
      provide: BadRequestHandler,
      useExisting: CustomBadRequestHandler
    },
    {
      provide: CartPageLayoutHandler,
      useClass: CustomCartPageLayoutHandler,
    },
    { provide: OrderConfirmationGuard, useClass: WsOrderConfirmationGuard },
    provideConfig(authConfig), 
  ],
  bootstrap: [AppComponent],
})
export class AppModule {
  private asmAutoLoginService = inject(AsmAutoLoginService);
  constructor(){
    this.asmAutoLoginService.autoLoginAsmAgent();
  }
 }
