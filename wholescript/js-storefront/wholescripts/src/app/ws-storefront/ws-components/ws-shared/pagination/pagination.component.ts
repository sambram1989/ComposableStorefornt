import { Component, ElementRef, input, model, OnChanges, Renderer2, Input } from '@angular/core';
import { Router } from '@angular/router';
import { PageChangedEvent } from 'ngx-bootstrap/pagination';

@Component({
  selector: 'ws-pagination',
  templateUrl: './pagination.component.html',
  styleUrl: './pagination.component.scss',
})
export class PaginationComponent implements OnChanges {
  totalItems = input<number>(0);
  itemsPerPage = input<number>(0);
  currentPage = model<number>(1);
  numberOfPagesNeedTOBeShown = input<number>(0);
  @Input() resetReposition = false;
  isWebinarPage: boolean = this.router.url.includes('/webinars')

  private repositioned = false;

  constructor(private renderer: Renderer2, private elRef: ElementRef, protected router: Router) {
  }

  ngOnChanges(): void {
    if (this.resetReposition) {
      this.repositioned = false;
      setTimeout(() => {
        this.repositionPaginationLinks();
        this.repositioned = true;
      });
    }
  }

  ngAfterViewInit(): void {
    this.observePaginationChanges();
    setTimeout(() => {
      const links = document.querySelectorAll('.pagination-first a');
      links?.forEach((link: Element) => {
        if (!link?.getAttribute('role')) {
          link?.setAttribute('role', 'button');
        }
        link?.addEventListener('click', (event: Event) => {
          event.preventDefault();
        });
      });
    });
  }

  ngAfterViewChecked(): void {
    if (!this.repositioned) {
      this.repositionPaginationLinks();
      this.repositioned = true;
    }
  }

  observePaginationChanges(): void {
    const config = { childList: true, subtree: true };
    const paginationContainer = this.elRef.nativeElement;
    const observer = new MutationObserver(() => {
      setTimeout(() => {
        const firstBtn = paginationContainer.querySelector('.pagination-first');
        const prevBtn = paginationContainer.querySelector('.pagination-prev');
        const lastBtn = paginationContainer.querySelector('.pagination-last');
        const nextBtn = paginationContainer.querySelector('.pagination-next');
    
        if (firstBtn && prevBtn && lastBtn && nextBtn) {
          this.repositionPaginationLinks();
          this.setAriaLabels();
          observer.disconnect(); // Done observing
        }
      }, 50); 
    });
    observer.observe(paginationContainer, config);
  }

  private setAriaLabels(): void {
    const paginationBtn = document.getElementsByTagName('button');
  
    Array.from(paginationBtn).forEach((btn, index) => {
      const hasLabel = btn.hasAttribute('aria-label');
      if (!hasLabel) {
        btn.setAttribute('aria-label', `action ${index + 1}`);
      }
    });
  
    const rleBtn = document.querySelectorAll('[role="button"]');
    rleBtn.forEach((btn) => {
      const hasLabel = btn.getAttribute('aria-label');
      if (!hasLabel) {
        btn.setAttribute('aria-label', 'Action button');
      }
    });
  }

  getVisiblePages(): number[] {
    const visiblePages: number[] = [];
    const maxVisiblePages = this.numberOfPagesNeedTOBeShown(); // usually 5
    const totalPages = this.totalPages();
    const currentPage = this.currentPage();
    
    const half = Math.floor(maxVisiblePages / 2);
    let startPage = Math.max(1, currentPage - half);
    let endPage = startPage + maxVisiblePages - 1;
    
    if (endPage > totalPages) {
      endPage = totalPages;
      startPage = Math.max(1, endPage - maxVisiblePages + 1);
    }
    
    for (let i = startPage; i <= endPage; i++) {
      visiblePages.push(i);
    }
  
    return visiblePages;
  }

  repositionPaginationLinks(): void {
    const hostElem = this.elRef.nativeElement;

    const firstBtn = hostElem.querySelector('.pagination-first');
    const prevBtn = hostElem.querySelector('.pagination-prev');
    const lastBtn = hostElem.querySelector('.pagination-last');
    const nextBtn = hostElem.querySelector('.pagination-next');

    // Reposition First button after Prev
    if (firstBtn && prevBtn && firstBtn !== prevBtn.nextSibling) {
      this.renderer.removeChild(firstBtn.parentNode, firstBtn);
      this.renderer.insertBefore(prevBtn.parentNode, firstBtn, prevBtn.nextSibling);
    }

    // Reposition Last button before Next
    if (lastBtn && nextBtn && lastBtn !== nextBtn.previousSibling) {
      this.renderer.removeChild(lastBtn.parentNode, lastBtn);
      this.renderer.insertBefore(nextBtn.parentNode, lastBtn, nextBtn);
    }

    if (firstBtn) {
      this.renderer.setStyle(firstBtn, 'display', this.showFirstPageLink() ? '' : 'none');
    }

    if (lastBtn) {
      this.renderer.setStyle(lastBtn, 'display', this.showLastPageLink() ? '' : 'none');
    }
  }

  pageChanged(event: PageChangedEvent): void {
    this.currentPage.set(event.page);
    this.repositioned = false;
  }

  showBoundaryLinks(): boolean {
   const threshold = this.isWebinarPage ? 6 : 5;
  return this.totalPages() > threshold && this.totalPages() > 0;
  }

  totalPages(): number {
    return Math.ceil(this.totalItems() / this.itemsPerPage());
  }

  showFirstPageLink(): boolean {
    const totalPages = this.totalPages();
    const currentPage = this.currentPage();
    return totalPages > 5 && currentPage >= 6 && !this.getVisiblePages().includes(1);
  }

  showLastPageLink(): boolean {
  const total = this.totalPages();
  const current = this.currentPage();
    const visiblePages = this.getVisiblePages();
  return !visiblePages.includes(total);
}
  
  getLastPage(): number {
    return this.totalPages();
  }
}
