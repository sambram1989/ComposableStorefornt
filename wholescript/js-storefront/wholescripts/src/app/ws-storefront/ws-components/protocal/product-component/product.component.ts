import { Component, input } from '@angular/core';
import { weekMap } from '../../ws-shared/edit-dosingdetailspopup/editDosingConstants';

@Component({
  selector: 'ws-product',
  templateUrl: './product.component.html',
  styleUrl: './product.component.scss'
})
export class ProductComponent {
  product = input<any>();
  weekMapItems = weekMap;
}
