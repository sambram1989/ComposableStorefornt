import {
  Component,
  ChangeDetectionStrategy,
  HostBinding,
  Input,
} from '@angular/core';
import { Breadcrumb } from '@spartacus/core';
import { FacetList, ICON_TYPE } from '@spartacus/storefront';
import { Observable } from 'rxjs';
import { WsFacetService } from '../services/facet.service';
import { Router } from '@angular/router';

@Component({
  selector: 'ws-active-facets',
  templateUrl: './active-facets.component.html',
  changeDetection: ChangeDetectionStrategy.Default,
})
export class WsActiveFacetsComponent {
  @HostBinding('attr.role') role = 'group';
  @HostBinding('attr.aria-label') label = 'cx-active-facets-groupName';
  isMedpaxPage: boolean = this.router.url.includes('/medpax');
  isWebinarPage: boolean = this.router.url.includes('/webinars')
  /** Active facets which are applied to the product results. */
  facetList$: Observable<FacetList> = this.facetService.facetList$;

  /** Configurable icon which is used for the active facet close button */
  @Input() closeIcon = ICON_TYPE.CLOSE;
  @Input() isListPage = false;

  constructor(
    protected facetService: WsFacetService,
    protected router: Router
  ) {}

  getLinkParams(facet: Breadcrumb) {
    if (this.isMedpaxPage) {
      this.facetService?.setFilterList(
        this.facetService.getLinkParams(facet.removeQuery?.query?.value ?? '')
      );
      return null;
    } else {
      return this.facetService.getLinkParams(
        facet.removeQuery?.query?.value ?? ''
      );
    }
  }

  /**
   * The focus key is used to persist the focus on the facet when the DOM is being
   * recreated. We only apply the focus key for the given _active_ facet when there
   * the original facets is not available. This happens for non multi-valued facets.
   *
   * With this approach, the we keep the focus, either at the facet list or on the
   * active facets.
   */
  getFocusKey(facetList: FacetList, facet: Breadcrumb) {
    return facetList.facets?.find((f) =>
      f.values?.find((val) => val.name === facet.facetValueName)
    )
      ? ''
      : facet.facetValueName;
  }

  /**
   * Purpose of this function is to allow keyboard users to click on a filter they
   * wish to remove by pressing spacebar. Event not handled natively by <a> elements.
   *
   * @param event spacebar keydown
   */
  removeFilterWithSpacebar(event?: Event): void {
    event?.preventDefault(); // Avoid spacebar scroll
    event?.target?.dispatchEvent(new MouseEvent('click', { cancelable: true }));
  }

  resetFilters() {
    if (this.isMedpaxPage) { 
      this.facetService?.setFilterList(this.facetService.getLinkParams(''));
    } else if (this.isWebinarPage) {
      this.router.navigate([], {
        queryParams: {},
        relativeTo: this.router.routerState.root,
      });
    }
    
    sessionStorage.setItem('selectedFilter', '');
    this.facetService.isFilterSelected.next(true);
  }

  removeFilter(facet: any) {
    sessionStorage.setItem('selectedFilter', '&query=' + facet.removeQuery?.query?.value || '');
    this.facetService.isFilterSelected.next(true);
  }
}
