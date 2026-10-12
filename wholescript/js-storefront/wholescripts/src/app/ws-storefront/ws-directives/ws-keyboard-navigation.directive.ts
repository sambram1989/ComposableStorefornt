import { Directive, HostListener, ElementRef } from '@angular/core';

@Directive({
  selector: 'a button' 
})
export class KeyboardNavigationDirective {
  constructor(private el: ElementRef) {}

  @HostListener('keydown', ['$event'])
  handleKeyboardEvent(event: KeyboardEvent): void {
    if (event.key === 'Enter' || event.key === ' ') {
      event.preventDefault(); 
      this.el.nativeElement.click();
    }
  }
}
