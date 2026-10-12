import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WsAutoshipsDetailComponent } from './ws-autoships-detail.component';

describe('WsAutoshipsDetailComponent', () => {
  let component: WsAutoshipsDetailComponent;
  let fixture: ComponentFixture<WsAutoshipsDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [WsAutoshipsDetailComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(WsAutoshipsDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
