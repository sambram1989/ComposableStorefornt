import { HttpUrlEncodingCodec } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Facet } from "@spartacus/core";
import { FacetCollapseState, FacetList, FacetGroupCollapsedState } from "@spartacus/storefront";
import { BehaviorSubject, Observable, tap, of } from "rxjs";
import { WsProductFacetService } from "./product-facet.service";

@Injectable({
  providedIn: 'root',
})
export class WsFacetService {

  private facetFilterList= new BehaviorSubject([]);
  public isFilterSelected= new BehaviorSubject(false);
  facetFilterList$ = this.facetFilterList.asObservable();

  setFilterList(facetList: any): void {
    this.facetFilterList.next([facetList]); 
  }

  protected facetState = new Map<string, BehaviorSubject<FacetCollapseState>>();

  protected readonly codec = new HttpUrlEncodingCodec();

  constructor(protected WsproductFacetService: WsProductFacetService) {}

  facetList$: Observable<FacetList> = this.WsproductFacetService.facetList$.pipe(
    tap((facetList) => {
      facetList.facets?.forEach((facet) => this.initialize(facet));
    })
  );


  getState(facet: Facet): Observable<FacetCollapseState> {
    this.initialize(facet);
    return facet.name ? this.facetState.get(facet.name) ?? of({}) : of({});
  }


  protected getStateSnapshot(facet: Facet): FacetCollapseState {
    return (this.getState(facet) as BehaviorSubject<FacetCollapseState>).value;
  }

  toggle(facet: Facet, isExpanded: boolean): void {
    const state = this.getStateSnapshot(facet);

    const toggledState = {
      toggled: isExpanded
        ? FacetGroupCollapsedState.COLLAPSED
        : FacetGroupCollapsedState.EXPANDED,
    } as FacetCollapseState;

    if (toggledState.toggled === FacetGroupCollapsedState.COLLAPSED) {
      toggledState.maxVisible = state.topVisible;
    }

    this.updateState(facet, toggledState);
  }


  increaseVisibleValues(facet: Facet): void {
    this.updateState(facet, { maxVisible: facet.values?.length });
  }


  decreaseVisibleValues(facet: Facet): void {
    this.updateState(facet, { maxVisible: facet.topValueCount });
  }

  protected initialize(facet: Facet): void {
    const topFacets =
      facet.topValueCount && facet.topValueCount > 0
        ? facet.topValueCount
        : facet.values?.length || 0;
    if (facet.name && !this.hasState(facet)) {
      this.facetState.set(
        facet.name,
        new BehaviorSubject({
          topVisible: topFacets,
          maxVisible: topFacets,
        } as FacetCollapseState)
      );
    }
  }

  protected updateState(facet: Facet, property: FacetCollapseState): void {
    const state = { ...this.getStateSnapshot(facet), ...property };
    if (facet.name) {
      this.facetState.get(facet.name)?.next(state);
    }
  }

  protected hasState(facet: Facet): boolean {
    if (facet.name) {
      return this.facetState.has(facet.name);
    }
    return false;
  }

  getLinkParams(query: string): { [key: string]: string } {
    return {
      query: this.codec
        .decodeValue(this.decodeUriComponentSafe(query))
        .replace(/\+/g, ' '),
    };
  }

  protected decodeUriComponentSafe(query: string): string {
    return query.replace(/%(?![0-9a-fA-F]{2})/g, '%25');
  }
}
