import { ChangeDetectorRef, Component, DestroyRef, EventEmitter, inject, Input, Output, SimpleChanges } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { Router } from '@angular/router';
import { CustomSearchboxService } from '../../../../ws-services/ws-custom-search.service';

@Component({
  selector: 'ws-search-page-faqs',
  templateUrl: './ws-search-page-faqs.component.html',
  styleUrl: './ws-search-page-faqs.component.scss'
})
export class WsSearchPageFaqsComponent {
  @Input() searchTerm: string = '';
  @Input() viewAllFlag: boolean = false;
  @Output() viewAll = new EventEmitter();
  currentPage: number = 0;
  pageSize: number = 9;
  private destroyRef = inject(DestroyRef);
  private router = inject(Router);

  toggleAnswer: any = {};

  constructor(private customSearchboxService: CustomSearchboxService,
    private cd: ChangeDetectorRef
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
    this.customSearchboxService.getWSSearchContent(this.searchTerm, 'Faq', this.currentPage, this.pageSize).pipe(takeUntilDestroyed(this.destroyRef)).subscribe(data => {
      this.searchData = data;
      this.cd.detectChanges();
    })
  }

  navigateTo(url: string) {
    this.router.navigate([url]);
  }

  toggleQuestion(faq: any) {
    this.toggleAnswer[faq.uid] = !this.toggleAnswer[faq.uid];
  }

}
