import { Component } from '@angular/core';
import { WsPage } from '../../../ws-services/ws-page.service';
import { slotMarkup, TESTIMONIALS_SLOT } from '../../../ws-models/page.model';
import { Observable } from 'rxjs';

@Component({
  selector: 'ws-homepage-testimonials',
  templateUrl: './homepage-testimonials.component.html',
  styleUrl: './homepage-testimonials.component.scss',
})
export class HomePageTestimonialsComponent {
  slides = [1, 2, 3];
  slideConfig = {
    slidesToShow: 1.69,
    slidesToScroll: 1,
    dots: true,
    infinite: true,
    arrows: false,
    dotsClass: 'slick-dots',
    centerPadding: '25%',
  };

  testimonialsContentSlot = new Observable<slotMarkup>;
  testimonialsContent: { [k: string]: any } = {};

  constructor(private wsPageService: WsPage) {

  }

  getTestimonials() {
    this.testimonialsContentSlot.subscribe((response) => {
      console.log(response)
      response.components.forEach((element: any) => {
        if (TESTIMONIALS_SLOT.includes(element.uid)) {
          this.testimonialsContent[element.uid] = element;
        }
      });
    })
  }
}
