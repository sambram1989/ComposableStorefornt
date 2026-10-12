/*
 * SPDX-FileCopyrightText: 2024 SAP Spartacus team <spartacus-team@sap.com>
 *
 * SPDX-License-Identifier: Apache-2.0
 */

import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import {
  CmsConfig,
  I18nModule,
  provideDefaultConfig,
  UrlModule,
} from '@spartacus/core';
import { PageSlotModule } from '@spartacus/storefront';
import { LoginComponent } from './login.component';
import { LoginSignupBtnComponent } from '../../../login-signup-btn/login-signup-btn.component';
import { WsNavigationModule } from '../../../ws-navigation/ws-navigation.module';

@NgModule({
  imports: [CommonModule, RouterModule, UrlModule, PageSlotModule, I18nModule, WsNavigationModule],
  providers: [
    provideDefaultConfig(<CmsConfig>{
      cmsComponents: {
        LoginComponent: {
          component: LoginComponent,
        },
      },
    }),
  ],
  declarations: [LoginComponent, LoginSignupBtnComponent],
  exports: [LoginComponent, LoginSignupBtnComponent],
})
export class LoginModule {}
