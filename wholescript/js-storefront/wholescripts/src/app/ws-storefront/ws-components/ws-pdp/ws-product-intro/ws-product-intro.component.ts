import { ChangeDetectorRef, Component, DestroyRef } from '@angular/core';
import { AuthService } from '@spartacus/core';
import { CurrentProductService } from '@spartacus/storefront';
import { Observable, tap } from 'rxjs';
import { PlpService } from '../../../ws-services/ws-plp.service';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';

declare var klaviyo: any;

@Component({
  selector: 'ws-ws-product-intro',
  templateUrl: './ws-product-intro.component.html',
  styleUrl: './ws-product-intro.component.scss'
})
export class WsProductIntroComponent {

  shortDescription: any[] = [];
  product$: Observable<any>;
  isLoggedIn: boolean = false;
  alerts: any[] = [];
  favoriteList: { wishLists?: { wishListEntries: { productCode: string }[] } } =
  {};

  constructor(
    private currentProductService: CurrentProductService,
    private authService: AuthService,
    private plpService: PlpService,
    private cdRef: ChangeDetectorRef,
    private destroyRef: DestroyRef,
  ) {
    this.authService.isUserLoggedIn().subscribe((res) => {
      (this.isLoggedIn = res)});
      this.plpService.fetchFavorites().subscribe();
  }

  ngOnInit() {
    this.plpService.favoriteItems$.pipe(takeUntilDestroyed(this.destroyRef)).subscribe((favorites) => {
      favorites?.wishLists?.forEach((element: any) => {
        // if(element.name.includes('plp_wishlist')) {
          this.favoriteList = {
            wishLists: {
              wishListEntries: [...element.wishListEntries]
            }
          }
        // }
      });
    });

    this.product$ = this.currentProductService.getProduct().pipe(
      tap((res: any) => {
        this.pushPDPViewToKlaviyo(res);
        const relevantClassification = res?.classifications?.find((classification: any) =>
          classification.code === "pimClassificationDataClass"
        );

        if (relevantClassification) {
          // List of codes to match
          const requiredCodes = [
            "PIMClassification/1.0/pimClassificationDataClass.shortdescription",
          ];

          // Filter the features based on the required codes
          this.shortDescription = relevantClassification?.features?.filter((feature: any) =>
            requiredCodes.includes(feature.code)
          );
        }
      })
    )
  }

  isFavorite(item: any): boolean {
    let productIndex = this.favoriteList?.wishLists?.wishListEntries?.findIndex(
      (product: any) => product.productCode == item.code
    );  
    return productIndex !== -1;
  }
  

  updateFavorites(item: any, isFav: boolean) {
    if (isFav) {
      this.plpService
        .removeFromFavorites(item.code)
        .pipe(takeUntilDestroyed(this.destroyRef))
        .subscribe((res) => {
          item.isFavorite = !item.isFavorite;
          this.add(!isFav);
          this.cdRef.markForCheck();
        });
    } else {
      this.plpService
        .addToFavorites(item.code)
        .pipe(takeUntilDestroyed(this.destroyRef))
        .subscribe((res) => {
          this.add(!isFav);
          this.cdRef.markForCheck();
        });
    }
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

  pushPDPViewToKlaviyo(product: any) {
    if (typeof klaviyo !== "undefined" && klaviyo) {
      const primaryImg = product?.images?.PRIMARY?.product?.url ?? '';
      const baseUrl = window.location.origin;

      const item = {
        "ProductName": product.name,
        "SKU": product.code,
        "ProductID": product.code,
        "Categories": product.categories?.map((category: any) => category.name),
        "ImageURL": primaryImg ? `${primaryImg}` : '',
        "URL": window.location.href,
        "Brand": product.brandName,
        "Price": product.yourPrice?.value,
      };
      klaviyo.push(["track", "Viewed Product", item]);
      console.log("Klaviyo 'Viewed Product' event tracked:", item);
    }
  }
}
