import { Component, Input } from '@angular/core';

@Component({
  selector: 'ws-product-status',
  templateUrl: './ws-product-status.component.html',
  styleUrl: './ws-product-status.component.scss'
})
export class WsProductStatusComponent {
  @Input() product: any;

  constructor() {}
}
