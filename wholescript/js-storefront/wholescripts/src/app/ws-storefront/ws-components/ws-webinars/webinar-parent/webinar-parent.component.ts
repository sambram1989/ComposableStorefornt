import { Component, DestroyRef, inject, signal, TemplateRef, ViewChild, ViewContainerRef } from '@angular/core';
import { useFeatureStyles } from '@spartacus/core';
import { ViewModes, ViewConfig } from '@spartacus/storefront';
import { Subscription, BehaviorSubject, debounceTime, distinctUntilChanged, map } from 'rxjs';
import { WebinarlistService } from '../webinarlist.service';
import { ActivatedRoute, Router } from '@angular/router';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { WsOverlay } from '../../../ws-services/ws-overlay.service';
import { WsCommonFacetService } from '../../ws-spartacus/product-facet-navigation/services/ws-common-facet.service';
import { TemplatePortal } from '@angular/cdk/portal';
import { ProtocolService } from '../../../ws-services/protocol/protocol.service';
import { WsUtilService } from '../../../ws-services/utils/utilService.service';
import { WsFacetService } from '../../ws-spartacus/product-facet-navigation/services/facet.service';


@Component({
  selector: 'ws-webinar-parent',
  templateUrl: './webinar-parent.component.html',
  styleUrls: ['../../protocal/edit-protocol/edit-protocol.component.scss', './webinar-parent.component.scss']
})
export class WebinarParentComponent {
  private activatedRoute = inject(ActivatedRoute);
  private destroyRef = inject(DestroyRef);
  private overlayService = inject(WsOverlay);
  private vcr = inject(ViewContainerRef);
  private router = inject(Router);
  private commonFacet = inject(WsCommonFacetService);
  private protocolService = inject(ProtocolService);
  private utilService = inject(WsUtilService);
  private subscription = new Subscription();

  sortByPortal: any;
  filterPortal: any;
  @ViewChild('filterPopUp') filterProtocol: TemplateRef<any>;
  @ViewChild('sortBy') sortByProtocol: TemplateRef<any>;

  isInfiniteScroll: boolean | undefined;
  viewMode$ = new BehaviorSubject<ViewModes>(ViewModes.Grid);
  ViewModes = ViewModes;
  webinars = signal<any>({});
  featuredWebinar = signal<any>({});

  pageSizeArray = [{
    code: '12',
    selected: true,
    name: '12'
  }, {
    code: '24',
    selected: false,
    name: '24'
  }, {
    code: '48',
    selected: false,
    name: '48'
  }, {
    code: '96',
    selected: false,
    name: '96'
  }];

  QueryString = '';

  imagePrefix = this.protocolService.getBaseUrl();


  constructor(
    public scrollConfig: ViewConfig,
    public service: WebinarlistService,
    protected facetService: WsFacetService
  ) {
    useFeatureStyles('a11ySortingOptionsTruncation');
    useFeatureStyles('a11yTruncatedTextForResponsiveView');
  }

  ngOnInit(): void {
    this.isInfiniteScroll = this.scrollConfig.view?.infiniteScroll?.active;
    this.router.navigate([], {
      relativeTo: this.activatedRoute,
      queryParams: { currentPage: 0 },
      queryParamsHandling: 'merge',
      replaceUrl: true
    });
    this.updateQueriesFromRouter();
  }

  ngAfterViewInit() {
    this.filterPortal = new TemplatePortal(this.filterProtocol, this.vcr);
    this.sortByPortal = new TemplatePortal(this.sortByProtocol, this.vcr);
    if(document.getElementsByTagName('title') && !document.getElementsByTagName('title')[0].innerText) document.getElementsByTagName('title')[0].innerText = 'Webinar';
  }

  loadWebinars() {
    this.service.fetchWebinars(this.QueryString).pipe(
      takeUntilDestroyed(this.destroyRef),
      map((response: any) => this.processResponse(response))
    )
      .subscribe((data: any) => {
        this.webinars.set(data);
        this.commonFacet.currentSearchList$.next(data);
        this.loadFeaturedWebinars();
      });
  }

  loadFeaturedWebinars() {
    this.service.fetchFeaturedWebinars().pipe(takeUntilDestroyed(this.destroyRef)).subscribe((res: any) => {                            
      this.featuredWebinar.set(res);
    });
  }

  private processResponse(response: any) {
    response.sorts = this.formatSorts(response?.sorts);
    response.results = this.formatResults(response?.results);
    const parsedPresenters = this.utilService.parseKeyValueArray(response?.meetPresenter);
    response.meetPresenter = this.removeDuplicatesByCode(parsedPresenters);
    response.facets = this.removeFacets(response?.facets);
    return response;
  }

  private removeDuplicatesByCode(items: any[]): any[] {
    const seen = new Set();
    return items.filter(item => {
      if (item?.code && !seen.has(item.code)) {
        seen.add(item.code);
        return true;
      }
      return false;
    });
  }

  private formatSorts(sorts: any[]) {
    return sorts.map((sort: any) => ({
      ...sort,
      name: sort.code.charAt(0).toUpperCase() + sort.code.slice(1)
    }));
  }

  private formatResults(results: any[]) {
    return results.map((result: any) => ({
      ...result,
      videoImage: `${this.imagePrefix}${result.videoImage}`,
      presenter:this.utilService.parseKeyValueArray(result.presenter)
    }));
  }

  private removeFacets(response:any[]){
   return response?.filter((item:any) => item.name !== "presenter");
  }

  updateQueriesFromRouter() {
    this.activatedRoute.queryParams.pipe(
      debounceTime(300),
      distinctUntilChanged(),
      takeUntilDestroyed(this.destroyRef)
    ).subscribe(() => {
      this.QueryString = this.getQueryString(this.router.url) || '';
      this.QueryString = this.QueryString.replace('?', '&');
      this.loadWebinars();
    });
  }

  private getQueryString(url: string): string {
    const questionMarkIndex = url.indexOf('?');
    return questionMarkIndex !== -1 ? url.substring(questionMarkIndex) : '';
  }

  setViewMode(mode: ViewModes): void {
    this.viewMode$.next(mode);
  }

  //Updating the search and filters
  sortList(sort: string): void {
    this.service.sort(sort);
  }
  updateSearch(key: string) {
    this.service.query(key);
  }
  pageChanged(event: any) {
    this.service.currentPage(event);
  }

  pageSizeChanged(event: any) {
    this.service.updatePageSize(event);
  }
  
  //Updating the search and filters

  sortByPopup() {
    this.overlayService.openModal(this.sortByPortal);
  }
  filterPopup() {
    this.overlayService.openModal(this.filterPortal);
  }
  closePopup() {
    this.overlayService.closeTopModal();
  }
  ngOnDestroy(): void {
    this.subscription.unsubscribe();
    sessionStorage.removeItem('selectedFilter');
  }

  updateFilter() {
    let data = sessionStorage.getItem('selectedFilter')!;
    if (data) this.QueryString = data;
    else this.QueryString = '';

    this.loadWebinars();
  }
}
