import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WsSearchPageWebinarComponent } from './ws-search-page-webinar.component';

describe('WsSearchPageWebinarComponent', () => {
  let component: WsSearchPageWebinarComponent;
  let fixture: ComponentFixture<WsSearchPageWebinarComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [WsSearchPageWebinarComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(WsSearchPageWebinarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
