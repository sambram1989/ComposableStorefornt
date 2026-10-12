import { inject, NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { WsProtocolLandingComponent } from './ws-protocol-landing/ws-protocol-landing.component';
import { ProtocolModule } from '../protocal/protocol.module';
import { WsSharedModule } from '../ws-shared/ws-shared.module';
import { AuthGuard, provideConfig } from '@spartacus/core';
import { CmsPageGuard, PageSlotModule } from '@spartacus/storefront';
import { CanActivateFn, RouterModule, Routes } from '@angular/router';
import { ToastrModule } from 'ngx-toastr';
import { WsCurrentUserService } from '../../ws-services/state-services/currentuser.service';
import { AlertModule } from 'ngx-bootstrap/alert';
import { CustomMedpaxModule } from '../custom-medpax/custom-medpax.module';

const Practitioner: CanActivateFn = () => {
  const userState = inject(WsCurrentUserService);
  return userState.isRoleExist('practitioner');
};
const routes: Routes = [
  {
    path: 'wsprotocols/list',
    canActivate: [CmsPageGuard],
    component: WsProtocolLandingComponent,
  },
];
@NgModule({
  declarations: [
    WsProtocolLandingComponent,
  ],
  imports: [
    CommonModule,
    ProtocolModule,
    CustomMedpaxModule,
    WsSharedModule,
    PageSlotModule,
    RouterModule,
    ToastrModule.forRoot(),
    RouterModule.forChild(routes),
    AlertModule
  ],
  providers: [
    provideConfig({
      cmsComponents: {
        wsProtocolflexComponent: {
          component: () =>
            import('./ws-protocol-landing/ws-protocol-landing.component').then(
              (m) => m.WsProtocolLandingComponent),
          guards: [AuthGuard]
        },
      },
    }),
  ],
})
export class WsProtocolModule {}
