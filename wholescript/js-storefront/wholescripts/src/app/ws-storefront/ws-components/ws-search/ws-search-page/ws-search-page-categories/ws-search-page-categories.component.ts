import { ChangeDetectorRef, Component, DestroyRef, EventEmitter, inject, Input, Output, SimpleChanges } from '@angular/core';
import { CustomSearchboxService } from '../../../../ws-services/ws-custom-search.service';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { Config} from '@spartacus/core';
import { Router } from '@angular/router';

@Component({
  selector: 'ws-search-page-categories',
  templateUrl: './ws-search-page-categories.component.html',
  styleUrl: './ws-search-page-categories.component.scss'
})
export class WsSearchPageCategoriesComponent {
  @Input() searchTerm: string = '';
  @Input() viewAllFlag: boolean = false;
  @Output() viewAll = new EventEmitter();
  currentPage: number = 0;
  pageSize: number = 9;
  private destroyRef = inject(DestroyRef);
  private router = inject(Router);

  constructor(private customSearchboxService: CustomSearchboxService,
    private cd: ChangeDetectorRef,
    protected config: Config,
  ) {}

  searchData: any = {};
  dynamicBackgrounds: any = []

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
    this.customSearchboxService.getWSSearchContent(this.searchTerm, 'Category', this.currentPage, this.pageSize).pipe(takeUntilDestroyed(this.destroyRef)).subscribe(data => {
      this.searchData = data;
      this.dynamicBackgrounds = this.searchData?.results?.map((result : any) => {
        return (result?.categoryImage) ? `url(${this.config?.backend?.occ?.baseUrl + result?.categoryImage})` : '';
      });
      this.cd.detectChanges();
    })
  }

  navigateTo(url: string) {
    this.router.navigate([url]);
  }
}
