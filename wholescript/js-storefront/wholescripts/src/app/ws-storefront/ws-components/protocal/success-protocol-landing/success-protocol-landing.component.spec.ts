import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SuccessProtocolLandingComponent } from './success-protocol-landing.component';

describe('SuccessProtocolLandingComponent', () => {
  let component: SuccessProtocolLandingComponent;
  let fixture: ComponentFixture<SuccessProtocolLandingComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [SuccessProtocolLandingComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(SuccessProtocolLandingComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
