import { Component, inject, input, output } from '@angular/core';
import { Location } from '@angular/common';
import { ActivatedRoute} from '@angular/router';
@Component({
  selector: 'ws-custom-medpax-floating-footer',
  templateUrl: './custom-medpax-floating-footer.component.html',
  styleUrl: '../../custom-medpax/custom-medpax-landing-page/custom-medpax-landing-page.component.scss'
})
export class CustomMedpaxFloatingFooterComponent {
  location = inject(Location);
  private activatedRoute = inject(ActivatedRoute);
  flowType = input<string>('');
  selectedProductCodes = input<any>({});
  medPaxProducts = input<any>({});
  recommendationEntries = input<any>({});
  proptocolEntries = input<any>({});
  plUser = input<boolean>(false);
  isMedPaxAvailable = input<boolean>(false);
  openViewPopup = output<any>({});
  currentUrlId = '';
  from: string;

  ngOnInit() {
    this.currentUrlId = this.activatedRoute.snapshot.paramMap.get('id');
    this.from = this.activatedRoute.snapshot.queryParams['from'];
  }
}
