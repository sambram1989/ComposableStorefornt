import { ChangeDetectorRef, Component, inject, signal } from '@angular/core';
import { AuthService, CmsService } from '@spartacus/core';
import { Observable, tap } from 'rxjs';
import { WsCurrentUserService } from '../../../ws-services/state-services/currentuser.service';
import { ActiveCartFacade } from '@spartacus/cart/base/root';

@Component({
  selector: 'ws-cartboxstyle',
  templateUrl: './cartboxstyle.component.html',
  styleUrl: './cartboxstyle.component.scss'
})

export class CartboxstyleComponent {
  authService = inject(AuthService);
  private cmsService = inject(CmsService);
  private currentUser = inject(WsCurrentUserService);
  private activeCartFacade = inject(ActiveCartFacade);
  isPatient = signal<boolean>(false);
  shippingMessage: string = '';
  PATIENT_SHIPPING_PRICE: number = 100;
  PRACTITIONER_SHIPPING_PRICE: number = 300;
  isCurrentUser$: Observable<any> = this.currentUser.isRoleExist('patient').pipe(tap((res: boolean) => this.isPatient.set(res)));
  cartTotalPrice: number;
  shippingPrice = 0;
  cartData: any;

  constructor(private cd: ChangeDetectorRef) {
    this.cmsService.getComponentData('PromotionsOrderParaComp').subscribe((data: any) => {

      this.shippingMessage = data?.content;
      this.shippingMessage = this.shippingMessage;
      this.cd.markForCheck();
    });
    this.activeCartFacade.getActive().subscribe(data => {
      this.cartData = data;
      this.cartTotalPrice = data?.subTotal?.value || 0;
      this.cd.markForCheck();
    })
  }
}
