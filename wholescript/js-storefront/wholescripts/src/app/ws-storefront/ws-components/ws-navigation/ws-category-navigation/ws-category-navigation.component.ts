import {
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component,
  DestroyRef,
  HostListener,
  inject,
} from '@angular/core';
import { CmsNavigationComponent, CmsService } from '@spartacus/core';
import {
  CategoryNavigationComponent,
  CmsComponentData,
  NavigationService,
} from '@spartacus/storefront';
import { map, Observable } from 'rxjs';
import { WsCurrentUserService } from '../../../ws-services/state-services/currentuser.service';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';

@Component({
  selector: 'ws-category-navigation',
  templateUrl: './ws-category-navigation.component.html',
  styleUrls: ['./ws-category-navigation.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class WsCategoryNavigationComponent extends CategoryNavigationComponent {
  override node$: Observable<any> = this.service.getNavigationNode(
    this.componentData.data$
  ).pipe(
    map((node) => {
      const categoriesNode = node?.children?.find(
        (child: any) => child.title === 'Categories'
      );
  
      if (categoriesNode?.children?.length > 1) {
        const [first, ...rest] = categoriesNode.children;
        const sortedRest = rest.sort((a, b) => a.title.localeCompare(b.title));
        categoriesNode.children = [first, ...sortedRest];
      }
      return node;
    })
  );
  images: any[] = [];
  activeCategory: string | null = null;
  showDropdown: string | null = null;
  headerHeight: number = 0;
  hoverTimeout: any;
  hideMedpaxPage: boolean = false;
  private currentUser = inject(WsCurrentUserService);
  currentUserData$: Observable<any> = this.currentUser.getCurrentUserData();

  constructor(
    componentData: CmsComponentData<CmsNavigationComponent>,
    service: NavigationService,
    private cmsService: CmsService,
    private cdref: ChangeDetectorRef,
    private destroyRef: DestroyRef,
  ) {
    super(componentData, service);
    this.retrieveBannerConfig();
    this.currentUserData$
    .pipe(takeUntilDestroyed(this.destroyRef))
    .subscribe({
      next: (data) => {
          this.hideMedpaxPage = data?.hideMedpaxPage;
      },
    })
  }
  @HostListener('document:click', ['$event'])
  closeDropdown(event: Event) {
    const targetElement = event.target as HTMLElement;
    if (targetElement.closest('.dropdown-link') || targetElement.closest('.menu')) {
      return; 
    }
    this.showDropdown = null; 
  }

  handleHover(title: string) {
    clearTimeout(this.hoverTimeout);
    if(this.showDropdown !== title ) this.showDropdown = null
    this.showDropdown = title;
    this.calculateHeaderHeight();
  }

  handleMouseLeave() {
    this.hoverTimeout = setTimeout(() => {
      this.showDropdown = null;
      this.cdref.detectChanges()
    }, 200);
  }
  toggleDropdown(title: string) {
    this.showDropdown = this.showDropdown === title ? null : title;
    this.calculateHeaderHeight();
  }
  retrieveBannerConfig() {
    this.data$.subscribe((res) => {
      const entries: any = res.navigationNode?.children.find(
        (item: any) => item.uid === 'CategoriesNavNode'
      );
      if (entries) {
        entries.children.forEach((element: any) => {
          const enrty = element.entries.filter(
            (ent: { itemType: string }) => ent.itemType === 'BannerComponent'
          );
          if (enrty.length) {
            this.cmsService
              .getComponentData(enrty[0].itemId)
              .subscribe((data: any) => {
                if(data?.media) {
                  this.images.push({content:data?.content , media:data?.media});
                }
              });
          }
        });
      }
      this.cdref.markForCheck();
    });
  }

  getMedia(linkName : string) {
    const media = this.images?.filter(e => {
      return e?.content === linkName;
    });
    return (media && media[0]) ? (media[0]?.media) : null;
  }

  calculateHeaderHeight() {
    if(document.getElementById('storefront-header')) {
      this.headerHeight = (document.getElementById('storefront-header')?.getBoundingClientRect()?.top || 0) + 70;
    }
  }
}
