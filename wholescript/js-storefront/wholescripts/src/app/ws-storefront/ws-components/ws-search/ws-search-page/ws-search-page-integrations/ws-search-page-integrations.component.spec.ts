import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WsSearchPageIntegrationsComponent } from './ws-search-page-integrations.component';

describe('WsSearchPageIntegrationsComponent', () => {
  let component: WsSearchPageIntegrationsComponent;
  let fixture: ComponentFixture<WsSearchPageIntegrationsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [WsSearchPageIntegrationsComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(WsSearchPageIntegrationsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
