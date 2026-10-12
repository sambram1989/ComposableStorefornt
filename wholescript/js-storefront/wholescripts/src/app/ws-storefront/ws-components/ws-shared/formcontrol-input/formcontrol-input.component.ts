import {
  AfterViewInit,
  Component,
  ElementRef,
  Input,
  input,
  OnDestroy,
  Renderer2,
  signal,
  ViewChild,
} from '@angular/core';
import { FormControl } from '@angular/forms';
export type input = 'text' | 'number' | 'email';
export interface dropdown {
  code: string | any;
  value: string;
}
@Component({
  selector: 'ws-formcontrol-input',
  templateUrl: './formcontrol-input.component.html',
  styleUrl: './formcontrol-input.component.scss',
})
export class FormcontrolInputComponent
  implements  AfterViewInit, OnDestroy
{
  WsInputType = input<input>('text');
  errorMessage = input<string>('Required');

  @Input() wsLabel: string = '';
  @Input() isSelectBox: boolean = false;
  @Input() wsheight: string = '38px';
  @Input() wswidth: string = '100%';
  @Input() isMasked: boolean = false;
  @Input() maskValue: string = '00-0000-000';
  @Input() isShowMaskTyped: boolean = false;
  @Input() isShowStart: boolean = false;
  @Input() wsValue: Date = new Date();
  @Input() isValue: boolean = false;
  @Input() isReadOnly: boolean = false;
  @Input() selectBoxData: any;
  @Input() top: any = '20px';
  @Input() left: any = '230px';
  @Input() FixedLabel:boolean = false;
  @Input() placeholder:string = '';
  @Input() maxLength?:number;

  @Input({ required: true })
  wsControlName!: FormControl;
  // for ngSelect input aria-label
  @ViewChild('ngSelectWrapper', { static: false })
  ngSelectWrapper!: ElementRef<HTMLElement>;
  observer!: MutationObserver;

  dropdownData = signal<dropdown[]>([
    { code: 2345, value: 'dataset' },
    { code: 6789, value: 'example' },
    { code: 1234, value: 'sample' },
    { code: 5678, value: 'testdata' },
    { code: 9101, value: 'information' },
    { code: 1122, value: 'demo' },
    { code: 3344, value: 'record' },
    { code: 5566, value: 'collection' },
    { code: 7788, value: 'archive' },
    { code: 9900, value: 'repository' },
  ]);

  constructor(private renderer: Renderer2) {}

  ngAfterViewInit(): void {
    this.observerSelectBox();
  }

  observerSelectBox() {
    if (this.isSelectBox) {
      const wrapper = this.ngSelectWrapper?.nativeElement;
      if (!wrapper) return;
      this.observer = new MutationObserver(() => {
        const inputElement = wrapper.querySelector('input');
        if (inputElement) {
          this.renderer.setAttribute(
            inputElement,
            'aria-label',
            'Select Frequency'
          );
          this.observer.disconnect();
        }
      });

      this.observer.observe(wrapper, {
        attributes: true,
        subtree: true,
      });

      const inputElement = wrapper.querySelector('input');
      if (inputElement) {
        this.renderer.setAttribute(
          inputElement,
          'aria-label',
          'Select Frequency'
        );
        this.observer.disconnect();
      }
    }
  }

  ngOnDestroy(): void {
    if (this.observer) {
      this.observer.disconnect();
    }
  }
}
