import {
  Component,
  DestroyRef,
  inject,
} from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { AuthService, CmsService } from '@spartacus/core';
import { Observable, shareReplay } from 'rxjs';

@Component({
  selector: 'ws-about-us',
  templateUrl: './ws-about-us.component.html',
  styleUrl: './ws-about-us.component.scss',
})
export class WsAboutUsComponent {
  isLoggedIn: boolean = false;
  bannerImg: Observable<any> = this.cmsservice.getComponentData('AboutUsPageBannerComponent');
  private destroyRef = inject(DestroyRef);
  componentListArray: any = [];

  constructor(private authService: AuthService, public cmsservice: CmsService) {
     this.authService.isUserLoggedIn().pipe(shareReplay(1)).subscribe((response) => {
                  this.isLoggedIn = response;
      })
      this.getCmsdata();
  }
  
    getCmsdata() {
      this.cmsservice
        .getContentSlot('AboutUsSection2')
        .pipe(takeUntilDestroyed(this.destroyRef))
        .subscribe((data: any) => {
          this.componentListArray = data?.components;
        });
    }
}
