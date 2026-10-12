import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WsSearchPageMagazinesComponent } from './ws-search-page-magazines.component';

describe('WsSearchPageMagazinesComponent', () => {
  let component: WsSearchPageMagazinesComponent;
  let fixture: ComponentFixture<WsSearchPageMagazinesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [WsSearchPageMagazinesComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(WsSearchPageMagazinesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
