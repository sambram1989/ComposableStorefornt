import { Component, inject } from '@angular/core';
import { CmsService } from '@spartacus/core';

@Component({
  selector: 'ws-cart-heading',
  templateUrl: './cart-heading.component.html',
  styleUrl: './cart-heading.component.scss'
})
export class CartHeadingComponent {
  private cmsService = inject(CmsService);
  cartData$ = this.cmsService.getComponentData(
    'wsCartTopHeadingParagraphComponent'
  );
}
