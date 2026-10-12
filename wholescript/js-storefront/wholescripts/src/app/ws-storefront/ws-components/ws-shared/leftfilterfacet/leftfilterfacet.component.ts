import { Component, ElementRef, inject, ChangeDetectorRef, Input, input, OnDestroy, OnInit, output, QueryList, signal, SimpleChanges, ViewChildren } from '@angular/core';
import { WsLeftFilterFacet } from '../../../ws-services/ws-left-filter-facet.service';

@Component({
  selector: 'ws-leftfilterfacet',
  templateUrl: './leftfilterfacet.component.html',
  styleUrl: './leftfilterfacet.component.scss'
})
export class LeftfilterfacetComponent implements OnInit, OnDestroy {

  service = inject(WsLeftFilterFacet);
  fromPopup = input<boolean>(false);
  filters = input<any[]>([]);
  computedFilters = signal<any[]>([]);
  //--active facets--//
  selectedFacets: any = [];
  activeFacets = signal<any[]>(this.selectedFacets);
  @Input('isDisable') isDisable: boolean = false;
  // isDisable=signal<boolean>(false);

  activeFilters = output<any[]>();

  expandedIndex = 0;
  viewMore: boolean[] = [];
  @ViewChildren('viewToggle') viewToggles!: QueryList<ElementRef>;
  private isKeyboardNav = false;

  constructor(private cdref: ChangeDetectorRef) {}


  ngOnInit() {
    window.addEventListener('keydown', this.handleKeyDown, true);
    window.addEventListener('mousedown', this.handleMouseDown, true);
    window.addEventListener('touchstart', this.handleMouseDown, true);
  }

  ngOnChanges(changes: SimpleChanges) {
    if (changes['filters']) {
      this.updateComputedFilters();
    }
  }

  ngDoCheck() {
    if (this.service.clearFilter()) {
      this.ClearFilters();
      this.service.clearFilter.set(false);
    }
  }
  //--------FILTERS SECTION-------//
  ClearFilters() {
    this.selectedFacets = [];
    this.activeFacets.set(this.selectedFacets);
    this.updateActiveFacetsState();
  }

  checkBoxState(event: any, selectedFacet: any, title?: any) {
    let facet = {
      title, selectedFacet
    }
    if (event.target.checked) {
      this.selectedFacets.push(facet);
      this.updateActiveFacetsState();
    } else {
      this.clearUserSpecificFilter(facet);
    }
  }

  clearUserSpecificFilter(filter: any) {
    this.selectedFacets = this.selectedFacets.filter((item: any) => item.selectedFacet.code !== filter.selectedFacet.code);
    this.updateActiveFacetsState();
  }

  isFacetSelected(facet: any): boolean {
    return this.selectedFacets.some((selected: any) => selected.selectedFacet.code === facet.code);
  }

  getVisibleFilters(filters: any) {
    if (!filters) {
      return null;
    }
    return filters.map((item: any) => {
      // item.filters = item.config.viewMore ? item.filters.slice(0, 5) : item.filters;
      return item;
    });

  }

  showVisibleFiltersAlone(index: number) {
    this.viewMore[index] = !this.viewMore[index];
    this.updateComputedFilters();
    setTimeout(() => {
      this.viewToggles.toArray()[index]?.nativeElement.focus();
    }, 0);
  }

  private updateComputedFilters() {
    if (!this.filters()) return;
    const FiltersClone = JSON.parse(JSON.stringify(this.filters()));
    this.computedFilters.set(this.getVisibleFilters(FiltersClone));
    this.cdref.detectChanges();
  }
  private updateActiveFacetsState() {
    this.activeFacets.set(this.selectedFacets);
    this.activeFilters.emit(this.selectedFacets);
  }
  toggleCheckbox(event: Event, filter: any, title: string): void {
    if (event instanceof KeyboardEvent) {
      if (event.key !== 'Enter' && event.key !== ' ') return; // Handle only Enter and Space
      event.preventDefault(); // Prevents default scrolling when pressing Space
    }
  
    const checkbox = event.target as HTMLInputElement;
    checkbox.checked = !checkbox.checked;
  
    // Fire the (change) event manually to update Angular binding
    const changeEvent = new Event('change', { bubbles: true });
    checkbox.dispatchEvent(changeEvent);
  }

  ngOnDestroy() {
    window.removeEventListener('keydown', this.handleKeyDown, true);
    window.removeEventListener('mousedown', this.handleMouseDown, true);
    window.removeEventListener('touchstart', this.handleMouseDown, true);
  }

  private handleKeyDown = () => {
    this.isKeyboardNav = true;
  };

  private handleMouseDown = () => {
    this.isKeyboardNav = false;
  };

  scrollToViewWithOffset(element: HTMLElement) {
    if (!this.isKeyboardNav) return;
    const stickyHeight = 400; 
    const rect = element.getBoundingClientRect();
    const absoluteElementTop = rect.top + window.scrollY;
    const offsetPosition = absoluteElementTop - stickyHeight - 5;
    window.scrollTo({
      top: offsetPosition,
      behavior: 'smooth'
    });
  }
    
  onKeyDown(event: KeyboardEvent, action: string, item: any, title?:string) {
    if (event.key === 'Enter') {
      event.preventDefault(); 
      if (action === 'showVisibleFiltersAlone') {
          this.showVisibleFiltersAlone(item);
      } else if (action === 'toggleCheckbox') {
        this.toggleCheckbox(event, item, title);
      }
    }
  }
}
