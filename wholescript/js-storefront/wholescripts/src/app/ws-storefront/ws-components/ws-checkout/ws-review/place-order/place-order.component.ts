import { Component, OnInit, ViewContainerRef } from '@angular/core';
import { UntypedFormBuilder } from '@angular/forms';
import { CheckoutPlaceOrderComponent } from '@spartacus/checkout/base/components';
import { RoutingService } from '@spartacus/core';
import { OrderFacade } from '@spartacus/order/root';
import { LAUNCH_CALLER, LaunchDialogService } from '@spartacus/storefront';
import { WsAccertifyService } from '../../../../ws-services/ws-checkout/ws-accertify.service';


@Component({
  selector: 'ws-place-order',
  templateUrl: './place-order.component.html',
  styleUrl: './place-order.component.scss'
})
export class WsPlaceOrderComponent extends CheckoutPlaceOrderComponent implements OnInit {

  constructor(
    protected override orderFacade: OrderFacade,
    protected override routingService: RoutingService,
    protected override fb: UntypedFormBuilder,
    protected override launchDialogService: LaunchDialogService,
    protected override vcr: ViewContainerRef,
    private wsAccertifyService: WsAccertifyService
  ) {
    super(orderFacade, routingService, fb, launchDialogService, vcr);
  }

  ngOnInit(): void {
    this.wsAccertifyService.loadAccertifyCollectorScript().then(() => {
      console.log('Accertify script loaded successfully ');
    }).catch(error => {
      console.error('Error loading Accertify script:', error);
    });;
  }

 override submitForm(): void {
    if (this.checkoutSubmitForm.valid) {
      this.placedOrder = this.launchDialogService.launch(
        LAUNCH_CALLER.PLACE_ORDER_SPINNER,
        this.vcr
      );
     
      this.orderFacade.placeOrder(this.checkoutSubmitForm.valid).subscribe({
        error: () => {
          if (!this.placedOrder) {
            return;
          }

          this.placedOrder
            .subscribe((component) => {
              this.launchDialogService.clear(LAUNCH_CALLER.PLACE_ORDER_SPINNER);
              if (component) {
                component.destroy();
              }
            })
            .unsubscribe();
        },
        next: () => this.onSuccess(),
      });
    } else {
      this.checkoutSubmitForm.markAllAsTouched();
    }
  }
  goBack(): void {
    this.routingService.go('checkout/PaymentMethods')
  }
}
