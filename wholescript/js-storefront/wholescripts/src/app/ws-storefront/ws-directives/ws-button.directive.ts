import { Directive, Input, Renderer2, ElementRef, OnInit, HostListener } from '@angular/core';
// background-color
@Directive({
  selector: '[wsBgC]'
})
export class BgColorDirective implements OnInit {
  @Input('wsBgC') backgroundColor: string = '';

  constructor(private el: ElementRef, private renderer: Renderer2) {}

  ngOnInit() {
    this.renderer.setStyle(this.el.nativeElement, 'background-color', `var(${this.backgroundColor})`);
  }
}
// font-size
@Directive({
  selector: '[wsFS]'
})
export class FontSizeDirective implements OnInit {
  @Input('wsFS') fontSize: string = '';

  constructor(private el: ElementRef, private renderer: Renderer2) {}

  ngOnInit() {
    this.renderer.setStyle(this.el.nativeElement, 'font-size', this.fontSize);
  }
}


// border-color
@Directive({
  selector: '[wsBrC]'
})
export class BorderColorDirective implements OnInit {
  @Input('wsBrC') borderColor: string = '';

  constructor(private el: ElementRef, private renderer: Renderer2) {}

  ngOnInit() {
    this.renderer.setStyle(this.el.nativeElement, 'border-color', `var(${this.borderColor})`);
  }
}

// color
@Directive({
  selector: '[wsFC]'
})
export class FontColorDirective implements OnInit {
  @Input('wsFC') fontColor: string = '';

  constructor(private el: ElementRef, private renderer: Renderer2) {}

  ngOnInit() {
        this.renderer.setStyle(this.el.nativeElement, 'color', `var(${this.fontColor})`);
  }
}

// border-radius
@Directive({
  selector: '[wsBrR]'
})
export class BorderRadiusDirective implements OnInit {
  @Input('wsBrR') borderRadius: string = '';

  constructor(private el: ElementRef, private renderer: Renderer2) {}

  ngOnInit() {
    this.renderer.setStyle(this.el.nativeElement, 'border-radius', this.borderRadius);
  }
}


@Directive({
  selector: '[wsBoxShadow]'
})
export class BoxShadowOnFocusDirective {
  @Input() wsBoxShadow: string = '--primary-color'; // Default to a CSS variable

  constructor(private el: ElementRef, private renderer: Renderer2) {}

  private getCssVariableValue(variable: string): string {
    const computedStyle = getComputedStyle(document.documentElement);
    return computedStyle.getPropertyValue(variable).trim();
  }

  private parseColor(value: string): string {
    if (value.startsWith('#') || value.startsWith('rgb')) {
      return value;
    } else if (value.startsWith('--')) {
      return this.getCssVariableValue(value);
    }
    return '#564091'; // Fallback color if not valid
  }

  @HostListener('focus') onFocus() {
    const color = this.parseColor(this.wsBoxShadow);
    // Apply the box shadow with the color and fixed opacity
    const boxShadow = `0px 3px 10px ${color}1.0`; // Using 1.0 for full opacity of the color
    this.renderer.setStyle(this.el.nativeElement, 'box-shadow', boxShadow);
  }

  @HostListener('blur') onBlur() {
    this.renderer.removeStyle(this.el.nativeElement, 'box-shadow');
  }
}



