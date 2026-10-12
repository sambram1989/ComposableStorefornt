import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WsWhyMedPaxPageComponent } from './ws-why-med-pax-page.component';

describe('WsWhyMedPaxPageComponent', () => {
  let component: WsWhyMedPaxPageComponent;
  let fixture: ComponentFixture<WsWhyMedPaxPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [WsWhyMedPaxPageComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(WsWhyMedPaxPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
