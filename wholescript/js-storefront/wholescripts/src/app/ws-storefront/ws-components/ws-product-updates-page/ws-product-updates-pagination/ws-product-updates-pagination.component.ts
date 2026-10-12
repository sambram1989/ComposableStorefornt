import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  selector: 'ws-product-updates-pagination',
  templateUrl: './ws-product-updates-pagination.component.html',
  styleUrl: './ws-product-updates-pagination.component.scss'
})
export class WsProductUpdatesPaginationComponent {
  @Input() currentPage: number = 0;
  @Input() totalPageCount: number = 0;
  @Output() setCurrentPage = new EventEmitter<number>();

  updatePage(page: number): void {
    this.setCurrentPage.emit(page);
  }

  counter(i: number) {
    return new Array(i);
}
  
}
