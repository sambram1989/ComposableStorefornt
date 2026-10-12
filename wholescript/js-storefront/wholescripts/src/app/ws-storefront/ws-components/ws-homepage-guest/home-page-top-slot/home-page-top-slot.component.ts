import { ChangeDetectorRef, Component } from '@angular/core';
import * as homePageResponse from '../../../ws-content/homepage-guest.json';

@Component({
  selector: 'ws-home-page-top-slot',
  templateUrl: './home-page-top-slot.component.html',
  styleUrl: './home-page-top-slot.component.scss'
})
export class HomePageTopSlotComponent {
  setBulletVisibility: boolean = true;
  sectionData = homePageResponse.header;

  constructor(private cdr: ChangeDetectorRef) {
    setInterval(() => {
    this.setBulletVisibility =  this.setBulletVisibility ? false : true;
      this.cdr.detectChanges()
    }, 12000);
  }
}
