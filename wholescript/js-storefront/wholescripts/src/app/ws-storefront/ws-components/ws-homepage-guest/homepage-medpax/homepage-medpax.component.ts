import { Component, DestroyRef, inject, signal } from '@angular/core';
import { slotMarkup, MEDPAX_CONTENT_SLOT, PRIMARY_SLOT } from '../../../ws-models/page.model';
import { Observable, tap } from 'rxjs';
import { WsPage } from '../../../ws-services/ws-page.service';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';

@Component({
  selector: 'ws-homepage-medpax',
  templateUrl: './homepage-medpax.component.html',
  styleUrl: './homepage-medpax.component.scss'
})
export class HomePageMedpaxComponent {
medPaxApi$:Observable<any>;
  medpaxContentSlot = new Observable<slotMarkup>;
  medpaxContent: { [k: string]: any } = {};
  medpaxContentSignal = signal<any>({});

  constructor(public wsPageService: WsPage) {
   
  }
ngOnInit(){
  this.medpaxContentSlot = this.wsPageService.getPage(PRIMARY_SLOT, 'MedPax');
  this.getMedPaxInfo();
}
  getMedPaxInfo() {
    this.medPaxApi$ =  this.medpaxContentSlot.
    pipe(tap((response:any)=>{
    response.components.forEach((element: any) => {
      if (MEDPAX_CONTENT_SLOT.includes(element.uid)) {
        this.medpaxContent[element.uid] = element;
      }
    });
    this.medpaxContentSignal.set(this.medpaxContent);
  }));
  }
}