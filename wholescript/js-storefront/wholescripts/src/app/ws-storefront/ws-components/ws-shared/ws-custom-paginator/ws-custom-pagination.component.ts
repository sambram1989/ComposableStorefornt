import {
    ChangeDetectorRef,
    Component,
    ElementRef,
    EventEmitter,
    forwardRef,
    Input,
    OnInit,
    Output,
    Provider,
    TemplateRef
} from '@angular/core';
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from '@angular/forms';
import { ConfigModel, PagesModel, PaginationConfig, PaginationLinkContext, PaginationNumberLinkContext } from './ws-pagination.config';


export interface PageChangedEvent {
    itemsPerPage: number;
    page: number;
}

export const PAGINATION_CONTROL_VALUE_ACCESSOR: Provider = {
    provide: NG_VALUE_ACCESSOR,
    useExisting: forwardRef(() => WsCustomPaginationComponent),
    multi: true
};

@Component({
    selector: 'ws-custom-pagination',
    template: `
    <ul class="pagination" [ngClass]="classMap" tabindex="0">
  <li class="pagination-first page-item" *ngIf="boundaryLinks" [class.disabled]="noPrevious() || disabled">
    <a class="page-link" href (click)="selectPage(1, $event)" aria-label="Go to first page">
      <ng-container [ngTemplateOutlet]="customFirstTemplate" 
                   [ngTemplateOutletContext]="{disabled: noPrevious() || disabled, currentPage: page}">
      </ng-container>
    </a>
  </li>

  <li class="pagination-prev page-item" *ngIf="directionLinks" [class.disabled]="noPrevious() || disabled">
    <a class="page-link" href (click)="selectPage(page - 1, $event)">
      <ng-container [ngTemplateOutlet]="customPreviousTemplate" 
                   [ngTemplateOutletContext]="{disabled: noPrevious() || disabled, currentPage: page}">
      </ng-container>
    </a>
  </li>

  <ul class="pagination" [ngClass]="classMap">
    <li *ngFor="let pg of pages"
        [class.active]="pg.active"
        [class.disabled]="disabled && !pg.active"
        class="pagination-page page-item">
      <a class="page-link" href (click)="selectPage(pg.number, $event)">
        {{ pg.text }}
      </a>
    </li>
  </ul>

  <li class="pagination-next page-item" *ngIf="directionLinks" [class.disabled]="noNext() || disabled">
    <a class="page-link" href (click)="selectPage(page + 1, $event)">
      <ng-container [ngTemplateOutlet]="customNextTemplate" 
                   [ngTemplateOutletContext]="{disabled: noNext() || disabled, currentPage: page}">
      </ng-container>
    </a>
  </li>

  <li class="pagination-last page-item" *ngIf="boundaryLinks" [class.disabled]="noNext() || disabled">
    <a class="page-link" href (click)="selectPage(totalPages, $event)">
      <ng-container [ngTemplateOutlet]="customLastTemplate" 
                   [ngTemplateOutletContext]="{disabled: noNext() || disabled, currentPage: page}">
      </ng-container>
    </a>
  </li>
</ul>

`,
    providers: [PAGINATION_CONTROL_VALUE_ACCESSOR]
})
export class WsCustomPaginationComponent implements ControlValueAccessor, OnInit {
    config?: Partial<ConfigModel>;
    @Input() align = true;
    @Input() maxSize?: number;
    @Input() boundaryLinks = false;
    @Input() directionLinks = true;
    @Input() firstText?: string;
    @Input() previousText?: string;
    @Input() nextText?: string;
    @Input() lastText?: string;
    @Input() rotate = true;
    @Input() pageBtnClass = '';
    @Input() disabled = false;
    @Input() customPageTemplate?: TemplateRef<PaginationNumberLinkContext>;
    @Input() customNextTemplate?: TemplateRef<PaginationLinkContext>;
    @Input() customPreviousTemplate?: TemplateRef<PaginationLinkContext>;
    @Input() customFirstTemplate?: TemplateRef<PaginationLinkContext>;
    @Input() customLastTemplate?: TemplateRef<PaginationLinkContext>;
    @Output() numPages = new EventEmitter<number>();
    @Output() pageChanged = new EventEmitter<PageChangedEvent>();
    onChange = Function.prototype;
    onTouched = Function.prototype;
    classMap = '';
    pages?: PagesModel[];
    protected inited = false;
    inputPage: number = 1;

    constructor(
        private elementRef: ElementRef,
        paginationConfig: PaginationConfig,
        private changeDetection: ChangeDetectorRef
    ) {
        this.elementRef = elementRef;
        if (!this.config) {
            this.configureOptions(paginationConfig.main);
        }
    }

    protected _itemsPerPage = 10;

    @Input()
    get itemsPerPage(): number {
        return this._itemsPerPage;
    }

    set itemsPerPage(v: number) {
        this._itemsPerPage = v;
        this.totalPages = this.calculateTotalPages();
    }

    protected _totalItems = 0;

    @Input()
    get totalItems(): number {
        return this._totalItems;
    }

    set totalItems(v: number) {
        this._totalItems = v;
        this.totalPages = this.calculateTotalPages();
    }

    protected _totalPages = 0;

    get totalPages(): number {
        return this._totalPages;
    }

    set totalPages(v: number) {
        this._totalPages = v;
        this.numPages.emit(v);
        if (this.inited) {
            this.selectPage(this.page);
        }
    }

    protected _page = 1;

    get page(): number {
        return this._page;
    }

