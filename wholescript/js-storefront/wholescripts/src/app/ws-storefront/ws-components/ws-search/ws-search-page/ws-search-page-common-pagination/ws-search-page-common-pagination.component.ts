import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  selector: 'ws-search-page-common-pagination',
  templateUrl: './ws-search-page-common-pagination.component.html',
  styleUrl: './ws-search-page-common-pagination.component.scss',
})
export class WsSearchPageCommonPaginationComponent {
  @Input() currentPage: number = 0;
  @Input() totalPageCount: number = 0;
  @Output() setCurrentPage = new EventEmitter<number>();

  updatePage(page: number): void {
    if (page >= 0 && page < this.totalPageCount) {
      this.setCurrentPage.emit(page);
    }
  }

  getVisiblePages(): number[] {
    const visiblePages: number[] = [];
    const maxVisiblePages = 5;
    const startPage = Math.max(1, this.currentPage - 2);
    const endPage = Math.min(this.totalPageCount - 1, this.currentPage + 2);

    for (let i = startPage; i <= endPage; i++) {
      if (i !== this.totalPageCount - 1) {
        visiblePages.push(i);
      }
    }

    return visiblePages;
  }
}
