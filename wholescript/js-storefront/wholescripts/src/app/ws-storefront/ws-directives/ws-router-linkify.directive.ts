import {
  Directive,
  ElementRef,
  Renderer2,
  AfterViewInit,
  ChangeDetectorRef,
} from '@angular/core';
import { Router } from '@angular/router';

@Directive({
  selector: '[appRouterLinkify]',
})
export class RouterLinkifyDirective implements AfterViewInit {
  constructor(
    private el: ElementRef,
    private renderer: Renderer2,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  ngAfterViewInit() {
    this.cdr.detectChanges();

    setTimeout(() => {
      const anchors = this.el.nativeElement.querySelectorAll('a');

      anchors.forEach((anchor: HTMLAnchorElement) => {
        const href = anchor.getAttribute('href');

        if (href && href.startsWith('/')) {
          this.renderer.removeAttribute(anchor, 'href');

          this.renderer.listen(anchor, 'click', (event) => {
            event.preventDefault();
            this.router.navigateByUrl(href);
          });

          anchor.style.cursor = 'pointer';
        }
      });
    }, 100);
  }
}
