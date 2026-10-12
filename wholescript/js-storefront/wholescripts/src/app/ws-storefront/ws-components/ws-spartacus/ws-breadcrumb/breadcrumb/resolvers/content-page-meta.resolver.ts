import { Injectable } from "@angular/core";
import { PageMetaResolver, PageTitleResolver, PageDescriptionResolver, PageBreadcrumbResolver, PageRobotsResolver, CanonicalPageResolver, BasePageMetaResolver, PageType, BreadcrumbMeta, PageRobotsMeta } from "@spartacus/core";
import { Observable } from "rxjs";
import { WsBasePageMetaResolver } from "./base-page-meta-resolver";

@Injectable({
  providedIn: 'root',
})
export class WsContentPageMetaResolver
  extends PageMetaResolver
  implements
    PageTitleResolver,
    PageDescriptionResolver,
    PageBreadcrumbResolver,
    PageRobotsResolver,
    CanonicalPageResolver
{
  constructor(protected basePageMetaResolver: WsBasePageMetaResolver) {
    super();
    this.pageType = PageType.CONTENT_PAGE;
  }

  resolveTitle(): Observable<string | undefined> {
    return this.basePageMetaResolver.resolveTitle();
  }

  resolveDescription(): Observable<string | undefined> {
    return this.basePageMetaResolver.resolveDescription();
  }

  resolveBreadcrumbs(): Observable<BreadcrumbMeta[] | undefined> {
    return this.basePageMetaResolver.resolveBreadcrumbs();
  }

  resolveRobots(): Observable<PageRobotsMeta[]> {
    return this.basePageMetaResolver.resolveRobots();
  }

  resolveCanonicalUrl(): Observable<string | undefined> {
    return this.basePageMetaResolver.resolveCanonicalUrl();
  }

  resolveKeywords(): Observable<string | undefined> {
    return this.basePageMetaResolver.resolveKeywords();
  }
}
