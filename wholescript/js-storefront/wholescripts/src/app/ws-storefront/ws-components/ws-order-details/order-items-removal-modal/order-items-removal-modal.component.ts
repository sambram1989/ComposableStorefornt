import { Component, EventEmitter, Input, Output } from '@angular/core';
import { BsModalRef } from 'ngx-bootstrap/modal';

@Component({
  selector: 'ws-order-items-removal-modal',
  templateUrl: './order-items-removal-modal.component.html',
  styleUrl: './order-items-removal-modal.component.scss'
})
export class OrderItemsRemovalModalComponent {
  @Input() outOfStockItems: any[] = [];

  @Output() confirmAddToCart = new EventEmitter<void>(); 
  constructor(public bsModalRef: BsModalRef) {}

  onConfirmClick() {
    this.confirmAddToCart.emit(); 
    this.bsModalRef.hide();       
  }

  close() {
    this.bsModalRef.hide();
  }
}
