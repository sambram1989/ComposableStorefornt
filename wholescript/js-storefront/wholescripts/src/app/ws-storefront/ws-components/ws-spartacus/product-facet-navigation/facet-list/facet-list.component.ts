import { Component, ChangeDetectionStrategy, OnInit, OnDestroy, ViewChild, ElementRef, Input, Output, EventEmitter, HostListener, Optional, inject, Renderer2 } from "@angular/core";
import { FeatureConfigService, Facet } from "@spartacus/core";
import { FacetList, ICON_TYPE, FocusConfig, KeyboardFocusService, FacetComponent, FacetGroupCollapsedState } from "@spartacus/storefront";
import { Subscription, Observable, map } from "rxjs";
import { WsFacetService } from "../services/facet.service";


@Component({
  selector: 'ws-facet-list',
  templateUrl: './facet-list.component.html',
  styleUrl: './facet-list.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class WsFacetListComponent implements OnInit, OnDestroy {
  protected subscriptions = new Subscription();
  private _isDialog: boolean;

  @ViewChild('backToResultsBtn')
  backToResultsBtn: ElementRef<HTMLButtonElement>;

  @Input() medPaxFilterText: any;
  @Input()
  set isDialog(value: boolean) {
    this._isDialog = value;
    if (value) {
      this.renderer.addClass(document.body, 'modal-open');
    }
  }

  get isDialog(): boolean {
    return this._isDialog;
  }

  /** Emits when the list must close */
  @Output() closeList = new EventEmitter();

  /** The list of all facet and values related to the products in the list */
  facetList$: Observable<FacetList> = this.facetService.facetList$;

  iconTypes = ICON_TYPE;
  updatedfacets: Facet[];

  dialogFocusConfig: FocusConfig = {
    trap: true,
    block: true,
    focusOnEscape: true,
    autofocus: 'cx-facet > button',
  };

  @HostListener('click') handleClick() {
    this.close();
  }
  @Optional() focusService = inject(KeyboardFocusService, { optional: true });
  @Optional() featureConfigService = inject(FeatureConfigService, {
    optional: true,
  });

  constructor(
    protected facetService: WsFacetService,
    protected elementRef: ElementRef,
    protected renderer: Renderer2
  ) { }

  ngOnInit(): void {
    // TODO: (CXSPA-7321) - Remove feature flag next major release
    if (this.featureConfigService?.isEnabled('a11yFacetsDialogFocusHandling')) {
      this.enableFocusHandlingOnFacetListChanges();
    }

    type NameMappings = {
      webinarTag: string;
      presenterCode: string;
      duration: string;
      status: string;
    };

    const renameFacets = (facets: any[], nameMappings: NameMappings) => {
      return facets.map(item => ({
        ...item,
        name: nameMappings[item.name as keyof NameMappings] || item.name
      }));
    };
    this.facetList$.subscribe((data) => {
      this.updatedfacets = data?.facets || [];
      if(this.medPaxFilterText) {
        const matchedFacet = this.updatedfacets.find(facet => facet.name === this.medPaxFilterText);
        this.updatedfacets = matchedFacet ? [matchedFacet] : [];
      }
      if (this.updatedfacets.length > 0) {
        const nameMappings: NameMappings = {
          webinarTag: "Filter by webinar category",
          presenterCode: "Filter by presenter",
          duration: "Filter by duration",
          status: "Filter by status"
        };
        this.updatedfacets = renameFacets(this.updatedfacets, nameMappings);
        const desiredOrder = [
          "Filter by status",
          "Filter by presenter",
          "Filter by webinar category",
          "Filter by duration"
        ];
        this.updatedfacets.sort((a, b) => {
          const indexA = desiredOrder.indexOf(a.name);
          const indexB = desiredOrder.indexOf(b.name);
          return indexA - indexB;
        });
      }
    });
  }

  /**
   * Toggles the facet group in case it is not expanded.
   */
  expandFacetGroup(facet: Facet, ref: FacetComponent) {
    if (!ref.isExpanded) {
      this.facetService.toggle(facet, ref.isExpanded);
    }
  }

  /**
   * Indicates that the facet group has been expanded.
   */
  isExpanded(facet: Facet): Observable<boolean> {
    return this.facetService
      .getState(facet)
      .pipe(
        map((value) => value.toggled === FacetGroupCollapsedState.EXPANDED)
      );
  }

  /**
   * Indicates that the facet group has been collapsed.
   */
  isCollapsed(facet: Facet): Observable<boolean> {
    return this.facetService
      .getState(facet)
      .pipe(
        map((value) => value.toggled === FacetGroupCollapsedState.COLLAPSED)
      );
  }

  close(event?: boolean): void {
    this.renderer.removeClass(document.body, 'modal-open');
    this.closeList.emit(event);
  }

  block(event?: MouseEvent) {
    event?.stopPropagation();
  }

  protected enableFocusHandlingOnFacetListChanges(): void {
    this.subscriptions.add(
      this.facetService.facetList$.subscribe((facetList) =>
        this.handleDialogFocus(facetList.facets)
      )
    );
  }

  protected handleDialogFocus(facets: Facet[]): void {
    // Only apply new focus for the dialog view
    if (!this.isDialog) {
      return;
    }

    const focusKey = this.focusService?.get();
    if (!focusKey) {
      return;
    }

    const focusedFacet = facets.find((facet) =>
      facet.values?.some((value) => {
        return value.name === focusKey;
      })
    );
    if (focusedFacet) {
      return;
    }

    if (!facets?.length) {
      // If there are no facets to display then focus on the "Back To Results" button
      this.backToResultsBtn?.nativeElement.focus();
      this.focusService?.clear();
      return;
    }

    const firstAvailableFacet = facets[0];
    this.focusService?.set(firstAvailableFacet.name);
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }
}
