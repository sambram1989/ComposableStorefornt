import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WsAutoshipsLandingPageComponent } from './ws-autoships-landing-page.component';

describe('WsAutoshipsLandingPageComponent', () => {
  let component: WsAutoshipsLandingPageComponent;
  let fixture: ComponentFixture<WsAutoshipsLandingPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [WsAutoshipsLandingPageComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(WsAutoshipsLandingPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
