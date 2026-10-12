import { ChangeDetectorRef, Component, DestroyRef, inject, OnInit } from '@angular/core';
import { CmsService } from '@spartacus/core';
import { Observable } from 'rxjs';
import { FaqQuestionsAndAnswers, FaqTopicList } from '../../../ws-models/ws-faq-page.model';
import { FAQPageService } from '../../../ws-services/ws-faq-page.service';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';

@Component({
  selector: 'ws-ws-autoships-landing-page',
  templateUrl: './ws-autoships-landing-page.component.html',
  styleUrl: './ws-autoships-landing-page.component.scss'
})
export class WsAutoshipsLandingPageComponent implements OnInit {
  private cmsService = inject(CmsService);
  private destroyRef = inject(DestroyRef);

  faqQuestionsAndAnswers: FaqQuestionsAndAnswers = {
    title: '',
    uid: '',
    faqTopicList: []
  };
  toggleAnswer: any = {};

  constructor(private faqPageService: FAQPageService, private cd: ChangeDetectorRef) { }


  AutoshipsHeaderBannerComp$: Observable<any> = this.cmsService.getComponentData('AutoshipsHeaderBannerComp');
  HowDoesItWorkHeaderComp$: Observable<any> = this.cmsService.getComponentData('HowDoesItWorkHeaderComp');
  SelectYourProductsBannerComp$: Observable<any> = this.cmsService.getComponentData('SelectYourProductsBannerComp');
  SetAutoshipFrequencyBannerComp$: Observable<any> = this.cmsService.getComponentData('SetAutoshipFrequencyBannerComp');
  GotoCatalogLink$: Observable<any> = this.cmsService.getComponentData('GotoCatalogLink');
  EnjoyAutoshipDeliveriesBannerComp$: Observable<any> = this.cmsService.getComponentData('EnjoyAutoshipDeliveriesBannerComp');

  ngOnInit() {
    this.getAutoshipFAQs();
  }

  getAutoshipFAQs() {
    this.faqPageService.getFaqQusAnsById('autoShipping')
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe(data => {
        this.faqQuestionsAndAnswers = data;
        this.cd.markForCheck();
      })
  }

  toggleQuestion(topic: FaqTopicList) {
    this.toggleAnswer[topic.uid] = !this.toggleAnswer[topic.uid];
    this.cd.detectChanges();
  }
}
