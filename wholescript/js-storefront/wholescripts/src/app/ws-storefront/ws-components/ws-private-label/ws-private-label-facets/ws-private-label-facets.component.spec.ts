import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WsPrivateLabelFacetsComponent } from './ws-private-label-facets.component';

describe('WsPrivateLabelFacetsComponent', () => {
  let component: WsPrivateLabelFacetsComponent;
  let fixture: ComponentFixture<WsPrivateLabelFacetsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [WsPrivateLabelFacetsComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(WsPrivateLabelFacetsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
