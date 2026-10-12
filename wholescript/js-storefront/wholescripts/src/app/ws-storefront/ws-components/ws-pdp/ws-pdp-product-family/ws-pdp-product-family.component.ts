import {
  ChangeDetectorRef,
  Component,
  DestroyRef,
  HostListener,
  inject,
  OnInit,
} from '@angular/core';
import { ActivatedRoute, Router, NavigationEnd } from '@angular/router';
import { WsPdpProductFamilyService } from '../../../ws-services/ws-pdp-product-family.service';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { AuthService, EventService } from '@spartacus/core';
import { ActiveCartFacade, CartAddEntrySuccessEvent } from '@spartacus/cart/base/root';
import { FavoriteService } from '../../../ws-services/ws-favorite.service';
import { WsCustomMedPaxService } from '../../custom-medpax/medpaxService/customMedpaxservice.service';
import { first, map, Observable, Subscription } from 'rxjs';

@Component({
  selector: 'ws-pdp-product-family',
  templateUrl: './ws-pdp-product-family.component.html',
  styleUrl: './ws-pdp-product-family.component.scss',
})
export class WsPdpProductFamilyComponent {
  private customMedPaxService = inject(WsCustomMedPaxService);
  private router = inject(Router);
  productCode: string = '';
  productFamily: any[] = [];
  alerts: any[] = [];
  itemsPerSlide = 4;
  cartItems: any[] = [];
  isLoggedIn: boolean = false;
  slickConfig: any = {};
  favoriteList: { wishLists?: { wishListEntries: { productCode: string }[] } } =
    {};
  protected cartEventsSubscription: Subscription = new Subscription();
  cartIsLoading$: Observable<boolean> = this.cartService
      .isStable()
      .pipe(map((loaded) => !loaded));
  @HostListener('window:resize', ['$event'])
    onResize() {
      this.updateItemsPerSlide();
    }
  constructor(
    private route: ActivatedRoute,
    private productFamilyService: WsPdpProductFamilyService,
    private destroyRef: DestroyRef,
    protected authService: AuthService,
    private cdr: ChangeDetectorRef,
    protected cartService: ActiveCartFacade,
    private favouriteService: FavoriteService,
    private eventService: EventService
  ) {
    this.authService
      .isUserLoggedIn()
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe((response) => {
        this.isLoggedIn = response;
      });
    this.favouriteService.favoriteItems$
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe((favorites) => {
        favorites?.wishLists?.forEach((element: any) => {
          if(element.name.includes('plpwishlist')) {
            this.favoriteList = {
              wishLists: {
                wishListEntries: [...element.wishListEntries]
              }
            }
          }
        });
      });

    this.productCode = this.route.snapshot.paramMap.get('productCode');
    if (this.productCode) {
      this.fetchProductFamily(this.productCode);
    }
    this.cartService.getEntries().subscribe((res) =>
      res.forEach((el: any) => {
        this.cartItems.push(el.product.code);
      })
    );
    this.updateItemsPerSlide();
  }

  ngOnInit() {
    this.router.events.pipe(takeUntilDestroyed(this.destroyRef)).subscribe((event) => {
      if (event instanceof NavigationEnd) {
        this.productCode = this.route.snapshot.paramMap.get('productCode');
        if (this.productCode) {
          this.fetchProductFamily(this.productCode);
        }
      }
    });
  }

  updateItemsPerSlide() {
    const width = window.innerWidth;
    this.itemsPerSlide = width <= 900 ? 1 : width <= 1280 ? 2 : 4;
    this.slickConfig = {
      slidesToShow: this.itemsPerSlide,
      slidesToScroll: this.itemsPerSlide,
      dots: true,
      infinite: true,
      arrows: true,
      autoplay: false,
      speed: 500,
      responsive: [
        {
          breakpoint: 1280,
          settings: {
            slidesToShow: 2,
            slidesToScroll: 1
          }
        },
        {
          breakpoint: 900,
          settings: {
            slidesToShow: 1,
            slidesToScroll: 1
          }
        }
      ]
    };
  }

  navigateToUrl(url: string): void {
    this.router.navigate([url]);
  }

  onKeyDown(event: KeyboardEvent, action: string, item: any) {
    if (event.key === 'Enter') {
      event.preventDefault(); 
      event.stopPropagation(); 
      if (action === 'addQuantity') {
        this.addQuantity(item);
      } else if (action === 'removeQuantity') {
        this.removeQuantity(item);
      } else if (action === 'updateFavorites') {
        this.updateFavorites(item, this.isFavorite(item));
      } else if (action === 'brandUrl') {
        this.navigateToUrl(item);
      } else if (action === 'productUrl') {
        this.navigateToUrl(item);
      } else if (action === 'gotToPDP') {
        this.gotToPDP(item);
      } else if (action === 'goToMedPax') {
        this.goToMedPax(item);
      } else if (action === 'addProductToCart') {
        this.addProductToCart(item);
      }
    }
  }    
  
  

  fetchProductFamily(productCode: string) {
    this.productFamilyService
      .getProductFamily(productCode)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe((res: any) => {
        this.productFamily = res?.wholescriptsProductFamilyList;
        this.cdr.detectChanges();
      });
  }

  addProductToCart(item: any) {
    this.cartService.addEntry(item.sku, item.quantity);
    this.watchAddEntrySuccessEvent();
  }

    protected watchAddEntrySuccessEvent(): void {
      this.cartEventsSubscription.add(
        this.eventService
          .get(CartAddEntrySuccessEvent)
          .pipe(first())
          .subscribe(() => {
            this.addCart();
          })
      );
    }
  
    addCart(): void {
      this.alerts.push({
        type: 'info',
        msg: `Product added to cart`,
        timeout: 2000,
      });
      this.cdr.markForCheck();
    }

  addQuantity(item: any) {
    item.quantity = Number(item.quantity) || 1;
    item.quantity += 1;
  }
  removeQuantity(item: any) {
    item.quantity = Number(item.quantity) || 1;
    if (item.quantity > 1) item.quantity -= 1;
  }

  setQuantity(product: any, ev: any) {
    if (ev.target.value !== '') product.quantity = ev.target.value;
    else product.quantity = 1;
  }

  add(isFavorite: boolean): void {
    this.alerts.push({
      type: 'info',
      msg: isFavorite
        ? `Product added to favorites.`
        : `Product removed from favorites.`,
      timeout: 2000,
    });
  }

  onClosed(dismissedAlert: any): void {
    this.alerts = this.alerts.filter((alert) => alert !== dismissedAlert);
  }

  updateFavorites(item: any, isFav: boolean) {
    if (isFav) {
      this.favouriteService
        .removeFromFavorites(item.sku)
        .pipe(takeUntilDestroyed(this.destroyRef))
        .subscribe((res) => {
          item.isFavorite = !item.isFavorite;
          this.add(!isFav);
          this.cdr.markForCheck();
        });
    } else {
      this.favouriteService
        .addToFavorites(item.sku)
        .pipe(takeUntilDestroyed(this.destroyRef))
        .subscribe((res) => {
          this.add(!isFav);
          this.cdr.markForCheck();
        });
    }
  }
  isFavorite(item: any): boolean {
    let product = this.favoriteList?.wishLists?.wishListEntries?.findIndex((product: any) => product.productCode === (item.code || item.sku));
    return product > -1 ? true : false;
  }

  goToMedPax(product:any){
    const queryString = product?.productUrl.replace('?q=','');
    this.router.navigate(['/medpax'],{ queryParams: { query: queryString } });
   } 
   
   gotToPDP(url: string): void {
    this.router.navigate([url]);
  }
   
}
