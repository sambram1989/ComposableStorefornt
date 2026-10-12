import {
  ChangeDetectorRef,
  Component,
  DestroyRef,
  EventEmitter,
  inject,
  Input,
  Output,
  SimpleChanges,
} from '@angular/core';
import { CustomSearchboxService } from '../../../../ws-services/ws-custom-search.service';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';

@Component({
  selector: 'ws-search-page-private-label',
  templateUrl: './ws-search-page-private-label.component.html',
  styleUrl: './ws-search-page-private-label.component.scss',
})
export class WsSearchPagePrivateLabelComponent {
  @Input() searchTerm: string = '';
  @Input() viewAllFlag: boolean = false;
  @Output() viewAll = new EventEmitter();
  private destroyRef = inject(DestroyRef);
  currentPage: number = 0;
  pageSize: number = 9;
  searchData: any = {};

  constructor(private customSearchboxService: CustomSearchboxService, private cd: ChangeDetectorRef,) {}

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
    this.customSearchboxService.getPrivateLabelSearchContent(this.searchTerm, this.currentPage, this.pageSize).pipe(takeUntilDestroyed(this.destroyRef)
    ).subscribe((data: any) => {
      this.searchData = data;
      this.cd.detectChanges();
    })
  }
}
