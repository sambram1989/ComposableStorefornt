import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WsSearchPageCategoriesComponent } from './ws-search-page-categories.component';

describe('WsSearchPageCategoriesComponent', () => {
  let component: WsSearchPageCategoriesComponent;
  let fixture: ComponentFixture<WsSearchPageCategoriesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [WsSearchPageCategoriesComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(WsSearchPageCategoriesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
