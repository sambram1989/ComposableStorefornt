import { Injectable } from "@angular/core";
import { Router, NavigationEnd } from "@angular/router";
import { PageTitleResolver, PageDescriptionResolver, PageBreadcrumbResolver, PageRobotsResolver, CanonicalPageResolver, CmsService, TranslationService, RoutingPageMetaResolver, PageLinkService, Page, PageRobotsMeta, BreadcrumbMeta, CanonicalUrlOptions } from "@spartacus/core";
import { Observable, defer, filter, map, combineLatest, shareReplay, startWith } from "rxjs";
import { WsCurrentUserService } from "../../../../../ws-services/state-services/currentuser.service";
export interface KeywordsPageResolver {
  resolveKeywords(): Observable<string | undefined>;
}
export interface CustomPage extends Page {
  keywords?: string;
}
@Injectable({
  providedIn: 'root',
})
export class WsBasePageMetaResolver
  implements
    PageTitleResolver,
    PageDescriptionResolver,
    PageBreadcrumbResolver,
    PageRobotsResolver,
    CanonicalPageResolver,
    KeywordsPageResolver
{
  constructor(
    protected cmsService: CmsService,
    protected translation: TranslationService,
    protected routingPageMetaResolver: RoutingPageMetaResolver,
    protected router: Router,
    protected pageLinkService: PageLinkService
  ) {}

  /**
   * Helper to provide access to the current CMS page
   */
  protected page$: Observable<Page> = defer(() =>
    this.cmsService.getCurrentPage()
  ).pipe(filter((p) => Boolean(p)));

  protected title$: Observable<string | undefined> = this.page$.pipe(
    map((p) => {
        return p.title === 'Dashboard' ? 'Practitioner Dashboard' : p.title;
    })
  );

  protected description$: Observable<string | undefined> = this.page$.pipe(
    map((p) => p.description)
  );

  protected keywords$: Observable<string | undefined> = this.page$.pipe(
    map((p) => p.properties)
  );

  protected robots$: Observable<PageRobotsMeta[]> = this.page$.pipe(
    map((page) => page.robots || [])
  );

  /**
   * Breadcrumb for the home page.
   * category, plp, slp,pdp - home will be full cataloge
   */

  protected homeBreadcrumb$: Observable<BreadcrumbMeta[]> = this.translation
    .translate('common.dashboard')
    .pipe(map((label) => [{ label: label, link: '/' }] as BreadcrumbMeta[]));

  /**
   * All the resolved breadcrumbs (including those from Angular child routes).
   */
  protected breadcrumb$: Observable<BreadcrumbMeta[]> = combineLatest([
    this.homeBreadcrumb$,
    defer(() => this.routingPageMetaResolver?.resolveBreadcrumbs()),
  ]).pipe(
    map((breadcrumbs) => {
      return breadcrumbs.flat();
    }),
    shareReplay({ bufferSize: 1, refCount: true })
  );

  resolveTitle(): Observable<string | undefined> {
    return this.title$;
  }

  resolveDescription(): Observable<string | undefined> {
    return this.description$;
  }

  resolveKeywords(): Observable<string | undefined> {
    return this.keywords$;
  }

  /**
   * Resolves a single breadcrumb item to the home page for each `ContentPage`.
   * The home page label is resolved from the translation service.
   */
  resolveBreadcrumbs(): Observable<BreadcrumbMeta[] | undefined> {
    return this.breadcrumb$;
  }

  resolveRobots(): Observable<PageRobotsMeta[]> {
    return this.robots$;
  }

  resolveCanonicalUrl(options?: CanonicalUrlOptions): Observable<string> {
    return this.router.events.pipe(
      filter((ev) => ev instanceof NavigationEnd),
      startWith(null),
      map(() => this.pageLinkService.getCanonicalUrl(options))
    );
  }
}
