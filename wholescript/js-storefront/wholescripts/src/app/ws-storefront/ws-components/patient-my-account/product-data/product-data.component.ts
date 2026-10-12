import {
  ChangeDetectorRef,
  Component,
  DestroyRef,
  inject,
  Input,
  OnInit,
  EventEmitter,
  Output
} from '@angular/core';
import { ActiveCartFacade } from '@spartacus/cart/base/root';
import { FavoriteService } from '../../../ws-services/ws-favorite.service';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { weekArray } from '../../ws-shared/edit-dosingdetailspopup/editDosingConstants';

@Component({
  selector: 'ws-product-data',
  templateUrl: './product-data.component.html',
  styleUrls: [
    './product-data.component.scss',
    '../patient-my-account-protocol/patient-my-account-protocol.component.scss',
  ],
})
export class ProductDataComponent implements OnInit {
  @Input('item') item: any;
  @Output() emitAddToCartEvent = new EventEmitter<any>();
  private cartService = inject(ActiveCartFacade);
  private favoriteService = inject(FavoriteService);
  alerts: any[] = [];
  favoriteList: { wishLists?: { wishListEntries: { productCode: string }[] } } =
    {};

  constructor(
    private destroyRef: DestroyRef,
    private cdRef: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.favoriteService.favoriteItems$.subscribe(
      (favorites: any) => {
        favorites?.wishLists?.forEach((element: any) => {
          if(element.name.includes('plpwishlist')) {
            this.favoriteList = {
              wishLists: {
                wishListEntries: [...element.wishListEntries]
              }
            }
          }
        });
      }
    );
  }

  getDurationValue(key: string): string {
    const duration = weekArray.find(item => item.key === key);
    return duration ? duration.value : key; 
  }

  addSingleToCart(product: any) {
    this.cartService.addEntry(product?.product?.code, product?.quantity);
    this.emitAddToCartEvent.emit(); 
  }

  updateFavorites(item: any, isFav: boolean) {
    if (isFav) {
      this.favoriteService
        .removeFromFavorites(item.product.code)
        .pipe(takeUntilDestroyed(this.destroyRef))
        .subscribe(() => {
          this.refreshFavoriteList(); // Refresh list
        });
    } else {
      this.favoriteService
        .addToFavorites(item.product.code)
        .pipe(takeUntilDestroyed(this.destroyRef))
        .subscribe(() => {
          this.refreshFavoriteList(); // Refresh list
        });
    }
  }

  refreshFavoriteList() {
    this.favoriteService.favoriteItems$.subscribe((favorites: any) => {
      favorites?.wishLists?.forEach((element: any) => {
        if (element.name.includes('plpwishlist')) {
          this.favoriteList = {
            wishLists: {
              wishListEntries: [...element.wishListEntries],
            },
          };
        }
      });
      this.cdRef.markForCheck();
    });
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

  isFavorite(item: any): boolean {
    let product = this.favoriteList?.wishLists?.wishListEntries?.findIndex(
      (product: any) => product.productCode === item.product?.code
    );
    return product > -1;
  }
  
  onClosed(dismissedAlert: any): void {
    this.alerts = this.alerts.filter((alert) => alert !== dismissedAlert);
  }
}
