import { Injectable } from "@angular/core";
import { Params } from "@angular/router";
import { RoutingService, ProductSearchPage, ActivatedRouterStateSnapshot, PageType, Breadcrumb } from "@spartacus/core";
import { ProductListComponentService, FacetList } from "@spartacus/storefront";
import { map, Observable } from "rxjs";
import { WsCommonFacetService } from "./ws-common-facet.service";

@Injectable({
  providedIn: 'root',
})
export class WsProductFacetService {
  constructor(
    protected routing: RoutingService,
    protected productListComponentService: ProductListComponentService,
    protected commonFacet:WsCommonFacetService
  ) {}

  protected readonly routeState$ = this.routing
    .getRouterState()
    .pipe(map((routerState) => routerState.state));



  readonly facetList$: Observable<FacetList> = this.commonFacet.currentSearchList$.pipe(
    map(
      (result: ProductSearchPage) =>
        ({
          facets: result.facets,
          activeFacets: result.breadcrumbs,
        } as FacetList)
    )
  );

 
  protected filterForPage(
    state: ActivatedRouterStateSnapshot,
    page: ProductSearchPage
  ): boolean {
    if (!page.currentQuery?.query?.value) {
      return false;
    }
    if (state.context.type === PageType.CATEGORY_PAGE) {
      return (
        page.currentQuery.query.value.indexOf(
          `allCategories:${state.context.id}`
        ) > -1
      );
    }

    if (
      state.context.type === PageType.CONTENT_PAGE &&
      state.context.id === 'search'
    ) {
      return page.freeTextSearch === state.params["query"].split(':')[0];
    }
    return false;
  }

 
  protected filterBreadcrumbs(
    breadcrumbs: Breadcrumb[],
    params: Params
  ): Breadcrumb[] {
    return breadcrumbs
      ? breadcrumbs.filter(
          (breadcrumb) =>
            !(
              breadcrumb.facetCode === 'allCategories' &&
              (breadcrumb.facetValueCode === params["categoryCode"] ||
                breadcrumb.facetValueCode === params["brandCode"])
            )
        )
      : [];
  }
}
