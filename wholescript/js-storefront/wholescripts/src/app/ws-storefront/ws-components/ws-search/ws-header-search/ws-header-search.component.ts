import { AfterViewInit, ChangeDetectorRef, Component, DestroyRef, ElementRef, inject, Input, OnDestroy, OnInit, Optional, Renderer2, signal, TemplateRef, ViewChild } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { CmsSearchBoxComponent, WindowRef, RoutingService, PageType, CmsService } from '@spartacus/core';
import { SearchBoxConfig, ICON_TYPE, SearchBoxComponentService, CmsComponentData, SearchResults, SearchBoxSuggestionSelectedEvent, SearchBoxProductSelectedEvent } from '@spartacus/storefront';
import { Subscription, Observable, of, map, tap, switchMap, filter, debounceTime, distinctUntilChanged, Subject } from 'rxjs';
import { CustomSearchboxService } from '../../../ws-services/ws-custom-search.service';
import { Router } from '@angular/router';
import { WsUtilService } from '../../../ws-services/utils/utilService.service';
import { WsCurrentUserService } from '../../../ws-services/state-services/currentuser.service';

const DEFAULT_SEARCH_BOX_CONFIG: SearchBoxConfig = {
  minCharactersBeforeRequest: 1,
  displayProducts: true,
  displaySuggestions: true,
  maxProducts: 5,
  maxSuggestions: 5,
  displayProductImages: true,
};

@Component({
  selector: 'ws-header-search',
  templateUrl: './ws-header-search.component.html',
  styleUrl: './ws-header-search.component.scss'
})
export class WsHeaderSearchComponent implements OnInit, OnDestroy, AfterViewInit {
  @Input() config: SearchBoxConfig;
  @ViewChild('searchInput') searchInput: any;
  searchResults: any[] = [];
  searchResultsWebinar: any[] = [];
  searchPrivateLabel: any[] = [];
  isSearchLoading: boolean = false;
  isFocused = false;
  private router = inject(Router);
  private utilService = inject(WsUtilService);
  /**
   * Sets the search box input field
   */
  @Input('queryText')
  set queryText(value: string) {
    if (value) {
      this.search(value);
    }
  }
  isPlUser = signal<boolean>(false);
  iconTypes = ICON_TYPE;

  /**
   * In some occasions we need to ignore the close event,
   * for example when we click inside the search result section.
   */
  private ignoreCloseEvent = false;
  chosenWord = '';
  public subscription: Subscription;

  private destroyRef = inject(DestroyRef);
  componentListArray: any = [];
  activeTabId: string = 'wsGlobalSearchProductParagraph';
  private searchSubject = new Subject<any>();

  @ViewChild('searchElement') searchElement: ElementRef | undefined;

  constructor(
    protected searchBoxComponentService: SearchBoxComponentService,
    @Optional()
    protected componentData: CmsComponentData<CmsSearchBoxComponent>,
    protected winRef: WindowRef,
    protected routingService: RoutingService,
    public cmsService: CmsService,
    private customSearchboxService: CustomSearchboxService,
    private wsCurrentUserService: WsCurrentUserService,
    private cd: ChangeDetectorRef,
    private renderer: Renderer2
  ) {
    this.getCmsdata();
  }

  ngAfterViewInit() {
    this.searchSubject.pipe(
      debounceTime(300),
      distinctUntilChanged()
    ).subscribe((data: { searchText: string, contentType: string }) => {
      const { searchText, contentType } = data;
      this.customSearchboxService.getWSSearchContent(searchText, contentType, 0, 5).pipe(takeUntilDestroyed(this.destroyRef)).subscribe({
        next: data => {
          this.searchResults = data?.results || [];
          this.isSearchLoading = false;
          if (contentType === 'Integrations') {
            if (this.searchResults?.length > 0) {
              this.searchResults = this.searchResults?.map((result: { integrations: string[]; }) => {
                if (result?.integrations?.length > 0) {
                  return this.utilService.parseKeyValueArray(result?.integrations, '; ')?.[0] || {};
                }
                return result;
              });
            }
          }
          this.cd.detectChanges();
        }, error: () => {
          this.isSearchLoading = false;
        }
      });
      this.customSearchboxService.getWebinarSSearchContent(searchText, 0, 5).pipe(takeUntilDestroyed(this.destroyRef)).subscribe({
        next: data => {
          this.searchResultsWebinar = data.results || [];
          this.isSearchLoading = false;
          this.cd.detectChanges();
        },
        error: () => {
          this.isSearchLoading = false;
        }
      });
      this.customSearchboxService.getPrivateLabelSearchContent(searchText, 0, 5).pipe(takeUntilDestroyed(this.destroyRef)).subscribe({
        next: data => {
          this.searchPrivateLabel = data.products || [];
          this.isSearchLoading = false;
          this.cd.detectChanges();
        },
        error: () => {
          this.isSearchLoading = false;
        }
      });
    });
    this.renderer.listen('document', 'click', (event: MouseEvent) => this.onClickOutside(event));
  }

