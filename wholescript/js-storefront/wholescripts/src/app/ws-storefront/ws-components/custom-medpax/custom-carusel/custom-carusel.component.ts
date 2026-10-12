import { Component, ElementRef, Input, ViewChild } from '@angular/core';

@Component({
  selector: 'ws-custom-carusel-medpax',
  templateUrl: './custom-carusel.component.html',
  styleUrl: './custom-carusel.component.scss'
})
export class CustomCaruselComponent {
  @Input() slides: any[] = [];
  @ViewChild('cardContainer', { static: false }) cardContainer!: ElementRef<HTMLElement>;

  currentIndex = 0;
  cardsToShow = 2; // Number of cards to show at once
  observer!: IntersectionObserver;

  ngAfterViewInit() {
    this.initializeScrollSnap();
    this.updateArrowVisibility();
  }

  ngOnDestroy() {
    if (this.observer) {
      this.observer.disconnect();
    }
  }

  initializeScrollSnap() {
    this.observer = new IntersectionObserver((entries) => {
      entries.forEach(entry => {
        const index = Number(entry.target.getAttribute('data-index'));
        if (entry.isIntersecting) {
          this.currentIndex = index;
          this.updateArrowVisibility();
        }
      });
    }, { root: this.cardContainer.nativeElement, threshold: 0.5 });

    const cardElements = Array.from(this.cardContainer.nativeElement.children);
    cardElements.forEach((card) => {
      this.observer.observe(card as HTMLElement);
    });
  }

  get isLeftArrowVisible(): boolean {
    return this.currentIndex > 0; // Show left arrow only if not at the first index
  }

  get isRightArrowVisible(): boolean {
    return this.currentIndex + this.cardsToShow < this.slides.length; // Show right arrow if not at the last index
  }

  goToPrevious() {
    if (this.currentIndex > 0) {
      this.currentIndex--;
      this.scrollToCurrentIndex();
    }
  }

  goToNext() {
    if (this.currentIndex + this.cardsToShow < this.slides.length) {
      this.currentIndex++;
      this.scrollToCurrentIndex();
    }
  }

  scrollToCurrentIndex() {
    const container = this.cardContainer.nativeElement as HTMLElement;
    const cardWidth = container.children[0].clientWidth; // Get the width of a single card

    const targetScrollLeft = this.currentIndex * cardWidth;
    container.scrollTo({
      left: targetScrollLeft,
      behavior: 'smooth' // Use smooth scroll for better experience
    });
  }

  updateArrowVisibility() {
    // Update arrow visibility when the current index changes
    this.isLeftArrowVisible; // Triggers the getter for visibility
    this.isRightArrowVisible; // Triggers the getter for visibility
  }

  getSlides(){
    let filteredSlides = this.slides.filter((item, index, self) =>
      index === self.findIndex((t) => (
        t.name === item.name &&
        t.url === item.url &&
        t.src === item.src &&
        t.backgroundSrc === item.backgroundSrc
      ))
   );
   return filteredSlides;
  }
}

