import { Injectable } from '@angular/core';
import { ActiveCartFacade } from '@spartacus/cart/base/root';
import {
  PageMetaResolver,
  PageHeadingResolver,
  PageTitleResolver,
  PageDescriptionResolver,
  PageRobotsResolver,
  TranslationService,
  BasePageMetaResolver,
  PageType,
  PageRobotsMeta,
  BreadcrumbMeta,
  RoutingService,
} from '@spartacus/core';
import { Observable, of } from 'rxjs';
import { WsBasePageMetaResolver } from './base-page-meta-resolver';

const checkout = [
  {
    url: 'ShippingAddresses',
    breadcrumb: 'Shipping Address'
  },
  {
    url: 'ShippingMethods',
    breadcrumb: 'Shipping Method'
  },
  {
    url: 'PaymentMethods',
    breadcrumb: 'Payment Method'
  },
  {
    url: 'ReviewOrder',
    breadcrumb: 'Order Review'
  },
  {
    url: 'completed',
    breadcrumb: 'Order Confirmation'
  }
]

@Injectable({
  providedIn: 'root',
})
export class WsCheckoutPageMetaResolver
  extends PageMetaResolver
  implements
    PageHeadingResolver,
    PageTitleResolver,
    PageDescriptionResolver,
    PageRobotsResolver
{
  constructor(
    protected translationService: TranslationService,
    protected activeCartFacade: ActiveCartFacade,
    protected basePageMetaResolver: WsBasePageMetaResolver,
    protected routingService: RoutingService,
  ) {
    super();
    this.pageType = PageType.CONTENT_PAGE;
    this.pageTemplate = 'MultiStepCheckoutSummaryPageTemplate';
  }

  /**
   * @override
   * Resolves the page title for the Checkout Page to include checkout step.
   * The page title used by the browser (history, tabs) and crawlers.
   *
   * The title from the page data is ignored for this page title.
   */
  resolveTitle(): Observable<string | undefined> {
    if (this.basePageMetaResolver.resolveTitle) {
      return this.basePageMetaResolver.resolveTitle();
    }
    return of(undefined);
  }

  /**
   * Resolves breadcrumbs for the Product Detail Page. The breadcrumbs are driven by
   * a static home page crumb and a crumb for each category.
   */
  resolveBreadcrumbs(): Observable<BreadcrumbMeta[]> {
    const breadcrumbs = [];
    breadcrumbs.push({ label: 'Cart', link: '/' } as BreadcrumbMeta);
    return of(breadcrumbs);
  }
  /**
   * Resolves the page heading for the Checkout Page.
   * The page heading is used in the UI (`<h1>`), where as the page
   * title is used by the browser and crawlers.
   */
  resolveHeading(): Observable<string> {
    return this.translationService.translate('pageMetaResolver.checkout.title');
  }

  resolveDescription(): Observable<string | undefined> {
    return this.basePageMetaResolver.resolveDescription();
  }

  resolveRobots(): Observable<PageRobotsMeta[]> {
    return this.basePageMetaResolver.resolveRobots();
  }
}
