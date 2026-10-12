import { HttpClient } from "@angular/common/http";
import { inject, Injectable } from "@angular/core";
import { CheckoutStepService } from "@spartacus/checkout/base/components";
import { OccEndpointsService } from "@spartacus/core";
import { GET_PAYMENT_DETAILS } from "../../ws-constants/ws-api-endpoint.constant";
import { WsCheckoutPaymentService } from "./ws-checkout-payment.service";
@Injectable({
  providedIn: 'root',
})
export class wsCheckoutStepService extends CheckoutStepService {
  
  private http = inject(HttpClient);
  private occ = inject(OccEndpointsService);
  private paymentService = inject(WsCheckoutPaymentService);
  
  private finalStepNavigationEnabled = false;

  enableFinalStepNavigation(): void {
    this.finalStepNavigationEnabled = true;
  }
  
  disableFinalStepNavigation(): void {
    this.finalStepNavigationEnabled = false;
  }
  
  isFinalStepNavigationEnabled(): boolean {
    return this.finalStepNavigationEnabled;
  }
  getStepUrlFromStepRouteClone(stepRoute: string): string | null {
    return (
      this.routingConfigService.getRouteConfig(stepRoute)?.paths?.[0] ?? null
    );
  }
  goToFinalStep(): void {
    const finalStep = this.allSteps[this.allSteps.length - 1]; 
    if (finalStep && !finalStep.disabled) {
      const finalStepUrl = this.getStepUrlFromStepRouteClone(finalStep.routeName);
      console.debug('Final step is not available or is disabled.',finalStep.routeName);
      this.routingService.go(finalStepUrl);
      // this.router.navigateByUrl('checkout/ReviewOrder');
    } else {
      console.debug('Final step is not available or is disabled.');
    }
  }

  getPaymentDetails(){
    return this.http.get(this.occ.buildUrl(GET_PAYMENT_DETAILS(this.paymentService.getCartIdFromLocalStorage())))
  }

}