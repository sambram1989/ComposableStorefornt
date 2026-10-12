import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WsSearchPageComponent } from './ws-search-page.component';

describe('WsSearchPageComponent', () => {
  let component: WsSearchPageComponent;
  let fixture: ComponentFixture<WsSearchPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [WsSearchPageComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(WsSearchPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
