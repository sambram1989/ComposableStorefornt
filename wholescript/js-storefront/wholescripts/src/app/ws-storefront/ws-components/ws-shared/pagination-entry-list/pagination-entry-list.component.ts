import { Component, computed, effect, input, model } from '@angular/core';

@Component({
  selector: 'ws-pagination-entry-list',
  templateUrl: './pagination-entry-list.component.html',
  styleUrl: './pagination-entry-list.component.scss'
})
export class PaginationEntryListComponent {
  totalItems = input<number>(0);
  itemPerPage = input<number>(0)
  currentPage = model<number>(1);

  startPage = computed(() => {
    return (this.itemPerPage() * this.currentPage()) - (this.itemPerPage() - 1)
  });
  endPage = computed(() => {
    return (this.itemPerPage() * this.currentPage())
  });
  endLine = input<string>('');

  constructor(){
 
  }
}
