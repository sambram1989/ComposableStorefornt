import { ChangeDetectorRef, Component } from '@angular/core';
import { FAQPageService } from '../../../ws-services/ws-faq-page.service';
import { FaqQuestionsAndAnswers, FaqTopicList } from '../../../ws-models/ws-faq-page.model';

@Component({
  selector: 'ws-ws-faq-questions-and-answers',
  templateUrl: './ws-faq-questions-and-answers.component.html',
  styleUrl: './ws-faq-questions-and-answers.component.scss'
})
export class WsFaqQuestionsAndAnswersComponent {
  faqQuestionsAndAnswers: FaqQuestionsAndAnswers[] = [];
  toggleAnswer: any = {};
  searchTerm: string = '';
  noResultsFound: boolean = false;
  activeTopicUid: string = '';

  constructor(private faqService: FAQPageService, private cd: ChangeDetectorRef) {
    this.getPageInfo();
  }



  private readonly faqOrder = [
    "general", "orders", "practitioners", "patients", "xymogen",
    "medpax", "specialOffers", "api", "shipping", "autoShipping"
  ];

  getPageInfo() {
    this.faqService.fetchFaqQusAnsDetails().subscribe((res) => {
      this.faqQuestionsAndAnswers = res?.data
        ? this.reOrderFaq(res?.data)
        : [];
      this.cd.detectChanges();
    });
  }

  reOrderFaq(data: any[]): any[] {
    const orderMap = new Map(this.faqOrder.map((uid, index) => [uid, index]));
    return data.sort((a, b) => {
      const indexA = orderMap.get(a.uid) ?? this.faqOrder.length;
      const indexB = orderMap.get(b.uid) ?? this.faqOrder.length;
      return indexA - indexB;
    });
  }

  toggleQuestion(topic:FaqTopicList) {
    this.toggleAnswer[topic.uid] = !this.toggleAnswer[topic.uid];
    this.cd.detectChanges();
  }

  goToTopic(faqQuestionsAndAnswer: FaqQuestionsAndAnswers) {
    this.activeTopicUid = faqQuestionsAndAnswer.uid;
    const id = faqQuestionsAndAnswer.title;
    let el = document.getElementById(id);
    if(id && el && window) { 
      const offset = 78;
      const elementRect = el.getBoundingClientRect();
      const elementTop = elementRect.top + window.scrollY;
      const scrollPosition = elementTop - offset;
      window.scrollTo({
          top: scrollPosition,
          behavior: 'smooth'
      });
    };

    if(faqQuestionsAndAnswer?.child)  this.toggleSubmenu(faqQuestionsAndAnswer);
  }

  toggleSubmenu(item: any): void {
    item.expanded = !item.expanded;
  }

  boldSearchTerm(value: string): string {
    if (!this.searchTerm || this.searchTerm=== '' || this.searchTerm?.length < 3) {
      return value;
    }

    const re = new RegExp(this.searchTerm, 'gi'); 
    return value.replace(re, match => `<span class="highlight-text">${match}</span>`); 
  }

  search() {
    if(this.searchTerm && this.searchTerm?.trim().length > 2) {
      let scrollToId = '';
      this.toggleAnswer = {};
      this.noResultsFound = true;
      this.faqQuestionsAndAnswers?.map(faqQuestionsAndAnswer => {
        faqQuestionsAndAnswer?.faqTopicList?.map(topic => {
          if(topic) {
            if(topic?.question?.toLowerCase().includes(this.searchTerm?.toLowerCase())) {
              scrollToId = faqQuestionsAndAnswer.uid;
              this.toggleAnswer[topic.uid] = true;
              this.noResultsFound = false;
            }
            if(topic?.answer?.toLowerCase()?.includes(this.searchTerm?.toLowerCase())) {
              this.toggleAnswer[topic.uid] = true;
              this.noResultsFound = false;
            }
          }
        })
  
      })
    } else {
      this.noResultsFound = false;
      this.toggleAnswer = {};
    }
  }

  clearSearch() {
    this.searchTerm = '';
    this.search();
  }

}
