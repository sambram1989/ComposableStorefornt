import { ChangeDetectorRef, Component, DestroyRef, EventEmitter, inject, Input, Output, SimpleChanges } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { Router } from '@angular/router';
import { CustomSearchboxService } from '../../../../ws-services/ws-custom-search.service';
import { MediaService } from '@spartacus/storefront';
import { Config} from '@spartacus/core';
import { DomSanitizer } from '@angular/platform-browser';

@Component({
  selector: 'ws-search-page-brands',
  templateUrl: './ws-search-page-brands.component.html',
  styleUrl: './ws-search-page-brands.component.scss'
})
export class WsSearchPageBrandsComponent {
  @Input() searchTerm: string = '';
  @Input() viewAllFlag: boolean = false;
  @Output() viewAll = new EventEmitter();
  currentPage: number = 0;
  pageSize: number = 9;
  private destroyRef = inject(DestroyRef);
  private router = inject(Router);

  constructor(private customSearchboxService: CustomSearchboxService,
    private cd: ChangeDetectorRef,
    protected mediaService: MediaService,
    protected config: Config,
    private sanitizer: DomSanitizer
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
    this.customSearchboxService.getWSSearchContent(this.searchTerm, 'Brand', this.currentPage, this.pageSize).pipe(takeUntilDestroyed(this.destroyRef)).subscribe(data => {
      this.searchData = data;
      this.cd.detectChanges();
    })
  }

  navigateTo(url: string) {
    if(url) this.router.navigate([url]);
  }

  getMedia(description: string, url: string) {
    const bypassUr = this.sanitizer.bypassSecurityTrustStyle(this.config.backend?.occ?.baseUrl + url);
    console.log("bypassUr : ",bypassUr);
    const image = {
      url: this.config.backend?.occ?.baseUrl + url,
      altText: description
    }
    return this.mediaService.getMedia(image).src;
  }

  getImage(brand: any) {
    const baseUrl = this.config.backend?.occ?.baseUrl ? this.config.backend?.occ?.baseUrl : '';
    return `${baseUrl}${brand?.brandLogo}`;
  }


}
