import { Router } from '@angular/router';
import { Injectable } from '@angular/core';
import { CmsNavigationComponent, CmsService, SemanticPathService } from '@spartacus/core';
import { combineLatest, Observable, of } from 'rxjs';
import { filter, map, switchMap, tap } from 'rxjs/operators';

export interface WsNavigationNode {
  media?: any;
  isCurrentUrl?: boolean;
  uid?: string;
  active?: boolean;
  title?: string;
  url?: string | string[];
  target?: string;
  children?: Array<WsNavigationNode>;
  styleClasses?: string;
  styleAttributes?: string;
}


@Injectable({
  providedIn: 'root',
})
export class WsNavigationService {
  constructor(protected cmsService: CmsService, protected semanticPathService: SemanticPathService, protected router: Router) {}

  public createNavigation(data$: Observable<CmsNavigationComponent>): Observable<WsNavigationNode> {
    return combineLatest([data$, this.getNavigationNode(data$)]).pipe(
      map(([data, nav]) => {
        return data
          ? {
              title: data.name,
              children: [nav],
            }
          : undefined;
      })
    );
  }

  /**
   * returns an observable with the `NavigationNode` for the given `CmsNavigationComponent`.
   * This function will load the navigation underlying entries and children if they haven't been
   * loaded so far.
   */
  public getNavigationNode(data$: Observable<CmsNavigationComponent>): Observable<WsNavigationNode> {
    if (!data$) {
      return of();
    }
    return data$.pipe(
      filter((data) => !!data),
      switchMap((data) => {
        const navigation = data.navigationNode ? data.navigationNode : data;
        return this.cmsService.getNavigationEntryItems(navigation.uid).pipe(
          tap((items) => {
            if (items === undefined) {
              this.loadNavigationEntryItems(navigation, true);
            } else {
              // we should check whether the existing node items are what expected
              const expectedItems: any[] = [];
              this.loadNavigationEntryItems(navigation, false, expectedItems);
              const existingItems = Object.keys(items).map((key) => items[key].uid);
              const missingItems = expectedItems.filter((it) => !existingItems.includes(it.id));
              if (missingItems.length > 0) {
                this.cmsService.loadNavigationItems(navigation.uid, missingItems);
              }
            }
          }),
          filter(Boolean),
          map((items) => this.populateNavigationNode(navigation, items))
        );
      })
    );
  }

  /**
   * Loads all navigation entry items' type and id. Dispatch action to load all these items
   * @param nodeData
   * @param root
   * @param itemsList
   */
  private loadNavigationEntryItems(nodeData: any, root: boolean, itemsList: any[] = []): void {
    if (nodeData.entries && nodeData.entries.length > 0) {
      nodeData.entries.forEach((entry: any) => {
        itemsList.push({
          superType: entry.itemSuperType,
          id: entry.itemId,
        });
      });
    }

    if (nodeData.children && nodeData.children.length > 0) {
      nodeData.children.forEach((child: any) => this.loadNavigationEntryItems(child, false, itemsList));
    }

    if (root) {
      this.cmsService.loadNavigationItems(nodeData.uid, itemsList);
    }
  }

  /**
   * Create a new node tree for the view
   * @param nodeData
   * @param items
   */
  private populateNavigationNode(nodeData: any, items: any): WsNavigationNode {
    const node: WsNavigationNode = {};

    if (nodeData.entries && nodeData.entries.length > 0) {
      this.populateLink(node, nodeData.entries[0], items);
    }

    if (nodeData.children?.length > 0) {
      const children = nodeData.children.map((child: any) => this.populateNavigationNode(child, items)).filter(Boolean);
      if (children.length > 0) {
        node.children = children;
      }
    }
    // return null in case there are no children
    return Object.keys(node).length === 0 ? null : node;
  }

  /**
   * The node link is driven by the first entry.
   */
  private populateLink(node: WsNavigationNode, entry: any, items: any[]) {
    //@ts-ignore
    const item = items[`${entry.itemId}_${entry.itemSuperType}`];

    // now we only consider BannerComponent
    if (item && entry.itemType === 'BannerComponent') {
      if (!node.title) {
        node.title = item.headline;
      }
      const url = item.urlLink;
      node.media = item.media;
      // only populate the node link if we have a visible node
      if (node.title && url) {
        node.url = url;
        if (this.router.url === node.url) {
          node.isCurrentUrl = true;
        } else {
          node.isCurrentUrl = false;
        }
        // the backend provide boolean value for the target
        // in case the link should be opened in a new window
        if (item.target === 'true' || item.target === true) {
          node.target = '_blank';
        }

        if (item.uid) {
          node.uid = item.uid;
        }
      }
      // populate style classes to apply CMS driven styling
      if (item.styleClasses) {
        node.styleClasses = item.styleClasses;
      }
      // populate style attributes to apply CMS driven styling
      if (item.styleAttributes) {
        node.styleAttributes = item.styleAttributes;
      }
      if (item.media) {
        node.media = item.media;
      }
    }
     // only if CMSLinkComponent
     if (item && entry.itemType === 'CMSLinkComponent') {
      if (!node.title) {
        node.title = item.linkName;
      }
      const url = this.getLink(item);
      // only populate the node link if we have a visible node
      if (node.title && url) {
        node.url = url;
        // the backend provide boolean value for the target
        // in case the link should be opened in a new window
        if (item.target === 'true' || item.target === true) {
          node.target = '_blank';
        }
      }
      // populate style classes to apply CMS driven styling
      if (item.styleClasses) {
        node.styleClasses = item.styleClasses;
      }
      // populate style attributes to apply CMS driven styling
      if (item.styleAttributes) {
        node.styleAttributes = item.styleAttributes;
      }
    }
  }
  /**
   *
   * Gets the URL or link to a related item (category),
   * also taking into account content pages (contentPageLabelOrId)
   * and product pages (productCode)
   */
  protected getLink(item: any) {
    if (item.url) {
      return item.url;
    } else if (item.contentPageLabelOrId) {
      return item.contentPageLabelOrId;
    } else if (item.categoryCode) {
      return this.semanticPathService.transform({
        cxRoute: 'category',
        params: {
          code: item.categoryCode,
          name: item.name,
        },
      });
    } else if (item.productCode) {
      return this.semanticPathService.transform({
        cxRoute: 'product',
        params: {
          code: item.productCode,
          name: item.name,
        },
      });
    }
  }
}
