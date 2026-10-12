import {
    AfterViewInit,
    Component,
    ElementRef,
    ViewChild,
    HostListener,
  } from '@angular/core';
  
  @Component({
    selector: 'ws-autoship-carousel',
    templateUrl: './ws-autoship-carousel.component.html',
    styleUrls: ['./ws-autoship-carousel.component.scss'],
  })
  export class WsAutoshipCarouselComponent implements AfterViewInit {
    @ViewChild('container') containerRef!: ElementRef;
    @ViewChild('itemsContainer') itemsRef!: ElementRef;
  
    showScrollButtons = false;
    scrollAmount = 200;
  
    ngAfterViewInit(): void {
      this.updateScrollButtons();
    }
  
    @HostListener('window:resize')
    onResize() {
      this.updateScrollButtons();
    }
  
    updateScrollButtons(): void {
      const containerEl = this.containerRef.nativeElement;
      const itemsEl = this.itemsRef.nativeElement;
      this.showScrollButtons = itemsEl.scrollWidth > containerEl.clientWidth;
    }
  
    scrollLeft(): void {
      this.itemsRef.nativeElement.scrollBy({
        left: -this.scrollAmount,
        behavior: 'smooth',
      });
    }
  
    scrollRight(): void {
      this.itemsRef.nativeElement.scrollBy({
        left: this.scrollAmount,
        behavior: 'smooth',
      });
    }
  }
  