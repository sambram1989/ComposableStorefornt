export interface FaqQuestionsAndAnswers {
    title: string;
    uid: string;
    faqTopicList: FaqTopicList[]
    child?: FaqChildList[]
    expanded?: false,
}

export interface FaqChildList {
    title: string;
    uid: string;
    faqTopicList: FaqTopicList[]
}

export interface FaqTopicList {
    answer: string;
    question: string;
    uid: string;
}