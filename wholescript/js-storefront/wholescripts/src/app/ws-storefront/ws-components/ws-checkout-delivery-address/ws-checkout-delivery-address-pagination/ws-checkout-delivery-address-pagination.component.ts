import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  selector: 'ws-checkout-delivery-address-pagination',
  templateUrl: './ws-checkout-delivery-address-pagination.component.html',
  styleUrl: './ws-checkout-delivery-address-pagination.component.scss'
})
export class WsCheckoutDeliveryAddressPaginationComponent {

  @Input() currentPage: number = 0;
  @Input() totalPageCount: number = 0;
  @Output() setCurrentPage = new EventEmitter<number>();

  updatePage(page: number): void {
    this.setCurrentPage.emit(page);
  }

  counter(i: number) {
    return new Array(i);
  }

}
