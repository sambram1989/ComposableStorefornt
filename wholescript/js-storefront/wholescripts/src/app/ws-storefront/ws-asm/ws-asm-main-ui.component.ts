import { Component,OnInit } from '@angular/core';
import { AsmComponentService, AsmMainUiComponent } from '@spartacus/asm/components';
import { AsmService } from '@spartacus/asm/core';
import { AsmDeepLinkParameters, CsAgentAuthService } from '@spartacus/asm/root';
import { AuthService, GlobalMessageService, GlobalMessageType, RoutingService } from '@spartacus/core';
import { LaunchDialogService } from '@spartacus/storefront';
import { UserAccountFacade } from '@spartacus/user/account/root';
import { of, switchMap, take } from 'rxjs';

@Component({
  selector: 'cx-asm-main-ui',
  templateUrl: './ws-asm-main-ui.component.html'
})
export class WsAsmMainUiComponent extends AsmMainUiComponent implements OnInit {
  
  
  constructor(
    protected override authService: AuthService,
    protected override csAgentAuthService: CsAgentAuthService,
    protected override asmComponentService: AsmComponentService,
    protected override globalMessageService: GlobalMessageService,
    protected override routingService: RoutingService,
    protected override asmService: AsmService,
    protected override userAccountFacade: UserAccountFacade,
    protected override launchDialogService: LaunchDialogService
  ) {
    super(authService, csAgentAuthService, asmComponentService, globalMessageService, routingService, asmService, userAccountFacade, launchDialogService);
  }

  override ngOnInit(): void {
    super.ngOnInit();
  }

  override startCustomerEmulationSession(
    { customerId }: { customerId?: string },
    parameters?: AsmDeepLinkParameters
  ): void {
    if (customerId) {
      this.csAgentAuthService.startCustomerEmulationSession(customerId);
      this.startingCustomerSession = true;
      this.showCustomerEmulationInfoAlert = true;
      this.showCreateCustomerSuccessfullyAlert = false;
      if (parameters) {
        this.asmComponentService.handleDeepLinkNavigation({
          customerId,
          ...parameters,
        });
      }
      
    } else {
      this.globalMessageService.add(
        { key: 'asm.error.noCustomerId' },
        GlobalMessageType.MSG_TYPE_ERROR
      );
    }
  }
}
