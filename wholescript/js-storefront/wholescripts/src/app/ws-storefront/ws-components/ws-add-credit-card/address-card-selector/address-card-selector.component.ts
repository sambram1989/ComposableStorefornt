import { Component, input, Input, model } from '@angular/core';

@Component({
  selector: 'ws-address-card-selector',
  templateUrl: './address-card-selector.component.html',
  styleUrl: './address-card-selector.component.scss'
})
export class AddressCardSelectorComponent {
isActive = model<boolean>(false);
existingAddress = input<any>({});
}
