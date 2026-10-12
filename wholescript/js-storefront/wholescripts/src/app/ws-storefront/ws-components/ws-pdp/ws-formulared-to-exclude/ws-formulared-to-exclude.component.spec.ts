import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WsFormularedToExcludeComponent } from './ws-formulared-to-exclude.component';

describe('WsFormularedToExcludeComponent', () => {
  let component: WsFormularedToExcludeComponent;
  let fixture: ComponentFixture<WsFormularedToExcludeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [WsFormularedToExcludeComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(WsFormularedToExcludeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
