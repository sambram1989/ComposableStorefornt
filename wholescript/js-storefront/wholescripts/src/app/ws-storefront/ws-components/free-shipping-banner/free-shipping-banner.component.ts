import { ChangeDetectorRef, Component, DestroyRef } from '@angular/core';
import { CmsService } from '@spartacus/core';
import { Observable } from 'rxjs';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';

@Component({
  selector: 'ws-free-shipping-banner',
  templateUrl: './free-shipping-banner.component.html',
  styleUrl: './free-shipping-banner.component.scss'
})
export class FreeShippingBannerComponent {
  freeShippingBannerMessage: string  ='';
  componentDataForFreeShipingMessage$: Observable<any>;
  constructor(
    protected cmsService: CmsService,
    protected destroyRef: DestroyRef,
    private cdr: ChangeDetectorRef
  ) {}
  
  ngOnInit(): void {
    this.freeShippingBannerMessage = '';
    this.getPageInfo();
  }

  getPageInfo() {
    this.cmsService.getContentSlot('FreeShippingBannerMessage')
    .pipe(takeUntilDestroyed(this.destroyRef))
    .subscribe((res) => {
      const freeShippingBannerComponent = res.components?.filter((res: any) => {
        return res.uid == 'shippingMessagePractionerParagraphComponent' || res.uid == 'shippingMessagePatientParagraphComponent';
      });

      if (freeShippingBannerComponent.length) {
        this.componentDataForFreeShipingMessage$ = this.cmsService.getComponentData(
          freeShippingBannerComponent[0].uid
        );

        this.componentDataForFreeShipingMessage$.pipe(takeUntilDestroyed(this.destroyRef)).subscribe((componentData) => {
          this.freeShippingBannerMessage = componentData?.content || '';
          this.cdr.markForCheck();
        });
        
      } else {
        this.freeShippingBannerMessage = '';    // reset the message if the slot is not available
        this.cdr.markForCheck(); 
      }
    });
  }
}
