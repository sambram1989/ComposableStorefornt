import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WsPatientFindPractitionerSearchMapComponent } from './ws-patient-find-practitioner-search-map.component';

describe('WsPatientFindPractitionerSearchMapComponent', () => {
  let component: WsPatientFindPractitionerSearchMapComponent;
  let fixture: ComponentFixture<WsPatientFindPractitionerSearchMapComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [WsPatientFindPractitionerSearchMapComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(WsPatientFindPractitionerSearchMapComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
