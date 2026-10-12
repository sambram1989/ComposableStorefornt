import { AfterViewInit, ChangeDetectorRef, Component, DestroyRef, inject, OnDestroy, Optional } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { ActivatedRoute, Router } from '@angular/router';
import { CmsService, CmsSearchBoxComponent, WindowRef, RoutingService } from '@spartacus/core';
import { CmsComponentData, SearchBoxComponent, SearchBoxComponentService, SearchResults } from '@spartacus/storefront';
import { debounceTime, Observable, Subject, switchMap, takeUntil } from 'rxjs';
import { CustomSearchboxService } from '../../../ws-services/ws-custom-search.service';
import { Location } from '@angular/common';

@Component({
  selector: 'ws-search-page',
  templateUrl: './ws-search-page.component.html',
  styleUrl: './ws-search-page.component.scss'
})


export class WsSearchPageComponent extends SearchBoxComponent implements OnDestroy, AfterViewInit {

  private destroyRef = inject(DestroyRef);
  componentListArray:any =[];
  searchTerm: string = '';
  activeTabId: string = 'wsBodySearchAllParagraph';
  showAll: boolean = false;
  resultsCount: number;
  private unsubscribe$ = new Subject<void>();

  constructor(private cd: ChangeDetectorRef, public cmsService: CmsService,
    searchBoxComponentService: SearchBoxComponentService, 
    @Optional() componentData: CmsComponentData<CmsSearchBoxComponent>, winRef: WindowRef, 
    routingService: RoutingService,
    private activatedRoute: ActivatedRoute,
    private location: Location
  ) {
    super(searchBoxComponentService, componentData, winRef, routingService);
    this.getCmsdata();
     this.activatedRoute.paramMap.pipe(
      debounceTime(500),
      switchMap((params: any) => {
        this.searchTerm = params.get('query');
        this.cd.detectChanges();
        return [];
      }),
      takeUntil(this.unsubscribe$)
    ).subscribe();
    this.activatedRoute.queryParams.subscribe((params: any) => {
      const type = params?.type || '';
      if(type) {
        const activeTab = type?.replace?.('Global', 'Body') || '';
        activeTab && this.changeTab(activeTab);
        this.showAll = false;
      }
    });
  }

  ngAfterViewInit(): void {
    if(document.getElementsByTagName('title') && !document.getElementsByTagName('title')[0].innerText) document.getElementsByTagName('title')[0].innerText = 'Search Page';
  }
  toggleShowAll() {
    this.showAll = true;
    this.cd.markForCheck();
  }

  getCmsdata() {
    this.cmsService.getComponentData('wsSearchBoxBodyTabContainer')
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe((data: any) => {
        const componentList = data.components?.split(' ');
        this.componentListArray = componentList;
      });
  }

  getActiveClass(uid: string) {
    return this.activeTabId === uid;
  }

  changeTab(uid: string) {
    this.activeTabId = uid;
    this.showAll = false;
    this.cd.markForCheck();
  }

  updateResultsCount(count: number) {
    this.resultsCount = count
  }

  changeSelectedTab(e: any) {
    this.activeTabId = e?.target?.value;
    this.showAll = false;
    this.cd.markForCheck();
  }

  viewAll(activeTab: string) {
    this.activeTabId = activeTab;
    this.cd.markForCheck();
  }

  searching(event: any) {
    const searchTerm = event?.target?.value;
    this.searchTerm = searchTerm;
    this.location.replaceState(`/search/${this.searchTerm}?type=${this.activeTabId}`);
  }

  clearSearch() {
    this.searchTerm = '';
    this.location.replaceState(`/search/${this.searchTerm}?type=${this.activeTabId}`);
  }

  override ngOnDestroy(): void {
    this.unsubscribe$.next();
    this.unsubscribe$.complete();
  }
}
