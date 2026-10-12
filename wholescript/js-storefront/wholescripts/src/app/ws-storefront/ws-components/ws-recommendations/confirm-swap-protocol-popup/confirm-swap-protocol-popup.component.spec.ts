import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ConfirmSwapProtocolPopupComponent } from './confirm-swap-protocol-popup.component';

describe('ConfirmSwapProtocolPopupComponent', () => {
  let component: ConfirmSwapProtocolPopupComponent;
  let fixture: ComponentFixture<ConfirmSwapProtocolPopupComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ConfirmSwapProtocolPopupComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ConfirmSwapProtocolPopupComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
