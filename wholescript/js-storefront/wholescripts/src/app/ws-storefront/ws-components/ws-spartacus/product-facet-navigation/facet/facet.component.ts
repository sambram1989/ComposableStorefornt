import { Component, ChangeDetectionStrategy, Input, HostBinding, ViewChildren, QueryList, ElementRef, ViewChild, Optional, inject, ChangeDetectorRef } from "@angular/core";
import { Facet, FeatureConfigService, FacetValue } from "@spartacus/core";
import { FacetCollapseState, ICON_TYPE, FocusDirective } from "@spartacus/storefront";
import { Observable } from "rxjs";
import { WsFacetService } from "../services/facet.service";
import { Router } from "@angular/router";


@Component({
  selector: 'ws-facet',
  templateUrl: './facet.component.html',
  styleUrls: ['../facet-list/facet-list.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class WsFacetComponent {
  protected _facet: Facet;
  router = inject(Router);
  state$: Observable<FacetCollapseState>;
  isMedpaxPage: boolean = this.router.url.includes('/medpax');
  isWebinarPage: boolean = this.router.url.includes('/webinars')

  /** configurable icon that is used to collapse the facet group  */
  @Input() expandIcon: ICON_TYPE = ICON_TYPE.CARET_DOWN;
  @Input() collapseIcon: ICON_TYPE = ICON_TYPE.CARET_UP;

  @HostBinding('class.multi-select') isMultiSelect: boolean;

  @ViewChildren('facetValue') values: QueryList<ElementRef<HTMLElement>>;

  @ViewChild(FocusDirective) keyboardFocus: FocusDirective;

  @ViewChild('facetHeader') facetHeader: ElementRef<HTMLElement>;

  @Input()
  set facet(value: Facet) {
    this._facet = value;
    this.isMultiSelect = !!value.multiSelect;
    this.state$ = this.facetService.getState(value);
   
  }

  get facet(): Facet {
    return this._facet;
  }

  @Optional() featureConfigService = inject(FeatureConfigService, {
    optional: true,
  });

  // TODO: (CXSPA-6892) - Remove getter next major release.
  /** Temporary getter, not ment for public use */
  get isFacetKeyboardNavigationEnabled(): boolean {
    return !!this.featureConfigService?.isEnabled(
      'a11yFacetKeyboardNavigation'
    );
  }

  constructor(
    protected facetService: WsFacetService,
    protected elementRef: ElementRef<HTMLElement>,
    protected cd: ChangeDetectorRef
  ) {
   
  }

  /**
   * Handles clicking the heading of the facet group, which means toggling
   * the visibility of the group (collapse / expand) and optionally focusing
   * the group.
   */
  toggleGroup(event: UIEvent) {
    const host: HTMLElement = this.elementRef.nativeElement;
    const isLocked = this.keyboardFocus?.isLocked;

    this.facetService.toggle(this.facet, this.isExpanded);

    if (!isLocked || this.isExpanded) {
      host.focus();
      // we stop propagating the event as otherwise the focus on the host will trigger
      // an unlock event from the LockFocus directive.
      event.stopPropagation();
    }
  }

  get isExpanded(): boolean {
    return this.values?.first?.nativeElement.offsetParent !== null;
  }

  openLink(event: KeyboardEvent) {
    (event.target as HTMLElement).click();
    event.preventDefault();
  }

  
  increaseVisibleValues(): void {
    this.facetService.increaseVisibleValues(this.facet);
  }


  decreaseVisibleValues(): void {
    this.facetService.decreaseVisibleValues(this.facet);
  }

  getLinkParams(value: FacetValue) {
    return this.facetService.getLinkParams(value.query?.query?.value ?? '');
  }

  onKeydown(event: KeyboardEvent): void {
    // TODO: (CXSPA-6892) - Remove feature flag next major release.
    if (!this.isFacetKeyboardNavigationEnabled) {
      return;
    }
    const targetIndex = this.values.toArray().findIndex((el) => {
      return el.nativeElement === event.target;
    });
    switch (event.key) {
      case 'ArrowLeft':
        this.onArrowLeft(event);
        break;
      case 'ArrowRight':
        this.onArrowRight(event);
        break;
      case 'ArrowDown':
        this.onArrowDown(event, targetIndex);
        break;
      case 'ArrowUp':
        this.onArrowUp(event, targetIndex);
        break;
    }
  }

  onArrowRight(event: Event): void {
    if (!this.isExpanded) {
      this.toggleGroup(event as UIEvent);
    }
  }

  onArrowLeft(event: Event): void {
    if (this.isExpanded) {
      this.toggleGroup(event as UIEvent);
      this.facetHeader.nativeElement.focus();
    }
  }

  onArrowDown(event: Event, targetIndex: number): void {
    if (this.isExpanded) {
      event.preventDefault();
      if (event.target === this.facetHeader.nativeElement) {
        this.values?.first?.nativeElement.focus();
        return;
      }
      this.values.get(targetIndex + 1)?.nativeElement.focus();
    }
  }

  onArrowUp(event: Event, targetIndex: number): void {
    if (this.isExpanded) {
      event.preventDefault();
      this.values.get(targetIndex - 1)?.nativeElement.focus();
    }
  }

  setFilter(value: any) {
    this.facetService.setFilterList(value);
  }

  setFilterState(value: any) {
    sessionStorage.setItem('selectedFilter', '&query=' + value.query?.query?.value || '');
    this.facetService.isFilterSelected.next(true);
  }
}
