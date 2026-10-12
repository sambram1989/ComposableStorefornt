import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WsConditionSpecificMedpaxPageComponent } from './ws-condition-specific-medpax-page.component';

describe('WsConditionSpecificMedpaxPageComponent', () => {
  let component: WsConditionSpecificMedpaxPageComponent;
  let fixture: ComponentFixture<WsConditionSpecificMedpaxPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [WsConditionSpecificMedpaxPageComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(WsConditionSpecificMedpaxPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
