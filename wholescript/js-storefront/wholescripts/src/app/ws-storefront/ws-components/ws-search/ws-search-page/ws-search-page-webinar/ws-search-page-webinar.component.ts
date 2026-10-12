import {
  ChangeDetectorRef,
  Component,
  DestroyRef,
  EventEmitter,
  inject,
  Input,
  Output,
  signal,
  SimpleChanges,
} from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { CustomSearchboxService } from '../../../../ws-services/ws-custom-search.service';
import { Config } from '@spartacus/core';

@Component({
  selector: 'ws-search-page-webinar',
  templateUrl: './ws-search-page-webinar.component.html',
  styleUrl: './ws-search-page-webinar.component.scss',
})
export class WsSearchPageWebinarComponent {
  @Input() searchTerm: string = '';
  @Input() viewAllFlag: boolean = false;
  @Output() viewAll = new EventEmitter();
  currentPage: number = 0;
  pageSize: number = 9;
  private destroyRef = inject(DestroyRef);
  searchData: any = {};
  @Output() resultsLength = new EventEmitter<number>();

  constructor(
    private customSearchboxService: CustomSearchboxService,
    private cd: ChangeDetectorRef,
    private config: Config,
  ) { }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes?.['searchTerm'] || changes?.['viewAllFlag']) {
      this.currentPage = 0;
      if (!this.viewAllFlag) {
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
    this.customSearchboxService.getWebinarSSearchContent(this.searchTerm, this.currentPage, this.pageSize).pipe(takeUntilDestroyed(this.destroyRef)
    ).subscribe((data: any) => {
      this.searchData = data;
      this.resultsLength.emit(this.searchData?.results?.length);
      this.cd.detectChanges();
    })
  }

  getThumbnail(webinar: any) {
    const baseUrl = this.config.backend?.occ?.baseUrl ? this.config.backend?.occ?.baseUrl : '';
    return `${baseUrl}${webinar?.videoImage}`;
  }
}
