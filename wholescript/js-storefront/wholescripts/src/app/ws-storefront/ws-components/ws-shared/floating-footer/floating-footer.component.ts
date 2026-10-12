import { Component } from '@angular/core';

@Component({
  selector: 'ws-floating-footer',
  template: `<ng-content></ng-content>` ,
  styles: `:host {
    position: fixed;
    bottom: 0;
    left: 0;
    width: 100%;
    background-color: #f8f9fa;
    border-top: 1px solid #e9ecef;
    z-index: 1000;
    box-shadow: #C6BBDB 0 3px 16px;
  }`
})
export class FloatingFooterComponent {

}
