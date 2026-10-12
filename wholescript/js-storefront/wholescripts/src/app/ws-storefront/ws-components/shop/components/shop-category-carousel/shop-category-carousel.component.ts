import {
  AfterViewInit,
  ChangeDetectorRef,
  Component,
  DestroyRef,
  HostListener,
  Input,
  OnChanges,
  SimpleChanges,
  ViewChild,
} from '@angular/core';
import { AuthService, EventService } from '@spartacus/core';
import { first, map, Observable, Subscription } from 'rxjs';
import { WsCustomMedPaxService } from '../../../custom-medpax/medpaxService/customMedpaxservice.service';
import { Router } from '@angular/router';
import {
  ActiveCartFacade,
  CartAddEntrySuccessEvent,
} from '@spartacus/cart/base/root';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { PlpService } from '../../../../ws-services/ws-plp.service';
import { DomSanitizer, SafeHtml } from '@angular/platform-browser';

@Component({
  selector: 'ws-shop-category-carousel',
  templateUrl: './shop-category-carousel.component.html',
  styleUrl: './shop-category-carousel.component.scss',
})
export class ShopCategoryCarouselComponent implements OnChanges, AfterViewInit {
  @Input() items: any[]=[];
  isMobileView = window.innerWidth <= 747;
  itemsPerSlide = this.isMobileView ? 1 : 4;
  isLoggedIn$: Observable<boolean>;
  alert: any = {};
  cartItems: any[] = [];
  protected cartEventsSubscription: Subscription = new Subscription();
  userDetails: any;
  currentIndex : number = 0;
  cartIsLoading$: Observable<boolean> = this.cartService
    .isStable()
    .pipe(map((loaded) => !loaded));

  slickConfig = {
    slidesToShow: this.itemsPerSlide,
    slidesToScroll: 4,
    infinite: true,
    arrows: true,
    dots: true,
    autoplay: false,
    speed: 500,
    responsive: [
      {
        breakpoint: 767,
        settings: {
          slidesToShow: 1,
          slidesToScroll: 1,
        },
      },
      {
        breakpoint: 1024,
        settings: {
          slidesToShow: 3,
          slidesToScroll: 1,
        },
      },
    ],
  };

  constructor(
    private customMedPaxService: WsCustomMedPaxService,
    private router: Router,
    protected cdr: ChangeDetectorRef,
    protected authService: AuthService,
    protected eventService: EventService,
    private plpService: PlpService,
    private destroyRef: DestroyRef,
    protected cartService: ActiveCartFacade,
    private sanitizer: DomSanitizer
  ) {
    this.isLoggedIn$ = authService.isUserLoggedIn();
    this.cartService.getEntries().subscribe((res) =>
      res.forEach((el: any) => {
        this.cartItems.push(el.product.code);
      })
    );
    this.userDetails = JSON.parse(sessionStorage.getItem('user_config'));
    
  }
  ngOnInit(){
    this.updateItemsPerSlide();
  }
  ngOnChanges(changes: SimpleChanges): void {
    if (changes?.['items'].currentValue) {
      this.items = changes?.['items'].currentValue;
      this.cdr.detectChanges();
    }
  }

  ngAfterViewInit(): void {
    if(document.getElementsByTagName('title') && !document.getElementsByTagName('title')[0].innerText) document.getElementsByTagName('title')[0].innerText = 'Shop';
  }
  @HostListener('window:resize', [])
  onResize(): void {
    this.updateItemsPerSlide();
  }
  updateItemsPerSlide() {
    this.isMobileView = window.innerWidth <= 747;
    this.itemsPerSlide = this.isMobileView ? 1 : 4;
    this.slickConfig.slidesToShow = this.itemsPerSlide;
    this.cdr.detectChanges()
  }

  nextSlide(): void {
    if (this.items && this.items.length > 0) {
      this.currentIndex = (this.currentIndex + 1) % this.items.length;
    }
  }

  prevSlide(): void {
    if (this.items && this.items.length > 0) {
      this.currentIndex = (this.currentIndex - 1 + this.items.length) % this.items.length;
    }
  }

  addProductToCart(item: any, event: any): void {
    this.watchAddEntrySuccessEvent();
    this.cartService.addEntry(item.code, item.quantity);
    event.stopPropagation();
  }

  goToRegister(event: Event) {
    event.preventDefault();
    this.router.navigate(['/sign-up']);
  }
  
  stopPdpNavigation(event: any): void {
    event.stopPropagation();
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
    this.alert = {
      type: 'info',
      msg: `Product added to cart`,
      timeout: 2000,
    };
    this.cdr.markForCheck();
  }

  onClosed(): void {
    this.alert = {};
  }

  goToMedPax(product: any) {
    const queryString =  product?.productUrl.replace('?q=', '');
    this.router.navigate(['/medpax'],{ queryParams: { query: queryString } });
  }

  goToProduct(product: any): void {
    if (product?.url) {
      this.router.navigateByUrl(product.url); // Navigating to product page
    }
  }
  goToBrand(brandUrl: string): void {
    if (brandUrl) {
      this.router.navigateByUrl(brandUrl);
    }
  }

  signUp(): void {
    this.router.navigate(['/sign-up']);
  }

  updateFavorites(item: any) {
    item.isFavorite = !item.isFavorite;
    if (!item?.isFavorite) {
      this.plpService
        .removeFromFavorites(item.code)
        .pipe(takeUntilDestroyed(this.destroyRef))
        .subscribe((res) => {
          this.add(item.isFavorite);
          this.cdr.markForCheck();
        });
    } else {
      this.plpService
        .addToFavorites(item.code)
        .pipe(takeUntilDestroyed(this.destroyRef))
        .subscribe((res) => {
          this.add(item.isFavorite);
          this.cdr.markForCheck();
        });
    }
  }

  add(isFavorite: boolean): void {
    this.alert = {
      type: 'info',
      msg: isFavorite
        ? `Product added to favorites.`
        : `Product removed from favorites.`,
      timeout: 2000,
    };
    this.cdr.markForCheck();
  }

  removeQuantity(item: any) {
    if(!item?.quantity) item.quantity = 1;
    if (Number(item.quantity) > 1) item.quantity = Number(item.quantity) - 1;
  }
  
  addQuantity(item: any) {
    if(!item?.quantity) item.quantity = 1;
    item.quantity = Number(item.quantity) + 1;
  }
  
  setQuantity(product: any, ev: any, type?: any) {
    const value = ev.target.value;
  
    if (value !== '') {
      const reg = new RegExp(/^\d+$/);
      if (!reg.test(value)) {
        ev.target.value = value.replace(/\D/g, '');
      }
  
      if (value.length >= 1 && value.startsWith('0')) {
        ev.target.value = value.replace(/^0+/, '');
      }

      product.quantity = ev.target.value;
    }
  }

  setQuantityOnFocus(product: any, ev: any, type?: any) {
    const value = ev.target.value;
    if (!value) {
      ev.target.value = 1;
      product.quantity = 1;
    }
  }
  getSanitizedDescription(description: string): SafeHtml {
    return this.sanitizer.bypassSecurityTrustHtml(description);
  }
}
