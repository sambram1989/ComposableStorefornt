import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProtocolLandingPageComponent } from './protocol-landing-page.component';

describe('ProtocolLandingPageComponent', () => {
  let component: ProtocolLandingPageComponent;
  let fixture: ComponentFixture<ProtocolLandingPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ProtocolLandingPageComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ProtocolLandingPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
