import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  selector: 'ws-search-page-common-heading',
  templateUrl: './ws-search-page-common-heading.component.html',
  styleUrl: './ws-search-page-common-heading.component.scss'
})
export class WsSearchPageCommonHeadingComponent {
  @Input() viewAllFlag: boolean = false;
  @Input() heading: string = '';
  @Input() totalResultsCount: number;
  numberOfResults: number = 24;
  showDropDownValues: number[] = [24, 48, 96];

  @Output() viewAll = new EventEmitter();
  @Output() showNumberOfResults = new EventEmitter<number>();

  viewAllEmit() {
    this.viewAll.emit()
  }

  getNumberOfResults(el: any) {
     this.numberOfResults = el?.target?.value;
     this.showNumberOfResults.emit(this.numberOfResults);
   }
}
