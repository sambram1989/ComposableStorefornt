import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WsAutoshipsDetailPageComponent } from './ws-autoships-detail-page.component';

describe('WsAutoshipsDetailPageComponent', () => {
  let component: WsAutoshipsDetailPageComponent;
  let fixture: ComponentFixture<WsAutoshipsDetailPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [WsAutoshipsDetailPageComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(WsAutoshipsDetailPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
