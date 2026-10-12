import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  selector: 'ws-search-page-magazines',
  templateUrl: './ws-search-page-magazines.component.html',
  styleUrl: './ws-search-page-magazines.component.scss'
})
export class WsSearchPageMagazinesComponent {
  currentPage: number = 0;
  totalPageCount: number = 9;
  @Input() viewAllFlag: boolean = false;
  @Output() viewAll = new EventEmitter();
  viewAllEmit() {
    this.viewAll.emit()
  }

  setCurrentPage(page: number) {
    this.currentPage = page;
  }
}
