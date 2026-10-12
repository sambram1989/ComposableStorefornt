/*
 * SPDX-FileCopyrightText: 2024 SAP Spartacus team <spartacus-team@sap.com>
 *
 * SPDX-License-Identifier: Apache-2.0
 */

import { NgModule } from '@angular/core';
import { LoginFormModule, VerificationTokenFormModule, LoginRegisterModule, MyAccountV2UserModule, OneTimePasswordLoginFormModeule } from '@spartacus/user/account/components';
import { LoginModule } from './login/login.module';

@NgModule({
  imports: [
    LoginModule,
    LoginFormModule,
    VerificationTokenFormModule,
    LoginRegisterModule,
    MyAccountV2UserModule,
    OneTimePasswordLoginFormModeule,
  ],
})
export class UserAccountComponentsModule {}
