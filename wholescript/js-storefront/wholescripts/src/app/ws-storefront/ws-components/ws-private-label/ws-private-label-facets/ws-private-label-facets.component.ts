import { ChangeDetectorRef, Component, DestroyRef, EventEmitter, Input, Output } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { WsPrivateLabelService } from '../../../ws-services/ws-private-label.service';

@Component({
  selector: 'ws-private-label-facets',
  templateUrl: './ws-private-label-facets.component.html',
  styleUrl: './ws-private-label-facets.component.scss',
})
export class WsPrivateLabelFacetsComponent {
  @Input() facetFilters: any[] = [];
  @Input() allResults: any;
  @Output() setFilter = new EventEmitter<any>();
  @Output() resetFilter  = new EventEmitter();

  constructor(
    private privateLabelService: WsPrivateLabelService,
    private cdRef: ChangeDetectorRef,
    private destroyRef: DestroyRef
  ) {}

  setFilterChange(option: any) {
    this.setFilter.emit(option);
  }

  ClearFilters(){
    // Logic to be implementation for the clear filters
    this.facetFilters.forEach((element: any) => {
      element.values.forEach((el: any) => {
        el.selected = false;
      });
    });
    this.resetFilter.emit();
  }
  getQueryString(): string {
    const baseQuery = 'products:relevance';
    const selectedQueries = this.facetFilters
      .map((level: any) =>
        level.values
          .filter((value: any) => value.selected)
          .map((value: any) =>
            value.query.query.value.replace(`${baseQuery}:`, '')
          )
      )
      .flat()
      .join(':');

    return selectedQueries
      ? `?query=${baseQuery}:${selectedQueries}`
      : this.allResults?.currentQuery?.query?.value;
  }
  setOption(option: any) {
    option.selected = !option.selected;
    this.setFilterChange(option);
  }
}
