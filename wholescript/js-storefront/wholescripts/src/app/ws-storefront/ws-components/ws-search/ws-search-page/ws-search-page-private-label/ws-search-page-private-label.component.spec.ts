import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WsSearchPagePrivateLabelComponent } from './ws-search-page-private-label.component';

describe('WsSearchPagePrivateLabelComponent', () => {
  let component: WsSearchPagePrivateLabelComponent;
  let fixture: ComponentFixture<WsSearchPagePrivateLabelComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [WsSearchPagePrivateLabelComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(WsSearchPagePrivateLabelComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
