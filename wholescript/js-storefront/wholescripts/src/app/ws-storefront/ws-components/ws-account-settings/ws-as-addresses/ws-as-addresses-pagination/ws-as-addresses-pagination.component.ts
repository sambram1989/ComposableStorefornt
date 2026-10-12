import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  selector: 'ws-as-addresses-pagination',
  templateUrl: './ws-as-addresses-pagination.component.html',
  styleUrl: './ws-as-addresses-pagination.component.scss'
})
export class WsAsAddressesPaginationComponent {

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
