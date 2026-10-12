import { Component, effect, EventEmitter, input, Input, model, OnChanges, Output, signal, SimpleChanges } from '@angular/core';
import { FormControl } from '@angular/forms';
import { debounceTime } from 'rxjs';

@Component({
  selector: 'ws-search-box',
  templateUrl: './search-box.component.html',
  styleUrl: './search-box.component.scss',
})
export class WsSearchBoxComponent implements OnChanges {
  searchControl = new FormControl('');
  isFocused = false;
  isBlueBgIcon = input<boolean>(false);
  @Input() placeholder: string = 'Search';
  @Input() isDisable: boolean = false;
  @Output() searchChange = new EventEmitter<string>();
  @Input() searchValue!: string;
  @Input() maxLength?: number;
  constructor() {
    this.searchControl.valueChanges
      .pipe(debounceTime(300))
      .subscribe(value => this.searchChange.emit(value));
  }

  ngOnChanges(changes: SimpleChanges) {
    if (changes['searchValue']) {
      this.searchControl.setValue(changes['searchValue'].currentValue);
    }
  }

  handleClearSearchKey(event: KeyboardEvent): void {
    if ((event.key === 'Enter' || event.key === ' ') && this.searchControl?.value?.length > 0) {
      event.preventDefault(); 
      this.clearSearch();
    }
  }

  clearSearch() {
    if (this.searchControl.value.length > 0) {
      this.searchControl.setValue('');
    }
  }
}
