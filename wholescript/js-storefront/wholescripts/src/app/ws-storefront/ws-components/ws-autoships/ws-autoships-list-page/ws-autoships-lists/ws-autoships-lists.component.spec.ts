import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WsAutoshipsListsComponent } from './ws-autoships-lists.component';

describe('WsAutoshipsListsComponent', () => {
  let component: WsAutoshipsListsComponent;
  let fixture: ComponentFixture<WsAutoshipsListsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [WsAutoshipsListsComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(WsAutoshipsListsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
