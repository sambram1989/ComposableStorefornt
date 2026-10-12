import { Component, Input, input, SimpleChanges, signal } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'ws-custom-medpax-right-container',
  templateUrl: './custom-medpax-right-container.component.html',
  styleUrl: './custom-medpax-right-container.component.scss'
})
export class CustomMedpaxRightContainerComponent {
  @Input() isVisibleActions: boolean = true;
  @Input() isPlUser = false;
  medPaxData = input<any>({});
  isMedPaxAvailable = input(false);
  isRightContainerPopup = input(false);
  productCodes: string[] = [];
  productName: string[] = [];
  isSupplementVisible = signal<boolean>(false);
  isMedpaxPage: boolean = this.router.url.includes('/medpax') && this.router.url.includes('from=cart');
  toggleAccordion = false;
  
  constructor(protected router: Router) {}

  ngOnChanges(changes: SimpleChanges) {
    if (changes['medPaxData'] && this.medPaxData()) {
      this.extractProductDetails();

      //Code to redirect the page to medpax when editing medpax coming from cart, else after removing all products it's not adding new products to it
      const prevValue = changes['medPaxData'] && changes['medPaxData']['previousValue'] && changes['medPaxData']['previousValue']['medpaxEntry'];
      const currValue = changes['medPaxData'] && changes['medPaxData']['currentValue'] && changes['medPaxData']['currentValue']['medpaxEntry'];
      if (prevValue && prevValue.length > 0 && !currValue && this?.isMedpaxPage) {
        this.router.navigate(['/medpax']);
      }
    }
  }

  extractProductDetails() {
    const medpaxEntries = this.medPaxData()?.medpaxEntry || [];
    if (medpaxEntries.length === 0) {
      this.productCodes = [];
      this.productName = [];
      return;
    }
    this.productCodes = medpaxEntries.map((entry: any) => entry.product?.code || '');
    this.productName = medpaxEntries.map((entry: any) => entry.product?.name || '');
  }

  toggleSupplementFacts() {
    this.toggleAccordion = !this.toggleAccordion;
    this.isSupplementVisible.set(!this.isSupplementVisible());
  }
}
