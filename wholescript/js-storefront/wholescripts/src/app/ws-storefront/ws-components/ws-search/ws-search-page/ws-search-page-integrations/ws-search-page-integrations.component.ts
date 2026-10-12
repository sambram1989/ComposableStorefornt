import { ChangeDetectorRef, Component, DestroyRef, EventEmitter, inject, Input, Output, SimpleChanges } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { Router } from '@angular/router';
import { CustomSearchboxService } from '../../../../ws-services/ws-custom-search.service';
import { MediaService } from '@spartacus/storefront';
import { WsUtilService } from '../../../../ws-services/utils/utilService.service';

@Component({
  selector: 'ws-search-page-integrations',
  templateUrl: './ws-search-page-integrations.component.html',
  styleUrl: './ws-search-page-integrations.component.scss'
})
export class WsSearchPageIntegrationsComponent {
  @Input() searchTerm: string = '';
  @Input() viewAllFlag: boolean = false;
  @Output() viewAll = new EventEmitter();
  currentPage: number = 0;
  pageSize: number = 9;
  private destroyRef = inject(DestroyRef);
  private router = inject(Router);
  private utilService = inject(WsUtilService);

  constructor(private customSearchboxService: CustomSearchboxService,
    private cd: ChangeDetectorRef,
    protected mediaService: MediaService
  ) {}

  searchData: any = {};

  ngOnChanges(changes: SimpleChanges): void {
    if(changes?.['searchTerm'] || changes?.['viewAllFlag']) {
      this.currentPage = 0;
      if(!this.viewAllFlag) {
        this.pageSize = 9;
      } else {
        this.pageSize = this.pageSize > 9 ? this.pageSize : 24;
      }
      this.searchProducts();
    }
  }
  
  setCurrentPage(page: number) {
    this.currentPage = page;
    this.searchProducts();
  }

  viewAllEmit() {
    this.viewAll.emit();
    this.pageSize = 24;
    this.currentPage = 0;
    this.searchProducts();
  }

  showNumberOfResults(pageSize: number) {
    this.pageSize = pageSize;
    this.searchProducts();
  }

  searchProducts() {
    this.customSearchboxService.getWSSearchContent(this.searchTerm, 'Integrations', this.currentPage, this.pageSize).pipe(takeUntilDestroyed(this.destroyRef)).subscribe(data => {
      this.searchData = data;
      const results = this.searchData?.results || [];
      if(results?.length > 0) {
        this.searchData.results = results?.map((result: { integrations: string[]; }) => {
          if(result?.integrations?.length > 0) {
            return this.utilService.parseKeyValueArray(result?.integrations, '; ')?.[0] || {};
          }
          return result;
        });
      }
      this.cd.detectChanges();
    })
  }

  navigateTo(url: string) {
    if(url?.includes('https') || url?.includes('www.')) {
      window.open(url, '_blank'); 
    } else {
      this.router.navigate([url]);
    }
  }

  getMedia(description: string, url: string) {
    const image ={
      url: url,
      altText: description,
      format:'product'
    }
    return this.mediaService.getMedia(image).src;
  }

}
