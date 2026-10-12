import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SwapProtocolPopupComponent } from './swap-protocol-popup.component';

describe('SwapProtocolPopupComponent', () => {
  let component: SwapProtocolPopupComponent;
  let fixture: ComponentFixture<SwapProtocolPopupComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [SwapProtocolPopupComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(SwapProtocolPopupComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
