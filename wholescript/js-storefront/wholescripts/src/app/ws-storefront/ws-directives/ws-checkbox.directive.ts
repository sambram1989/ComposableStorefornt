import { Directive, ElementRef, HostBinding, HostListener, Input, OnChanges, Renderer2, SimpleChanges } from '@angular/core';
import { DomSanitizer } from '@angular/platform-browser';

@Directive({
  selector: '[wsCheckbox]'
})
export class HighlightCheckboxDirective {

  private wasChecked = false; // Track the previous state

  constructor(private el: ElementRef, private renderer: Renderer2) { }

  @HostListener('change', ['$event.target'])
  onChange(target: HTMLInputElement) {
    if (target.checked) {
      // Remove highlight when checked
      this.removeHighlight();
      this.wasChecked = true;
    } else {
      // Apply highlight when unchecked
      this.setHighlight();
      this.wasChecked = false;
    }
  }

  @HostListener('focus')
  onFocus() {
  const parentElement = this.el.nativeElement.parentElement;
  if (parentElement) {
    this.renderer.addClass(parentElement, 'focused');
  }
}

  @HostListener('blur')
  onBlur() {
    // Remove highlight on blur if it was previously unchecked
    if (!this.wasChecked) {
      this.removeHighlight();
    }
  }

  private setHighlight() {
    const parentElement = this.el.nativeElement.parentElement;
    if (parentElement) {
      this.renderer.addClass(parentElement, 'highlighted');
    }
  }

  private removeHighlight() {
    const parentElement = this.el.nativeElement.parentElement;
    if (parentElement) {
      this.renderer.removeClass(parentElement, 'highlighted');
    }
  }
}

@Directive({
  selector: '[wsMinLengthCheck]'
})
export class MinLengthCheckDirective {
  private readonly minLength: number = 1;

  constructor(private el: ElementRef, private renderer: Renderer2) {}

  @HostListener('input') onInput() {
    const value = (this.el.nativeElement as HTMLInputElement).value;
    if (value.length >= this.minLength) {
      this.renderer.addClass(this.el.nativeElement, 'has-min-length');
    } else {
      this.renderer.removeClass(this.el.nativeElement, 'has-min-length');
    }
  }
}

@Directive({
  selector: '[wsDisableLink]'
})
export class DisableLinkDirective implements OnChanges {
  @Input('wsDisableLink') disabled: boolean = false; 
  // @Input() routerLink: any; 
  @HostBinding('attr.aria-disabled') ariaDisabled: boolean;

  constructor(private sn: DomSanitizer) {}

  ngOnChanges(changes: SimpleChanges) {
    this.setDisabled();
  }

  setDisabled() {
    this.ariaDisabled = this.disabled;
  }

  @HostListener('click', ['$event'])
  handleClick(event: MouseEvent) {
    console.log("DIRECTIVE",this.disabled);
    if (this.disabled) {
      console.log("EVENT")
      event.preventDefault(); 
      event.stopPropagation(); 
      // return false; 
    }else{
      // return true;
    }
  }
}

