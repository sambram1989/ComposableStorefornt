
import { Injectable } from '@angular/core';
import { Meta, Title } from '@angular/platform-browser';
import { BreadcrumbMeta, PageMeta, PageMetaService, PageRobotsMeta } from '@spartacus/core';
import { PageMetaLinkService, SeoMetaService } from '@spartacus/storefront';
import { WsPageMetaService } from '../../ws-components/ws-spartacus/ws-breadcrumb/breadcrumb/resolvers/page-meta.service';

interface ResolvePageMeta {
  title?: string;
  heading?: string;
  description?: string;
  robots?: PageRobotsMeta[];
  image?: string;
  breadcrumbs?: BreadcrumbMeta[];
  canonicalUrl?: string;
  keywords?: string;
}
@Injectable({
  providedIn: 'root',
})
export class WsSeoMetaService extends SeoMetaService {
  constructor(
    ngTitle: Title,
    ngMeta: Meta,
    pageMetaService: WsPageMetaService,
    pageMetaLinkService?: PageMetaLinkService
  ) {
    super(ngTitle, ngMeta, pageMetaService as unknown as PageMetaService, pageMetaLinkService);
  }

  customWrapper(meta: ResolvePageMeta) {this.meta = meta}

  override set meta(meta: ResolvePageMeta) {
    this.title = meta.title;
    this.description = meta.description;
    this.image = meta.image;
    this.robots = meta.robots;
    this.canonicalUrl = meta.canonicalUrl;
    this.keyword = meta.keywords;
  }

  protected set keyword(value: string | undefined) {
    if (value) {
      this.addTag({ name: 'keywords', content: value || '' });
    } else {
      this.ngMeta.removeTag('name="keywords"');
    }
  }
}