    set page(value: number) {
        const _previous = this._page;
        this._page = value > this.totalPages ? this.totalPages : value || 1;
        this.changeDetection.markForCheck();

        if (_previous === this._page || typeof _previous === 'undefined') {
            return;
        }

        this.pageChanged.emit({
            page: this._page,
            itemsPerPage: this.itemsPerPage
        });
    }

    configureOptions(config: Partial<ConfigModel>): void {
        this.config = Object.assign({}, config);
    }

    ngOnInit(): void {
        if (typeof window !== 'undefined') {
            this.classMap = this.elementRef.nativeElement.getAttribute('class') || '';
        }

        if (typeof this.maxSize === 'undefined') {
            this.maxSize = this.config?.maxSize || 0;
        }

        if (typeof this.rotate === 'undefined') {
            this.rotate = !!this.config?.rotate;
        }

        if (typeof this.boundaryLinks === 'undefined') {
            this.boundaryLinks = !!this.config?.boundaryLinks;
        }


        if (typeof this.directionLinks === 'undefined') {
            this.directionLinks = !!this.config?.directionLinks;
        }

        if (typeof this.pageBtnClass === 'undefined') {
            this.pageBtnClass = this.config?.pageBtnClass || '';
        }

        if (typeof this.itemsPerPage === 'undefined') {
            this.itemsPerPage = this.config?.itemsPerPage || 0;
        }

        this.totalPages = this.calculateTotalPages();
        this.pages = this.getPages(this.page, this.totalPages);
        this.inited = true;
        this.inputPage = this.page;
    }

    onInputChange(value: number): void {
        if (value > this.totalPages) {
            this.inputPage = this.totalPages; // Clamp to max pages
        } else if (value < 1) {
            this.inputPage = 1; // Clamp to min page
        } else {
            this.inputPage = value;
        }
        this.selectPage(this.inputPage); // Navigate to the input page
    }

    writeValue(value: number): void {
        this.page = value;
        this.pages = this.getPages(this.page, this.totalPages);
    }

    getText(key: string): string {
        return (this as any)[`${key}Text`] || (this as any).config[`${key}Text`];
    }

    noPrevious(): boolean {
        return this.page === 1;
    }

    noNext(): boolean {
        return this.page === this.totalPages;
    }

    registerOnChange(fn: () => void): void {
        this.onChange = fn;
    }

    registerOnTouched(fn: () => void): void {
        this.onTouched = fn;
    }

    selectPage(page: number, event?: Event): void {
        if (event) {
            event.preventDefault();
        }

        if (!this.disabled) {
            if (event && event.target) {
                const target: any = event.target;
                target.blur();
            }
            this.writeValue(page);
            this.onChange(this.page);
        }
    }

    protected makePage(
        num: number,
        text: string,
        active: boolean
    ): { number: number; text: string; active: boolean } {
        return { text, number: num, active };
    }

    // protected getPages(currentPage: number, totalPages: number): PagesModel[] {
    //     const pages: PagesModel[] = [];

    //     if (totalPages <= 7) {
    //         for (let i = 1; i <= totalPages; i++) {
    //             pages.push(this.makePage(i, i.toString(), i === currentPage));
    //         }
    //     } else {
    //         if (currentPage <= 5) {
    //             for (let i = 1; i <= 5; i++) {
    //                 pages.push(this.makePage(i, i.toString(), i === currentPage));
    //             }
    //             pages.push(this.makePage(0, '...', false)); 
    //             pages.push(this.makePage(totalPages, totalPages.toString(), false));
    //         } else if (currentPage >= totalPages - 4) {

    //             pages.push(this.makePage(1, '1', false));
    //             pages.push(this.makePage(0, '...', false)); 
    //             for (let i = totalPages - 4; i <= totalPages; i++) {
    //                 pages.push(this.makePage(i, i.toString(), i === currentPage));
    //             }
    //         } else {

    //             pages.push(this.makePage(1, '1', false));
    //             pages.push(this.makePage(0, '...', false)); 

    //             for (let i = currentPage - 1; i <= currentPage + 1; i++) {
    //                 pages.push(this.makePage(i, i.toString(), i === currentPage));
    //             }

    //             pages.push(this.makePage(0, '...', false)); 
    //             pages.push(this.makePage(totalPages, totalPages.toString(), false));
    //         }
    //     }

    //     return pages;
    // }

    protected getPages(currentPage: number, totalPages: number): PagesModel[] {
        const pages: PagesModel[] = [];

        pages.push(this.makePage(1, '1', currentPage === 1));

        if (totalPages > 7 && currentPage > 4) {
            pages.push(this.makePage(0, '...', false));
        }

        const startPage = Math.max(2, currentPage - 2);
        const endPage = Math.min(totalPages - 1, currentPage + 2);

        for (let num = startPage; num <= endPage; num++) {
            if (num < totalPages) {
                pages.push(this.makePage(num, num.toString(), num === currentPage));
            }
        }


        if (totalPages > 7 && currentPage < totalPages - 3) {
            pages.push(this.makePage(0, '...', false));
        }


        if (totalPages > 1) {
            pages.push(this.makePage(totalPages, totalPages.toString(), currentPage === totalPages));
        }


        if (totalPages > 11 && currentPage < totalPages - 4) {
            pages.splice(pages.length - 1, 0, this.makePage(0, '...', false));
        }

        return pages;
    }



    protected calculateTotalPages(): number {
        const totalPages =
            this.itemsPerPage < 1
                ? 1
                : Math.ceil(this.totalItems / this.itemsPerPage);

        return Math.max(totalPages || 0, 1);
    }
}
