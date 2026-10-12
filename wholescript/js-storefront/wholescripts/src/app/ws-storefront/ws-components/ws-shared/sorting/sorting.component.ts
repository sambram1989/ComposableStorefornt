import { Component, EventEmitter, Input, Output } from '@angular/core';
 interface SortModel {
  code?: string;
  name?: string;
  selected?: boolean;
}
@Component({
  selector: 'ws-sorting',
  templateUrl: './sorting.component.html',
  styleUrls: ['../search-box/search-box.component.scss']
})
export class SortingComponent {
  @Input()
  sortOptions: SortModel[] | undefined;
  @Input()
  ariaControls: string;
  @Input()
  ariaLabel: string | undefined;
  @Input()
  selectedOption: string | undefined;
  @Input()
  placeholder: string;
  @Input()
  sortLabels: { [code: string]: string } | null;

  @Output()
  sortListEvent: EventEmitter<string>;

  constructor() {
    this.sortListEvent = new EventEmitter<string>();
  }

  sortList(sort: string): void {
    this.sortListEvent.emit(sort);
  }

}
