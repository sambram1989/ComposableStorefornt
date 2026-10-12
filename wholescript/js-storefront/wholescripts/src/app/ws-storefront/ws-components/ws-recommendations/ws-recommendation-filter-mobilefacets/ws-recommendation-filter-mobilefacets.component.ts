import {
  Component,
  inject,
  Input,
  input,
  output,
  signal,
  SimpleChanges,
} from '@angular/core';
import { WsLeftFilterFacet } from '../../../ws-services/ws-left-filter-facet.service';
import { Observable } from 'rxjs';
import { BsModalRef } from 'ngx-bootstrap/modal';

@Component({
  selector: 'ws-ws-recommendation-filter-mobilefacets',
  templateUrl: './ws-recommendation-filter-mobilefacets.component.html',
  styleUrl: './ws-recommendation-filter-mobilefacets.component.scss',
})
export class WsRecommendationFilterMobilefacetsComponent {
  service = inject(WsLeftFilterFacet);
  fromPopup = input<boolean>(false);
  @Input() filtersApi$!: Observable<any[]>;
  computedFilters = signal<any[]>([]);
  selectedFacets: any = [];
  activeFacets = signal<any[]>(this.selectedFacets);
  @Input('isDisable') isDisable: boolean = false;
  activeFilters = output<any[]>();

  expandedIndex = 0;
  viewMore: boolean[] = [true];
  constructor(private bsModalRef: BsModalRef) {}

  ngOnInit() {
    if (this.filtersApi$) {
      this.filtersApi$.subscribe((data) => {
        this.computedFilters.set(data);
      });
    }
  }

  ngOnChanges(changes: SimpleChanges) {
    if (changes['filters'] && changes['filters'].currentValue) {
      this.updateComputedFilters(changes['filters'].currentValue);
    }
  }

  ngDoCheck() {
    if (this.service.clearFilter()) {
      this.ClearFilters();
      this.service.clearFilter.set(false);
    }
  }

  //--------Close Modal------//
  closeModal() {
    this.bsModalRef.hide();
  }

  //--------FILTERS SECTION-------//
  ClearFilters() {
    this.selectedFacets = [];
    this.activeFacets.set(this.selectedFacets);
    this.updateActiveFacetsState();
  }

  checkBoxState(event: any, selectedFacet: any, title?: any) {
    let facet = {
      title,
      selectedFacet,
    };
    if (event.target.checked) {
      this.selectedFacets.push(facet);
      this.updateActiveFacetsState();
    } else {
      this.clearUserSpecificFilter(facet);
    }
  }

  clearUserSpecificFilter(filter: any) {
    this.selectedFacets = this.selectedFacets.filter(
      (item: any) => item.selectedFacet.code !== filter.selectedFacet.code
    );
    this.updateActiveFacetsState();
  }

  isFacetSelected(facet: any): boolean {
    return this.selectedFacets.some(
      (selected: any) => selected.selectedFacet.code === facet.code
    );
  }

  getVisibleFilters(filters: any) {
    if (!filters) {
      return null;
    }
    return filters.map((item: any) => {
      return item;
    });
  }

  showVisibleFiltersAlone(index: number) {
    this.filtersApi$.subscribe((filtersData) => {
      if (filtersData && filtersData[index]) {
        filtersData[index].config.viewMore =
          !filtersData[index].config.viewMore;
        this.updateComputedFilters(filtersData);
      }
    });
  }

  private updateComputedFilters(filtersData: any[]) {
    if (!filtersData) return;
    const FiltersClone = JSON.parse(JSON.stringify(filtersData));
    this.computedFilters.set(this.getVisibleFilters(FiltersClone));
  }

  private updateActiveFacetsState() {
    this.activeFacets.set(this.selectedFacets);
    this.activeFilters.emit(this.selectedFacets);
  }
}
