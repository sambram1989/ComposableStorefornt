import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WsContactUsPageComponent } from './ws-contact-us-page.component';

describe('WsContactUsPageComponent', () => {
  let component: WsContactUsPageComponent;
  let fixture: ComponentFixture<WsContactUsPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [WsContactUsPageComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(WsContactUsPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
