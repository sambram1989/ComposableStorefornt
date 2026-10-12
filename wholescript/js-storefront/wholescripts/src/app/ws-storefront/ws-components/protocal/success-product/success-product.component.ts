import { Component, Input } from '@angular/core';
import { weekMap } from '../../ws-shared/edit-dosingdetailspopup/editDosingConstants';

@Component({
  selector: 'ws-success-product',
  templateUrl: './success-product.component.html',
  styleUrl: './success-product.component.scss'
})
export class SuccessProductComponent {
  @Input() selectedProduct: any;
  weekMapItems = weekMap;
}