  onFocus() {
    this.isFocused = true;
  }

  onBlur() {
    this.isFocused = false;
  }

  getCmsdata() {
    this.cmsService.getComponentData('wsglobalSearchBoxTabContainer')
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe((data: any) => {
        const componentList = data.components?.split(' ');
         if (!this.isPlUser()) {
          const index = componentList?.indexOf('wsGlobalSearchPrivateLabelParagraph');
          if (index !== -1) {
            componentList.splice(index, 1);
          }
        }
        this.componentListArray = componentList;
      });
  }

  /**
   * Returns the SearchBox configuration. The configuration is driven by multiple
   * layers: default configuration, (optional) backend configuration and (optional)
   * input configuration.
   */
  protected config$: Observable<SearchBoxConfig> = (
    this.componentData?.data$ || of({} as any)
  ).pipe(
    map((config) => {
      const isBool = (obj: SearchBoxConfig, prop: string): boolean =>
        obj?.[prop as keyof SearchBoxConfig] !== 'false' && obj?.[prop as keyof SearchBoxConfig] !== false;

      return {
        ...DEFAULT_SEARCH_BOX_CONFIG,
        ...config,
        displayProducts: isBool(config, 'displayProducts'),
        displayProductImages: isBool(config, 'displayProductImages'),
        displaySuggestions: isBool(config, 'displaySuggestions'),
        // we're merging the (optional) input of this component, but write the merged
        // result back to the input property, as the view logic depends on it.
        ...this.config,
      };
    }),
    tap((config) => (this.config = config))
  );

  results$: Observable<SearchResults> = this.config$.pipe(
    switchMap((config) => this.searchBoxComponentService.getResults(config))
  );

  ngOnInit(): void {
    this.subscription = this.routingService
      .getRouterState()
      .pipe(filter((data) => !data.nextState))
      .subscribe((data) => {
        if (
          !(
            data.state.context?.id === 'search' &&
            data.state.context?.type === PageType.CONTENT_PAGE
          )
        )
          this.chosenWord = '';

          this.clearSearchBox()
      });
      this.wsCurrentUserService
      .isRoleExist('pluser')
      .pipe(
        takeUntilDestroyed(this.destroyRef),
        tap((res) => {
          this.isPlUser.set(res)
        })
      )
      .subscribe();
  }

  /**
   * Closes the searchBox and opens the search result page.
   */
  search(query: string): void {
    this.searchSubject.next(query);
    this.searchBoxComponentService.search(query, this.config);
    if (this.activeTabId !== 'wsGlobalSearchProductParagraph') {
      this.setActiveTab(this.activeTabId);
    }
    // force the searchBox to open
    this.open();
  }

  resetSearchInput(): void {
    if (this.searchInput && this.searchInput.nativeElement) {
      this.searchInput.nativeElement.value = '';
    }
  }

  /**
   * Opens the type-ahead searchBox
   */
  open(): void {
    const labelElement = document.querySelector('#ws-header-search-wrapper label');
      if (labelElement) {
        labelElement.classList.add('label-bg');
    }
    this.searchBoxComponentService.toggleBodyClass('searchbox-is-active', true);
  }

  /**
   * Dispatch UI events for Suggestion selected
   *
   * @param eventData the data for the event
   */
  dispatchSuggestionEvent(eventData: SearchBoxSuggestionSelectedEvent): void {
    this.searchBoxComponentService.dispatchSuggestionSelectedEvent(eventData);
  }

  /**
   * Dispatch UI events for Product selected
   *
   * @param eventData the data for the event
   */
  dispatchProductEvent(eventData: SearchBoxProductSelectedEvent): void {
    this.searchBoxComponentService.dispatchProductSelectedEvent(eventData);
    this.resetSearchInput();
  }

  /**
   * Closes the type-ahead searchBox.
   */
  close(event: Event, force?: boolean): void {
    // Use timeout to detect changes
    setTimeout(() => {
      if ((!this.ignoreCloseEvent && !this.isSearchBoxFocused()) || force) {
        this.blurSearchBox(event);
      }
      const labelElement = document.querySelector('#ws-header-search-wrapper label');
      if (labelElement) {
        labelElement.classList.remove('label-bg');
      }
    });
  }

  protected blurSearchBox(event: Event): void {
    this.searchBoxComponentService.toggleBodyClass(
      'searchbox-is-active',
      false
    );
    if (event && event.target) {
      (<HTMLElement>event.target).blur();
    }
  }

  // Check if focus is on searchbox or result list elements
  private isSearchBoxFocused(): boolean {
    return (
      this.getResultElements().includes(this.getFocusedElement()) ||
      this.winRef.document.querySelector('input[aria-label="Search"]') ===
      this.getFocusedElement()
    );
  }

