import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WsConditionSpecificMedpaxIndividualPageComponent } from './ws-condition-specific-medpax-individual-page.component';

describe('WsConditionSpecificMedpaxIndividualPageComponent', () => {
  let component: WsConditionSpecificMedpaxIndividualPageComponent;
  let fixture: ComponentFixture<WsConditionSpecificMedpaxIndividualPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [WsConditionSpecificMedpaxIndividualPageComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(WsConditionSpecificMedpaxIndividualPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
