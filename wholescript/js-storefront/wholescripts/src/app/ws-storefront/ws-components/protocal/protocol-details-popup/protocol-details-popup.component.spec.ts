import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProtocolDetailsPopupComponent } from './protocol-details-popup.component';

describe('ProtocolDetailsPopupComponent', () => {
  let component: ProtocolDetailsPopupComponent;
  let fixture: ComponentFixture<ProtocolDetailsPopupComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ProtocolDetailsPopupComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ProtocolDetailsPopupComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
