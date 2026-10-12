import { Injectable } from "@angular/core";
import { PageMetaResolver, PageTitleResolver, PageBreadcrumbResolver, PageRobotsResolver, ProductSearchPage, Page, ProductSearchService, CmsService, TranslationService, BasePageMetaResolver, PageType, BreadcrumbMeta, PageRobotsMeta } from "@spartacus/core";
import { Observable, filter, switchMap, of, combineLatest, map } from "rxjs";
import { PlpService } from "../../../../../ws-services/ws-plp.service";
const payLoad = {
  currentPage: 0,
  pageSize: 24,
  discount: '',
  referralCode: '',
  status: '',
  query: '',
  sort: '',
  practitionerId: '',
  fields: 'FULL',
  filters: '',
}

@Injectable({
  providedIn: 'root',
})
export class WsCategoryPageMetaResolver
  extends PageMetaResolver
  implements PageTitleResolver, PageBreadcrumbResolver, PageRobotsResolver
{
  
  // reusable observable for search page data
  protected searchPage$: Observable<ProductSearchPage | Page> = this.cms
    .getCurrentPage()
    .pipe(
      filter((page) => Boolean(page)),
      switchMap((page: Page) => {   
        // only the existence of a plp component tells us if products
        // are rendered or if this is an ordinary content page
   
        return this.hasProductListComponent(page)
          ? this.plpService
            .allproducts$
              .pipe(filter((result) => Boolean(result)))
          : of(page)
})
    );

  constructor(
    protected productSearchService: ProductSearchService,
     private plpService: PlpService,
    protected cms: CmsService,
    protected translation: TranslationService,
    protected basePageMetaResolver: BasePageMetaResolver
  ) {
    super();
    this.pageType = PageType.CATEGORY_PAGE;
  }

  resolveTitle(): Observable<string> {
     return (<Observable<ProductSearchPage>>this.searchPage$).pipe(
      filter((page: ProductSearchPage) => !!page.pagination),
      switchMap((p: ProductSearchPage) => {
        return this.translation.translate('pageMetaResolver.category.title', {
          query: p.breadcrumbs?.length
            ? p.breadcrumbs[0].facetValueName
            : undefined,
        })
      })
    )
  }

  resolveBreadcrumbs(): Observable<BreadcrumbMeta[]> {
    return combineLatest([
      (<Observable<ProductSearchPage>>this.searchPage$).pipe(),
      this.translation.translate('common.fullCatalog'),
    ]).pipe(
      map(([page, label]: [ProductSearchPage, string]) => {
      
      return  page.breadcrumbs
          ? this.resolveBreadcrumbData(<ProductSearchPage>page, label)
          : this.resolveBreadcrumbData({}, label)
      })
    );
  }

  protected resolveBreadcrumbData(
    page: ProductSearchPage | {breadcrumbs:[]},
    label: string
  ): BreadcrumbMeta[] {
    const breadcrumbs: BreadcrumbMeta[] = [];
    breadcrumbs.push({ label: label, link: label === 'Full Catalog' ? '/catalog':'/' });


    for (const br of page?.breadcrumbs ?? []) {
      if (br.facetValueName) {
        if (br.facetCode === 'category' || br.facetCode === 'allCategories') {
          breadcrumbs.push({
            label: br.facetValueName,
            link: `/c/${br.facetValueCode}`,
          });
        }
        if (br.facetCode === 'brand') {
          breadcrumbs.push({
            label: br.facetValueName,
            link: `/Brands/${br.facetValueName}/c/${br.facetValueCode}`,
          });
        }
      }
    }
    return breadcrumbs;
  }

  protected hasProductListComponent(page: Page): boolean {

    return !!Object.keys(page.slots || {}).find(
      (key) =>
        !!page.slots?.[key].components?.find(
          (comp) =>
            comp.typeCode === 'CMSProductListComponent' ||
            comp.typeCode === 'ProductGridComponent'
        )
    );
  }

  resolveRobots(): Observable<PageRobotsMeta[]> {
    return this.basePageMetaResolver.resolveRobots();
  }

  /**
   * Resolves the canonical url for the category listing page.
   *
   * The default options will be used to resolve the url, which means that
   * all query parameters are removed and https and www are added explicitly.
   */
  resolveCanonicalUrl(): Observable<string> {
    return this.basePageMetaResolver.resolveCanonicalUrl();
  }
}
