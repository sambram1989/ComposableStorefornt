import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import {
  CmsConfig,
  ConfigModule,
  I18nModule,
  provideDefaultConfig,
} from '@spartacus/core';
import { LayoutConfig, MediaModule } from '@spartacus/storefront';
import { WsRegisterComponent } from './ws-register/ws-register.component';
import { RouterModule } from '@angular/router';
import { WsSpinnerModule } from '../ws-components/ws-spartacus/spinner/ws-spinner.module';
import { WsGigyaRaasComponent } from './ws-raas/ws-rass.component';

@NgModule({
  imports: [
    CommonModule,
    I18nModule,
    MediaModule,
    RouterModule,
    WsSpinnerModule,
    ConfigModule.withConfig(<CmsConfig | LayoutConfig>{
      layoutSlots: {
        registrationPageTemplate: {
          slots: [
            'SiteLogo',
            'topSlot',
            'leftBodySlot',
            'resettopSlot',
            'cdcLoginleftBodySlot',
            'patientleftBodySlot',
            'practitionerleftBodySlot',
            'BodyContent-gigya-patient-register',
            'BodyContent-gigya-forgot-password',
            'BodyContent-gigya-Reset-password',
            'BodyContent-gigya-practitioner-register',
            'BodyContent-gigya-login',
            'rightBodySlot',
            'patientrightBodySlot',
            'practitionerrightBodySlot',
            'cdcLoginrightBodySlot',
            'footerSlot',
            'resetfooterSlot'
          ],
        },
        cdcLoginPageTemplate: {
          slots: [
            'cdcLoginrightBodySlot',
            'cdcLoginleftBodySlot',
            'BodyContent-gigya-login'
          ]
        },
        forgotPasswordPageTemplate: {
          slots: [
            'topSlot',
            'footerSlot',
            'BodyContent-gigya-patient-register',
            'BodyContent-gigya-forgot-password'
          ]
        },
        resetPasswordPageTemplate: {
          slots: [
            'BodyContent-gigya-Reset-password'
          ]
        },
        patientRegistrationPageTemplate: {
          slots: [
            'BodyContent-gigya-patient-register',
            'patientrightBodySlot',
            'patientleftBodySlot'
          ]
        },
        practiotionerRegistrationPageTemplate: {
          slots: [
            'BodyContent-gigya-practitioner-register',
            'practitionerrightBodySlot',
            'practitionerleftBodySlot'
          ]
        }
      },
      cmsComponents: {
        GigyaRaasComponent: { component: WsGigyaRaasComponent },
      }
    }),
  ],
  declarations: [WsRegisterComponent, WsGigyaRaasComponent],
  providers: [
    provideDefaultConfig(<CmsConfig>{
      cmsComponents: {
        registerflexComponent: {
          component: WsRegisterComponent,
        },

          GigyaRaasComponent: { component: WsGigyaRaasComponent },
        
      },
    })
  ],
  exports: [WsRegisterComponent,WsGigyaRaasComponent],
})
export class WsGigyaRaasModule {}
