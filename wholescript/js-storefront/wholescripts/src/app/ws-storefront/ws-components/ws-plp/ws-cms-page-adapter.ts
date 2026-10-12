import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import {
  PageType,
  OccEndpointsService,
  ConverterService,
  PageContext,
  HOME_PAGE_CONTEXT,
  SMART_EDIT_CONTEXT,
  OccCmsPageAdapter,
} from '@spartacus/core';

export interface OccCmsPageRequest {
  pageLabelOrId?: string;
  pageType?: PageType;
  code?: string;
  fields?: string;
  url?: string;
}

@Injectable({
  providedIn: 'root',
})
export class WsOccCmsPageAdapter extends OccCmsPageAdapter {
  constructor(
    http: HttpClient,
    occEndpoints: OccEndpointsService,
    converter: ConverterService
  ) {
    super(http, occEndpoints, converter);
  }

  protected override getPagesRequestParams(
    context: PageContext
  ): OccCmsPageRequest {
    if (context.id === HOME_PAGE_CONTEXT || context.id === SMART_EDIT_CONTEXT) {
      return {};
    }
    // wholescript specific category and shopify page params
    if (context.id.indexOf('shopify/catalog') > -1) {
      const catalogHttpParams: OccCmsPageRequest = {};
      catalogHttpParams.pageLabelOrId = context.id;
      catalogHttpParams.pageType = PageType.CONTENT_PAGE;
      return catalogHttpParams;
    } else if (context.id.indexOf('catalog') > -1) {
      const catalogHttpParams: OccCmsPageRequest = {};
      catalogHttpParams.url = context.id;
      catalogHttpParams.code = '';
      catalogHttpParams.pageType = PageType.CATEGORY_PAGE;
      return catalogHttpParams;
    }

    const httpParams: OccCmsPageRequest = {};
    if (context.type) {
      httpParams.pageType = context.type;
    }
    if (context.type === PageType.CONTENT_PAGE) {
      httpParams.pageLabelOrId = context.id;
    } else {
      httpParams.code = context.id;
    }

    return httpParams;
  }
}
