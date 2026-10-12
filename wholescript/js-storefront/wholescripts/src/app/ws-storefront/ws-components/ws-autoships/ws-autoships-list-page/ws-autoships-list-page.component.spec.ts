import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WsAutoshipsListPageComponent } from './ws-autoships-list-page.component';

describe('WsAutoshipsListPageComponent', () => {
  let component: WsAutoshipsListPageComponent;
  let fixture: ComponentFixture<WsAutoshipsListPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [WsAutoshipsListPageComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(WsAutoshipsListPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
