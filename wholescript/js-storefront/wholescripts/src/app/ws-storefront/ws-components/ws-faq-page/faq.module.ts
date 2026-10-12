import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {
  ConfigModule
} from '@spartacus/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { WsFaqQuestionsAndAnswersComponent } from './ws-faq-questions-and-answers/ws-faq-questions-and-answers.component';


@NgModule({
  declarations: [
    WsFaqQuestionsAndAnswersComponent,
  ],
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    ConfigModule.withConfig({
      cmsComponents: {
        faqQuestionflexComponent: {
          component: WsFaqQuestionsAndAnswersComponent,
        },
      },
    }),
  ],
  exports: [
    WsFaqQuestionsAndAnswersComponent,
  ],
})
export class FAQModule {}
