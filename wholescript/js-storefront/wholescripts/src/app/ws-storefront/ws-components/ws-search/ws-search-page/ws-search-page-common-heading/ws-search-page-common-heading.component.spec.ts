import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WsSearchPageCommonHeadingComponent } from './ws-search-page-common-heading.component';

describe('WsSearchPageCommonHeadingComponent', () => {
  let component: WsSearchPageCommonHeadingComponent;
  let fixture: ComponentFixture<WsSearchPageCommonHeadingComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [WsSearchPageCommonHeadingComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(WsSearchPageCommonHeadingComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
