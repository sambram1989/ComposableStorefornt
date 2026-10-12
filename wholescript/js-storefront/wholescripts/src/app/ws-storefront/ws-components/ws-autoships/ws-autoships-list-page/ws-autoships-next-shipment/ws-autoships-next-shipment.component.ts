import { Component, Input, OnChanges, SimpleChanges } from '@angular/core';
import { Router } from '@angular/router';
import { CurrencyService } from '@spartacus/core';
import { Observable } from 'rxjs';

@Component({
  selector: 'ws-autoships-next-shipment',
  templateUrl: './ws-autoships-next-shipment.component.html',
  styleUrl: './ws-autoships-next-shipment.component.scss'
})
export class WsAutoshipsNextShipmentComponent {
  @Input() nextShipmentProducts: any[] = [];
  @Input() nextShipmendDate: any;
  @Input() nextShipmentTotalPrice: number;
  @Input() isPractioner: boolean = false;
  toggleListViewMoreFlag: boolean = true;
  totalPrice: number = 0;

  public Number = Number;
  currency$: Observable<string>;
  objectKeys = Object.keys;

  constructor(protected currencyService: CurrencyService, private router: Router) {
    this.currency$ = this.currencyService.getActive();
  }

  goToAutoShipDetails(orderNumber: number) {
    this.router.navigateByUrl('autoships-details?id=' + orderNumber)
  }

  onKeydown(event: KeyboardEvent, orderNumber: number): void {
    if (event.key === 'Enter' || event.key === ' ') {
      event.preventDefault();
      this.goToAutoShipDetails(orderNumber);
    }
  }  
}
