import { ChangeDetectorRef, Component, OnDestroy } from '@angular/core';
import { CmsService } from '@spartacus/core';
import { CONDITION_SPECIFIC_MEDPAX_CONTENT_SPECIFIC_SLOT } from '../../../ws-models/page.model';
import { WsPage } from '../../../ws-services/ws-page.service';
import { Subject, takeUntil } from 'rxjs';

@Component({
  selector: 'ws-condition-specific-medpax-individual-page',
  templateUrl: './ws-condition-specific-medpax-individual-page.component.html',
  styleUrl: './ws-condition-specific-medpax-individual-page.component.scss'
})
export class WsConditionSpecificMedpaxIndividualPageComponent implements OnDestroy {
  medpaxSpecificContent: { [k: string]: any } = {};
  private ngUnsubscribe = new Subject<void>();

  constructor(public wsPageService: WsPage, private cd: ChangeDetectorRef, private cmsService: CmsService) {
    this.getMedPaxInfo();
  }

  getMedPaxInfo() {
    this.cmsService.getCurrentPage().pipe(takeUntil(this.ngUnsubscribe)).subscribe((pageData) => {
      if (pageData && pageData.slots) {
        for (var key in pageData.slots) {
          pageData?.slots?.[key]?.components?.map(element => {
            if (CONDITION_SPECIFIC_MEDPAX_CONTENT_SPECIFIC_SLOT.includes(key) && element?.uid) {
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

  ngOnDestroy() {
    this.ngUnsubscribe.next();
    this.ngUnsubscribe.complete();
  }
}
