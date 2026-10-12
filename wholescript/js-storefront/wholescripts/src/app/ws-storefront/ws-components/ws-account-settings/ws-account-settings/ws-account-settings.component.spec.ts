import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WsAccountSettingsComponent } from './ws-account-settings.component';

describe('WsAccountSettingsComponent', () => {
  let component: WsAccountSettingsComponent;
  let fixture: ComponentFixture<WsAccountSettingsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [WsAccountSettingsComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(WsAccountSettingsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