  /**
   * Especially in mobile we do not want the search icon
   * to focus the input again when it's already open.
   * */
  avoidReopen(event: Event): void {
    if (this.searchBoxComponentService.hasBodyClass('searchbox-is-active')) {
      this.close(event);
      event.preventDefault();
    }
  }

  // Return result list as HTMLElement array
  private getResultElements(): HTMLElement[] {
    return Array.from(
      this.winRef.document.querySelectorAll(
        '.products > li a, .suggestions > li a'
      )
    );
  }

  // Return focused element as HTMLElement
  private getFocusedElement(): HTMLElement {
    return <HTMLElement>this.winRef.document.activeElement;
  }

  updateChosenWord(chosenWord: string): void {
    this.chosenWord = chosenWord;
  }

  private getFocusedIndex(): number {
    return this.getResultElements().indexOf(this.getFocusedElement());
  }

  // Focus on previous item in results list
  focusPreviousChild(event: Event) {
    event.preventDefault(); // Negate normal keyscroll
    const [results, focusedIndex] = [
      this.getResultElements(),
      this.getFocusedIndex(),
    ];
    // Focus on last index moving to first
    if (results.length) {
      if (focusedIndex < 1) {
        results[results.length - 1].focus();
      } else {
        results[focusedIndex - 1].focus();
      }
    }
  }

  // Focus on next item in results list
  focusNextChild(event: Event) {
    this.open();
    event.preventDefault(); // Negate normal keyscroll
    const [results, focusedIndex] = [
      this.getResultElements(),
      this.getFocusedIndex(),
    ];
    // Focus on first index moving to last
    if (results.length) {
      if (focusedIndex >= results.length - 1) {
        results[0].focus();
      } else {
        results[focusedIndex + 1].focus();
      }
    }
  }

  /**
   * Opens the PLP with the given query.
   *
   * TODO: if there's a single product match, we could open the PDP.
   */
  launchSearchResult(event: Event, query: string): void {
    // if (!query || query.trim().length === 0) {
    //   return;
    // }
    this.close(event);
    this.searchBoxComponentService.launchSearchPage(query);
  }

  /**
   * Disables closing the search result list.
   */
  disableClose(): void {
    this.ignoreCloseEvent = true;
  }

  preventDefault(ev: Event): void {
    ev.preventDefault();
  }

  /**
   * Clears the search box input field
   */
  clear(el: HTMLInputElement): void {
    this.disableClose();
    el.value = '';
    this.searchBoxComponentService.clearResults();

    // Use Timeout to run after blur event to prevent the searchbox from closing on mobile
    setTimeout(() => {
      // Retain focus on input lost by clicking on icon
      el.focus();
      this.ignoreCloseEvent = false;
    });
  }

  updateSearch(key: string, contentType: string) {
    this.searchSubject.next({ searchText: key, contentType });
  }

  setActiveTab(uid: string) {
    this.activeTabId = uid;
    let searchText: string = this.searchInput?.nativeElement?.value || '';
    this.searchResults = [];
    let contentType: string = '';
    if (this.activeTabId === 'wsGlobalSearchCategoryParagraph') {
      contentType = 'Category';
    } else if (this.activeTabId === 'wsGlobalSearchBrandParagraph') {
      contentType = 'Brand';
    } else if (this.activeTabId === 'wsGlobalSearchFAQParagraph') {
      contentType = 'Faq';
    } else if (this.activeTabId === 'wsGlobalSearchIntegrationParagraph') {
      contentType = 'Integrations';
    }
    if (contentType) {
      this.isSearchLoading = true;
      this.updateSearch(searchText, contentType);
    }
    if (this.activeTabId === 'wsGlobalSearchWebinarsParagraph') {
      this.isSearchLoading = true;
      this.updateSearch(searchText, contentType);
    }
    if (this.activeTabId === 'wsGlobalSearchPrivateLabelParagraph') {
      this.isSearchLoading = true;
      this.updateSearch(searchText, contentType);
    }
  }

  goToUrl(event: any, url: string) {
    this.router.navigate([url]);
    this.close(event, true);

  }

  // Handle click outside logic
  private onClickOutside(event: MouseEvent): void {
    const path = event.composedPath() as HTMLElement[];
    // Check if any element in the path matches the container
    const clickedInside = path.some(el => el?.id === this.searchElement.nativeElement?.id);
    if (this.searchElement && !clickedInside && this.searchBoxComponentService.hasBodyClass('searchbox-is-active')) {
      this.close(event, true);
    }
  }

  clearSearchBox(): void {
    if (this.searchInput?.nativeElement) {
      this.searchInput.nativeElement.value = '';
    }
    this.chosenWord = '';
    this.searchBoxComponentService.clearResults();
    this.searchBoxComponentService.toggleBodyClass('searchbox-is-active', false);
  }

  ngOnDestroy(): void {
    this.subscription?.unsubscribe();
  }
}