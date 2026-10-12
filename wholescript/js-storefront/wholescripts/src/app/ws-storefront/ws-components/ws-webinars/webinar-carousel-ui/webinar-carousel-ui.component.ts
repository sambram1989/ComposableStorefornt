import { Component, inject, input, signal, SimpleChanges } from '@angular/core';
import { WsResponsiveService } from '../../../ws-services/ws-responsive.service';

@Component({
  selector: 'ws-webinar-carousel-ui',
  templateUrl: './webinar-carousel-ui.component.html',
  styleUrl: './webinar-carousel-ui.component.scss'
})
export class WebinarCarouselUiComponent {
  isTitleIcon = input<boolean>(true);
  isWebinarCard = input<boolean>(true);
  webinarData = input<any>({});
  presenterData = input<any>({});
  maxWidth = input<string>('350px');
  webinarTitle = input<string>('Meet The Presenters');
  titleIconColor = input<string>('#333');
  responsive = inject(WsResponsiveService);
  slides: any[] = [];
  visibleSlides = signal<number[]>([]);
  slidesPerPage = signal<number>(3);
  incomingSlidesPerPage = input<number>(0);
  currentSlide = signal<number>(0);
  indicators: number[] = [];
i: any;


  ngOnChanges(changes: SimpleChanges) {
    if (changes['webinarData'] || changes['presenterData']) {
      this.intialCarouselUpdate();
    }
  }

  intialCarouselUpdate() {
    this.slides = this.isWebinarCard() ? this.webinarData()||[] : this.presenterData()||[];
    if (this.responsive.smallWidth()) {
      this.slidesPerPage.set(2);
      this.updateSlides();
    } else if (this.responsive.xsmallWidth()) {
      this.slidesPerPage.set(1);
      this.updateSlides();
    } else {
      (this.incomingSlidesPerPage() > 0) ? this.slidesPerPage.set(this.incomingSlidesPerPage()) : this.slidesPerPage.set(3);
      this.updateSlides();
    }
  }

  updateSlides() {
    // Ensure we don't go beyond the bounds of slides
    const start = Math.max(0, this.currentSlide());
    const end = Math.min(this.slides.length, start + this.slidesPerPage());
    this.visibleSlides.set(this.slides.slice(start, end));

    // Adjust the number of indicators based on the number of slides and slides per page
    this.indicators = Array(Math.ceil(this.slides.length / this.slidesPerPage())).fill(0);
  }

  jumpToIndicator(index: number) {
    this.currentSlide.set(index * this.slidesPerPage());
    this.updateSlides();
  }

  nextSlide() {
    // Move to the next set of slides without exceeding the total number of slides
    if (this.currentSlide() + this.slidesPerPage() < this.slides.length) {
      this.currentSlide.set(this.currentSlide() + this.slidesPerPage());
    } else {
      this.currentSlide.set(0);
    }
    this.updateSlides();
  }

  prevSlide() {
    // Move to the previous set of slides, or loop back to the last set
    if (this.currentSlide() > 0) {
      this.currentSlide.set(this.currentSlide() - this.slidesPerPage());
    } else {
      this.currentSlide.set(this.slides.length - (this.slides.length % this.slidesPerPage() || this.slidesPerPage()));
    }
    this.updateSlides();
  }

  getSlideStyle() {
    return {
      '--slides-per-page': `${this.slidesPerPage()}`,
      width: `${this.slidesPerPage() * 100}%`,
      transition: 'transform 0.5s ease-in-out',
    };
  }

}
