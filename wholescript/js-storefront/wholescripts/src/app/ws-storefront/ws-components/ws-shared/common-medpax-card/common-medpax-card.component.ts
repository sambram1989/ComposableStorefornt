import { Component, input } from '@angular/core';

@Component({
  selector: 'ws-common-medpax-card',
  templateUrl: './common-medpax-card.component.html',
  styleUrl: '../../../ws-components/custom-medpax/custom-medpax-landing-page/custom-medpax-landing-page.component.scss'
})
export class CommonMedpaxCardComponent {
  medPaxProducts = input<any>({});
}
