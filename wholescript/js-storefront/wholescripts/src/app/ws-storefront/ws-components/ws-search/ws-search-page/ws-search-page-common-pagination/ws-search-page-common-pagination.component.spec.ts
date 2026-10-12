import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WsSearchPageCommonPaginationComponent } from './ws-search-page-common-pagination.component';

describe('WsSearchPageCommonPaginationComponent', () => {
  let component: WsSearchPageCommonPaginationComponent;
  let fixture: ComponentFixture<WsSearchPageCommonPaginationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [WsSearchPageCommonPaginationComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(WsSearchPageCommonPaginationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
