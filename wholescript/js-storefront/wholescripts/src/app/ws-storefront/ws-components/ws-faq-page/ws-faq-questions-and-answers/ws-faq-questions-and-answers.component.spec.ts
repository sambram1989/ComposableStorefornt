import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WsFaqQuestionsAndAnswersComponent } from './ws-faq-questions-and-answers.component';

describe('WsFaqQuestionsAndAnswersComponent', () => {
  let component: WsFaqQuestionsAndAnswersComponent;
  let fixture: ComponentFixture<WsFaqQuestionsAndAnswersComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [WsFaqQuestionsAndAnswersComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(WsFaqQuestionsAndAnswersComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
