import { AfterViewInit, ChangeDetectorRef, Component, OnDestroy } from '@angular/core';
import { AuthService , CmsService } from '@spartacus/core';
import { WsPage } from '../../../ws-services/ws-page.service';
import { MEDPAX_CONTENT_SLOT } from '../../../ws-models/page.model';
import { Subject, takeUntil, shareReplay } from 'rxjs';

@Component({
  selector: 'ws-why-med-pax-page',
  templateUrl: './ws-why-med-pax-page.component.html',
  styleUrl: './ws-why-med-pax-page.component.scss'
})
export class WsWhyMedPaxPageComponent implements OnDestroy, AfterViewInit {
  medpaxSpecificContent: { [k: string]: any } = {};
  activeWhyMedpaxCarousel: number = 1;
  private ngUnsubscribe = new Subject<void>();
  private observer: IntersectionObserver | null = null;
  isLoggedIn: boolean = false;

  constructor(public wsPageService: WsPage, private cd: ChangeDetectorRef, private cmsService: CmsService, protected authService: AuthService) {
    this.getMedPaxInfo();
    this.authService.isUserLoggedIn().pipe(shareReplay(1)).subscribe((response) => {
        this.isLoggedIn = response;
    })
  }

  ngAfterViewInit(): void {

    // Setup the IntersectionObserver
    const callback = (entries: IntersectionObserverEntry[]) => {
      entries.forEach(entry => {
        if (entry.isIntersecting) {
          window?.addEventListener('scroll', this.whyMPScroll);
        } else {
          window?.removeEventListener('scroll', this.whyMPScroll);
        }
      });
    };

    this.observer = new IntersectionObserver(callback);

    const target = document.querySelector('.why-medpax');
    if (target) {
      this.observer.observe(target);
    }


    if (navigator.maxTouchPoints > 0) {
      document.getElementById('mp-mobileScrollIcon').style.display = 'block';
    } else document.getElementById('mp-desktopScrollIcon').style.display = 'block';

  }

  scrollIntoViewWithOffset = (step: number) => {
    let target: HTMLElement = document.querySelector('.why-medpax');
    const contentContainer = document.getElementById('why-med-pax-page-content');
    const offset = (step == 1) ? 0 : 400 * (step + 1);

    contentContainer.scroll({
      top: target.offsetTop + offset
    });
  }

  whyMPScroll = () => {
    let whyMedPaxInner: HTMLElement = document.querySelector('.why-medpax__inner');
    let offset: number = whyMedPaxInner.offsetTop;
    let target: HTMLElement = document.querySelector('.why-medpax');
    let height = target.offsetHeight * .8;

    if (offset >= (height * .3) && offset < (height * .6)) {
      this.activeWhyMedpaxCarousel = 2;
    } else if (offset >= (height * .6)) {
      this.activeWhyMedpaxCarousel = 3;
    } else {
      this.activeWhyMedpaxCarousel = 1;
    }
    this.cd.detectChanges();
  }

  whyMPIndicator(step: number) {
    this.activeWhyMedpaxCarousel = step;
    this.cd.detectChanges();
  }

  getMedPaxInfo() {
    this.cmsService.getCurrentPage().pipe(takeUntil(this.ngUnsubscribe)).subscribe((pageData) => {
      if (pageData && pageData.slots) {
        for (var key in pageData.slots) {
          pageData?.slots?.[key]?.components?.map(element => {
            if (MEDPAX_CONTENT_SLOT.includes(key) && element?.uid) {
              const id = element?.uid;
              this.cmsService
                .getComponentData(id)
                .pipe(takeUntil(this.ngUnsubscribe))
                .subscribe((comp: any) => {
                  this.medpaxSpecificContent[id] = comp;
                  if (comp.banners) {
                    const bannerClasses = comp?.banners?.split(' ');
                    bannerClasses.map((bannerClass: string) => {
                      this.cmsService.getComponentData(bannerClass).pipe(takeUntil(this.ngUnsubscribe)).subscribe((bannerData: any) => {
                        this.medpaxSpecificContent[bannerClass] = bannerData;
                        if (bannerData?.item) {
                          this.cmsService.getComponentData(bannerData?.item).subscribe(itemData => {
                            this.medpaxSpecificContent[bannerData.item] = itemData;
                            this.cd.detectChanges();
                          })
                        }
                        this.cd.detectChanges();
                      })
                    })
                  }
                }
                );
            }
          })
        }
      }
    });

  }

  ngOnDestroy() {
    this.ngUnsubscribe.next();
    this.ngUnsubscribe.complete();
    if (window) {
      window.removeEventListener('scroll', this.whyMPScroll);
    }
    if (this.observer) {
      this.observer.disconnect();
    }
  }

}
