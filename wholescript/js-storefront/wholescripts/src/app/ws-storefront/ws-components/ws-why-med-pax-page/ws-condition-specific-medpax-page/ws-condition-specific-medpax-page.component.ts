import { ChangeDetectorRef, Component, OnDestroy } from '@angular/core';
import { CmsService } from '@spartacus/core';
import { CONDITION_SPECIFIC_MEDPAX_CONTENT_SLOT } from '../../../ws-models/page.model';
import { WsPage } from '../../../ws-services/ws-page.service';
import { Subject, takeUntil } from 'rxjs';

@Component({
  selector: 'ws-condition-specific-medpax-page',
  templateUrl: './ws-condition-specific-medpax-page.component.html',
  styleUrl: './ws-condition-specific-medpax-page.component.scss'
})
export class WsConditionSpecificMedpaxPageComponent implements OnDestroy {
  medpaxSpecificContent: { [k: string]: any } = {};
  activeHeroImage: number = 1;
  private ngUnsubscribe = new Subject<void>();

  constructor(public wsPageService: WsPage, private cd: ChangeDetectorRef, private cmsService: CmsService) {
    this.getMedPaxInfo();
    this.manageHeroTransitions();
  }

  getMedPaxInfo() {
    this.cmsService.getCurrentPage().pipe(takeUntil(this.ngUnsubscribe)).subscribe((pageData) => {
      if (pageData && pageData.slots) {
        for (var key in pageData.slots) {
          pageData?.slots?.[key]?.components?.map(element => {
            if (CONDITION_SPECIFIC_MEDPAX_CONTENT_SLOT.includes(key) && element?.uid) {
              const id = element?.uid;
              this.cmsService
                .getComponentData(id)
                .subscribe((comp: any) => {
                  this.medpaxSpecificContent[id] = comp;
                  if (comp?.item) {
                    this.cmsService.getComponentData(comp?.item).pipe(takeUntil(this.ngUnsubscribe)).subscribe(itemData => {
                      this.medpaxSpecificContent[comp.item] = itemData;
                      this.cd.detectChanges();
                    })
                  }
                });
            }
          })
        }
      }
    });
  }

  manageHeroTransitions() {
    setInterval(() => {
      if (this.activeHeroImage === 3) {
        this.activeHeroImage = 1;
      } else {
        this.activeHeroImage += 1
      }
      this.cd.detectChanges();
    }, 8000);
  }

  ngOnDestroy() {
    this.ngUnsubscribe.next();
    this.ngUnsubscribe.complete();
  }
}
