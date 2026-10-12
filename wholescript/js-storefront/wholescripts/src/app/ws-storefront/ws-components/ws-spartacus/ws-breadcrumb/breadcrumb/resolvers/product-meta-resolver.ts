import { Injectable } from "@angular/core";
import { PageMetaResolver, PageHeadingResolver, PageTitleResolver, PageDescriptionResolver, PageBreadcrumbResolver, PageImageResolver, PageRobotsResolver, RoutingService, ProductService, TranslationService, BasePageMetaResolver, PageLinkService, PageType, Product, ProductScope, isNotUndefined, BreadcrumbMeta, Category, PageRobotsMeta } from "@spartacus/core";
import { Observable, map, filter, switchMap, combineLatest, of } from "rxjs";
import { CATEGORY_PREFIX } from "../../../../../ws-constants/ws-api-endpoint.constant";

/**
 * Resolves the page data for the Product Detail Page
 * based on the `PageType.PRODUCT_PAGE`.
 *
 * The page title, heading, description, breadcrumbs and
 * first GALLERY image are resolved if available in the data.
 */
@Injectable({
  providedIn: 'root',
})
export class WsProductPageMetaResolver
  extends PageMetaResolver
  implements
    PageHeadingResolver,
    PageTitleResolver,
    PageDescriptionResolver,
    PageBreadcrumbResolver,
    PageImageResolver,
    PageRobotsResolver
{
  constructor(
    protected routingService: RoutingService,
    protected productService: ProductService,
    protected translation: TranslationService,
    protected basePageMetaResolver: BasePageMetaResolver,
    protected pageLinkService: PageLinkService
  ) {
    super();
    this.pageType = PageType.PRODUCT_PAGE;
  }

  // reusable observable for product data based on the current page
  protected product$: Observable<Product> = this.routingService
    .getRouterState()
    .pipe(
      map((state) => {  
        return state.state.params['productCode'];
      }),
      filter((code) => !!code),
      switchMap((code) => this.productService.get(code, ProductScope.DETAILS)),
      filter(isNotUndefined)
    );

  /**
   * Resolves the page heading for the Product Detail Page.
   * The page heading is used in the UI (`<h1>`), where as the page
   * title is used by the browser and crawlers.
   */
  resolveHeading(): Observable<string> {
    return this.product$.pipe(
      switchMap((p: Product) =>
        this.translation.translate('pageMetaResolver.product.heading', {
          heading: p.name,
        })
      )
    );
  }

  /**
   * Resolves the page title for the Product Detail Page. The page title
   * is resolved with the product name, the first category and the manufacturer.
   * The page title used by the browser (history, tabs) and crawlers.
   */
  resolveTitle(): Observable<string> {
    return this.product$.pipe(
      switchMap((product) => {
        let data = this.getClassification(product);
        let title = data?.features.find((x: any) => x.code.includes('seometatitle'))?.featureValues[0].value;
        return this.translation.translate('pageMetaResolver.product.title', {
          title: title ?? product?.name,
        });
      })
    );
  }

  getClassification(product: any){
    return product?.classifications.find((x: any) => x.code == 'seoMetaClassificationDataClass');
  }

  /**
   * Resolves the page description for the Product Detail Page. The description
   * is based on the `product.summary`.
   */
  resolveDescription(): Observable<string> {
    return this.product$.pipe(
      switchMap((product) =>
        this.translation.translate('pageMetaResolver.product.description', {
          description: product.summary,
        })
      )
    );
  }

  /**
   * Resolves breadcrumbs for the Product Detail Page. The breadcrumbs are driven by
   * a static home page crumb and a crumb for each category.
   */
  resolveBreadcrumbs(): Observable<BreadcrumbMeta[]> {
    return combineLatest([
      this.product$.pipe(),
      this.translation.translate('common.fullCatalog'),
    ]).pipe(
      map(([product, label]) => {
        const breadcrumbs = [];
        breadcrumbs.push({ label: label, link: label === 'Full Catalog' ? '/catalog':'/' });
        this.routingService.getRouterState().subscribe((res:any) => {
          if(res?.state?.queryParams?.origin?.toLowerCase()?.indexOf('brands') > -1) {
            breadcrumbs.push({
              label: (product as any)?.brandName              ,
              link: (product as any)?.brandUrl,
            } as BreadcrumbMeta);
          } else {
            const pc = product.categories.find(c => c.code === `${CATEGORY_PREFIX}${res.state?.queryParams?.origin}`);
              if(pc) {
                const { name, code, categoryUrl } = pc as any;
                breadcrumbs.push({
                  label: name || code,
                  link: categoryUrl,
                } as BreadcrumbMeta);
              }
          }
        })
        return breadcrumbs;
      })
    );
  }

  /**
   * Resolves the main page image for the Product Detail Page. The product image
   * is based on the PRIMARY product image. The zoom format is used by default.
   */
  resolveImage(): Observable<string> {
    return this.product$.pipe(
      map((product) => (<any>product.images['PRIMARY'])?.zoom?.url ?? null)
    );
  }

  protected resolveFirstCategory(product: Product): string {
    const firstCategory: Category | undefined = product?.categories?.[0];

    return firstCategory
      ? ` | ${firstCategory.name || firstCategory.code}`
      : '';
  }

  protected resolveManufacturer(product: Product): string {
    return product.manufacturer ? ` | ${product.manufacturer}` : '';
  }

  resolveRobots(): Observable<PageRobotsMeta[]> {
    return this.basePageMetaResolver.resolveRobots();
  }

  /**
   * Resolves the canonical url for the product page using the default canonical url
   * configuration.
   *
   * In case of a variant product, the baseProduct code is used to resolve the url. It's important
   * to know that this has a few limitations:
   * - We're not always able to get the super baseProduct, in case of multi-level variants.
   *   OCC only exposes the direct baseProduct, which might still not resolve in the correct
   *   canonical URL. This is business driven and subject to change in a customization.
   * - The url resolved for the variant doesn't contain any content other then the product code.
   *   This means that we do not provide any product data to resolve pretty URLs (for example
   *   the product title).
   */
  resolveCanonicalUrl(): Observable<string> {
    return this.product$.pipe(
      switchMap((product) => this.findBaseProduct(product)),
      map((product) => {
        const url = this.routingService.getFullUrl({
          cxRoute: 'product',
          params: product,
        });
        return this.pageLinkService.getCanonicalUrl({}, url);
      })
    );
  }

  /**
   * Resolves the base product whenever the given product is a variant product.
   *
   * Since product variants can be multi-layered, we recursively try to find the base product
   * this might be too opinionated for your business though.
   */
  protected findBaseProduct(product: Product): Observable<Product> {
    if (product?.baseProduct) {
      return this.productService
        .get(product.baseProduct, ProductScope.LIST)
        .pipe(
          filter(isNotUndefined),
          switchMap((baseProduct) => this.findBaseProduct(baseProduct))
        );
    }
    return of(product);
  }
}
