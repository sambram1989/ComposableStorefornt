import { Injectable } from "@angular/core";
import { PageMetaResolver, PageTitleResolver, PageRobotsResolver, RoutingService, ProductSearchService, TranslationService, PageType, PageRobotsMeta } from "@spartacus/core";
import { Observable } from "rxjs";
import { WsBasePageMetaResolver } from "./base-page-meta-resolver";

@Injectable({
  providedIn: 'root',
})
export class WsSearchPageMetaResolver
  extends PageMetaResolver
  implements PageMetaResolver, PageTitleResolver, PageRobotsResolver
{
 
  constructor(
    protected routingService: RoutingService,
    protected productSearchService: ProductSearchService,
    protected translation: TranslationService,
    protected basePageMetaResolver: WsBasePageMetaResolver
  ) {
    super();
    this.pageType = PageType.CONTENT_PAGE;
    this.pageTemplate = 'SearchResultsListPageTemplate';
  }

  resolveTitle(): Observable<string> {
   return  this.basePageMetaResolver.resolveTitle();
  }

  resolveRobots(): Observable<PageRobotsMeta[]> {
    return this.basePageMetaResolver.resolveRobots();
  }
}
