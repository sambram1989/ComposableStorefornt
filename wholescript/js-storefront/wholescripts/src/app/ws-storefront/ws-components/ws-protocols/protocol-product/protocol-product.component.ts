import { Component, input } from '@angular/core';

@Component({
  selector: 'ws-protocol-product',
  templateUrl: './protocol-product.component.html',
  styleUrl: './protocol-product.component.scss'
})
export class ProtocolProductComponent {
  product = input<any>();
}
