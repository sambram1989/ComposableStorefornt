import {
  ChangeDetectorRef,
  Component,
  DestroyRef,
} from '@angular/core';
import { PlpService } from '../../../ws-services/ws-plp.service';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { AuthService , Config} from '@spartacus/core';
import { DomSanitizer } from '@angular/platform-browser';

@Component({
  selector: 'ws-category-banner',
  templateUrl: './ws-category-banner.component.html',
  styleUrl: './ws-category-banner.component.scss',
})
export class WsCategoryBannerComponent {

  bannerSpecs: any;
  isCategory: boolean = true;
  isLoggedIn: boolean = false;
  showDescription: boolean = false;
  categoryBackgroundImage: any;
  dynamicStyles: { [key: string]: string } = {};


  constructor(
    private plpService: PlpService,
    private cdRef: ChangeDetectorRef,
    private destroyRef: DestroyRef,
    private authService: AuthService,
    protected config: Config,
    private sanitizer: DomSanitizer
  ) {
    this.authService
    .isUserLoggedIn()
    .subscribe((res: any) => (this.isLoggedIn = res));
    this.getPageCategory();
  }

  ngOnInit() {
    this.dynamicStyles = {
      'background-image': "url(/assets/icons/plp-icons/left-hex.svg), url(/assets/icons/plp-icons/right-hex.svg)"
    };
  }

  expandDescription() {
    this.showDescription = true;
  }

  getPageCategory() {
    this.plpService.refreshResults.subscribe((res) => {
      if(res?.currentQuery) {
      if(res?.currentQuery?.categoryCode) {
          this.plpService.getPlpCategory(res.currentQuery.categoryCode)
          .subscribe((res) => {
            //dynamic background image of the header 
            if(res?.categoryDetails) {
              if(res?.categoryDetails?.categoryImage && res?.categoryDetails?.categoryImage?.url) {
                this.categoryBackgroundImage = res?.categoryDetails?.categoryImage?.url;
                  if(this.categoryBackgroundImage) 
                    this.dynamicStyles = {
                      'background-image':  `url(${this.config.backend?.occ?.baseUrl + this?.categoryBackgroundImage}),` + "url(/assets/icons/plp-icons/left-hex.svg), url(/assets/icons/plp-icons/right-hex.svg)",
                      'background-size': 'contain',
                      'background-position': '90% bottom, left center, 100% center',
                    } 
              }
            }

            this.bannerSpecs = res.categoryDetails;
            this.isCategory = true;
            this.cdRef.markForCheck();
          })
        }
        if(res?.currentQuery?.brandCode) {
          this.plpService.getPlpBrand(res.currentQuery.brandCode)
          .subscribe((res) => {
            this.bannerSpecs = res.brandDetails;
            this.isCategory = false;
            this.cdRef.markForCheck();
          })
        }
      }
    })
  }
}
