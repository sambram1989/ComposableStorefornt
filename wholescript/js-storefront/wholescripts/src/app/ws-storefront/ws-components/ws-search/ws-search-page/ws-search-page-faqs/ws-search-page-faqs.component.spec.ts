import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WsSearchPageFaqsComponent } from './ws-search-page-faqs.component';

describe('WsSearchPageFaqsComponent', () => {
  let component: WsSearchPageFaqsComponent;
  let fixture: ComponentFixture<WsSearchPageFaqsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [WsSearchPageFaqsComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(WsSearchPageFaqsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
